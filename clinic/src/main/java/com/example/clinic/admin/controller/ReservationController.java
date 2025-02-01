package com.example.clinic.admin.controller;

import com.example.clinic.admin.model.Patient;
import com.example.clinic.admin.model.Reservation;
import com.example.clinic.admin.service.PatientService;
import com.example.clinic.admin.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequestMapping("/admin/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final PatientService patientService;
    @Autowired
    public ReservationController(ReservationService reservationService, PatientService patientService) {
        this.reservationService = reservationService;
        this.patientService = patientService;
    }
    @GetMapping("/")
    public String viewReservations(
            @RequestParam(value = "patientId", required = false) Long patientId,
            RedirectAttributes redirectAttributes,
            Model model) {

        List<Reservation> reservations;

        if (patientId != null) {
            // Get reservations for a specific patient
            reservations = reservationService.getReservationsByPatientId(patientId);

            // Create a DateTimeFormatter to format the day of the week
            DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE");
            for (Reservation reservation : reservations) {
                if (reservation.getDate() != null) {
                    // Format the LocalDate into the day of the week
                    String dayOfWeek = reservation.getDate().format(dayFormatter);
                    reservation.setDayOfWeek(dayOfWeek); // Set the day of the week in the reservation
                } else {
                    // Handle the case where date is null
                    reservation.setDayOfWeek("غير محدد");
                }
            }

            // If no reservations found for the patient
            if (reservations.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "لا يوجد أي حجوزات لهذا المريض");
                return "redirect:/admin/patients/"; // Redirect to patients page with error
            }

            model.addAttribute("reservations", reservations);
            model.addAttribute("filter", true); // Indicates filtered view
        } else {
            // Get all reservations when no patientId is provided
            reservations = reservationService.getAllReservations();

            // Create a DateTimeFormatter to format the day of the week
            DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE");
            for (Reservation reservation : reservations) {
                if (reservation.getDate() != null) {
                    // Format the LocalDate into the day of the week
                    String dayOfWeek = reservation.getDate().format(dayFormatter);
                    reservation.setDayOfWeek(dayOfWeek); // Set the day of the week in the reservation
                } else {
                    // Handle the case where date is null
                    reservation.setDayOfWeek("غير محدد");
                }
            }

            model.addAttribute("reservations", reservations);
            model.addAttribute("filter", false); // Indicates all reservations view
        }

        return "admin/reservations";
    }
    @PostMapping("/")
    public String createReservation(@ModelAttribute Reservation reservation, RedirectAttributes redirectAttributes) {
        try {
            reservationService.createReservation(reservation);
            redirectAttributes.addFlashAttribute("message", "تم الحجز بنجاح");
            return "redirect:/admin/reservations/"; // Redirect to the reservations page
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/reservations/add"; // Redirect back to the form in case of error
        }
    }


    @GetMapping("/add")
    public String showReservationForm(@RequestParam(required = false) Long patientId, Model model) {
        // Fetch all patients for the dropdown
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        // Create a new Reservation object
        Reservation reservation = new Reservation();
        // If patientId is provided, fetch the patient and set it in the reservation
        if (patientId != null) {
            Patient patient = patientService.findPatientById(patientId);
            if (patient != null) {
                reservation.setPatient(patient);
            }
        }

        // Add the reservation object to the model
        model.addAttribute("reservation", reservation);

        // Return the view name
        return "admin/reservationForm";
    }
    @GetMapping("/reserved-turns")
    public ResponseEntity<List<Integer>> getReservedTurns(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Integer> reservedTurns = reservationService.getReservedTurnsByDate(date);
        return ResponseEntity.ok(reservedTurns);
    }



}

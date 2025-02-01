package com.example.clinic.admin.controller;

import com.example.clinic.admin.model.Patient;
import com.example.clinic.admin.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Display all patients
    @GetMapping("/")
    public String getPatientsPage(Model model) {
        model.addAttribute("username", "admin");

        model.addAttribute("patients", patientService.getAllPatients());
        return "admin/patients";
    }

    // Search patients by mobile
    @GetMapping("/search")
    public String searchPatients(@RequestParam(required = false) String mobile, Model model) {
        model.addAttribute("patients", patientService.searchByMobile(mobile));
        return "admin/patients";
    }

    // Show form for adding or editing a patient
    @GetMapping("/add")
    public String showPatientForm(@RequestParam(value = "patientId", required = false) Long patientId, Model model) {
        Patient patient = (patientId != null) ? patientService.findPatientById(patientId) : new Patient();
        model.addAttribute("patient", patient);
        return "admin/patientForm";
    }

    // Delete a patient
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patientService.deletePatient(id);
            redirectAttributes.addFlashAttribute("message", "تم حذف المريض بنجاح");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "حدث خطأ أثناء حذف المريض");
        }
        return "redirect:/admin/patients/";
    }

    // Save or update a patient
    @PostMapping("/")
    public String savePatient(@ModelAttribute Patient patient, Model model) {
        try {
            if (patient.getId() != null) {
                patientService.updatePatient(patient);
            } else {
                patientService.savePatient(patient);
            }
            model.addAttribute("message", "تم حفظ المريض بنجاح");
            return "redirect:/admin/patients/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin/patientForm";
        }
    }





    @GetMapping("/profile/{id}")
    public String showPatientProfile(@PathVariable Long id, Model model) {
        Patient patient = patientService.findPatientById(id);
        if (patient == null) {
            model.addAttribute("error", "المريض غير موجود.");
            return "redirect:/admin/patients/";
        }

        // Check if the patient has a reservation of type "كشف"
        boolean hasKashfReservation = patient.getReservations().stream()
                .anyMatch(reservation -> "كشف".equals(reservation.getType()));
        boolean hasKKashfReservation = patient.getReservations().stream()
                .anyMatch(reservation -> "استشارة".equals(reservation.getType()));
        boolean hasLabs = !patient.getLabTests().isEmpty();
        // Add the boolean flag to the model
        model.addAttribute("patient", patient);
        model.addAttribute("hasKashfReservation", hasKashfReservation);
        model.addAttribute("hasKKashfReservation", hasKKashfReservation);
        model.addAttribute("hasLabs", hasLabs);

        return "admin/patientProfile";
    }


}

package com.example.clinic.admin.controller;
import com.example.clinic.admin.service.PatientService;
import com.example.clinic.admin.service.ReservationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardController {
    private final PatientService patientService;
private final ReservationService reservationService;

    public DashboardController(PatientService patientService, ReservationService reservationService) {
        this.patientService = patientService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String getDashboardPage(Model model) {

            long totalPatients = patientService.getTotalPatientCount();
            long totalReservations = reservationService.getTotalReservationCount();
            model.addAttribute("totalPatients", totalPatients);
            model.addAttribute("totalReservations", totalReservations);

            model.addAttribute("username", "admin");

        return "admin/dashboard";
    }
    @GetMapping("/login")
    public String getLoginPage() {

        return "admin/login";
    }



}

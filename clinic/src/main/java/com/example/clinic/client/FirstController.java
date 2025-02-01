package com.example.clinic.client;

import com.example.clinic.admin.model.Patient;
import com.example.clinic.admin.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller

public class FirstController {

    private final PatientService patientService;
@Autowired
    public FirstController(PatientService patientService) {
        this.patientService = patientService;
    }


    @GetMapping("/")
    public String index(@RequestParam(value = "patientId", required = false) Long patientId, Model model) {
        Patient patient = (patientId != null) ? patientService.findPatientById(patientId) : new Patient();
        model.addAttribute("patient", patient);
        return "client/index";
    }



    @PostMapping("/")
    public String savePatient(@ModelAttribute Patient patient, Model model) {
        try {
            if (patient.getId() != null) {
                patientService.updatePatient(patient);
            } else {
                patientService.savePatient(patient);
            }
            model.addAttribute("message", "تم حفظ المريض بنجاح");
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "client/index";
        }
    }



}

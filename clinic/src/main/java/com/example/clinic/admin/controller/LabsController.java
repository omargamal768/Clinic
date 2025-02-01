package com.example.clinic.admin.controller;

import com.example.clinic.admin.model.LabTest;
import com.example.clinic.admin.model.Patient;
import com.example.clinic.admin.service.LabsService;
import com.example.clinic.admin.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/labs")
public class LabsController {

    private final LabsService labsService;
    private final PatientService patientService;

    @Autowired
    public LabsController(LabsService labsService, PatientService patientService) {
        this.labsService = labsService;
        this.patientService = patientService;
    }

    @GetMapping("/")
    public String getAllLabTests(Model model) {
        List<LabTest> labTests = labsService.getAllLabTests();
        model.addAttribute("labTests", labTests);
        return "admin/labs"; // This corresponds to a Thymeleaf template named "labs.html"
    }

    @GetMapping("/search")
    public String searchLabTests(@RequestParam("name") String name, Model model) {
        List<LabTest> filteredLabTests = labsService.searchByName(name);
        model.addAttribute("labTests", filteredLabTests);
        return "admin/labs";
    }
    @GetMapping("/add")
    public String showLabsForm(@RequestParam(required = false) Long patientId, Model model) {
        // Fetch all lab tests
        List<LabTest> labTests = labsService.getAllLabTests();
        model.addAttribute("labTests", labTests); // Add lab tests to the model

        // Fetch all patients
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);

        // Create a new LabTest object for binding
        LabTest labTest = new LabTest();
        if (patientId != null) {
            Patient patient = patientService.findPatientById(patientId);
            if (patient != null) {
                labTest.setPatient(Collections.singletonList(patient)); // Add the patient to the list
            }
        }

        model.addAttribute("labTest", labTest); // Add LabTest object to the model
        return "admin/AddLabsPatient"; // Return the view name
    }
    @PostMapping("/save")
    public String saveLabTest(@RequestParam("labTest.id") Long labTestId,
                              @RequestParam("patient.id") Long patientId,
                              Model model) {
        try {
            // Fetch Patient and LabTest by ID
            Patient patient = patientService.findPatientById(patientId);
            LabTest labTest = labsService.findLabTestById(labTestId);

            if (patient != null && labTest != null) {
                // Add the labTest to the patient's list of labTests
                patient.getLabTests().add(labTest);

                // Save the patient to persist the relationship
                patientService.savePatient(patient);

                model.addAttribute("message", "Lab test successfully assigned to the patient.");
            } else {
                model.addAttribute("error", "Invalid patient or lab test ID.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while saving the lab test.");
        }

        // Redirect back to the labs page
        return "redirect:/admin/labs/";
    }



    @GetMapping("/add_new")
    public String showLabForm(@RequestParam(value = "labId", required = false) Long labId, Model model) {
        LabTest labTest = (labId != null) ? labsService.findLabTestById(labId)  : new LabTest();
        model.addAttribute("labTest", labTest);
        return "admin/labForm";
    }


    @PostMapping("/")
    public String saveLabTest(@ModelAttribute LabTest labTest, Model model) {
        try {
            if (labTest.getId() != null) {
                labsService.updateLabTest(labTest);
            } else {
              labsService.saveLabTest(labTest);
            }
            model.addAttribute("message", "تم حفظ التحليل بنجاح");
            return "redirect:/admin/labs/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin/labForm";
        }}


}

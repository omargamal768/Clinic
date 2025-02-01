package com.example.clinic.admin.service;
import com.example.clinic.admin.model.Patient;
import com.example.clinic.admin.model.PatientStatus;
import com.example.clinic.admin.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {



    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    // Method to save a new patient
    public void savePatient(Patient patient) {
        patientRepository.save(patient);
    }

    // Method to update an existing patient
    public void updatePatient(Patient patient) {
        // Check if patient exists before updating
        if (patient.getId() != null && patientRepository.existsById(patient.getId())) {
            patientRepository.save(patient);
        } else {
            throw new IllegalArgumentException("Patient with ID " + patient.getId() + " does not exist.");
        }
    }

    // Method to find a patient by ID
    public Patient findPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    public void deletePatient(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            patientRepository.delete(patient.get());
        } else {
            throw new RuntimeException("المريض غير موجود");
        }
    }


    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    public List<Patient> searchByMobile(String mobile) {
        return mobile == null || mobile.isEmpty() ? getAllPatients() : patientRepository.findByMobileContaining(mobile);
    }



    private boolean isStatusTransitionValid(PatientStatus current, PatientStatus next) {
        if (current == null && next == PatientStatus.RESERVATION_MADE) return true;
        if (current == PatientStatus.RESERVATION_MADE && next == PatientStatus.TESTS_AND_RADIOLOGY_DONE) return true;
        if (current == PatientStatus.TESTS_AND_RADIOLOGY_DONE && next == PatientStatus.DIAGNOSIS_COMPLETE) return true;
        return false;
    }

    public long getTotalPatientCount() {
        return patientRepository.count(); // Assuming you have a PatientRepository with a count method
    }
}

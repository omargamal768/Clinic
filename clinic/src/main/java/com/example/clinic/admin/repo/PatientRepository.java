package com.example.clinic.admin.repo;

import com.example.clinic.admin.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByMobileContaining(String mobile);
}

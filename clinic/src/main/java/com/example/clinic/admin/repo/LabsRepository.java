package com.example.clinic.admin.repo;

import com.example.clinic.admin.model.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabsRepository  extends JpaRepository<LabTest, Long> {
    List<LabTest> findByNameContainingIgnoreCase(String name);
}

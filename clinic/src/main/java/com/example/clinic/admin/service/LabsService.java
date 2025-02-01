package com.example.clinic.admin.service;

import com.example.clinic.admin.model.LabTest;
import com.example.clinic.admin.repo.LabsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabsService {
    private final LabsRepository labsRepository;
    @Autowired
    public LabsService(LabsRepository labsRepository) {
        this.labsRepository = labsRepository;
    }



    public List<LabTest> getAllLabTests() {
        return labsRepository.findAll();
    }




    public List<LabTest> searchByName(String name) {
        return labsRepository.findByNameContainingIgnoreCase(name);
    }

    public LabTest findLabTestById(Long id) {
        return labsRepository.findById(id).orElse(null);
    }
    public void saveLabTest(LabTest labTest) {
        labsRepository.save(labTest);
    }
    public void updateLabTest(LabTest labTest) {
        // Check if patient exists before updating
        if (labTest.getId() != null && labsRepository.existsById(labTest.getId())) {
            labsRepository.save(labTest);
        } else {
            throw new IllegalArgumentException("LabTest with ID " + labTest.getId() + " does not exist.");
        }
    }
}

package com.example.clinic.admin.model;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String mobile;
    @ManyToMany
    @JoinTable(name = "patient_lab_test", joinColumns = @JoinColumn(name = "patient_id"), inverseJoinColumns = @JoinColumn(name = "lab_test_id"))
    private List<LabTest> labTests;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;


    // Getters and setters
    public Long getId() {return id;}
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {this.mobile = mobile;}

    public List<LabTest> getLabTests() {
        return labTests;
    }
    public void setLabTests(List<LabTest> labTests) {
        this.labTests = labTests;
    }


    public List<Reservation> getReservations() {
        return reservations;
    }
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


}
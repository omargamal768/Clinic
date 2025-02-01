package com.example.clinic.admin.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservation",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"patient_id", "date"}), // Prevent same patient from reserving on same date
                @UniqueConstraint(columnNames = {"date", "turn"})  // Prevent two patients from having same turn on same date
        })
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int turn;

    private LocalDate date;

    private String clinicName;

    private String type; // Added the type field of type String
    private String dayOfWeek;  // Add this field for the day of the week

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Getters and setters
    public Long getId() {
        return id;
    }
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getType() {
        return type; // Getter for type
    }

    public void setType(String type) {
        this.type = type; // Setter for type
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}

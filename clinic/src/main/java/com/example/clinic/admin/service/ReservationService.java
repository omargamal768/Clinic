package com.example.clinic.admin.service;

import com.example.clinic.admin.model.Reservation;
import com.example.clinic.admin.repo.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByPatientId(Long patientId) {
        return reservationRepository.findByPatientId(patientId);
    }

    public List<Integer> getReservedTurnsByDate(LocalDate date) {
        return reservationRepository.findReservedTurnsByDate(date);
    }
    public boolean hasReservationOnSameDate(Long patientId, LocalDate date) {
        return reservationRepository.existsByPatientIdAndDate(patientId, date);
    }

    // Check if the turn is already taken on a specific date
    public boolean isTurnTaken(int turn, LocalDate date) {
        return reservationRepository.existsByTurnAndDate(turn, date);
    }

    // Create reservation if the rules are satisfied
    public Reservation createReservation(Reservation reservation) throws Exception {
        if (hasReservationOnSameDate(reservation.getPatient().getId(), reservation.getDate())) {
            throw new Exception("المريض لديه حجز في هذا التاريخ بالفعل.");
        }

        if (isTurnTaken(reservation.getTurn(), reservation.getDate())) {
            throw new Exception("الوقت المحدد محجوز بالفعل.");
        }

        return reservationRepository.save(reservation);
    }

    public long getTotalReservationCount() {
        return reservationRepository.count(); // Assuming you have a PatientRepository with a count method
    }

    public long countReservationsByMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return reservationRepository.countByDateBetween(startDate, endDate);
    }



}

package com.example.project.repository;

import java.time.LocalDateTime;

import com.example.project.dto.AppointmentDTO;
import com.example.project.entity.Appointment;

public interface AppointmentRepo {
    AppointmentDTO bookAppointment(int doctorId,int patientId,LocalDateTime start, LocalDateTime end);
    AppointmentDTO updateAppointment(int appointmentId, LocalDateTime newStart, LocalDateTime newEnd, String newStatus);
    void cancelAppointment(int appointmentId);
}

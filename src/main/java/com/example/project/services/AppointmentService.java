package com.example.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.dto.AppointmentDTO;
import com.example.project.dto.AppointmentSummaryDTO;
import com.example.project.entity.Appointment;
import com.example.project.entity.Doctor;
import com.example.project.entity.Patient;
import com.example.project.repository.AppointmentRepo;
import com.example.project.repository.AppointmentRepository;
import com.example.project.repository.DoctorRepository;
import com.example.project.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentService implements AppointmentRepo{

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<AppointmentSummaryDTO> getAppointmentsByDoctorId(int doctorId) {
        return appointmentRepository.findAppointmentsByDoctorId(doctorId);
    }

    public AppointmentDTO bookAppointment(int doctorId, int patientId, LocalDateTime start, LocalDateTime end) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Check if doctor is already booked in this time range
        List<Appointment> existing = appointmentRepository
                .findByDoctorIdAndStartTimeBetween(doctorId, start, end);

        if (!existing.isEmpty()) {
            throw new RuntimeException("Doctor already has an appointment in this slot");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStartTime(start);
        appointment.setEndTime(end);
        appointment.setStatus("PENDING");

        System.out.println(appointment.getDoctor()+" "+appointment.getPatient()+" "+appointment.getStartTime()+" "+appointment.getEndTime()+" "+appointment.getStatus());
        
        Appointment saved = appointmentRepository.save(appointment);
          
        System.out.println(saved);
         

         /*AppointmentDTO appointmentDTO=new AppointmentDTO(
            saved.getId(),saved.getStartTime(),saved.getEndTime(),saved.getStatus(),saved.getDoctor().getName(),saved.getPatient().getFullName());
         */

        return new AppointmentDTO(saved);
        
    }

     public AppointmentDTO updateAppointment(int appointmentId, LocalDateTime newStart, LocalDateTime newEnd, String newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

       if (newStart != null && newEnd != null) {
        // Find any overlapping appointments for this doctor (excluding the current appointment)
        List<Appointment> conflicts = appointmentRepository
                .findByDoctorIdAndStartTimeBetween(appointment.getDoctor().getId(), newStart, newEnd)
                .stream()
                .filter(a -> a.getId() != appointmentId) // exclude current appointment
                .toList();

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Doctor is already booked for this time slot");
        }

        appointment.setStartTime(newStart);
        appointment.setEndTime(newEnd);
      }
        if (newStatus != null) {
            appointment.setStatus(newStatus);
        }

        Appointment updated = appointmentRepository.save(appointment);
        return new AppointmentDTO(updated);
    }

     public void cancelAppointment(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }

    
}
    

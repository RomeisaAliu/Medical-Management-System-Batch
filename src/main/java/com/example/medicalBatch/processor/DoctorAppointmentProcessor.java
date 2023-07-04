package com.example.medicalBatch.processor;

import com.example.medicalmanagement.model.Appointment;
import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.repository.AppointmentRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DoctorAppointmentProcessor implements ItemProcessor<User, List<Appointment>> {

    private final AppointmentRepository appointmentRepository;

    public DoctorAppointmentProcessor(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Appointment> process(User doctor) {
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(24);
        return appointmentRepository.findByDoctorIdAndAppointmentDateStartTimeBeforeAndAppointmentDateEndTimeAfter(
                doctor.getId(), startDateTime, endDateTime);
    }
}

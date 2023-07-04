package com.example.medicalBatch.config;

import com.example.medicalmanagement.model.Appointment;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class AppointmentProcessor implements ItemProcessor<Appointment, Appointment> {
    @Override
    public Appointment process(Appointment appointment) throws Exception {
        return appointment;
    }
}

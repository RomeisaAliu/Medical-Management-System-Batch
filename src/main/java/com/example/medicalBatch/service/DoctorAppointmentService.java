package com.example.medicalBatch.service;

import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DoctorAppointmentService {
    final UserDto userDto;

    final JavaMailSender emailSender;

    @Autowired
    public DoctorAppointmentService(UserDto userDto, JavaMailSender emailSender) {
        this.userDto = userDto;
        this.emailSender = emailSender;
    }

    public void sendAppointmentNotifications(UserDto user, List<Appointment> appointments) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("romeisaaliu1@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Your Appointments for the Next 24 Hours");
        StringBuilder body = new StringBuilder();
        body.append("Dear Dr. ")
                .append(user.getEmail())
                .append(",\n\nHere is a list of your appointments for the next 24 hours:\n\n");

        if (appointments.isEmpty()) {
            body.append("No appointments scheduled.");
        } else {
            for (Appointment appointment : appointments) {
                body.append("- ").append(appointment.toString()).append("\n");
            }
        }

        message.setText(body.toString());
        emailSender.send(message);
    }
}

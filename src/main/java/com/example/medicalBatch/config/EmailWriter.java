package com.example.medicalBatch.config;

import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.Appointment;
import com.example.medicalmanagement.repository.AppointmentRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
 public abstract class EmailWriter implements ItemWriter<Appointment> {

    private final JavaMailSender javaMailSender;

    private final UserDto userDto;
    @Autowired
    private AppointmentRepository appointmentRepository;

@Autowired
    public EmailWriter(JavaMailSender javaMailSender, UserDto userDto) {
        this.javaMailSender = javaMailSender;
        this.userDto = userDto;
}


    public void write(List<? extends Appointment> appointments) throws Exception {
        for (Appointment appointment : appointments) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(userDto.getEmail());
            helper.setSubject("Appointment for " + appointment.getAppointmentDateStartTime());
            helper.setText("Hi " + userDto.getFullName() + ",\n\n"
                    + "This is a reminder for your appointment on " + appointment.getAppointmentDateStartTime()
                    + ".\n\nRegards");

            javaMailSender.send(message);
        }
    }
}

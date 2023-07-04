package com.example.medicalBatch.item;

import com.example.medicalmanagement.model.Appointment;
import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.repository.AppointmentRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Component
public class AppointmentDoctorProcessor implements ItemProcessor<User, MimeMessagePreparator> {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public MimeMessagePreparator process(User doctor) throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime next24HoursDateTime = currentDateTime.plusDays(1);

        Long doctorId = -1L;

        List<Appointment> appointments = appointmentRepository.findNext24HoursAppointments(
                doctorId, currentDateTime, next24HoursDateTime
        );


        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(doctor.getEmail());
            messageHelper.setSubject("Appointments for " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            messageHelper.setText(getEmailContent(appointments, doctor.getFullName()), true);
        };


        return preparator;
    }

    private String getEmailContent(List<Appointment> appointments, String doctorName) {
        StringBuilder content = new StringBuilder();
        content.append("<html><body>");
        content.append("<h2>Hi ").append(doctorName).append(",</h2>");
        content.append("<p>Here are your next appointments:</p>");
        content.append("<ul>");
        for (Appointment appointment : appointments) {
            String appointmentDateTime = appointment.getAppointmentDateStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
            String patientFullName = appointment.getPatient().getFullName();
            content.append("<li>").append(appointmentDateTime).append(" ").append(patientFullName).append("</li>");
        }
        content.append("</ul>");
        content.append("<p>Regards,</p>");
        content.append("</body></html>");
        return content.toString();
    }
}
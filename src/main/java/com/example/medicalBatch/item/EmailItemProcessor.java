package com.example.medicalBatch.item;

import com.example.medicalmanagement.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailItemProcessor implements ItemProcessor<User, MimeMessage> {
    private final JavaMailSender mailSender;

    public EmailItemProcessor(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public MimeMessage process(User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setSubject("Hi " + user.getFullName());
        helper.setText("here are you next appointments: ");

        return message;
    }
}

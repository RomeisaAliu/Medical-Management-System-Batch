package com.example.medicalBatch.send;

import org.springframework.batch.item.ItemReader;
import org.springframework.mail.SimpleMailMessage;

public class EmailReader implements ItemReader<SimpleMailMessage> {

    private boolean sent = false;

    @Override
    public SimpleMailMessage read() {
        if (!sent) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("romeisaaliu1@gmail.com");
            message.setSubject("Test Email");
            message.setText("This is a test email sent using Spring Batch and Spring Email.");
            sent = true;
            return message;
        }
        return null;
    }
}

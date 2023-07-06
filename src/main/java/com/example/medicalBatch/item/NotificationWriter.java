package com.example.medicalBatch.item;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public abstract class NotificationWriter implements ItemWriter<MimeMessage> {
    @Autowired
    private JavaMailSender mailSender;


    public void write(List<? extends MimeMessage> messages) throws Exception {
        List<MimeMessage> successfullySentMessages = new ArrayList<>();
        List<MimeMessage> failedMessages = new ArrayList<>();

        for (MimeMessage message : messages) {
            try {
                mailSender.send(message);
                successfullySentMessages.add(message);
            } catch (Exception e) {
                failedMessages.add(message);
            }
        }

        if (!successfullySentMessages.isEmpty()) {
            System.out.println("Successfully sent emails: " + successfullySentMessages.size());
        }

        if (!failedMessages.isEmpty()) {
            System.out.println("Failed to send emails: " + failedMessages.size());
        }
    }
}

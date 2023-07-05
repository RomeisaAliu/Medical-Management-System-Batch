package com.example.medicalBatch.item;


import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class EmailItemWriter implements ItemWriter<MimeMessagePreparator> {

    private JavaMailSender mailSender;

    public EmailItemWriter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void write(Chunk<? extends MimeMessagePreparator> chunk) throws Exception {
        for (MimeMessagePreparator preparator : chunk) {
            mailSender.send(preparator);
        }
    }
}

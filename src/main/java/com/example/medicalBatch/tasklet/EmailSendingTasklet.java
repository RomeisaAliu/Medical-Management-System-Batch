package com.example.medicalBatch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EmailSendingTasklet implements Tasklet {

    private final EmailSender emailSender;

    @Value("${recipient.email}")
    private String recipientEmail;
    public EmailSendingTasklet(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        String subject = "Spring Batch Job Report";
        String content = "The Spring Batch job has completed successfully.";
        emailSender.sendEmail(recipientEmail, subject, content);

        return RepeatStatus.FINISHED;
    }
}

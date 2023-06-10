package com.example.medicalBatch.listener;

import com.example.medicalmanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomStepExecutionListener implements StepExecutionListener {

    private final UserService userService;

    @Autowired
    public CustomStepExecutionListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Deleting all old records from the 'users' table");
        userService.deleteAllUsers();
        log.info("All users have been deleted before the step");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (ExitStatus.COMPLETED.equals(stepExecution.getExitStatus())) {
            log.info("The step has finished with status: {}", stepExecution.getExitStatus());
        } else {
            log.info("Something bad has happened after the step: {}", stepExecution.getExitStatus());
        }
        return stepExecution.getExitStatus();
    }
}

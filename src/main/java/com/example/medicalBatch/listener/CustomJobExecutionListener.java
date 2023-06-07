package com.example.medicalBatch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJobExecutionListener implements JobExecutionListener {


    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("--- Before job execution");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("--- After job computing the business logic");

    }
}
package com.example.medicalBatch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor

public class SkipListeners implements SkipListener<String, Number> {

    @Override
    public void onSkipInRead(Throwable t) {
        log.info("StepSkipListener - onSkipInRead");
    }

    @Override
    public void onSkipInWrite(Number item, Throwable t) {
       log.info("StepSkipListener - afterWrite");
    }

    @Override
    public void onSkipInProcess(String item, Throwable t) {
        log.info("StepSkipListener - onWriteError");
    }
}
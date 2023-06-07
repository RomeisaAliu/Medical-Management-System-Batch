package com.example.medicalBatch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j

public class ItemReaderListener implements ItemReadListener<String> {

    @Override
    public void beforeRead() {
        log.info("Before Item Read");
    }

    @Override
    public void afterRead(String item) {
        log.info("After Item Read: " + item);
    }

    @Override
    public void onReadError(Exception ex) {
        log.info("Item Read Error: " + ex.getMessage());
    }
}

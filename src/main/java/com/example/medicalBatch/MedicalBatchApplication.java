package com.example.medicalBatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableBatchProcessing
@SpringBootApplication
public class MedicalBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalBatchApplication.class, args);
	}

}

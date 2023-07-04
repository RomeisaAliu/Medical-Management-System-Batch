package com.example.medicalBatch;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableBatchProcessing
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.medicalmanagement.dto","com.example.medicalmanagement.repository"})
public class MedicalBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalBatchApplication.class, args);
	}

}

package com.cob.ppa;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PatientPaymentAutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientPaymentAutomationApplication.class, args);
    }

}

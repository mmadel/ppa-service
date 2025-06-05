package com.cob.ppa.service.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobBatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job processClinicDataJob(
            ImportJobListener importJobListener,
            Step validateStep,
            Step processBenefitsStep,
            Step processDocumentUnitsStep,
            Step processPaymentStep,
            Step cleanupStep,
            Step patientRecordStep,
            Step clinicEligibilityRecordStep) {
        return jobBuilderFactory.get("processClinicDataJob")
                .listener(importJobListener)
                .start(validateStep)
                .next(processBenefitsStep)
                .next(processDocumentUnitsStep)
                .next(processPaymentStep)
                .next(cleanupStep)
                .next(patientRecordStep)
                .next(clinicEligibilityRecordStep)
                .build();
    }
}

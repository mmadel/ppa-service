package com.cob.ppa.service.batch.configuration;

import com.cob.ppa.exception.HandleBatchException;
import com.cob.ppa.exception.business.BatchUploadException;
import com.cob.ppa.service.validator.SheetValidatorService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class ValidateFilesConfiguration {

    @Autowired
    SheetValidatorService sheetValidatorService;
    @Autowired
    HandleBatchException handleBatchException;
    @Bean
    public Step validateStep(StepBuilderFactory steps) {
        return steps.get("validateStep")
                .tasklet(validateTasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet validateTasklet() {
        return (contribution, chunkContext) -> {
            JobParameters jobParameters = chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getJobParameters();
            String prmId = jobParameters.getString("pmrb-id");
            String benefitFile = jobParameters.getString("benefitFilePath");
            String documentFile = jobParameters.getString("documentUnitsFilePath");
            String paymentFile = jobParameters.getString("paymentFilePath");
            boolean hasError = false;
            try {
                sheetValidatorService.validate(benefitFile, documentFile, paymentFile);
            } catch (BatchUploadException ex) {
                hasError = true;
                handleBatchException.handle(contribution, chunkContext, ex);
            }
            return RepeatStatus.FINISHED;

        };
    }
}

package com.cob.ppa.service.batch.configuration;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class CleanTmpFiles {
    @Bean
    public Step cleanupStep(StepBuilderFactory steps) {
        return steps.get("cleanupStep")
                .tasklet(cleanupTasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet cleanupTasklet() {
        return (contribution, chunkContext) -> {
            JobParameters jobParameters = chunkContext.getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getJobParameters();

            String benefitTmpFile = jobParameters.getString("benefitFilePath");
            String documentTmpFile = jobParameters.getString("documentUnitsFilePath");
            String paymentFile = jobParameters.getString("paymentFilePath");
            deleteFile(benefitTmpFile);
            deleteFile(documentTmpFile);
            deleteFile(paymentFile);
            return RepeatStatus.FINISHED;
        };
    }

    private void deleteFile(String tmpFile){
        if (tmpFile != null) {
            Path path = Paths.get(tmpFile);
            if (Files.exists(path)) {
                try {
                    Files.delete(path);
                    System.out.println("Deleted temporary file: " + path);
                } catch (IOException e) {
                    System.err.println("Failed to delete temporary file: " + e.getMessage());
                }
            }
        }
    }
}

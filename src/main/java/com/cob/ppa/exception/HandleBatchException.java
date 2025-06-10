package com.cob.ppa.exception;

import com.cob.ppa.exception.business.BatchUploadException;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class HandleBatchException {
    @Autowired
    ResourceBundleMessageSource messageSource;
    public  RepeatStatus handle(StepContribution contribution, ChunkContext chunkContext, BatchUploadException ex) throws Exception {
        String errorMessage = messageSource.getMessage(ex.getCode(), ex.getParameters(), Locale.ENGLISH);
        contribution.setExitStatus(new ExitStatus("FAILED"));
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        jobExecution.getExecutionContext().putString("errorMessage",errorMessage);
        throw new Exception(errorMessage);
    }
}

package com.cob.ppa.service.batch;

import com.cob.ppa.service.batch.status.PatientRecordImportTracker;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ImportJobListener implements JobExecutionListener {
    @Autowired
    private PatientRecordImportTracker tracker;

    private long jobId;
    @Override
    public void beforeJob(JobExecution jobExecution) {

        String pmrbId = jobExecution.getJobParameters().getString("pmrb-id");
        jobId = tracker.insertPendingStatus(pmrbId);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String pmrbId = jobExecution.getJobParameters().getString("pmrb-id");
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            tracker.setReady(pmrbId, com.cob.ppa.constant.BatchStatus.Ready.name());
        } else {
            String errorMessage = jobExecution.getAllFailureExceptions().stream()
                    .map(Throwable::getMessage)
                    .collect(Collectors.joining("; "));
            tracker.setFailed(pmrbId,  com.cob.ppa.constant.BatchStatus.Failed.name(),errorMessage);
        }
    }
}

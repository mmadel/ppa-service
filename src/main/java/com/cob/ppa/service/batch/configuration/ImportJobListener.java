package com.cob.ppa.service.batch.configuration;

import com.cob.ppa.constant.RequestStatus;
import com.cob.ppa.service.tracker.RequestTrackerService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ImportJobListener implements JobExecutionListener {

    @Autowired
    RequestTrackerService requestTrackerService;

    private long jobId;
    @Override
    public void beforeJob(JobExecution jobExecution) {

        String pmrbId = jobExecution.getJobParameters().getString("pmrb-id");
//        jobId = tracker.insertPendingStatus(pmrbId);
        requestTrackerService.initRequest(pmrbId);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String pmrbId = jobExecution.getJobParameters().getString("pmrb-id");
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            requestTrackerService.setCompleted(pmrbId);
        } else {
            String errorMessage = jobExecution.getAllFailureExceptions().stream()
                    .map(Throwable::getMessage)
                    .collect(Collectors.joining("; "));
            requestTrackerService.setFailed(pmrbId,errorMessage);
        }
    }
}

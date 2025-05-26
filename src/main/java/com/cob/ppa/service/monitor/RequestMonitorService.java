package com.cob.ppa.service.monitor;

import com.cob.ppa.constant.ImportRequestStatus;
import com.cob.ppa.entity.logging.RequestLogEntry;
import com.cob.ppa.repository.RequestLogEntryRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestMonitorService {
    @Autowired
    RequestLogEntryRecordRepository requestLogEntryRecordRepository;


    public void createLog(String pmrbId, String username) {
        RequestLogEntry logEntry = new RequestLogEntry();
        logEntry.setUsername(username);
        logEntry.setRequestId(pmrbId);
        logEntry.setStatus(ImportRequestStatus.Processing.getValue());
        requestLogEntryRecordRepository.save(logEntry);
    }

    @Async("taskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateLog(String pmrbId, Integer status) {
        requestLogEntryRecordRepository.findByRequestId(pmrbId)
                .ifPresent(requestLogEntry -> {
                    requestLogEntry.setStatus(status);
                    requestLogEntryRecordRepository.save(requestLogEntry);
                });
    }
}

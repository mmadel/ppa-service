package com.cob.ppa.service.activity.log;

import com.cob.ppa.constant.RequestStatus;
import com.cob.ppa.dto.RequestLogEntryDTO;
import com.cob.ppa.entity.logging.RequestLogEntry;
import com.cob.ppa.repository.RequestLogEntryRecordRepository;
import com.cob.ppa.response.patient.record.builder.PatientRecordBuilder;
import com.cob.ppa.response.patient.record.model.PatientRecordJobResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindActivityLogService {
    @Autowired
    RequestLogEntryRecordRepository requestLogEntryRecordRepository;
    @Autowired
    ModelMapper mapper;

    public PatientRecordJobResponse search(Pageable pageable , RequestStatus status , String requestId, String userName, LocalDateTime start , LocalDateTime end){
        Page<RequestLogEntry> pages = requestLogEntryRecordRepository.search(pageable,status,requestId,userName,start,end);
        List<RequestLogEntryDTO> models = pages.getContent()
                .stream()
                .map(requestLogEntry -> mapper.map(requestLogEntry, RequestLogEntryDTO.class))
                .collect(Collectors.toList());
        long total = (pages).getTotalElements();
        return PatientRecordBuilder.build(total,models);
    }

}

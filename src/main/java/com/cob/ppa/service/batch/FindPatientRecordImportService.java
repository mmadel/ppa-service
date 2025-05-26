package com.cob.ppa.service.batch;

import com.cob.ppa.constant.BatchStatus;
import com.cob.ppa.dto.PatientRecordImportJobDTO;
import com.cob.ppa.entity.PatientRecordImportJob;
import com.cob.ppa.repository.PatientRecordJobRepository;
import com.cob.ppa.response.patient.record.builder.PatientRecordBuilder;
import com.cob.ppa.response.patient.record.model.PatientRecordJobResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindPatientRecordImportService {
    @Autowired
    PatientRecordJobRepository patientRecordJobRepository;
    @Autowired
    ModelMapper mapper;

    public PatientRecordJobResponse find(Pageable pageable){
        Page<PatientRecordImportJob> pages = patientRecordJobRepository.findAll(pageable);
        List<PatientRecordImportJobDTO> models = pages.getContent()
                .stream()
                .map(patientRecordImportJob -> mapper.map(patientRecordImportJob, PatientRecordImportJobDTO.class))
                .collect(Collectors.toList());
        long total = (pages).getTotalElements();
        return PatientRecordBuilder.build(total,models);
    }

        public PatientRecordJobResponse search(Pageable pageable , BatchStatus status, String pmrbId, LocalDateTime start, LocalDateTime end){
        Page<PatientRecordImportJob> pages = patientRecordJobRepository.search(pageable, status,pmrbId,start,end);
        List<PatientRecordImportJobDTO> models = pages.getContent()
                .stream()
                .map(patientRecordImportJob -> mapper.map(patientRecordImportJob, PatientRecordImportJobDTO.class))
                .collect(Collectors.toList());
        long total = (pages).getTotalElements();
        return PatientRecordBuilder.build(total,models);
    }

}

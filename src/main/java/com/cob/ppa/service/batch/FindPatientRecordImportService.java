package com.cob.ppa.service.batch;

import com.cob.ppa.entity.PatientRecordImportJob;
import com.cob.ppa.repository.PatientRecordJobRepository;
import com.cob.ppa.response.patient.record.builder.PatientRecordBuilder;
import com.cob.ppa.response.patient.record.model.PatientRecordJobResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindPatientRecordImportService {
    @Autowired
    PatientRecordJobRepository patientRecordJobRepository;

    public PatientRecordJobResponse find(Pageable pageable){
        Page<PatientRecordImportJob> pages = patientRecordJobRepository.findAll(pageable);
        List<PatientRecordImportJob> entities = pages.getContent();
        long total = (pages).getTotalElements();
        return PatientRecordBuilder.build(total,entities);
    }
}

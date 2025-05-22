package com.cob.ppa.response.patient.record.builder;


import com.cob.ppa.entity.PatientRecordImportJob;
import com.cob.ppa.response.patient.record.model.PatientRecordJobResponse;

import java.util.List;


public class PatientRecordBuilder {
    public static PatientRecordJobResponse build(long total, List<PatientRecordImportJob> records) {
        return PatientRecordJobResponse.builder()
                .number_of_records(records.size())
                .number_of_matching_records((int) total)
                .records(records)
                .build();
    }
}

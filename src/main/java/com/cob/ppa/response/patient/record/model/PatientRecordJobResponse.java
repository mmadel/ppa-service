package com.cob.ppa.response.patient.record.model;

import com.cob.ppa.entity.PatientRecordImportJob;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Builder
public class PatientRecordJobResponse {
    Integer number_of_records;
    Integer number_of_matching_records;

    List<PatientRecordImportJob> records;
}

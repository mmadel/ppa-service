package com.cob.ppa.response.patient.record.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PatientMedicalRecordUploadResponse {
    private String pmrbId;
    private boolean hasError;
    private String errorMessage;
}

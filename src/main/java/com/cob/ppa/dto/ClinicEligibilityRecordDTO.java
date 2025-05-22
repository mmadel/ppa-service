package com.cob.ppa.dto;

import com.cob.ppa.constant.ClinicImportStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class ClinicEligibilityRecordDTO {
    private Long id;
    private String clinicName;
    private ClinicImportStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String pmrbId;
    private String errorMessage;
    private byte[] eligibilityFile;
    private LocalDate lastUpdate;
}

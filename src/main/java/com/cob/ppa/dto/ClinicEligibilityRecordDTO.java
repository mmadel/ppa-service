package com.cob.ppa.dto;

import com.cob.ppa.constant.ClinicImportStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
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
    public ClinicEligibilityRecordDTO(Long id,
                                      String clinicName,
                                      ClinicImportStatus status,
                                      LocalDateTime createdAt,
                                      LocalDateTime completedAt,
                                      String pmrbId,
                                      String errorMessage,
                                      LocalDate lastUpdate) {
        this.id = id;
        this.clinicName = clinicName;
        this.status = status;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.pmrbId = pmrbId;
        this.errorMessage = errorMessage;
        this.lastUpdate = lastUpdate;

    }

}

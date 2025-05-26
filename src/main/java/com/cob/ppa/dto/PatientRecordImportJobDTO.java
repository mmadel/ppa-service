package com.cob.ppa.dto;

import com.cob.ppa.constant.BatchStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PatientRecordImportJobDTO {
    private Long id;

    private String name;
    private BatchStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String pmrbId;
    private String errorMessage;
    private String userName;
}

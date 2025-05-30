package com.cob.ppa.dto;

import com.cob.ppa.constant.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ClinicBatchDto {
    private Long id;
    private String name;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String referenceKey;
}

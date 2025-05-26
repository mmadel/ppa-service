package com.cob.ppa.dto;

import com.cob.ppa.constant.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class RequestLogEntryDTO {
    private Long id;
    private String username;

    private String requestId;

    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String errorMessage;
}

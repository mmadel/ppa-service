package com.cob.ppa.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DocumentDto {
    private String clinicName;
    private String caseName;
    private String visitId;
    private String visitStatus;
    private String emrId;
    private LocalDate dos; // Date of Service
    private String cpt;
    private Integer units;
}

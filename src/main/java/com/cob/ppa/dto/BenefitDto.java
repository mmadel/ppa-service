package com.cob.ppa.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BenefitDto {

    private String clinicName;
    private String emrId;
    private String patientName;
    private LocalDate patientDob;
    private String appointmentType;
    private LocalDate appointmentDate;
    private String visitStatus;
    private String primaryInsurance;
    private String primaryInsurancePolicyNumber;
    private String secondaryInsurance;
    private String secondaryInsurancePolicyNumber;
}

package com.cob.ppa.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class PatientMedicalRecordDTO {
    private String clinicName;
    private String emrId;
    private String patientName;
    private String insuranceName;
    private String insuranceId;
    private LocalDate dos;
    private LocalDate dob;
    private Double clientPayment;
    private BigDecimal chargeAmount;
    private String visitId;
    private String visitStatus;
    private String secondaryInsuranceName;
    private String secondaryInsuranceId;

}

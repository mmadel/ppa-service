package com.cob.ppa.dto;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PaymentDto {
    private String clinicName;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateOfService;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateOfTransaction;


    private String provider;


    private String collectedBy;


    private String patientName;


    private String emrPatientId;

    private String caseName;


    private String chargeType;

    private String chargeDescription;


    private BigDecimal amountDue;


    private Double amountPaid;


    private String paymentMethod;

    private String creditType;

    private String paymentReferenceNumber; // For "Credit Card/Check #/Authorization #"

    private String primaryInsurance;

    private String secondaryInsurance;

    private String caseStatus;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate nextAppointment;
}
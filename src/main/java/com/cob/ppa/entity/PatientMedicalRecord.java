package com.cob.ppa.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(
        name = "patient_medical_record",
        indexes = {
                @Index(name = "idx_emr_id", columnList = "emr_id"),
                @Index(name = "idx_appointment_date", columnList = "appointment_date"),
                @Index(name = "idx_emr_id_appointment_date", columnList = "emr_id, appointment_date")
        },
        uniqueConstraints = @UniqueConstraint(columnNames = {"emr_id", "appointment_date"})
)
@IdClass(PatientMedicalRecordId.class)
@Getter
@Setter
public class PatientMedicalRecord {

    @Id
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;
    @Id
    @Column(name = "emr_id", nullable = false, length = 255)
    private String emrId;

    @Column(name = "charge_amount", precision = 19, scale = 2)
    private BigDecimal chargeAmount;

    @Column(name = "client_payment")
    private Double clientPayment;

    @Column(name = "insurance_id", length = 255)
    private String insuranceId;

    @Column(name = "insurance_name", length = 255)
    private String insuranceName;

    @Column(name = "patient_name", length = 255)
    private String patientName;

    @Column(name = "secondary_insurance_id", length = 255)
    private String secondaryInsuranceId;

    @Column(name = "secondary_insurance_name", length = 255)
    private String secondaryInsuranceName;

    @Column(name = "visit_id", length = 255)
    private String visitId;

    @Column(name = "visit_status", length = 255)
    private String visitStatus;

    @Column(name = "clinic_name")
    private String clinicName;

}

package com.cob.ppa.entity;

import com.cob.ppa.constant.ClinicImportStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "clinic_eligibility_record")
public class ClinicEligibilityRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clinicName;

    @Enumerated(EnumType.STRING)
    private ClinicImportStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @Column(nullable = false , name = "pmrb_id")
    private String pmrbId;

    @Column(name = "error_message" ,length = 2048,nullable = true)
    private String errorMessage;

    @Column(name = "last_update")
    private LocalDate lastUpdate;
    @Lob
    private byte[] eligibilityFile;
}

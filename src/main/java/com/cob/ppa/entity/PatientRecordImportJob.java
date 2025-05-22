package com.cob.ppa.entity;

import com.cob.ppa.constant.BatchStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "patient_record_import_job", indexes = {
        @Index(name = "idx_clinic_batch_reference_key", columnList = "pmrb_id")
})
public class PatientRecordImportJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private BatchStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @Column(unique = true, nullable = false , name = "pmrb_id")
    private String pmrbId;

    @Column(name = "error_message" ,length = 2048,nullable = true)
    private String errorMessage;
}

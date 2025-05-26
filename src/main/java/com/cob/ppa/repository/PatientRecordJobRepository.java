package com.cob.ppa.repository;

import com.cob.ppa.constant.BatchStatus;
import com.cob.ppa.constant.ClinicImportStatus;
import com.cob.ppa.entity.PatientRecordImportJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PatientRecordJobRepository extends JpaRepository<PatientRecordImportJob, Long> {

    Page<PatientRecordImportJob>  findAll(Pageable pageable);

    @Query("SELECT prm FROM PatientRecordImportJob prm WHERE " +
            "(:pmrbId IS NULL OR prm.pmrbId = :pmrbId) AND " +
            "(:status IS NULL OR prm.status = :status) AND " +
            "(:start IS NULL OR :end IS NULL OR prm.createdAt BETWEEN :start AND :end)")
    Page<PatientRecordImportJob> search(Pageable pageable,@Param("status") BatchStatus status,
                                        @Param("pmrbId") String pmrbId,
                                        @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);
}

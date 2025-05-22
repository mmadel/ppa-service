package com.cob.ppa.repository;

import com.cob.ppa.constant.ClinicImportStatus;
import com.cob.ppa.entity.ClinicEligibilityRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClinicEligibilityRecordRepository extends JpaRepository<ClinicEligibilityRecord, Long> {
    Optional<List<ClinicEligibilityRecord>> findByPmrbId(String pmrbId);

    @Query("SELECT c FROM ClinicEligibilityRecord c WHERE c.id IN :ids")
    List<ClinicEligibilityRecord> findAllByIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT cer FROM ClinicEligibilityRecord cer WHERE " +
            "(:pmrbId IS NULL OR cer.pmrbId = :pmrbId) AND " +
            "(:location IS NULL OR cer.clinicName = :location) AND " +
            "(:status IS NULL OR cer.status = :status) AND " +
            "(:lastUpdate IS NULL OR cer.lastUpdate = :lastUpdate)")
    List<ClinicEligibilityRecord> search(@Param("location") String location,
                                         @Param("status") ClinicImportStatus status,
                                         @Param("pmrbId") String pmrbId,
                                         @Param("lastUpdate") LocalDate lastUpdate);

}

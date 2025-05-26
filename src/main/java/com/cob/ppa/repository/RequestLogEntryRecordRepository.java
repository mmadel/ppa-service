package com.cob.ppa.repository;

import com.cob.ppa.constant.RequestStatus;
import com.cob.ppa.entity.logging.RequestLogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RequestLogEntryRecordRepository extends JpaRepository<RequestLogEntry,Long> {

    @Query("SELECT rl FROM RequestLogEntry rl WHERE " +
            "(:requestId IS NULL OR rl.requestId = :requestId) AND " +
            "(:status IS NULL OR rl.status = :status) AND " +
            "(:start IS NULL OR :end IS NULL OR rl.createdAt BETWEEN :start AND :end) AND " +
            "(:userName IS NULL OR rl.username = :userName) ")
    Page<RequestLogEntry> search(Pageable pageable, @Param("status") RequestStatus status,
                                 @Param("requestId") String requestId,
                                 @Param("userName") String userName,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);
}

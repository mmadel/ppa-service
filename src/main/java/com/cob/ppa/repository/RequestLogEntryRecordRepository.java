package com.cob.ppa.repository;

import com.cob.ppa.entity.logging.RequestLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestLogEntryRecordRepository extends JpaRepository<RequestLogEntry,Long> {
    Optional<RequestLogEntry> findByRequestId(String id);
}

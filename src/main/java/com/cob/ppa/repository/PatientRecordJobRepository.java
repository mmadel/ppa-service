package com.cob.ppa.repository;

import com.cob.ppa.entity.PatientRecordImportJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRecordJobRepository extends JpaRepository<PatientRecordImportJob, Long> {

    Page<PatientRecordImportJob>  findAll(Pageable pageable);
}

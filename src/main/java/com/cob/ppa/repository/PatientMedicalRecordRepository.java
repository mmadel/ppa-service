package com.cob.ppa.repository;


import com.cob.ppa.entity.PatientMedicalRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PatientMedicalRecordRepository extends CrudRepository<PatientMedicalRecord, Long> {

}

package com.cob.ppa.service.patient.record;

import com.cob.ppa.dto.PatientMedicalRecordDTO;
import com.cob.ppa.entity.PatientMedicalRecord;
import com.cob.ppa.repository.PatientMedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FindClinicEligibility {
    @Autowired
    PatientMedicalRecordRepository patientMedicalRecordRepository;

    public Map<String, List<PatientMedicalRecordDTO>> getRecordsGroupedByClinicName(String batchId) {
        List<PatientMedicalRecord> records =null;

        return records.stream()
                .map(this::toDto)
                .collect(Collectors.groupingBy(PatientMedicalRecordDTO::getClinicName));
    }
    private PatientMedicalRecordDTO toDto(PatientMedicalRecord record) {
        PatientMedicalRecordDTO dto = new PatientMedicalRecordDTO();

        dto.setDos(record.getAppointmentDate());
        dto.setEmrId(record.getEmrId());
        dto.setChargeAmount(record.getChargeAmount());
        dto.setClientPayment(record.getClientPayment());
        dto.setInsuranceId(record.getInsuranceId());
        dto.setInsuranceName(record.getInsuranceName());
        dto.setPatientName(record.getPatientName());
        dto.setSecondaryInsuranceId(record.getSecondaryInsuranceId());
        dto.setSecondaryInsuranceName(record.getSecondaryInsuranceName());
        dto.setVisitId(record.getVisitId());
        dto.setVisitStatus(record.getVisitStatus());
        dto.setClinicName(record.getClinicName());
        return dto;
    }
}

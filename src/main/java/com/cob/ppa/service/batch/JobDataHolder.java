package com.cob.ppa.service.batch;

import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.dto.DocumentDto;
import com.cob.ppa.dto.PatientMedicalRecordDTO;
import com.cob.ppa.dto.PaymentDto;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JobDataHolder {
    private final Map<String, List<?>> storage = new ConcurrentHashMap<>();
    private List<PatientMedicalRecordDTO> patientMedicalRecord = new ArrayList<>();

    @PostConstruct
    public void init() {
        storage.put("benefits", new ArrayList<BenefitDto>());
        storage.put("documents", new ArrayList<DocumentDto>());
        storage.put("payments", new ArrayList<PaymentDto>());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getData(String key) {
        return (List<T>) storage.get(key);
    }

    public List<PatientMedicalRecordDTO> getPatientMedicalRecord() {
        return patientMedicalRecord;
    }

    public void setPatientMedicalRecord(List<PatientMedicalRecordDTO> patientMedicalRecord) {
        this.patientMedicalRecord = patientMedicalRecord;
    }

    public synchronized void clearStorage() {
        storage.values().forEach(List::clear);

    }
    public synchronized void clearPatientMedicalRecord() {
        patientMedicalRecord.clear();
    }
}
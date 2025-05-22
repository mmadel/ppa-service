package com.cob.ppa.service.patient.record;

import com.cob.ppa.constant.PriceList;
import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.dto.DocumentDto;
import com.cob.ppa.dto.PatientMedicalRecordDTO;
import com.cob.ppa.dto.PaymentDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BuildPatientMedicalRecord {
    private static String compositeKey(String emrId, String dos) {
        return emrId + "_" + dos;
    }

    public List<PatientMedicalRecordDTO> create(List<BenefitDto> benefits, List<DocumentDto> documents, List<PaymentDto> payments) {
        // Step 1: Build index for documents by key: id|dos → List<Document>
        Map<String, List<DocumentDto>> documentMap = documents.stream()
                .collect(Collectors.groupingBy(d -> compositeKey(d.getEmrId(), d.getDos().toString())));

        Map<String, List<PaymentDto>> paymentMap = payments.stream()
                .collect(Collectors.groupingBy(p -> compositeKey(p.getEmrPatientId(), p.getDateOfService().toString())));
        List<PatientMedicalRecordDTO> records = new ArrayList<>();
        for (BenefitDto benefit : benefits) {
            String key = compositeKey(benefit.getEmrId(), benefit.getAppointmentDate().toString());
            List<DocumentDto> matchingDocs = documentMap.getOrDefault(key, Collections.emptyList());
            List<PaymentDto> matchingPayments = paymentMap.getOrDefault(key, Collections.emptyList());
            Double patientPayments = matchingPayments.stream()
                    .mapToDouble(PaymentDto::getAmountPaid)
                    .sum();

            if (matchingDocs.isEmpty())
                records.add(generateRecord(benefit, patientPayments));
            else
                records.add(generateRecords(benefit, patientPayments, matchingDocs));
        }

        return records;
    }


    private PatientMedicalRecordDTO generateRecord(BenefitDto benefitDto, Double patientPayments) {
        PatientMedicalRecordDTO record = new PatientMedicalRecordDTO();
        record.setClinicName(benefitDto.getClinicName());
        record.setEmrId(benefitDto.getEmrId());
        record.setPatientName(benefitDto.getPatientName());
        record.setDos(benefitDto.getAppointmentDate());
        record.setInsuranceName(benefitDto.getPrimaryInsurance());
        record.setInsuranceId(benefitDto.getPrimaryInsurancePolicyNumber());
        record.setSecondaryInsuranceName(benefitDto.getSecondaryInsurance());
        record.setSecondaryInsuranceId(benefitDto.getSecondaryInsurancePolicyNumber());
        record.setVisitStatus(benefitDto.getVisitStatus());
        record.setClientPayment(patientPayments);

        return record;
    }

    private PatientMedicalRecordDTO generateRecords(BenefitDto benefitDto, Double patientPayments, List<DocumentDto> matchedDocuments) {
        List<PatientMedicalRecordDTO> records = new ArrayList<>();
        for (DocumentDto documentDto : matchedDocuments) {
            PatientMedicalRecordDTO record = generateRecord(benefitDto, patientPayments);
            record.setVisitId(documentDto.getVisitId());
            BigDecimal chargedAmount = BigDecimal.valueOf((documentDto.getUnits() == null ? 0 : documentDto.getUnits()) * (PriceList.prices.get(documentDto.getCpt()) == null ? 0 : PriceList.prices.get(documentDto.getCpt())));
            record.setChargeAmount(chargedAmount);
            records.add(record);
        }
        BigDecimal totalChargeAmount = records.stream()
                .map(PatientMedicalRecordDTO::getChargeAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        PatientMedicalRecordDTO patientMedicalRecordDTO = records.stream()
                .findFirst().get();
        patientMedicalRecordDTO.setChargeAmount(totalChargeAmount);
        return patientMedicalRecordDTO;
    }

}


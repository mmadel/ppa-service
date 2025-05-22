package com.cob.ppa.service.batch;

import com.cob.ppa.constant.ClinicImportStatus;
import com.cob.ppa.dto.ClinicEligibilityRecordDTO;
import com.cob.ppa.entity.ClinicEligibilityRecord;
import com.cob.ppa.repository.ClinicEligibilityRecordRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindClinicEligibilityRecordsService {
    @Autowired
    ClinicEligibilityRecordRepository clinicEligibilityRecordRepository;
    @Autowired
    ModelMapper mapper;
    ;

    public List<ClinicEligibilityRecordDTO> find(String pmrbId) {
        List<ClinicEligibilityRecord> records = clinicEligibilityRecordRepository.findByPmrbId(pmrbId).orElseThrow(
                () -> new IllegalArgumentException("ClinicEligibilityRecord not found for pmrbId :" + pmrbId)
        );
        return mapper(records);
    }

    public List<ClinicEligibilityRecordDTO> search(String clinicName, ClinicImportStatus status , String pmrbId, LocalDate lastUpdate){
        List<ClinicEligibilityRecord> records = clinicEligibilityRecordRepository.search(clinicName,status,pmrbId,lastUpdate);
        return mapper(records);
    }

    private List<ClinicEligibilityRecordDTO> mapper(List<ClinicEligibilityRecord> records){
        return records.stream()
                .map(clinicEligibilityRecord -> {
                    ClinicEligibilityRecordDTO dto = new ClinicEligibilityRecordDTO();
                    dto.setClinicName(clinicEligibilityRecord.getClinicName());
                    dto.setId(clinicEligibilityRecord.getId());
                    dto.setCreatedAt(clinicEligibilityRecord.getCreatedAt());
                    dto.setCompletedAt(clinicEligibilityRecord.getCompletedAt());
                    dto.setStatus(clinicEligibilityRecord.getStatus());
                    dto.setPmrbId(clinicEligibilityRecord.getPmrbId());
                    dto.setErrorMessage(clinicEligibilityRecord.getErrorMessage());
                    dto.setLastUpdate(clinicEligibilityRecord.getLastUpdate());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

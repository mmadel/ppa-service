package com.cob.ppa.service.batch;

import com.cob.ppa.constant.ClinicImportStatus;
import com.cob.ppa.dto.ClinicEligibilityRecordDTO;
import com.cob.ppa.entity.ClinicEligibilityRecord;
import com.cob.ppa.repository.ClinicEligibilityRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public List<ClinicEligibilityRecordDTO> search(String clinicName,
                                                   ClinicImportStatus status,
                                                   String pmrbId,
                                                   LocalDate lastUpdate) {

        long start = System.currentTimeMillis();

        List<ClinicEligibilityRecordDTO> records =
                clinicEligibilityRecordRepository.search(clinicName, status, pmrbId, lastUpdate);

        long end = System.currentTimeMillis();

        log.info("CER search took {} ms | params={{location={}, status={}, pmrbId={}, lastUpdate={}}} | resultCount={}",
                (end - start), clinicName, status, pmrbId, lastUpdate, records.size());

        return records;
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

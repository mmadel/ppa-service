package com.cob.ppa.service;

import com.cob.ppa.constant.ClinicImportStatus;
import com.cob.ppa.dto.PatientMedicalRecordDTO;
import com.cob.ppa.entity.ExcelTemplate;
import com.cob.ppa.exception.business.BatchUploadException;
import com.cob.ppa.repository.ClinicEligibilityRecordRepository;
import com.cob.ppa.repository.ExcelTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenerateClinicEligibilityRecord {
    @Autowired
    ClinicEligibilityRecordRepository clinicEligibilityRecordRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ExcelTemplateRepository templateRepository;
    @Autowired
    CreateClinicEligibilitySheet createClinicEligibilitySheet;

    public void generate(List<PatientMedicalRecordDTO> patientMedicalRecords, String pmrbId) throws BatchUploadException {
        Map<String, List<PatientMedicalRecordDTO>> pmrClinicMap = patientMedicalRecords.stream()
                .collect(Collectors.groupingBy(PatientMedicalRecordDTO::getClinicName));

        boolean isMessingTemplate = templateRepository.findByTemplateName("eligibility_template").isEmpty();
        ExcelTemplate template;
        if (isMessingTemplate)
            handleError();
        else {
            template = templateRepository.findByTemplateName("eligibility_template").get();
            for (var entry : pmrClinicMap.entrySet()) {
                String clinicName = entry.getKey();
                Optional<LocalDate> optional = getNewestDos(entry.getValue());
                long cerId = setInit(clinicName, pmrbId, optional);
                createClinicEligibilitySheet.generate(clinicName, cerId, entry.getValue(), template);
            }
        }
    }

    private long setInit(String clinicName, String pmrbId, Optional<LocalDate> lastUpdate) {
        String sql = "INSERT INTO clinic_eligibility_record (clinic_name,status,completed_at,pmrb_id,last_update) " +
                "VALUES (?, ?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, clinicName);
            ps.setString(2, ClinicImportStatus.Pending.name());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setString(4, pmrbId);
            ps.setObject(5, lastUpdate.orElse(null));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private Optional<LocalDate> getNewestDos(List<PatientMedicalRecordDTO> records) {
        if (records == null || records.isEmpty()) {
            return Optional.empty();
        }

        return records.parallelStream()
                .map(PatientMedicalRecordDTO::getDos)
                .filter(date -> date != null)
                .max(LocalDate::compareTo);
    }

    private void handleError() throws BatchUploadException {
        throw new BatchUploadException(HttpStatus.CONFLICT, BatchUploadException.TEMPLATE_NOT_FOUND, new Object[]{});
    }
}

package com.cob.ppa.service;

import com.cob.ppa.entity.ClinicEligibilityRecord;
import com.cob.ppa.repository.ClinicEligibilityRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ExportClinicEligibilityReportService {
    @Autowired
    ClinicEligibilityRecordRepository clinicEligibilityRecordRepository;
    public byte[] export(List<Long> cerIds) throws IOException {
        List<ClinicEligibilityRecord> records = clinicEligibilityRecordRepository.findAllByIdIn(cerIds);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);
        for (ClinicEligibilityRecord record : records) {
            zipOut.putNextEntry(new ZipEntry(record.getClinicName() + "_Report.xlsx"));
            zipOut.write(record.getEligibilityFile());
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        return baos.toByteArray();
    }
}

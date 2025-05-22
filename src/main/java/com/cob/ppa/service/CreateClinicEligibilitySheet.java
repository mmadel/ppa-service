package com.cob.ppa.service;

import com.cob.ppa.constant.ClinicImportStatus;
import com.cob.ppa.dto.PatientMedicalRecordDTO;
import com.cob.ppa.entity.ExcelTemplate;
import com.cob.ppa.util.ExcelSheetStyle;
import com.cob.ppa.util.ExcelUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.cob.ppa.constant.SheetMonths.MONTH_NAMES;

@Service
@Async
public class CreateClinicEligibilitySheet {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void generate(String clinicName, long cerId, List<PatientMedicalRecordDTO> records, ExcelTemplate template) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(template.getFileContent()))) {
            Map<String, List<PatientMedicalRecordDTO>> dataPerMonth = groupClinicRecordByMonth(records);
            for (var monthData : dataPerMonth.entrySet()) {
                String monthName = monthData.getKey();
                List<PatientMedicalRecordDTO> monthlyClinicData = monthData.getValue();
                Sheet sheet = workbook.getSheet(monthName);
                if (sheet == null) {
                    sheet = workbook.cloneSheet(0);
                    workbook.setSheetName(workbook.getSheetIndex(sheet), monthName);
                }
                fillMonthlySheet(sheet, monthlyClinicData);
            }
            ExcelSheetStyle.preserveHeaderStyles(workbook);
            // Save clinic workbook to zip
            ByteArrayOutputStream clinicBaos = new ByteArrayOutputStream();
            workbook.write(clinicBaos);
            byte[] clinicReportBytes = clinicBaos.toByteArray();
            setReady(cerId, clinicReportBytes);
            zipOut.putNextEntry(new ZipEntry(clinicName + "_Report.xlsx"));
            zipOut.write(clinicBaos.toByteArray());
            zipOut.closeEntry();
            System.out.println("Location generated  : " + clinicName);

        } catch (IOException e) {
            setFailed(cerId, e.getMessage());
            System.out.println("error:export clinic : " + clinicName);
        }

    }

    private Map<String, List<PatientMedicalRecordDTO>> groupClinicRecordByMonth(List<PatientMedicalRecordDTO> clinicRecords) {
        Map<String, List<PatientMedicalRecordDTO>> clinicRecordsByMonth = new HashMap<>();
        for (PatientMedicalRecordDTO record : clinicRecords) {
            int monthIndex = record.getDos().getMonthValue();
            String monthName = MONTH_NAMES[monthIndex - 1];
            clinicRecordsByMonth.computeIfAbsent(monthName, k -> new ArrayList<>())
                    .add(record);
        }
        return clinicRecordsByMonth;
    }

    private void fillMonthlySheet(Sheet sheet, List<PatientMedicalRecordDTO> data) {
        // Clear existing data (keep headers)
        while (sheet.getLastRowNum() > 0) {
            sheet.removeRow(sheet.getRow(sheet.getLastRowNum()));
        }
        // Get header styles
        Row headerRow = sheet.getRow(0);
        CellStyle[] styles = new CellStyle[headerRow.getLastCellNum()];
        for (int i = 0; i < styles.length; i++) {
            styles[i] = headerRow.getCell(i).getCellStyle();
        }
        // Create day separator style
        CellStyle daySeparatorStyle = ExcelSheetStyle.createDaySeparatorStyle(sheet.getWorkbook());

        // Group patients by day
        Map<LocalDate, List<PatientMedicalRecordDTO>> clinicRecordByDay = data.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDos(), // Group by exact date string
                        LinkedHashMap::new, // Maintain insertion order
                        Collectors.toList()
                ));
        int rowNum = 1; // Start after header
        for (Map.Entry<LocalDate, List<PatientMedicalRecordDTO>> dayEntry : clinicRecordByDay.entrySet()) {
            LocalDate dayDate = dayEntry.getKey();
            List<PatientMedicalRecordDTO> dayRecords = dayEntry.getValue();
            // Add day separator row
            // Add patients for this day
            for (PatientMedicalRecordDTO record : dayRecords) {
                Row row = sheet.createRow(rowNum++);
                fillPatientRow(row, record, rowNum - 1, styles); // -1 because we already incremented
            }
        }
    }

    private void fillPatientRow(Row row, PatientMedicalRecordDTO record, int rowNum, CellStyle[] styles) {
        // Serial number
        ExcelUtils.createCell(row, 0, record.getEmrId(), styles[1]);
        ExcelUtils.createCell(row, 1, record.getPatientName(), styles[2]);
        ExcelUtils.createCell(row, 2, record.getInsuranceName(), styles[3]);
        ExcelUtils.createCell(row, 3, record.getDos(), styles[4]);
        ExcelUtils.createCell(row, 4, record.getClientPayment(), styles[5]);
        ExcelUtils.createCell(row, 32, record.getChargeAmount(), styles[14]);
        ExcelUtils.createCell(row, 24, record.getVisitStatus(), styles[24]);
        ExcelUtils.createCell(row, 27, record.getVisitId(), styles[27]);
        ExcelUtils.createCell(row, 29, record.getInsuranceId(), styles[29]);
        ExcelUtils.createCell(row, 30, record.getSecondaryInsuranceName(), styles[30]);
        ExcelUtils.createCell(row, 31, record.getSecondaryInsuranceId(), styles[31]);
    }

    private void setReady(long id, byte[] clinicEligibilitySheet) {
        String sql = "UPDATE clinic_eligibility_record SET completed_at =?, status=?,eligibility_file=? WHERE id =?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, ClinicImportStatus.Ready.name());
            ps.setBytes(3, clinicEligibilitySheet);
            ps.setLong(4, id);
            return ps;
        });
    }

    private void setFailed(long id, String errorMessage) {
        String sql = "UPDATE clinic_eligibility_record SET completed_at =?,status=?,error_message=? WHERE id =?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, ClinicImportStatus.Failed.name());
            ps.setString(3, errorMessage);
            ps.setLong(4, id);
            return ps;
        });
    }

}

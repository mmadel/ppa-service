package com.cob.ppa.parser.SAX.benefit;

import com.cob.ppa.constant.ExcelSheetHeaders;
import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.parser.util.DateParser;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenefitSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

    private final List<String> expectedHeaders = ExcelSheetHeaders.BENEFIT.getHeaders();
    private final Map<Integer, String> indexToHeader = new HashMap<>();
    private final List<BenefitDto> resultList = new ArrayList<>();
    private BenefitDto currentRow;
    private int currentRowNum = -1;
    private boolean isHeaderParsed = false;

    @Override
    public void startRow(int rowNum) {
        this.currentRowNum = rowNum;
        if (rowNum != 0) {
            currentRow = new BenefitDto();
        }
    }

    @Override
    public void cell(String cellReference, String formattedValue, org.apache.poi.xssf.usermodel.XSSFComment comment) {
        int colIndex = new CellAddress(cellReference).getColumn();

        if (currentRowNum == 0) {
            // Building header map only with expected header names
            String header = formattedValue != null ? formattedValue.trim() : "";
            if (expectedHeaders.contains(header)) {
                indexToHeader.put(colIndex, header);
            }
        } else {
            String header = indexToHeader.get(colIndex);
            if (header == null) return;
            // Map header to DTO field
            switch (header) {
                case "Clinic Name":
                    currentRow.setClinicName(formattedValue);
                    break;
                case "EMR Patient ID":
                    currentRow.setEmrId(formattedValue);
                    break;
                case "Patient Name":
                    currentRow.setPatientName(formattedValue);
                    break;
                case "Patient DOB":
                    currentRow.setPatientDob(DateParser.parse(formattedValue));
                    break;
                case "Appointment Type":
                    currentRow.setAppointmentType(formattedValue);
                    break;
                case "Appointment Date":
                    currentRow.setAppointmentDate(DateParser.parse(formattedValue));
                    break;
                case "Visit Status":
                    currentRow.setVisitStatus(formattedValue);
                    break;
                case "Primary Insurance":
                    currentRow.setPrimaryInsurance(formattedValue);
                    break;
                case "Primary Insurance Policy Number":
                    currentRow.setPrimaryInsurancePolicyNumber(formattedValue);
                    break;
                case "Secondary Insurance":
                    currentRow.setSecondaryInsurance(formattedValue);
                    break;
                case "Secondary Insurance Policy Number":
                    currentRow.setSecondaryInsurancePolicyNumber(formattedValue);
                    break;
                default:
                    // Unknown column — skip
                    break;
            }
        }
    }

    @Override
    public void endRow(int rowNum) {
        if (rowNum == 0) {
            validateHeaders();
            isHeaderParsed = true;
        } else if (currentRow != null) {
            resultList.add(currentRow);
        }
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {
        // Not needed
    }

    public List<BenefitDto> getParsedResults() {
        return resultList;
    }

    private void validateHeaders() {
        List<String> foundHeaders = new ArrayList<>(indexToHeader.values());
        for (String required : expectedHeaders) {
            if (!foundHeaders.contains(required)) {
                throw new IllegalArgumentException("Missing required column in Excel: " + required);
            }
        }
    }

}

package com.cob.ppa.parser.SAX.document;

import com.cob.ppa.constant.ExcelSheetHeaders;
import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.dto.DocumentDto;
import com.cob.ppa.parser.util.DateParser;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentUnitSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
    private final List<String> expectedHeaders = ExcelSheetHeaders.DOCUMENTED_UNIT.getHeaders();
    private final Map<Integer, String> indexToHeader = new HashMap<>();
    private final List<DocumentDto> resultList = new ArrayList<>();
    private DocumentDto currentRow;
    private int currentRowNum = -1;
    private boolean isHeaderParsed = false;

    @Override
    public void startRow(int rowNum) {
        this.currentRowNum = rowNum;
        if (rowNum != 0) {
            currentRow = new DocumentDto();
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
            switch (header) {
                case "Clinic Name":
                    currentRow.setClinicName(formattedValue);
                    break;
                case "Visit ID":
                    currentRow.setVisitId(formattedValue);
                    break;
                case "EMR Patient ID":
                    currentRow.setEmrId(formattedValue);
                    break;
                case "Date of Service":
                    currentRow.setDos(DateParser.parse(formattedValue));
                    break;
                case "CPT Code":
                    currentRow.setCpt(formattedValue);
                    break;
                case "Units":
                    currentRow.setUnits(Integer.parseInt(formattedValue));
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

    private void validateHeaders() {
        List<String> foundHeaders = new ArrayList<>(indexToHeader.values());
        for (String required : expectedHeaders) {
            if (!foundHeaders.contains(required)) {
                throw new IllegalArgumentException("Missing required column in Excel: " + required);
            }
        }
    }
    public List<DocumentDto> getParsedResults() {
        return resultList;
    }
}

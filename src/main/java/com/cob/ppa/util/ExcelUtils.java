package com.cob.ppa.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExcelUtils {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static String getString(Row row, int col) {
        Cell cell = row.getCell(col);
        return (cell != null) ? cell.toString().trim() : null;
    }
    public static String getCellValueAsString(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return "";
        }
        double val = cell.getNumericCellValue();
        // Check if it has no decimal part
        if (val == (long) val) {
            return String.valueOf((long) val); // e.g., 29530.0 -> "29530"
        } else {
            return String.valueOf(val); // e.g., 29530.75 -> "29530.75"
        }
    }
    public static Long getLongValue(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return cell.getCellType() == CellType.NUMERIC ?
                (long) cell.getNumericCellValue() :
                Long.parseLong(cell.getStringCellValue().trim());
    }
    public static Integer getIntegerValue(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        return cell.getCellType() == CellType.NUMERIC ?
                (int) cell.getNumericCellValue() :
                Integer.parseInt(cell.getStringCellValue().trim());
    }
    public static LocalDate getDate(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        try {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } catch (Exception e) {
            try {
                return LocalDate.parse(cell.toString().trim(), DATE_FORMAT);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public static Double getDouble(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return null;
        try {
            return Double.parseDouble(cell.toString());
        } catch (Exception ex) {
            return null;
        }
    }
    public static void fillDaySeparatorRow(Row row, LocalDate dayDate, CellStyle style, int columnsCount) {
        // Merge cells for the separator row
        Sheet sheet = row.getSheet();
        int firstCol = 0;
        int lastCol = columnsCount - 1;
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), firstCol, lastCol));

        // Create cell with day label
        Cell cell = row.createCell(firstCol);
        cell.setCellValue("Day: " + formatDateForDisplay(dayDate));
        cell.setCellStyle(style);

        // Fill remaining cells (though merged, Excel needs this)
        for (int i = firstCol + 1; i <= lastCol; i++) {
            Cell emptyCell = row.createCell(i);
            emptyCell.setCellStyle(style);
        }
    }
    private static String formatDateForDisplay(LocalDate originalDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return originalDate.format(formatter);
    }
    public static void createCell(Row row, int columnIndex, Object value, CellStyle baseStyle) {
        Cell cell = row.createCell(columnIndex);
        Workbook workbook = row.getSheet().getWorkbook();

        // Handle null values
        if (value == null) {
            cell.setCellValue("");
            cell.setCellStyle(baseStyle);
            return;
        }

        // Create a new style based on the template style
        CellStyle newStyle = workbook.createCellStyle();
        newStyle.cloneStyleFrom(baseStyle);
        newStyle.setBorderBottom(BorderStyle.THIN);
        newStyle.setBorderTop(BorderStyle.THIN);
        newStyle.setBorderRight(BorderStyle.THIN);
        newStyle.setBorderLeft(BorderStyle.THIN);

        // Special handling for dates
        if (value instanceof LocalDate) {
            cell.setCellType(CellType.NUMERIC);
            // For dates, we need to preserve both the format AND other styles
            CreationHelper createHelper = workbook.getCreationHelper();
            newStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
            cell.setCellValue((LocalDate) value);
        }
        // Handle numbers (including payment values)
        else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
            // Preserve number formatting if it exists
            if (baseStyle.getDataFormat() != 0) {
                newStyle.setDataFormat(baseStyle.getDataFormat());
            }
        }
        // Handle strings
        else {
            cell.setCellValue(value.toString());
        }
        Font boldFont = workbook.createFont();
        boldFont.setBold(false);
        newStyle.setFont(boldFont);
        // Apply the final style
        cell.setCellStyle(newStyle);
    }
}

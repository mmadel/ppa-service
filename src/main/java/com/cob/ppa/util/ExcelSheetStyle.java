package com.cob.ppa.util;

import org.apache.poi.ss.usermodel.*;

public class ExcelSheetStyle {
    public static CellStyle createDaySeparatorStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // Light gray background
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Bold font
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        // Center alignment
        style.setAlignment(HorizontalAlignment.CENTER);

        // Copy border styles from header
        CellStyle headerStyle = workbook.getSheetAt(0).getRow(0).getCell(0).getCellStyle();
        style.setBorderTop(headerStyle.getBorderTop());
        style.setBorderBottom(headerStyle.getBorderBottom());
        style.setBorderLeft(headerStyle.getBorderLeft());
        style.setBorderRight(headerStyle.getBorderRight());

        return style;
    }
    public static void preserveHeaderStyles(Workbook workbook) {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            Row headerRow = sheet.getRow(0);

            if (headerRow != null) {
                // Create a bold font
                Font boldFont = workbook.createFont();
                boldFont.setBold(true);

                // Apply to each header cell
                for (Cell cell : headerRow) {
                    CellStyle newStyle = workbook.createCellStyle();

                    // Copy all existing styles
                    newStyle.cloneStyleFrom(cell.getCellStyle());

                    // Apply bold font
                    newStyle.setFont(boldFont);

                    // Set the new style
                    cell.setCellStyle(newStyle);
                }
            }
        }
    }
}

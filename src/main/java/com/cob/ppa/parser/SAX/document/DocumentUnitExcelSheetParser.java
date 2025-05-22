package com.cob.ppa.parser.SAX.document;

import com.cob.ppa.constant.ExclusionAppointmentTypes;
import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.dto.DocumentDto;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DocumentUnitExcelSheetParser {
    public List<DocumentDto> parse(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            OPCPackage pkg = OPCPackage.open(is);
            XSSFReader reader = new XSSFReader(pkg);
            StylesTable styles = reader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) reader.getSheetsData();
            List<DocumentDto> result = new ArrayList<>();
            if (iter.hasNext()) {
                try (InputStream sheetStream = iter.next()) {
                    InputSource sheetSource = new InputSource(sheetStream);
                    XMLReader parser = XMLReaderFactory.createXMLReader();
                    DataFormatter formatter = new DataFormatter();
                    DocumentUnitSheetHandler handler = new DocumentUnitSheetHandler();
                    XSSFSheetXMLHandler xmlHandler =
                            new XSSFSheetXMLHandler(styles, strings, handler, formatter, false);

                    parser.setContentHandler(xmlHandler);
                    parser.parse(sheetSource);

                    result = handler.getParsedResults();
                }
            }
            return result;
        }
    }
}

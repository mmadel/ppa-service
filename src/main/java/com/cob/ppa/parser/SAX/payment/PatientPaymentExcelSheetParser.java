package com.cob.ppa.parser.SAX.payment;

import com.cob.ppa.dto.DocumentDto;
import com.cob.ppa.dto.PaymentDto;
import com.cob.ppa.parser.SAX.document.DocumentUnitSheetHandler;
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
import java.util.ArrayList;
import java.util.List;

@Component
public class PatientPaymentExcelSheetParser {
    public List<PaymentDto> parse(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            OPCPackage pkg = OPCPackage.open(is);
            XSSFReader reader = new XSSFReader(pkg);
            StylesTable styles = reader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) reader.getSheetsData();
            List<PaymentDto> result = new ArrayList<>();
            if (iter.hasNext()) {
                try (InputStream sheetStream = iter.next()) {
                    InputSource sheetSource = new InputSource(sheetStream);
                    XMLReader parser = XMLReaderFactory.createXMLReader();
                    DataFormatter formatter = new DataFormatter();
                    PaymentSheetHandler handler = new PaymentSheetHandler();
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

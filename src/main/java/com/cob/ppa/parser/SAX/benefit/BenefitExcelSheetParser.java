package com.cob.ppa.parser.SAX.benefit;

import com.cob.ppa.constant.ExclusionAppointmentTypes;
import com.cob.ppa.constant.ExclusionVisitStatus;
import com.cob.ppa.dto.BenefitDto;
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
public class BenefitExcelSheetParser {
    public List<BenefitDto> parse(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream()) {
            OPCPackage pkg = OPCPackage.open(is);
            XSSFReader reader = new XSSFReader(pkg);
            StylesTable styles = reader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);

            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) reader.getSheetsData();

            List<BenefitDto> result = new ArrayList<>();
            if (iter.hasNext()) {
                try (InputStream sheetStream = iter.next()) {
                    InputSource sheetSource = new InputSource(sheetStream);
                    XMLReader parser = XMLReaderFactory.createXMLReader();
                    DataFormatter formatter = new DataFormatter();
                    BenefitSheetHandler handler = new BenefitSheetHandler();
                    XSSFSheetXMLHandler xmlHandler =
                            new XSSFSheetXMLHandler(styles, strings, handler, formatter, false);

                    parser.setContentHandler(xmlHandler);
                    parser.parse(sheetSource);

                    result = handler.getParsedResults();
                }
            }
            return filterBenefits(result);
        }
    }

    private static List<BenefitDto> filterBenefits(List<BenefitDto> benefits) {
        Set<String> EXCLUDED_APP_TYPES = new HashSet<>(Arrays.asList(ExclusionAppointmentTypes.Types));
        Set<String> EXCLUDED_VISIT_STATUS = new HashSet<>(Arrays.asList(ExclusionVisitStatus.Types));
        return benefits.stream()
                .filter(benefit -> (!EXCLUDED_APP_TYPES.contains(benefit.getAppointmentType())
                        && (!EXCLUDED_VISIT_STATUS.contains(benefit.getVisitStatus()))))
                .collect(Collectors.toList());
    }
}

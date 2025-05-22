package com.cob.ppa.service.batch.reader;

import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.dto.DocumentDto;
import com.cob.ppa.service.batch.mapper.BenefitMapper;
import com.cob.ppa.service.batch.mapper.DocumentUnitMapper;
import com.cob.ppa.service.batch.reader.util.UTF8BOMSkipper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.item.ItemReader;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentUnitReader implements ItemReader<DocumentDto> {
    private List<DocumentDto> data;
    private int currentIndex = 0;

    public DocumentUnitReader(String filePath) throws Exception {
        long start = System.currentTimeMillis();
        try (Reader baseReader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
             Reader cleanedReader = UTF8BOMSkipper.skipUTF8BOM(baseReader);
             CSVParser parser = new CSVParser(cleanedReader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreSurroundingSpaces()
                     .withTrim())) {

            this.data = parser.getRecords().parallelStream()
                    .map(this::map)
                    .collect(Collectors.toList());
        }
        long time = System.currentTimeMillis() - start;
        System.out.println(time / 1000);
    }
    private DocumentDto map(CSVRecord record) {
        return DocumentUnitMapper.mapRecord(record);
    }
    @Override
    public DocumentDto read() {
        return currentIndex < data.size() ? data.get(currentIndex++) : null;
    }
}

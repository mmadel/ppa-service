package com.cob.ppa.service.batch.reader;

import com.cob.ppa.dto.PaymentDto;
import com.cob.ppa.parser.util.DateParser;
import com.cob.ppa.service.batch.mapper.PatientPaymentMapper;
import com.cob.ppa.service.batch.reader.util.UTF8BOMSkipper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.item.ItemReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class PatientPaymentReader implements ItemReader<PaymentDto> {
    private List<PaymentDto> data;
    private int currentIndex = 0;

    public PatientPaymentReader(String filePath) throws Exception {
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

    private PaymentDto map(CSVRecord record) {
        return PatientPaymentMapper.mapRecord(record);
    }
    @Override
    public PaymentDto read() {
        return currentIndex < data.size() ? data.get(currentIndex++) : null;
    }
}

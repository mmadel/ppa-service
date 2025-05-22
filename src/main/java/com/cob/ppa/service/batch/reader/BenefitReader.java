package com.cob.ppa.service.batch.reader;

import com.cob.ppa.constant.ExclusionAppointmentTypes;
import com.cob.ppa.constant.ExclusionVisitStatus;
import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.dto.PaymentDto;
import com.cob.ppa.service.batch.mapper.BenefitMapper;
import com.cob.ppa.service.batch.mapper.PatientPaymentMapper;
import com.cob.ppa.service.batch.reader.util.UTF8BOMSkipper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.batch.item.ItemReader;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BenefitReader implements ItemReader<BenefitDto> {
    private List<BenefitDto> data;
    private int currentIndex = 0;

    public BenefitReader(String filePath) throws Exception {
        long start = System.currentTimeMillis();
        try (Reader baseReader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
             Reader cleanedReader = UTF8BOMSkipper.skipUTF8BOM(baseReader);
             CSVParser parser = new CSVParser(cleanedReader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreSurroundingSpaces()
                     .withTrim())) {

            this.data = filterBenefits(parser.getRecords().parallelStream()
                    .map(this::map)
                    .collect(Collectors.toList()));
        }
        long time = System.currentTimeMillis() - start;
        System.out.println(time / 1000);
    }
    private BenefitDto map(CSVRecord record) {
        return BenefitMapper.mapRecord(record);
    }
    @Override
    public BenefitDto read() {
        return currentIndex < data.size() ? data.get(currentIndex++) : null;
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
package com.cob.ppa.service.batch.mapper;

import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.dto.DocumentDto;
import com.cob.ppa.parser.util.DateParser;
import org.apache.commons.csv.CSVRecord;

public class DocumentUnitMapper {
    public static DocumentDto map(String[] parts){
        DocumentDto documentDto = new DocumentDto();
        documentDto.setClinicName(parts[0]);
        documentDto.setVisitId(parts[1]);
        documentDto.setVisitId(parts[2]);
        documentDto.setEmrId(parts[3]);
        documentDto.setDos(DateParser.parse(parts[6]));
        documentDto.setCpt(parts[19]);
        try {
            documentDto.setUnits(Integer.parseInt(parts[20]));
        }catch (NumberFormatException e){
            documentDto.setUnits(0);
        }

        return documentDto;
    }
    public static DocumentDto mapRecord(CSVRecord record) {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setClinicName(record.get("Clinic Name"));
        documentDto.setVisitId(record.get("Visit ID"));
        documentDto.setEmrId(record.get("EMR Patient ID"));
        documentDto.setDos(DateParser.parse(record.get("Date of Service")));
        documentDto.setCpt(record.get("CPT Code"));
        documentDto.setUnits(Integer.parseInt(record.get("Units")));
        return documentDto;
    }
}

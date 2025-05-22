package com.cob.ppa.service.batch.mapper;

import com.cob.ppa.dto.PaymentDto;
import com.cob.ppa.parser.util.DateParser;
import org.apache.commons.csv.CSVRecord;

public class PatientPaymentMapper {
    public static PaymentDto mapRecord(CSVRecord record) {
        PaymentDto paymentDto =new PaymentDto();
        paymentDto.setClinicName(record.get("Clinic Name"));
        paymentDto.setEmrPatientId(record.get("EMR Patient ID"));
        paymentDto.setDateOfService(DateParser.parse(record.get("Date of Service")));
        paymentDto.setAmountPaid(Double.parseDouble(record.get("Amount Paid")));
        return paymentDto;

    }
}

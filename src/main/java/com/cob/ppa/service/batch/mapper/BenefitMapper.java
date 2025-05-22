package com.cob.ppa.service.batch.mapper;

import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.dto.PaymentDto;
import com.cob.ppa.parser.util.DateParser;
import org.apache.commons.csv.CSVRecord;

public class BenefitMapper {
    public static BenefitDto map(String[] fields) {
        BenefitDto benefit = new BenefitDto();
        benefit.setClinicName(fields[0]);
        benefit.setEmrId(fields[1]);
        benefit.setPatientName(fields[2]);
        benefit.setPatientDob(DateParser.parse(fields[3]));
        benefit.setAppointmentType(fields[8]);
        benefit.setAppointmentDate(DateParser.parse(fields[9]));
        benefit.setVisitStatus(10 > fields.length ? "" : fields[10]);
        benefit.setPrimaryInsurance(13 > fields.length ? "" : fields[13]);
        benefit.setPrimaryInsurancePolicyNumber(16 > fields.length ? "" : fields[16]);
//        benefit.setSecondaryInsurance(20 > fields.length ? "" : fields[20]);
        benefit.setSecondaryInsurancePolicyNumber(23 > fields.length ? "" : fields[23]);
        return benefit;
    }
    public static BenefitDto mapRecord(CSVRecord record) {
        BenefitDto benefit = new BenefitDto();
        benefit.setClinicName(record.get("Clinic Name"));
        benefit.setEmrId(record.get("EMR Patient ID"));
        benefit.setPatientName(record.get("Patient Name"));
        benefit.setPatientDob(DateParser.parse(record.get("Patient DOB")));
        benefit.setAppointmentType(record.get("Appointment Type"));
        benefit.setAppointmentDate(DateParser.parse(record.get("Appointment Date")));
        benefit.setVisitStatus(record.get("Visit Status"));
        benefit.setPrimaryInsurance(record.get("Primary Insurance"));
        benefit.setPrimaryInsurancePolicyNumber(record.get("Primary Insurance Policy Number"));
        benefit.setSecondaryInsurance(record.get("Secondary Insurance"));
        benefit.setSecondaryInsurancePolicyNumber(record.get("Secondary Insurance Policy Number"));
        return benefit;
    }
}

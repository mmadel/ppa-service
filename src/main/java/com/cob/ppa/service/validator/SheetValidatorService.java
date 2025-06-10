package com.cob.ppa.service.validator;

import com.cob.ppa.exception.business.BatchUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SheetValidatorService {
    @Autowired
    SheetValidator SheetValidator;

    public void validate(String benefitFile, String documentUnitFile, String patientPaymentFile) throws IOException, BatchUploadException {
        validateBenefitFile(benefitFile);
        validateDocumentUnitFile(documentUnitFile);
        validatePatientPaymentFile(patientPaymentFile);

    }

    private void validateBenefitFile(String filePath) throws IOException, BatchUploadException {
        final List<String> REQUIRED_HEADERS = List.of(
                "Clinic Name",
                "EMR Patient ID",
                "Patient Name",
                "Patient DOB",
                "Case Name",
                "Treating Therapist",
                "Case Therapist",
                "Appointment Type",
                "Appointment Date",
                "Visit Status",
                "Start Time",
                "End Time",
                "Check In Time",
                "Primary Insurance",
                "Primary Insurance Policy Number",
                "Secondary Insurance",
                "Secondary Insurance Policy Number"
        );
        List<String> missingFields = SheetValidator.validate(filePath, REQUIRED_HEADERS);
        if (!missingFields.isEmpty())
            throw new BatchUploadException(HttpStatus.CONFLICT, BatchUploadException.BENEFIT_MISSING_FIELDS, new Object[]{String.join(",", missingFields)});
    }

    private void validateDocumentUnitFile(String filePath) throws IOException, BatchUploadException {
        List<String> REQUIRED_HEADERS = List.of("Clinic Name", "Case Name", "Visit ID", "EMR Patient ID", "Patient DOB", "CPT Code", "Units");
        List<String> missingFields = SheetValidator.validate(filePath, REQUIRED_HEADERS);
        if (!missingFields.isEmpty())
            throw new BatchUploadException(HttpStatus.CONFLICT, BatchUploadException.DOCUMENT_MISSING_FIELDS, new Object[]{String.join(",", missingFields)});
    }

    private void validatePatientPaymentFile(String filePath) throws IOException, BatchUploadException {
        final List<String> REQUIRED_HEADERS = List.of("Clinic Name", "Date of Service", "Date of Transaction", "Provider", "Collected by", "Patient Name", "EMR Patient ID", "Case Name", "Charge Type", "Charge Description", "Amount Due", "Amount Paid", "Payment Method");
        List<String> missingFields = SheetValidator.validate(filePath, REQUIRED_HEADERS);
        if (!missingFields.isEmpty())
            throw new BatchUploadException(HttpStatus.CONFLICT, BatchUploadException.PATIENT_PAYMENT_MISSING_FIELDS, new Object[]{String.join(",", missingFields)});
    }
}

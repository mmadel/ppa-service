package com.cob.ppa.constant;

import java.util.List;

public enum ExcelSheetHeaders {
    BENEFIT(List.of(
            "Clinic Name", "EMR Patient ID", "Patient Name", "Patient DOB",
            "Appointment Type", "Appointment Date", "Visit Status",
            "Primary Insurance",
            "Primary Insurance Policy Number",
            "Secondary Insurance",
            "Secondary Insurance Policy Number"
    )),

    DOCUMENTED_UNIT(List.of(
            "Clinic Name", "Visit ID", "EMR Patient ID", "Date of Service", "CPT Code", "Units"
    )),

    PAYMENT(List.of(
            "Clinic Name", "Date of Service",
            "EMR Patient ID", "Amount Paid"
    ));

    private final List<String> headers;

    ExcelSheetHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getHeaders() {
        return headers;
    }
}

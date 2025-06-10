package com.cob.ppa.exception.business;

import org.springframework.http.HttpStatus;

public class BatchUploadException extends PPAException{
    public static final String BENEFIT_MISSING_FIELDS = Category.Business.value() + getPrefix() +"_00";
    public static final String DOCUMENT_MISSING_FIELDS = Category.Business.value() + getPrefix() +"_01";
    public static final String PATIENT_PAYMENT_MISSING_FIELDS = Category.Business.value() + getPrefix() +"_02";
    public static final String TEMPLATE_NOT_FOUND = Category.Business.value() + getPrefix() +"_03";
    public BatchUploadException(String code) {
        super(code);
    }

    public BatchUploadException(String code, Object[] parameters) {
        super(code, parameters);
    }

    public BatchUploadException(HttpStatus status, String code, Object[] parameters) {
        super(status, code, parameters);
    }

    protected static String getPrefix() {
        return "_batchUpload";
    }
}

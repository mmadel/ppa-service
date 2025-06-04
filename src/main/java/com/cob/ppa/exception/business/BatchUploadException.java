package com.cob.ppa.exception.business;

import org.springframework.http.HttpStatus;

public class BatchUploadException extends PPAException{

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
        return "_clinic";
    }
}

package com.cob.ppa.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public  class ErrorResponse {
    private String error;
}
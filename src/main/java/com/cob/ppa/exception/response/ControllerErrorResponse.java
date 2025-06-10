package com.cob.ppa.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
public class ControllerErrorResponse {
    String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    private LocalDateTime timestamp;
    HttpStatus errorCode;

    private ControllerErrorResponse(){
        timestamp = LocalDateTime.now();
    }
    public ControllerErrorResponse(String message, HttpStatus errorCode){
        this();
        this.message = message;
        this.errorCode =errorCode;
    }
}

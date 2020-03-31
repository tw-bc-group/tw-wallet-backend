package com.thoughtworks.wallet.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "error")
class ErrorResponse {
    ErrorResponse(HttpStatus httpStatus, String message, List<String> details) {
        super();
        this.code = httpStatus.value();
        this.status = httpStatus.getReasonPhrase();
        this.message = message;
        this.details = details;
    }

    private int code;

    private String status;

    private String message;

    private List<String> details;
}


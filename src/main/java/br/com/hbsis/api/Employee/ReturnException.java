package br.com.hbsis.api.Employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReturnException extends RuntimeException {
    public ReturnException(String message) {
        super(message);
    }
}

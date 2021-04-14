package it.academy.app.exception;

import it.academy.app.validators.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    ErrorMessages errorCode;

    public ValidationException(ErrorMessages message) {
        super(message.toString());
        this.errorCode = message;
    }

    public ErrorMessages getErrorCode() {
        return errorCode;
    }
}

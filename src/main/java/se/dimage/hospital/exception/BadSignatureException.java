package se.dimage.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BadSignatureException extends RuntimeException {
    public BadSignatureException(String message) {
        super(message);
    }
}

package se.dimage.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class JournalNotFoundException extends RuntimeException {
    public JournalNotFoundException(Long id) {
        super("Journal # " + id + " not found");
    }
}

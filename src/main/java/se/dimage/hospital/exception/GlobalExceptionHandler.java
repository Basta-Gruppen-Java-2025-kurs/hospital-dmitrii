package se.dimage.hospital.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private HashMap<String,String> getExceptionHashMap(RuntimeException e, String status) {
        HashMap<String, String> response = new HashMap<>();
        response.put("status", status);
        response.put("message", e.getLocalizedMessage());
        return response;
    }

    @ExceptionHandler(PatientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap<String, String> handlePatientNotFound(PatientNotFoundException e, WebRequest request) {
        return getExceptionHashMap(e, "fail");
    }

    @ExceptionHandler(BadSignatureException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public HashMap<String, String> handleSignatureException(BadSignatureException e, WebRequest request) {
        return getExceptionHashMap(e, "signature exception");
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(@NonNull NoResourceFoundException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        return super.handleNoResourceFoundException(ex, headers, status, request);
    }


}

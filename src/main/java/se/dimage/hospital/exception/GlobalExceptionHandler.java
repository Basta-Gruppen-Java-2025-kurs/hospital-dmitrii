package se.dimage.hospital.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError er : ex.getBindingResult().getFieldErrors()) {
            errors.add(er.getField() + ": " + er.getDefaultMessage());
        }
        for(ObjectError er : ex.getBindingResult().getGlobalErrors()) {
            errors.add(er.getObjectName() + ": " + er.getDefaultMessage());
        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

}

package se.dimage.hospital.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotationUtils;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";
    public static final String NOT_FOUND_STATUS = "not found";

    private HashMap<String,String> getExceptionHashMap(RuntimeException e, String status) {
        HashMap<String, String> response = new HashMap<>();
        response.put("status", status);
        response.put("message", e.getLocalizedMessage());
        return response;
    }

    @ExceptionHandler(PatientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap<String, String> handlePatientNotFound(PatientNotFoundException e, WebRequest request) {
        return getExceptionHashMap(e, NOT_FOUND_STATUS);
    }

    @ExceptionHandler(JournalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleJournalNotFound(JournalNotFoundException e, WebRequest request) {
        return getExceptionHashMap(e, NOT_FOUND_STATUS);
    }

    @ExceptionHandler(BadSignatureException.class)
    public ResponseEntity<Object> handleSignatureException(BadSignatureException e, WebRequest request) {
        Map<String, String> errorDetails = getExceptionHashMap(e, "signature exception");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            logger.error("Rethrowing exception: " + e.getLocalizedMessage());
            throw e;
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

}

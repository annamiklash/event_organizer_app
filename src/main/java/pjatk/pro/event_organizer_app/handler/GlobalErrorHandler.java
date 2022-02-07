package pjatk.pro.event_organizer_app.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pjatk.pro.event_organizer_app.exceptions.IllegalArgumentException;
import pjatk.pro.event_organizer_app.exceptions.*;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    /**
     * error handle for @Valid
     * MethodArgumentNotValidException
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        final List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> onValidationError(Exception ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundException.class)
    public void notFoundExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgumentExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidCredentialsException.class)
    public void invalidCredentialsExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFileExtensionException.class)
    public void invalidFileExtensionExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserExistsException.class)
    public void userExistsExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessVerificationException.class)
    public void businessVerificationExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public void validationExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotAvailableException.class)
    public void notAvailableExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LocationNotBookedException.class)
    public void locationNotBookedExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ActionNotAllowedException.class)
    public void actionNotAllowedExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenAccessException.class)
    public void forbiddenAccessExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
    }

}
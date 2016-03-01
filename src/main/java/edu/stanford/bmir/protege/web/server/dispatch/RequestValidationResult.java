package edu.stanford.bmir.protege.web.server.dispatch;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.dispatch.InvalidRequestException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public class RequestValidationResult {

    private static final RequestValidationResult VALID_RESULT = new RequestValidationResult();

    private ValidationResult validationResult;

    private Optional<Exception> exception;

    protected RequestValidationResult() {
        this(ValidationResult.VALID, Optional.<Exception>absent());
    }

    protected RequestValidationResult(String exceptionMessage) {
        this(new InvalidRequestException(exceptionMessage));
    }

    protected RequestValidationResult(Exception exception) {
        this(ValidationResult.INVALID, Optional.of(exception));
    }


    private RequestValidationResult(ValidationResult validationResult, Optional<Exception> exception) {
        this.validationResult = validationResult;
        this.exception = exception;
    }

    public static RequestValidationResult getValid() {
        return VALID_RESULT;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public boolean isValid() {
        return validationResult == ValidationResult.VALID;
    }

    public boolean isInvalid() {
        return validationResult == ValidationResult.INVALID;
    }

    public static RequestValidationResult getInvalid(Exception exception) {
        return new RequestValidationResult(exception);
    }

    public static RequestValidationResult getInvalid(String message) {
        return new RequestValidationResult(message);
    }

    public Optional<Exception> getInvalidException() {
        return exception;
    }

    public String getInvalidMessage() {
        if(exception.isPresent()) {
            String message = exception.get().getMessage();
            if(message == null) {
                return "";
            }
            else {
                return message;
            }
        }
        else {
            return "";
        }
    }
}

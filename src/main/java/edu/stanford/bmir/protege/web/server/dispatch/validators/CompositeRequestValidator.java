package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.ValidationResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 * <p>
 *     A request validator that is composed of a series of other request validators.  This {@link CompositeRequestValidator}
 *     requires of of the {@link RequestValidator}s that it is composed of to validate a request for it to validate
 *     the request.  (i.e. conjunction rather than disjunction).
 * </p>
 */
public class CompositeRequestValidator implements RequestValidator {

    private final List<RequestValidator> requestValidators = new ArrayList<>();


    public static <A extends Action<?>> CompositeRequestValidator get(List<RequestValidator> requestValidators) {
        return new CompositeRequestValidator(requestValidators);
    }

    public CompositeRequestValidator(RequestValidator ... requestValidators) {
        this(Arrays.asList(requestValidators));
    }

    public CompositeRequestValidator(List<RequestValidator> requestValidators) {
        this.requestValidators.addAll(requestValidators);
    }

    @Override
    public RequestValidationResult validateAction() {
        for(RequestValidator requestValidator : requestValidators) {
            RequestValidationResult result = requestValidator.validateAction();
            if(result.getValidationResult() == ValidationResult.INVALID) {
                return result;
            }
        }
        return RequestValidationResult.getValid();
    }
}

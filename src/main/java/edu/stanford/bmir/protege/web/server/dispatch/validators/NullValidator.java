package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 * <p>
 *     A request validator that always validates a request as valid.
 * </p>
 */
public class NullValidator implements RequestValidator {

    private static final NullValidator instance = new NullValidator();

    @Override
    public RequestValidationResult validateAction() {
        return RequestValidationResult.getValid();
    }

    @SuppressWarnings("unchecked")
    public static <A extends Action<?>> RequestValidator get() {
        return instance;
    }
}

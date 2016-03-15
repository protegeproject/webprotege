package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.GetClientApplicationPropertiesAction;
import edu.stanford.bmir.protege.web.shared.app.GetClientApplicationPropertiesResult;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/07/2013
 */
public class GetClientApplicationPropertiesActionHandler implements ActionHandler<GetClientApplicationPropertiesAction, GetClientApplicationPropertiesResult> {

    private final WebProtegeProperties properties;

    @Inject
    public GetClientApplicationPropertiesActionHandler(WebProtegeProperties properties) {
        this.properties = properties;
    }

    @Override
    public Class<GetClientApplicationPropertiesAction> getActionClass() {
        return GetClientApplicationPropertiesAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(GetClientApplicationPropertiesAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public GetClientApplicationPropertiesResult execute(GetClientApplicationPropertiesAction action, ExecutionContext executionContext) {
        ClientApplicationProperties clientApplicationProperties = properties.getClientApplicationProperties();
        return new GetClientApplicationPropertiesResult(clientApplicationProperties);
    }
}

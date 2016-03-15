package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectiveLayoutAction;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectiveLayoutResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class SetPerspectiveLayoutActionHandler implements ActionHandler<SetPerspectiveLayoutAction, SetPerspectiveLayoutResult> {

    private final PerspectiveLayoutStore perspectiveLayoutStore;

    @Inject
    public SetPerspectiveLayoutActionHandler(PerspectiveLayoutStore perspectiveLayoutStore) {
        this.perspectiveLayoutStore = perspectiveLayoutStore;
    }

    @Override
    public Class<SetPerspectiveLayoutAction> getActionClass() {
        return SetPerspectiveLayoutAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(SetPerspectiveLayoutAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public SetPerspectiveLayoutResult execute(SetPerspectiveLayoutAction action, ExecutionContext executionContext) {
        perspectiveLayoutStore.setPerspectiveLayout(action.getProjectId(), action.getUserId(), action.getLayout());
        return new SetPerspectiveLayoutResult();
    }
}

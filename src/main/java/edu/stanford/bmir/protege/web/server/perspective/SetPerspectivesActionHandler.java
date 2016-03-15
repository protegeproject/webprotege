package edu.stanford.bmir.protege.web.server.perspective;

import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectivesAction;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectivesResult;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class SetPerspectivesActionHandler implements ActionHandler<SetPerspectivesAction, SetPerspectivesResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public SetPerspectivesActionHandler(PerspectivesManager perspectivesManager) {
        this.perspectivesManager = perspectivesManager;
    }

    @Override
    public Class<SetPerspectivesAction> getActionClass() {
        return SetPerspectivesAction.class;
    }

    @Override
    public RequestValidator getRequestValidator(SetPerspectivesAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    public SetPerspectivesResult execute(SetPerspectivesAction action, ExecutionContext executionContext) {
        perspectivesManager.setPerspectives(action.getProjectId(), action.getUserId(), action.getPerspectiveIds());
        return new SetPerspectivesResult();
    }
}

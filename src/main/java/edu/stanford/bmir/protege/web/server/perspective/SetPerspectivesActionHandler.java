package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectivesAction;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectivesResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class SetPerspectivesActionHandler implements ProjectActionHandler<SetPerspectivesAction, SetPerspectivesResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public SetPerspectivesActionHandler(PerspectivesManager perspectivesManager) {
        this.perspectivesManager = perspectivesManager;
    }

    @Nonnull
    @Override
    public Class<SetPerspectivesAction> getActionClass() {
        return SetPerspectivesAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull SetPerspectivesAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public SetPerspectivesResult execute(@Nonnull SetPerspectivesAction action, @Nonnull ExecutionContext executionContext) {
        perspectivesManager.setPerspectives(action.getProjectId(), action.getUserId(), action.getPerspectiveIds());
        return new SetPerspectivesResult();
    }
}

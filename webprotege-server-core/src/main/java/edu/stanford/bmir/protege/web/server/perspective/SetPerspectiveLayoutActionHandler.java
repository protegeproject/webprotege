package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.ProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectiveLayoutAction;
import edu.stanford.bmir.protege.web.shared.perspective.SetPerspectiveLayoutResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class SetPerspectiveLayoutActionHandler implements ProjectActionHandler<SetPerspectiveLayoutAction, SetPerspectiveLayoutResult> {

    private final PerspectiveLayoutStore perspectiveLayoutStore;

    @Inject
    public SetPerspectiveLayoutActionHandler(PerspectiveLayoutStore perspectiveLayoutStore) {
        this.perspectiveLayoutStore = perspectiveLayoutStore;
    }

    @Nonnull
    @Override
    public Class<SetPerspectiveLayoutAction> getActionClass() {
        return SetPerspectiveLayoutAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull SetPerspectiveLayoutAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public SetPerspectiveLayoutResult execute(@Nonnull SetPerspectiveLayoutAction action, @Nonnull ExecutionContext executionContext) {
        perspectiveLayoutStore.setPerspectiveLayout(action.getProjectId(), action.getUserId(), action.getLayout());
        return new SetPerspectiveLayoutResult();
    }
}

package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.change.GetWatchedEntityChangesAction;
import edu.stanford.bmir.protege.web.shared.change.GetWatchedEntityChangesResult;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.watches.Watch;

import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
public class GetWatchedEntityChangesActionHandler extends AbstractHasProjectActionHandler<GetWatchedEntityChangesAction, GetWatchedEntityChangesResult> {

    @Inject
    public GetWatchedEntityChangesActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    protected RequestValidator<GetWatchedEntityChangesAction> getAdditionalRequestValidator(GetWatchedEntityChangesAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetWatchedEntityChangesResult execute(GetWatchedEntityChangesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        Set<Watch<?>> watches = project.getWatchManager().getWatches(action.getUserId());
        ImmutableList<ProjectChange> changes = project.getChangeManager().getProjectChangesForWatches(watches);
        return new GetWatchedEntityChangesResult(changes);
    }

    @Override
    public Class<GetWatchedEntityChangesAction> getActionClass() {
        return GetWatchedEntityChangesAction.class;
    }
}

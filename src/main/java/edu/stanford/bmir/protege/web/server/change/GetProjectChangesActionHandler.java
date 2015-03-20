package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.Revision;
import edu.stanford.bmir.protege.web.shared.Filter;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesAction;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesResult;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;

import javax.inject.Inject;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
public class GetProjectChangesActionHandler extends AbstractHasProjectActionHandler<GetProjectChangesAction, GetProjectChangesResult> {

    @Inject
    public GetProjectChangesActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    public Class<GetProjectChangesAction> getActionClass() {
        return GetProjectChangesAction.class;
    }

    @Override
    protected RequestValidator<GetProjectChangesAction> getAdditionalRequestValidator(GetProjectChangesAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetProjectChangesResult execute(final GetProjectChangesAction action, final OWLAPIProject project, ExecutionContext executionContext) {

        List<ProjectChange> changeList = project.getChangeManager().getProjectChanges(action.getSubject());
        return new GetProjectChangesResult(ImmutableList.copyOf(changeList));
    }
}

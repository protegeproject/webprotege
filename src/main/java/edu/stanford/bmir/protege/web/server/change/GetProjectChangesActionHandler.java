package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesAction;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesResult;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
public class GetProjectChangesActionHandler extends AbstractHasProjectActionHandler<GetProjectChangesAction, GetProjectChangesResult> {

    @Inject
    public GetProjectChangesActionHandler(ProjectManager projectManager,
                                          AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetProjectChangesAction> getActionClass() {
        return GetProjectChangesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_CHANGES;
    }

    @Override
    protected GetProjectChangesResult execute(final GetProjectChangesAction action, final OWLAPIProject project, ExecutionContext executionContext) {
        List<ProjectChange> changeList = project.getProjectChangesManager().getProjectChanges(action.getSubject());
        return new GetProjectChangesResult(ImmutableList.copyOf(changeList));
    }
}

package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesAction;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetRevisionSummariesActionHandler extends AbstractHasProjectActionHandler<GetRevisionSummariesAction, GetRevisionSummariesResult> {

    @Inject
    public GetRevisionSummariesActionHandler(ProjectManager projectManager,
                                             AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_CHANGES;
    }

    @Override
    protected GetRevisionSummariesResult execute(GetRevisionSummariesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new GetRevisionSummariesResult(ImmutableList.copyOf(project.getChangeManager().getRevisionSummaries()));
    }

    @Override
    public Class<GetRevisionSummariesAction> getActionClass() {
        return GetRevisionSummariesAction.class;
    }
}

package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesAction;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetRevisionSummariesActionHandler extends AbstractHasProjectActionHandler<GetRevisionSummariesAction, GetRevisionSummariesResult> {

    @Nonnull
    private final RevisionManager revisionManager;

    @Inject
    public GetRevisionSummariesActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull RevisionManager revisionManager) {
        super(accessManager);
        this.revisionManager = revisionManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_CHANGES;
    }

    @Override
    public GetRevisionSummariesResult execute(GetRevisionSummariesAction action, ExecutionContext executionContext) {
        return new GetRevisionSummariesResult(ImmutableList.copyOf(revisionManager.getRevisionSummaries()));
    }

    @Override
    public Class<GetRevisionSummariesAction> getActionClass() {
        return GetRevisionSummariesAction.class;
    }
}

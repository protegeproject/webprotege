package edu.stanford.bmir.protege.web.server.revision;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberAction;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetHeadRevisionNumberActionHandler extends AbstractProjectActionHandler<GetHeadRevisionNumberAction, GetHeadRevisionNumberResult> {

    @Nonnull
    private final RevisionManager revisionManager;

    @Inject
    public GetHeadRevisionNumberActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull RevisionManager revisionManager) {
        super(accessManager);
        this.revisionManager = revisionManager;
    }

    @Nonnull
    @Override
    public Class<GetHeadRevisionNumberAction> getActionClass() {
        return GetHeadRevisionNumberAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetHeadRevisionNumberAction action) {
        return VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetHeadRevisionNumberResult execute(@Nonnull GetHeadRevisionNumberAction action, @Nonnull ExecutionContext executionContext) {
        return new GetHeadRevisionNumberResult(revisionManager.getCurrentRevision());
    }
}

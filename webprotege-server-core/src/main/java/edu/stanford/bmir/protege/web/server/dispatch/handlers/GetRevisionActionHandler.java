package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.actions.GetRevisionAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.GetRevisionResult;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionDetails;
import edu.stanford.bmir.protege.web.server.revision.RevisionDetailsExtractor;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2018
 */
public class GetRevisionActionHandler extends AbstractProjectActionHandler<GetRevisionAction, GetRevisionResult> {

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final RevisionDetailsExtractor extractor;

    @Inject
    public GetRevisionActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull RevisionManager revisionManager,
                                    @Nonnull RevisionDetailsExtractor extractor) {
        super(accessManager);
        this.revisionManager = checkNotNull(revisionManager);
        this.extractor = checkNotNull(extractor);
    }

    @Nonnull
    @Override
    public Class<GetRevisionAction> getActionClass() {
        return GetRevisionAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetRevisionAction action) {
        return BuiltInAction.VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetRevisionResult execute(@Nonnull GetRevisionAction action, @Nonnull ExecutionContext executionContext) {
        Optional<Revision> revision = revisionManager.getRevision(action.getRevisionNumber());
        Optional<RevisionDetails> details = revision.map(extractor::extractRevisionDetails);
        return new GetRevisionResult(details);
    }
}

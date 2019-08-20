package edu.stanford.bmir.protege.web.server.dispatch.actions;

import edu.stanford.bmir.protege.web.server.revision.RevisionDetails;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2018
 */
public class GetRevisionResult implements Result {

    @Nullable
    private final RevisionDetails revisionDetails;

    public GetRevisionResult(@Nonnull Optional<RevisionDetails> revisionDetails) {
        this.revisionDetails = revisionDetails.orElse(null);
    }

    @Nonnull
    public Optional<RevisionDetails> getRevisionDetails() {
        return Optional.ofNullable(revisionDetails);
    }
}

package edu.stanford.bmir.protege.web.shared.issues.mention;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.Mention;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
@TypeAlias("RevisionMention")
public class RevisionMention implements Mention {

    @Nonnull
    private RevisionNumber revisionNumber;

    @PersistenceConstructor
    public RevisionMention(@Nonnull RevisionNumber revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    @GwtSerializationConstructor
    private RevisionMention() {
    }

    @Nonnull
    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }


    @Override
    public String toString() {
        return toStringHelper("RevisionMention")
                .addValue(revisionNumber)
                .toString();
    }
}

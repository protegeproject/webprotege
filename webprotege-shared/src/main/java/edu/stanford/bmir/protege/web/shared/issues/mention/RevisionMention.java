package edu.stanford.bmir.protege.web.shared.issues.mention;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.Mention;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
public class RevisionMention extends Mention {

    private long revisionNumber;


    public RevisionMention(long revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    @GwtSerializationConstructor
    private RevisionMention() {
    }

    public long getRevisionNumber() {
        return revisionNumber;
    }


    @Override
    public String toString() {
        return toStringHelper("RevisionMention")
                .addValue(revisionNumber)
                .toString();
    }
}

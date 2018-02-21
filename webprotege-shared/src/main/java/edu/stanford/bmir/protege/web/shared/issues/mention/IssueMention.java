package edu.stanford.bmir.protege.web.shared.issues.mention;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.Mention;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
public class IssueMention extends Mention {

    private int issueNumber;


    public IssueMention(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    @GwtSerializationConstructor
    private IssueMention() {
    }

    public int getIssueNumber() {
        return issueNumber;
    }


    @Override
    public String toString() {
        return toStringHelper("IssueMention")
                .add("issueNumber", issueNumber)
                .toString();
    }
}

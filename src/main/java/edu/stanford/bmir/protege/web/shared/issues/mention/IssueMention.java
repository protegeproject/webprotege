package edu.stanford.bmir.protege.web.shared.issues.mention;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.Mention;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
@TypeAlias("IssueMention")
public class IssueMention implements Mention {

    private int issueNumber;

    @PersistenceConstructor
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

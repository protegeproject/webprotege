package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Sep 2016
 */
public class GetIssueResult implements Result {

    @SuppressWarnings("GwtInconsistentSerializableClass" )
    private Issue issue;

    public GetIssueResult(Issue issue) {
        this.issue = issue;
    }

    @GwtSerializationConstructor
    private GetIssueResult() {
    }

    public Issue getIssue() {
        return issue;
    }
}

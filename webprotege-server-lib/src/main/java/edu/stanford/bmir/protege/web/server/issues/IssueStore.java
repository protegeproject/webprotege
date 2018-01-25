package edu.stanford.bmir.protege.web.server.issues;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.shared.issues.Issue;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class IssueStore {

    private final MongoClient client;

    public IssueStore(MongoClient client) {
        this.client = client;
    }

    public int getNextIssueNumber() {
        return 0;
    }

    public Optional<Issue> getIssue(int issueNumber) {
        return Optional.empty();
    }
}

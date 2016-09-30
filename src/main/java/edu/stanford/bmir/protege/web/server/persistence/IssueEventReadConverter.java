package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import edu.stanford.bmir.protege.web.shared.issues.Milestone;
import edu.stanford.bmir.protege.web.shared.issues.events.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class IssueEventReadConverter implements Converter<BasicDBObject, IssueEvent> {

    private OWLEntityReadConverter entityReadConverter = new OWLEntityReadConverter();

    @Override
    public IssueEvent convert(BasicDBObject dbObject) {
        String eventClass = dbObject.getString("_class");
        UserId userId = UserId.getUserId(dbObject.getString("userId"));
        long timestamp = dbObject.getLong("timestamp");
        if("IssueAssigned".equals(eventClass)) {
            UserId assignee = UserId.getUserId(dbObject.getString("assignee"));
            return new IssueAssigned(userId, timestamp, assignee);
        }
        else if("IssueUnassigned".equals(eventClass)) {
            UserId assignee = UserId.getUserId(dbObject.getString("assignee"));
            return new IssueUnassigned(userId, timestamp, assignee);
        }
        else if("IssueLabelled".equals(eventClass)) {
            return new IssueLabelled(userId, timestamp, dbObject.getString("label"));
        }
        else if("IssueUnlabelled".equals(eventClass)) {
            return new IssueUnlabelled(userId, timestamp, dbObject.getString("label"));
        }
        else if("IssueMilestoned".equals(eventClass)) {
            return new IssueMilestoned(userId, timestamp, new Milestone(dbObject.getString("milestone")));
        }
        else if("IssueDemilestoned".equals(eventClass)) {
            return new IssueDemilestoned(userId, timestamp, new Milestone(dbObject.getString("milestone")));
        }
        else if("IssueLocked".equals(eventClass)) {
            return new IssueLocked(userId, timestamp);
        }
        else if("IssueUnlocked".equals(eventClass)) {
            return new IssueUnlocked(userId, timestamp);
        }
        else if("IssueRenamed".equals(eventClass)) {
            String from = dbObject.getString("from");
            String to = dbObject.getString("to");
            return new IssueRenamed(userId, timestamp, from, to);
        }
        else if("IssueClosed".equals(eventClass)) {
            return new IssueClosed(userId, timestamp);
        }
        else if("IssueReopened".equals(eventClass)) {
            return new IssueReopened(userId, timestamp);
        }
        else if("IssueTargetAdded".equals(eventClass)) {
            OWLEntity targetEntity = entityReadConverter.convert((DBObject) dbObject.get("entity"));
            return new IssueTargetAdded(userId, timestamp, targetEntity);
        }
        else if("IssueTargetRemoved".equals(eventClass)) {
            OWLEntity targetEntity = entityReadConverter.convert((DBObject) dbObject.get("entity"));
            return new IssueTargetRemoved(userId, timestamp, targetEntity);
        }
        else if("IssueReferenced".equals(eventClass)) {
            int issueNumber = dbObject.getInt("issueNumber");
            int referencedByIssueNumber = dbObject.getInt("referencedByIssueNumber");
            return new IssueReferenced(userId, timestamp, issueNumber, referencedByIssueNumber);
        }
//        else if("IssueSubscribed".equals(eventClass)) {
//
//        }
        throw new RuntimeException("Unknown IssueEvent class: " + eventClass);
    }
}

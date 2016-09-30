package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import edu.stanford.bmir.protege.web.shared.issues.Mention;
import edu.stanford.bmir.protege.web.shared.issues.mention.EntityMention;
import edu.stanford.bmir.protege.web.shared.issues.mention.IssueMention;
import edu.stanford.bmir.protege.web.shared.issues.mention.RevisionMention;
import edu.stanford.bmir.protege.web.shared.issues.mention.UserIdMention;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class MentionReadConverter implements Converter<BasicDBObject, Mention> {

    private OWLEntityReadConverter entityReadConverter = new OWLEntityReadConverter();

    @Override
    public Mention convert(BasicDBObject dbObject) {
        Object mentionClass = dbObject.get("_class" );
        if("IssueMention".equals(mentionClass)) {
            return new IssueMention(dbObject.getInt("issueNumber"));
        }
        else if("RevisionMention".equals(mentionClass)) {
            return new RevisionMention(dbObject.getLong("revisionNumber"));
        }
        else if("UserIdMention".equals(mentionClass)) {
            return new UserIdMention(UserId.getUserId(dbObject.get("userId").toString()));
        }
        else if("EntityMention".equals(mentionClass)) {
            DBObject entityObject = (DBObject) dbObject.get("entity");
            return new EntityMention(entityReadConverter.convert(entityObject));
        }
        throw new RuntimeException("Unknown class of Mention: " + mentionClass);
    }
}

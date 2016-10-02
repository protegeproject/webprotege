package edu.stanford.bmir.protege.web.server.crud.obo;

import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.crud.oboid.UserIdRange;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
public class UserIdRangeConverter implements DocumentConverter<UserIdRange> {

    private static final String USER_ID = "userId";

    private static final String START = "start";

    private static final String END = "end";

    @Inject
    public UserIdRangeConverter() {
    }

    @Override
    public Document toDocument(@Nonnull UserIdRange object) {
        Document document = new Document();
        document.append(USER_ID, object.getUserId().getUserName());
        document.append(START, object.getStart());
        document.append(END, object.getEnd());
        return document;
    }

    @Override
    public UserIdRange fromDocument(@Nonnull Document document) {
        UserId userId = UserId.getUserId(document.getString(USER_ID));
        long start = document.getLong(START);
        long end = document.getLong(END);
        return new UserIdRange(userId, start, end);
    }
}

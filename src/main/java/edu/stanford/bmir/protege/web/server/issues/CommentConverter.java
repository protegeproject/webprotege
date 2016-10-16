package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.CommentId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.print.Doc;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class CommentConverter implements DocumentConverter<Comment> {

    private static final String CREATED_BY = "createdBy";

    private static final String CREATED_AT = "createdAt";

    private static final String BODY = "body";

    private static final String UPDATED_AT = "updatedAt";

    @Inject
    public CommentConverter() {
    }

    @Override
    public Document toDocument(@Nonnull Comment object) {
        Document document = new Document();
        document.append(CREATED_BY, object.getCreatedBy().getUserName());
        document.append(CREATED_AT, object.getCreatedAt());
        object.getUpdatedAt().ifPresent(l -> document.append(UPDATED_AT, l));
        document.append(BODY, object.getBody());
        return document;
    }

    @Override
    public Comment fromDocument(@Nonnull Document document) {
        UserId createdBy = UserId.getUserId(document.getString(CREATED_BY));
        long createdAt = document.getLong(CREATED_AT);
        Optional<Long> updatedAt = Optional.ofNullable(document.getLong(UPDATED_AT));
        String body = document.getString(BODY);
        return new Comment(CommentId.create(), createdBy, createdAt, updatedAt, body, body);
    }
}

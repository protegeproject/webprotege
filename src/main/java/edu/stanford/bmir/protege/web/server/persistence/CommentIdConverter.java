package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.issues.CommentId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class CommentIdConverter extends TypeSafeConverter<String, CommentId> implements SimpleValueConverter {

    public CommentIdConverter() {
        super(CommentId.class);
    }

    @Override
    public CommentId decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return CommentId.fromString(fromDBObject.toString());
    }

    @Override
    public String encodeObject(CommentId value, MappedField optionalExtraInfo) {
        return value.getId();
    }
}

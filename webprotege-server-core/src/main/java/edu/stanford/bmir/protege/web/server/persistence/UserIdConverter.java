package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class UserIdConverter extends TypeSafeConverter<String, UserId> implements SimpleValueConverter {

    @Inject
    public UserIdConverter() {
        super(UserId.class);
    }

    @Override
    public UserId decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return UserId.getUserId(fromDBObject);
    }

    @Override
    public String encodeObject(UserId value, MappedField optionalExtraInfo) {
        return value.getUserName();
    }
}

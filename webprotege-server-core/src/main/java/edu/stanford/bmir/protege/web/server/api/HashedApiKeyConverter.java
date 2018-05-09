package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
public class HashedApiKeyConverter extends TypeSafeConverter<String, HashedApiKey> implements SimpleValueConverter {

    public HashedApiKeyConverter() {
        super(HashedApiKey.class);
    }

    @Override
    public HashedApiKey decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return HashedApiKey.valueOf(fromDBObject);
    }

    @Override
    public String encodeObject(HashedApiKey value, MappedField optionalExtraInfo) {
        return value.get();
    }
}

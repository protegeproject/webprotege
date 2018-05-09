package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.api.ApiKeyId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class ApiKeyIdConverter extends TypeSafeConverter<String, ApiKeyId> implements SimpleValueConverter {

    public ApiKeyIdConverter() {
        super(ApiKeyId.class);
    }

    @Override
    public ApiKeyId decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return ApiKeyId.valueOf(fromDBObject);
    }

    @Override
    public String encodeObject(ApiKeyId value, MappedField optionalExtraInfo) {
        return value.getId();
    }
}

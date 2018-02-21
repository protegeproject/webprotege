package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jul 2017
 */
public class CollectionIdConverter extends TypeSafeConverter<String, CollectionId> implements SimpleValueConverter {

    @Inject
    public CollectionIdConverter() {
        super(CollectionId.class);
    }

    @Override
    public CollectionId decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return CollectionId.get(fromDBObject);
    }

    @Override
    public String encodeObject(CollectionId value, MappedField optionalExtraInfo) {
        return value.getId();
    }
}

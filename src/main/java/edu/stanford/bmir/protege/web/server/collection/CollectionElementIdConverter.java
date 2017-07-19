package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jul 2017
 */
public class CollectionElementIdConverter extends TypeSafeConverter<String, CollectionElementId> implements SimpleValueConverter {

    @Inject
    public CollectionElementIdConverter() {
        super(CollectionElementId.class);
    }

    @Override
    public CollectionElementId decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return CollectionElementId.get(fromDBObject);
    }

    @Override
    public String encodeObject(CollectionElementId value, MappedField optionalExtraInfo) {
        return value.getId();
    }
}

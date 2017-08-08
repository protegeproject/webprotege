package edu.stanford.bmir.protege.web.server.collection;

import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jul 2017
 */
public class CollectionItemConverter extends TypeSafeConverter<String, CollectionItem> implements SimpleValueConverter {

    @Inject
    public CollectionItemConverter() {
        super(CollectionItem.class);
    }

    @Override
    public CollectionItem decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return CollectionItem.get(fromDBObject);
    }

    @Override
    public String encodeObject(CollectionItem value, MappedField optionalExtraInfo) {
        return value.getName();
    }
}

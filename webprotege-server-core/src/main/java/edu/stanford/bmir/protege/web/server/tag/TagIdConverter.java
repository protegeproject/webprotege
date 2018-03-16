package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2018
 */
public class TagIdConverter extends TypeSafeConverter<String, TagId> implements SimpleValueConverter {

    @Inject
    public TagIdConverter() {
        super(TagId.class);
    }

    @Override
    public TagId decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return TagId.getId(fromDBObject);
    }

    @Override
    public String encodeObject(TagId value, MappedField optionalExtraInfo) {
        return value.getId();
    }
}

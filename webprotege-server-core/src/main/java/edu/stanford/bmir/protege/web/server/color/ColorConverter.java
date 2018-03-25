package edu.stanford.bmir.protege.web.server.color;

import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.color.Color;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2018
 */
public class ColorConverter extends TypeSafeConverter<String, Color> implements SimpleValueConverter{

    @Inject
    public ColorConverter() {
        super(Color.class);
    }

    @Override
    public Color decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return Color.getHex(fromDBObject);
    }

    @Override
    public String encodeObject(Color value, MappedField optionalExtraInfo) {
        return value.getHex();
    }
}

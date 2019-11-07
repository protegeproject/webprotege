package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jul 2017
 */
public class FormIdConverter extends TypeSafeConverter<String, FormId> implements SimpleValueConverter {

    @Inject
    public FormIdConverter() {
        super(FormId.class);
    }

    @Override
    public FormId decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return FormId.get(fromDBObject);
    }

    @Override
    public String encodeObject(FormId value, MappedField optionalExtraInfo) {
        return value.getId();
    }
}

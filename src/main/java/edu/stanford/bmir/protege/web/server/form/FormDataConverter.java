package edu.stanford.bmir.protege.web.server.form;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import edu.stanford.bmir.protege.web.server.persistence.TypeSafeConverter;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jul 2017
 */
public class FormDataConverter extends TypeSafeConverter<DBObject, FormData> implements SimpleValueConverter {

    private final FormDataValueConverter valueConverter;

    @Inject
    public FormDataConverter(@Nonnull FormDataValueConverter valueConverter) {
        super(FormData.class);
        this.valueConverter = valueConverter;
    }

    @Override
    public FormData decodeObject(DBObject fromDBObject, MappedField optionalExtraInfo) {
        Map<FormElementId, FormDataValue> map = new HashMap<>();
        fromDBObject.keySet().forEach(key -> {
            FormDataValue value = valueConverter.decodeObject(fromDBObject.get(key), optionalExtraInfo);
            map.put(FormElementId.get(key), value);
        });
        return new FormData(map);
    }

    @Override
    public DBObject encodeObject(FormData value, MappedField optionalExtraInfo) {
        if(value == null) {
            return null;
        }
        DBObject object = new BasicDBObject();
        value.getData().forEach((formElementId, formDataValue) -> {
            if (formDataValue != null) {
                object.put(formElementId.getId(), valueConverter.encodeObject(formDataValue, optionalExtraInfo));
            }
        });
        return object;
    }
}

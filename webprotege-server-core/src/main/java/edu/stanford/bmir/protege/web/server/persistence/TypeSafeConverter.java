package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.DBObject;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public abstract class TypeSafeConverter<D, O> extends TypeConverter {

    public TypeSafeConverter(Class ... cls) {
        super(cls);
    }

    /**
     * decode the {@link DBObject} and provide the corresponding java (type-safe) object <br><b>NOTE: optionalExtraInfo might
     * be
     * null</b>
     *
     * @param targetClass       the class to create and populate
     * @param fromDBObject      the DBObject to use when populating the new instance
     * @param optionalExtraInfo the MappedField that contains the metadata useful for decoding
     * @return the new instance
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object decode(Class<?> targetClass, Object fromDBObject, MappedField optionalExtraInfo) {
        return decodeObject((D) fromDBObject, optionalExtraInfo);
    }

    public abstract O decodeObject(D fromDBObject, MappedField optionalExtraInfo);

    /**
     * encode the (type-safe) java object into the corresponding {@link DBObject}
     *
     * @param value             The object to encode
     * @param optionalExtraInfo the MappedField that contains the metadata useful for decoding
     * @return the encoded version of the object
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object encode(Object value, MappedField optionalExtraInfo) {
        return encodeObject((O) value, optionalExtraInfo);
    }

    public abstract D encodeObject(O value, MappedField optionalExtraInfo);


}

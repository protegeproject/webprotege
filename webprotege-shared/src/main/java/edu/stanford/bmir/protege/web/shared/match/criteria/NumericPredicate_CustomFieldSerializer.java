package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class NumericPredicate_CustomFieldSerializer extends CustomFieldSerializer<NumericPredicate> {


    public static NumericPredicate instantiate(SerializationStreamReader streamReader) throws SerializationException {
        return NumericPredicate.values()[streamReader.readInt()];
    }

    public static void serialize(SerializationStreamWriter streamWriter, NumericPredicate instance) throws SerializationException {
    }

    public static void deserialize(@SuppressWarnings("unused") SerializationStreamReader streamReader, @SuppressWarnings("unused") NumericPredicate instance) {
    }

    @Override
    public void deserializeInstance(SerializationStreamReader streamReader, NumericPredicate instance) {
        deserialize(streamReader, instance);
    }

    @Override
    public boolean hasCustomInstantiateInstance() {
        return true;
    }

    @Override
    public NumericPredicate instantiateInstance(SerializationStreamReader streamReader) throws SerializationException {
        return instantiate(streamReader);
    }

    @Override
    public void serializeInstance(SerializationStreamWriter streamWriter, NumericPredicate instance) throws SerializationException {
        serialize(streamWriter, instance);
    }
}

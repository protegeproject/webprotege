package edu.stanford.bmir.protege.web.shared.entity;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import org.semanticweb.owlapi.model.IRI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Mar 2017
 */
public class IRIData_CustomFieldSerializer extends CustomFieldSerializer<IRIData> {


    @Override
    public boolean hasCustomInstantiateInstance() {
        return true;
    }

    public static IRIData instantiate(SerializationStreamReader streamReader) throws SerializationException {
        return new IRIData(IRI.create(streamReader.readString()));
    }

    public static void serialize(SerializationStreamWriter streamWriter, IRIData instance) throws SerializationException {
        streamWriter.writeString(instance.getObject().toString());
    }

    public static void deserialize(SerializationStreamReader streamReader, IRIData instance) {

    }


    @Override
    public void deserializeInstance(SerializationStreamReader streamReader,
                                    IRIData instance) throws SerializationException {

    }

    @Override
    public void serializeInstance(SerializationStreamWriter streamWriter,
                                  IRIData instance) throws SerializationException {
        serialize(streamWriter, instance);
    }

    @Override
    public IRIData instantiateInstance(SerializationStreamReader streamReader) throws SerializationException {
        return instantiate(streamReader);
    }
}

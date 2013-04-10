package edu.stanford.bmir.protege.web.shared.event;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.events.EventTag;

/**
 * An implementation of CustomFieldSerilizer for serializing {@link GetProjectEventsAction}
 * objects.
 */
public class GetProjectEventsAction_CustomFieldSerializer extends CustomFieldSerializer<GetProjectEventsAction> {

    /**
     * @return <code>true</code> if a specialist {@link #instantiateInstance} is
     *         implemented; <code>false</code> otherwise
     */
    @Override
    public boolean hasCustomInstantiateInstance() {
        return true;
    }

    /**
     * Instantiates an object from the {@link com.google.gwt.user.client.rpc.SerializationStreamReader}.
     * <p>
     * Most of the time, this can be left unimplemented and the framework
     * will instantiate the instance itself.  This is typically used when the
     * object being deserialized is immutable, hence it has to be created with
     * its state already set.
     * <p>
     * If this is overridden, the {@link #hasCustomInstantiateInstance} method
     * must return <code>true</code> in order for the framework to know to call
     * it.
     * @param streamReader the {@link com.google.gwt.user.client.rpc.SerializationStreamReader} to read the
     * object's content from
     * @return an object that has been loaded from the
     *         {@link com.google.gwt.user.client.rpc.SerializationStreamReader}
     * @throws com.google.gwt.user.client.rpc.SerializationException
     *          if the instantiation operation is not
     *          successful
     */
    @Override
    public GetProjectEventsAction instantiateInstance(SerializationStreamReader streamReader) throws SerializationException {
        return instantiate(streamReader);
    }

    public static GetProjectEventsAction instantiate(SerializationStreamReader streamReader) throws SerializationException {
        String projectName = streamReader.readString();
        String userName = streamReader.readString();
        int ordinal = streamReader.readInt();
        return new GetProjectEventsAction(EventTag.get(ordinal), ProjectId.get(projectName), UserId.getUserId(userName));
    }


    /**
     * Serializes the content of the object into the
     * {@link com.google.gwt.user.client.rpc.SerializationStreamWriter}.
     * @param streamWriter the {@link com.google.gwt.user.client.rpc.SerializationStreamWriter} to write the
     * object's content to
     * @param instance the object instance to serialize
     * @throws com.google.gwt.user.client.rpc.SerializationException
     *          if the serialization operation is not
     *          successful
     */
    @Override
    public void serializeInstance(SerializationStreamWriter streamWriter, GetProjectEventsAction instance) throws SerializationException {
        serialize(streamWriter, instance);
    }

    public static void serialize(SerializationStreamWriter streamWriter, GetProjectEventsAction instance) throws SerializationException {
        streamWriter.writeString(instance.getProjectId().getProjectName());
        streamWriter.writeString(instance.getUserId().getUserName());
        streamWriter.writeInt(instance.getSinceTag().getOrdinal());
    }


    /**
     * Deserializes the content of the object from the
     * {@link com.google.gwt.user.client.rpc.SerializationStreamReader}.
     * @param streamReader the {@link com.google.gwt.user.client.rpc.SerializationStreamReader} to read the
     * object's content from
     * @param instance the object instance to deserialize
     * @throws com.google.gwt.user.client.rpc.SerializationException
     *          if the deserialization operation is not
     *          successful
     */
    @Override
    public void deserializeInstance(SerializationStreamReader streamReader, GetProjectEventsAction instance) throws SerializationException {
        deserialize(streamReader, instance);
    }

    public static void deserialize(SerializationStreamReader streamReader, GetProjectEventsAction instance) throws SerializationException {

    }
}
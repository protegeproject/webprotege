package edu.stanford.bmir.protege.web.shared.event;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.events.ProjectEventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of CustomFieldSerilizer for serializing {@link GetProjectEventsResult}
 * objects.
 */
public class GetProjectEventsResult_CustomFieldSerializer extends CustomFieldSerializer<GetProjectEventsResult> {

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
    public GetProjectEventsResult instantiateInstance(SerializationStreamReader streamReader) throws SerializationException {
        return instantiate(streamReader);
    }

    @SuppressWarnings("unchecked")
    public static GetProjectEventsResult instantiate(SerializationStreamReader streamReader) throws SerializationException {
        byte empty = streamReader.readByte();
        List<ProjectEvent<?>> events;
        if(empty == 1) {
            events = (List<ProjectEvent<?>>) streamReader.readObject();
        }
        else {
            events = Collections.emptyList();
        }
        String projectName = streamReader.readString();
        int startTagOrdinal = streamReader.readInt();
        int endTagOrdinal = streamReader.readInt();
        final EventTag startTag = EventTag.get(startTagOrdinal);
        final EventTag endTag = EventTag.get(endTagOrdinal);
        ProjectEventList.Builder builder = ProjectEventList.builder(startTag, ProjectId.get(projectName), endTag);
        builder.addEvents(events);
        return new GetProjectEventsResult(builder.build());
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
    public void serializeInstance(SerializationStreamWriter streamWriter, GetProjectEventsResult instance) throws SerializationException {
        serialize(streamWriter, instance);
    }

    public static void serialize(SerializationStreamWriter streamWriter, GetProjectEventsResult instance) throws SerializationException {
        if(instance.getEvents().isEmpty()) {
            streamWriter.writeByte((byte) 0);
        }
        else {
            streamWriter.writeByte((byte) 1);
            streamWriter.writeObject(new ArrayList<WebProtegeEvent<?>>(instance.getEvents().getEvents()));
        }
        streamWriter.writeString(instance.getEvents().getProjectId().getId());
        int startTagOrdinal = instance.getEvents().getStartTag().getOrdinal();
        streamWriter.writeInt(startTagOrdinal);
        int endTagOrdinal = instance.getEvents().getEndTag().getOrdinal();
        streamWriter.writeInt(endTagOrdinal);
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
    public void deserializeInstance(SerializationStreamReader streamReader, GetProjectEventsResult instance) throws SerializationException {
        deserialize(streamReader, instance);
    }

    public static void deserialize(SerializationStreamReader streamReader, GetProjectEventsResult instance) throws SerializationException {

    }
}
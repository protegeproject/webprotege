package edu.stanford.bmir.protege.web.server.util;

import static edu.stanford.bmir.protege.web.server.util.JavaUtil.cast;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * These utility methods are for manipulating arbitrary JSON without needing to worry about what
 * typed entities the JSON should be de/serialized to/from.
 * 
 * Initial motivation is to be able to change the value of "projectId" in project settings and 
 * project forms import JSON.
 * 
 * @author Chris Wolf <c.wolf@elsevier.com>
 */
public class TypelessJSONSerialization {
    
    // TODO: should probably be injected, but effects the whole upstream DI call chain...
    // TODO: ...and plus, ObjectMapper very expensive, so should be created only once.
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TypelessJSONSerialization() {
    }

    /**
     * Accepts any arbitrary JSON and deserializes to nested <code>LinkedHashMap<String, Object></code>
     * instances.  Note that the <code>Object</code> type parameter could be of type <code>String</code>, 
     * <code>List</code>, or <code>Map</code> where the latter is for arbitrary nesting levels.
     * 
     * <b>N.B.</b> For normal, strongly typed Webprotege serialization, use the <code>ObjectMapper</code> 
     * obtained from <code>ObjectMapperProvider</code> from <i>webprotege-server-core</i>.
     * 
     * @param json
     * @return
     * @throws IOException
     */
    public static Map<String, Object> deserializeJSON(@Nonnull String json) throws IOException {
	ObjectReader objectReader = objectMapper.readerFor(new TypeReference<Map<String, Object>>() {
	});
	return objectReader.readValue(json);
    }

    /**
     * 
     * @param object
     * @return
     * @throws IOException
     */
    public static String serializeToJSON(Map<String, Object> object) throws IOException {
	return serializeToJSON(object, false);
    }

    public static String serializeToJSON(Map<String, Object> object, boolean prettyPrint) throws IOException {
	ObjectWriter objectWriter = prettyPrint ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();
	return objectWriter.writeValueAsString(object);
    }

    public static String resplaceAllStringValue(@Nonnull String json, @Nonnull String keyName,
	    @Nonnull String replacementValue) throws IOException {

	Map<String, Object> deserialized = deserializeJSON(json);

	walkMapAndDReplace(deserialized, keyName, replacementValue);

	String serialized = serializeToJSON(deserialized);

	return serialized;
    }

    static void walkMapAndDReplace(Map<String, Object> data, String keyName, String replacementValue) {
	for (var mapEntry : data.entrySet()) {
	    if (mapEntry.getValue() instanceof String && mapEntry.getKey().equals(keyName)) {
		mapEntry.setValue(replacementValue);
	    } else if (mapEntry.getValue() instanceof List) {
		List<Object> objlist = cast(mapEntry.getValue());
		for (Object listEntry : objlist) {
		    if (listEntry instanceof Map)
			walkMapAndDReplace(cast(listEntry), keyName, replacementValue);
		}
	    } else if (mapEntry.getValue() instanceof Map) {
		walkMapAndDReplace(cast(mapEntry.getValue()), keyName, replacementValue);
	    }
	}
    }
}

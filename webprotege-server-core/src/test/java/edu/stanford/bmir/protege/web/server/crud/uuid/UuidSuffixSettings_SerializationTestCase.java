package edu.stanford.bmir.protege.web.server.crud.uuid;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class UuidSuffixSettings_SerializationTestCase {

    @Test
    public void shouldDeserializeLegacySerialization() throws IOException {
        var serialization = "{\"_class\" : \"edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings\"}";
        ObjectMapperProvider objectMapperProvider = new ObjectMapperProvider();
        ObjectMapper objectMapper = objectMapperProvider.get();
        var deserializedObject = objectMapper.readValue(serialization, EntityCrudKitSuffixSettings.class);
        assertThat(deserializedObject, is(UuidSuffixSettings.get()));
    }

    @Test
    public void shouldSerializeDefaultSettings() throws IOException {
        var settings = UuidSuffixSettings.get();
        JsonSerializationTestUtil.testSerialization(settings, EntityCrudKitSuffixSettings.class);
    }
}

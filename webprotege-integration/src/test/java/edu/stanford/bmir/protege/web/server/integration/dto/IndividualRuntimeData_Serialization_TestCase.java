package edu.stanford.bmir.protege.web.server.integration.dto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Jackson round-trip tests for integration DTOs.
 * Discovered by Surefire ({@code mvn test -pl webprotege-integration}); no production callers.
 * Synthetic JSON only — no external data files.
 */
public class IndividualRuntimeData_Serialization_TestCase {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        // count is a derived getter on ListResponse; ignore it when round-tripping JSON.
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    public void shouldRoundTripIndividualRuntimeData() throws Exception {
        Map<String, String> properties = new LinkedHashMap<>();
        properties.put("http://example.org/hasStatus", "RUNNING");
        properties.put("http://example.org/hasTemp", "23.5");
        IndividualRuntimeData original = new IndividualRuntimeData(
                "00000000-0000-0000-0000-000000000001",
                "http://example.org/Sensor1",
                properties,
                1_700_000_000_000L,
                "api-user",
                Collections.singletonList("http://example.org/Sensor")
        );

        String json = objectMapper.writeValueAsString(original);
        JsonNode node = objectMapper.readTree(json);
        assertThat(node.get("projectId").asText(), is(original.getProjectId()));
        assertThat(node.get("individualIri").asText(), is(original.getIndividualIri()));
        assertThat(node.get("updatedAt").asLong(), is(original.getUpdatedAt()));
        assertThat(node.get("updatedBy").asText(), is("api-user"));
        assertThat(node.get("properties").get("http://example.org/hasStatus").asText(), is("RUNNING"));
        assertThat(node.get("types").get(0).asText(), is("http://example.org/Sensor"));

        IndividualRuntimeData restored = objectMapper.readValue(json, IndividualRuntimeData.class);
        assertThat(restored, is(original));
    }

    @Test
    public void shouldDeserializeNullPropertiesAsEmptyMap() throws Exception {
        String json = "{"
                + "\"projectId\":\"00000000-0000-0000-0000-000000000001\","
                + "\"individualIri\":\"http://example.org/Sensor1\","
                + "\"properties\":null,"
                + "\"updatedAt\":0,"
                + "\"updatedBy\":null"
                + "}";

        IndividualRuntimeData data = objectMapper.readValue(json, IndividualRuntimeData.class);

        assertThat(data.getProperties().isEmpty(), is(true));
        assertThat(data.getUpdatedBy(), nullValue());
        assertThat(data.getTypes().isEmpty(), is(true));
    }

    @Test
    public void shouldRoundTripRequest() throws Exception {
        IndividualRuntimeDataRequest original = new IndividualRuntimeDataRequest(
                "http://example.org/Sensor1",
                Collections.singletonMap("http://example.org/hasStatus", "IDLE")
        );

        String json = objectMapper.writeValueAsString(original);
        IndividualRuntimeDataRequest restored = objectMapper.readValue(json, IndividualRuntimeDataRequest.class);

        assertThat(restored, is(original));
        assertThat(restored.getProperties(), hasEntry("http://example.org/hasStatus", "IDLE"));
    }

    @Test
    public void shouldRoundTripListResponseIncludingCount() throws Exception {
        IndividualRuntimeData item = new IndividualRuntimeData(
                "00000000-0000-0000-0000-000000000001",
                "http://example.org/Sensor1",
                Collections.singletonMap("http://example.org/hasStatus", "RUNNING"),
                10L,
                "api-user"
        );
        IndividualRuntimeDataListResponse original =
                new IndividualRuntimeDataListResponse(Collections.singletonList(item));

        String json = objectMapper.writeValueAsString(original);
        JsonNode node = objectMapper.readTree(json);
        assertThat(node.get("count").asInt(), is(1));
        assertThat(node.get("items").isArray(), is(true));
        assertThat(node.get("items").size(), is(1));

        IndividualRuntimeDataListResponse restored =
                objectMapper.readValue(json, IndividualRuntimeDataListResponse.class);
        assertThat(restored.getCount(), is(1));
        assertThat(restored.getItems().get(0), is(item));
    }

    @Test
    public void shouldExposeWithPropertiesCopy() {
        IndividualRuntimeData original = new IndividualRuntimeData(
                "00000000-0000-0000-0000-000000000001",
                "http://example.org/Sensor1",
                Collections.singletonMap("http://example.org/hasStatus", "RUNNING"),
                1L,
                "old-user"
        );

        IndividualRuntimeData updated = original.withProperties(
                Collections.singletonMap("http://example.org/hasStatus", "IDLE"),
                2L,
                "new-user"
        );

        assertThat(updated.getProperties(), hasEntry("http://example.org/hasStatus", "IDLE"));
        assertThat(updated.getUpdatedAt(), is(2L));
        assertThat(updated.getUpdatedBy(), is("new-user"));
        assertThat(updated.getTypes(), is(original.getTypes()));
        assertThat(original.getProperties(), hasEntry("http://example.org/hasStatus", "RUNNING"));
    }
}

package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Aug 2018
 */
public class PerspectiveId_Json_TestCase {

    private static final String ID = "12345678-1234-1234-1234-123456789abc";

    private PerspectiveId perspectiveId;

    @Before
    public void setUp() throws Exception {
        perspectiveId = PerspectiveId.get(ID);
    }

    @Test
    public void shouldSerializeJson() throws Exception {
        String result = new ObjectMapper().writeValueAsString(perspectiveId);
        assertThat(result, is("\"" + ID + "\""));
    }

    @Test
    public void shouldDeserializeJson() throws Exception {
        PerspectiveId perspectiveId = new ObjectMapper()
                .readerFor(PerspectiveId.class)
                .readValue("\"" + ID + "\"");
        assertThat(perspectiveId, is(this.perspectiveId));
    }
}

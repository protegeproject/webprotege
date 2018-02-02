package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Feb 2018
 */
public class ActionId_Json_TestCase {

    private static final String ID = "TheActionId";

    private ActionId actionId;

    @Before
    public void setUp() throws Exception {
        actionId = new ActionId(ID);
    }

    @Test
    public void shouldSerializeJson() throws Exception {
        String result = new ObjectMapper().writeValueAsString(actionId);
        assertThat(result, is("\"" + ID + "\""));
    }

    @Test
    public void shouldDeserializeJson() throws Exception {
        ActionId readActionId = new ObjectMapper()
                .readerFor(ActionId.class)
                .readValue("\"" + ID + "\"");
        assertThat(readActionId, is(actionId));
    }
}

package edu.stanford.bmir.protege.web.shared.user;

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
public class UserId_Json_TestCase {

    private static final String THE_USER_NAME = "The User Name";

    private UserId userId;

    @Before
    public void setUp() throws Exception {
        userId = UserId.getUserId(THE_USER_NAME);
    }

    @Test
    public void shouldSerializeJson() throws Exception {
        String result = new ObjectMapper().writeValueAsString(userId);
        assertThat(result, is("\"" + THE_USER_NAME + "\""));
    }

    @Test
    public void shouldDeserializeJson() throws Exception {
        UserId readUserId = new ObjectMapper()
                .readerFor(UserId.class)
                .readValue("\"" + THE_USER_NAME + "\"");
        assertThat(readUserId, is(userId));
    }
}

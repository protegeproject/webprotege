package edu.stanford.bmir.protege.web.shared.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Feb 2018
 */
public class EmailAddress_Json_TestCase {

    private static final String ADDRESS = "jane.doe@stanford.edu";

    private EmailAddress emailAddress;

    @Before
    public void setUp() throws Exception {
        emailAddress = new EmailAddress(ADDRESS);
    }

    @Test
    public void shouldSerializeJson() throws Exception {
        String result = new ObjectMapper().writeValueAsString(emailAddress);
        assertThat(result, is("\"" + ADDRESS + "\""));
    }

    @Test
    public void shouldDeserializeJson() throws Exception {
        EmailAddress readEmailAddress = new ObjectMapper()
                .readerFor(EmailAddress.class)
                .readValue("\"" + ADDRESS + "\"");
        assertThat(readEmailAddress, is(emailAddress));
    }
}

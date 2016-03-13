package edu.stanford.bmir.protege.web.server.app;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.json.Json;
import javax.json.JsonObject;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class UserInSessionEncoder_TestCase {

    private UserInSessionEncoder encoder;

    @Mock
    private UserInSession userInSession;

    @Mock
    private UserDetails userDetails;

    @Mock
    private UserId userId;

    private JsonObject expectedJsonObject;

    @Before
    public void setUpExpectedJsonObject() {
        InputStream is = UserInSessionEncoder_TestCase.class.getResourceAsStream("UserInSession.json");
        expectedJsonObject = Json.createReader(is).readObject();
    }

    @Before
    public void setUp() throws Exception {
        encoder = new UserInSessionEncoder();
        when(userInSession.getUserDetails()).thenReturn(userDetails);
        when(userDetails.getUserId()).thenReturn(userId);
        when(userId.getUserName()).thenReturn("JohnSmith");
        when(userDetails.getDisplayName()).thenReturn("John Smith");
        when(userDetails.getEmailAddress()).thenReturn(Optional.of("john.smith@gmail.com"));
    }


    @Test
    public void shouldEncodeUserInSession() {
        JsonObject json = encoder.encode(userInSession);
        assertThat(json, is(expectedJsonObject));
    }


}

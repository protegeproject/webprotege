package edu.stanford.bmir.protege.web.server.app;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ApplicationResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22 Dec 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class UserInSessionFactory_TestCase {

    @Mock
    private AccessManager accessManager;

    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private UserDetails userDetails;

    private UserId userId = UserId.getUserId("TheUser");

    private UserInSessionFactory factory;

    private Set<ActionId> appActions;


    @Before
    public void setUp() throws Exception {
        appActions = Sets.newHashSet(mock(ActionId.class));
        factory = new UserInSessionFactory(accessManager, userDetailsManager);
        // By default, unrecognized user
        when(userDetailsManager.getUserDetails(any())).thenReturn(Optional.empty());
        when(accessManager.getActionClosure(any(),
                                            eq(ApplicationResource.get()))).thenReturn(Collections.emptySet());

        // Guest user
        when(userDetailsManager.getUserDetails(UserId.getGuest())).thenReturn(Optional.of(UserDetails.getGuestUserDetails()));
        when(accessManager.getActionClosure(eq(Subject.forUser(userId)),
                                            eq(ApplicationResource.get()))).thenReturn(appActions);

        // Specific user
        when(userDetailsManager.getUserDetails(userId)).thenReturn(Optional.of(userDetails));
        when(accessManager.getActionClosure(eq(Subject.forGuestUser()),
                                            eq(ApplicationResource.get()))).thenReturn(Collections.emptySet());
    }

    @Test
    public void shouldProvideUserInSession() {
        UserInSession userInSession = factory.getUserInSession(userId);
        assertThat(userInSession.getUserDetails(), is(userDetails));
        assertThat(userInSession.getAllowedApplicationActions(), is(appActions));
    }

    @Test
    public void shouldProvideUserInSessionForGuestUser() {
        UserInSession userInSession = factory.getUserInSession(UserId.getGuest());
        assertThat(userInSession.getUserDetails(), is(UserDetails.getGuestUserDetails()));
        assertThat(userInSession.getAllowedApplicationActions(), is(Collections.emptySet()));
    }

    @Test
    public void shouldProvideUserInSessionForUnknownUser() {
        UserInSession userInSession = factory.getUserInSession(UserId.getUserId("SomeOtherUserId"));
        assertThat(userInSession.getUserDetails(), is(UserDetails.getGuestUserDetails()));
        assertThat(userInSession.getAllowedApplicationActions(), is(Collections.emptySet()));
    }
}

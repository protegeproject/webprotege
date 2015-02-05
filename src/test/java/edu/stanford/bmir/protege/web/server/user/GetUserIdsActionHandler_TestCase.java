package edu.stanford.bmir.protege.web.server.user;

import com.beust.jcommander.internal.Sets;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsAction;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetUserIdsActionHandler_TestCase {


    private GetUserIdsActionHandler handler;

    @Mock
    private MetaProject metaProject;

    @Mock
    private User userA, userB, userC, nullUser;

    @Mock
    private ExecutionContext executionContext;

    @Before
    public void setUp() throws Exception {
        handler = new GetUserIdsActionHandler(metaProject);
        when(userA.getName()).thenReturn("User A");
        when(userB.getName()).thenReturn("User B");
        when(userC.getName()).thenReturn("User C");
        when(nullUser.getName()).thenReturn(null);
        Set<User> users = Sets.newHashSet();
        users.add(userA);
        users.add(userB);
        users.add(userC);
        users.add(nullUser);
        when(metaProject.getUsers()).thenReturn(users);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new GetUserIdsActionHandler(null);
    }

    @Test
    public void shouldReturnUsers() {
        GetUserIdsResult result = handler.execute(new GetUserIdsAction(), executionContext);
        ImmutableList<UserId> resultUserIds = result.getUserIds();
        assertThat(resultUserIds, containsInAnyOrder(
                UserId.getUserId("User A"),
                UserId.getUserId("User B"),
                UserId.getUserId("User C")));
    }




}

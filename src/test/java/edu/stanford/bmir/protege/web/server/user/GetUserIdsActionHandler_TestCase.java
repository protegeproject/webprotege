package edu.stanford.bmir.protege.web.server.user;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsAction;
import edu.stanford.bmir.protege.web.shared.user.GetUserIdsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
    private HasUserIds hasUserIds;

    @Mock
    private UserId userA, userB, userC, nullUser;

    @Mock
    private ExecutionContext executionContext;

    @Before
    public void setUp() throws Exception {
        handler = new GetUserIdsActionHandler(hasUserIds);
        when(userA.getUserName()).thenReturn("User A");
        when(userB.getUserName()).thenReturn("User B");
        when(userC.getUserName()).thenReturn("User C");
        Set<UserId> users = Sets.newHashSet();
        users.add(userA);
        users.add(userB);
        users.add(userC);
        when(hasUserIds.getUserIds()).thenReturn(users);
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
                userA,
                userB,
                userC));
    }
}

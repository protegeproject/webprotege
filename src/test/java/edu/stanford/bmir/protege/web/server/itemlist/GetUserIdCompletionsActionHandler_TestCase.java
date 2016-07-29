
package edu.stanford.bmir.protege.web.server.itemlist;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.shared.itemlist.GetUserIdCompletionsAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetUserIdCompletionsActionHandler_TestCase {

    private GetUserIdCompletionsActionHandler actionHandler;

    @Mock
    private UserDetailsManager userDetailsManager;

    private List<UserId> userIds;

    @Mock
    private UserId johnSmith, janeDoe;

    @Mock
    private GetUserIdCompletionsAction action;

    @Before
    public void setUp() {
        actionHandler = new GetUserIdCompletionsActionHandler(userDetailsManager);
        userIds = Arrays.asList(johnSmith, janeDoe);
        when(userDetailsManager.getUserIds()).thenReturn(userIds);
        when(johnSmith.getUserName()).thenReturn("John Smith");
        when(janeDoe.getUserName()).thenReturn("Jane Doe");
        when(userDetailsManager.getUserIdsContainingIgnoreCase(anyString(), anyLong())).thenReturn(userIds);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userDetailsManager_IsNull() {
        new GetUserIdCompletionsActionHandler(null);
    }

    @Test
    public void shouldReturnFoundUserIds() {
        when(action.getCompletionText()).thenReturn("j");
        GetPossibleItemCompletionsResult<UserId> result = actionHandler.execute(action, mock(ExecutionContext.class));
        assertThat(result.getPossibleItemCompletions(), hasItems(janeDoe, johnSmith));
    }
}

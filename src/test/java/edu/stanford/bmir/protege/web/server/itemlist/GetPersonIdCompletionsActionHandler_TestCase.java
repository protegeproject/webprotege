
package edu.stanford.bmir.protege.web.server.itemlist;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdCompletionsAction;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetPersonIdCompletionsActionHandler_TestCase {

    private GetPersonIdCompletionsActionHandler actionHandler;

    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private UserId johnSmith, janeDoe;

    @Mock
    private GetPersonIdCompletionsAction action;

    @Before
    public void setUp()
            throws Exception
    {
        actionHandler = new GetPersonIdCompletionsActionHandler(userDetailsManager);
        when(johnSmith.getUserName()).thenReturn("John Smith");
        when(janeDoe.getUserName()).thenReturn("Jane Doe");
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userDetailsManager_IsNull() {
        new GetPersonIdCompletionsActionHandler(null);
    }

    @Test
    public void shouldIgnoreCase() {
        when(action.getCompletionText()).thenReturn("j");
        GetPossibleItemCompletionsResult<PersonId> result = actionHandler.execute(action, mock(ExecutionContext.class));
        assertThat(result.getPossibleItemCompletions(), hasItems(new PersonId(janeDoe.getUserName()), new PersonId(johnSmith.getUserName())));
    }

    @Test
    public void shouldMatchWithinUserName() {
        when(action.getCompletionText()).thenReturn("Doe");
        GetPossibleItemCompletionsResult<PersonId> result = actionHandler.execute(action, mock(ExecutionContext.class));
        assertThat(result.getPossibleItemCompletions(), hasItem(new PersonId(janeDoe.getUserName())));
    }

}


package edu.stanford.bmir.protege.web.server.itemlist;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.user.UserDetailsManager;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdItemsAction;
import edu.stanford.bmir.protege.web.shared.itemlist.GetPersonIdItemsResult;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetPersonIdItemsActionHandler_TestCase {

    private GetPersonIdItemsActionHandler actionHandler;

    @Mock
    private UserDetailsManager userDetailsManager;

    private UserId
            johnSmith_UpperCase = UserId.getUserId("John Smith"),
            johnSmith_LowerCase = UserId.getUserId("john smith");

    @Mock
    private GetPersonIdItemsAction action;

    @Before
    public void setUp()
            throws Exception
    {
        actionHandler = new GetPersonIdItemsActionHandler(userDetailsManager);
        when(userDetailsManager.getUserDetails(johnSmith_UpperCase)).thenReturn(Optional.of(mock(UserDetails.class)));
        when(userDetailsManager.getUserDetails(johnSmith_LowerCase)).thenReturn(Optional.of(mock(UserDetails.class)));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userDetailsManager_IsNull() {
        new GetPersonIdItemsActionHandler(null);
    }

    @Test
    public void shouldOnlyMatchExact() {
        when(action.getItemNames()).thenReturn(Arrays.asList("John Smith"));
        GetPersonIdItemsResult result = actionHandler.execute(action, mock(ExecutionContext.class));
        assertThat(result.getItems(), hasItems(new PersonId(johnSmith_UpperCase.getUserName())));
    }

}

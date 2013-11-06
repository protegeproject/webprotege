package edu.stanford.bmir.protege.web.shared.mail;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class GetEmailAddressActionTestCase {

    public static final UserId DUMMY_USER_ID = UserId.getUserId("dummy");

    @Test(expected = NullPointerException.class)
    public void nullUserIdThrowsNullPointerException() {
        new GetEmailAddressAction(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void guestUserIdThrowsIllegalArgumentException() {
        new GetEmailAddressAction(UserId.getGuest());
    }

    @Test
    public void getUserIdReturnsSuppliedUserId() {
        GetEmailAddressAction action = new GetEmailAddressAction(DUMMY_USER_ID);
        assertEquals(DUMMY_USER_ID, action.getUserId());
    }

    @Test
    public void equalsReturnsTrueForSameUserId() {
        GetEmailAddressAction actionA = new GetEmailAddressAction(DUMMY_USER_ID);
        GetEmailAddressAction actionB = new GetEmailAddressAction(DUMMY_USER_ID);
        assertEquals(actionA, actionB);
    }

    @Test
    public void hashCodeReturnsSameValueForSameUserId() {
        GetEmailAddressAction actionA = new GetEmailAddressAction(DUMMY_USER_ID);
        GetEmailAddressAction actionB = new GetEmailAddressAction(DUMMY_USER_ID);
        assertEquals(actionA.hashCode(), actionB.hashCode());
    }
}

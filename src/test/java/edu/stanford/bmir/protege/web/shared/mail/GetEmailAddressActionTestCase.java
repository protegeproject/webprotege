package edu.stanford.bmir.protege.web.shared.mail;

import edu.stanford.bmir.protege.web.MockingUtils;
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
        UserId userId = MockingUtils.mockUserId();
        GetEmailAddressAction action = new GetEmailAddressAction(userId);
        assertEquals(userId, action.getUserId());
    }

    @Test
    public void equalsReturnsTrueForSameUserId() {
        UserId userId = MockingUtils.mockUserId();
        GetEmailAddressAction actionA = new GetEmailAddressAction(userId);
        GetEmailAddressAction actionB = new GetEmailAddressAction(userId);
        assertEquals(actionA, actionB);
    }

    @Test
    public void hashCodeReturnsSameValueForSameUserId() {
        UserId userId = MockingUtils.mockUserId();
        GetEmailAddressAction actionA = new GetEmailAddressAction(userId);
        GetEmailAddressAction actionB = new GetEmailAddressAction(userId);
        assertEquals(actionA.hashCode(), actionB.hashCode());
    }
}

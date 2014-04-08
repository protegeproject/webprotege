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
public class SetEmailAddressActionTestCase {

    public static final String DUMMY_EMAIL = "test@test.com";

    public static final UserId DUMMY_USER_ID = MockingUtils.mockUserId();

    @Test(expected = NullPointerException.class)
    public void nullUserIdThrowsNullPointerException() {
        new SetEmailAddressAction(null, DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void guestUserIdThrowsIllegalArgumentException() {
        new SetEmailAddressAction(UserId.getGuest(), DUMMY_EMAIL);
    }

    @Test(expected = NullPointerException.class)
    public void nullEmailAddressThrowsNullPointerException() {
        new SetEmailAddressAction(DUMMY_USER_ID, null);
    }

    @Test
    public void sameUserIdAndEmailAddressReturnSameHashCode() {
        SetEmailAddressAction a1 = new SetEmailAddressAction(DUMMY_USER_ID, DUMMY_EMAIL);
        SetEmailAddressAction a2 = new SetEmailAddressAction(DUMMY_USER_ID, DUMMY_EMAIL);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    public void sameUserIdAndEmailAddressAreEqualActions() {
        SetEmailAddressAction a1 = new SetEmailAddressAction(DUMMY_USER_ID, DUMMY_EMAIL);
        SetEmailAddressAction a2 = new SetEmailAddressAction(DUMMY_USER_ID, DUMMY_EMAIL);
        assertEquals(a1, a2);
    }

    @Test
    public void suppliedUserIdIsReturnedByGetter() {
        SetEmailAddressAction a = new SetEmailAddressAction(DUMMY_USER_ID, DUMMY_EMAIL);
        assertEquals(DUMMY_USER_ID, a.getUserId());
    }

    @Test
    public void suppliedEMailAddressIsReturnedByGetter() {
        SetEmailAddressAction a = new SetEmailAddressAction(DUMMY_USER_ID, DUMMY_EMAIL);
        assertEquals(DUMMY_EMAIL, a.getEmailAddress());
    }
}

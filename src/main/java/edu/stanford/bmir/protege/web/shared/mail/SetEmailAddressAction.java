package edu.stanford.bmir.protege.web.shared.mail;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 * <p>
 *     An action that sets the email address of a user.
 * </p>
 */
public class SetEmailAddressAction implements Action<SetEmailAddressResult> {

    private UserId userId;

    private String emailAddress;

    private SetEmailAddressAction() {
    }

    /**
     * Constructs a {@link SetEmailAddressAction} object using the specified email address.
     * @param userId The {@link UserId} of the user whose email address should be set.  Not {@code null}.
     * @param emailAddress The email address to set.  Not {@code null}.
     * @throws NullPointerException if {@code userId} or {@code emailAddress} is {@code null}.
     * @throws IllegalArgumentException if the value of {@link UserId} is the user id of the guest user.
     */
    public SetEmailAddressAction(UserId userId, String emailAddress) {
        this.userId = checkNotNull(userId);
        if(userId.isGuest()) {
            throw new IllegalArgumentException("userId cannot be guest");
        }
        this.emailAddress = checkNotNull(emailAddress);
    }

    /**
     * Gets the {@link UserId} of the user whose email address should be set.
     * @return The {@link UserId}.  Not {@code null}.
     */
    public UserId getUserId() {
        return userId;
    }

    /**
     * Gets the email address to set.
     * @return A string representing the email address. Not {@code null}.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public int hashCode() {
        return "SetEmailAddressAction".hashCode() + emailAddress.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof SetEmailAddressAction)) {
            return false;
        }
        SetEmailAddressAction other = (SetEmailAddressAction) o;
        return this.emailAddress.equals(other.emailAddress);
    }
}

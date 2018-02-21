package edu.stanford.bmir.protege.web.shared.mail;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 * <p>
 *     The result from a {@link GetEmailAddressAction}.
 * </p>
 */
public class GetEmailAddressResult implements Result {

    private UserId userId;

    @Nullable
    private EmailAddress emailAddress;

    /**
     * For serialization purposes only
     */
    private GetEmailAddressResult() {
    }

    /**
     * Constructs a {@link GetEmailAddressResult} object.
     * @param userId The {@link UserId} of the user that the email address belongs to.  Not {@code null}.
     * @param emailAddress The email address of the user identified by the specified {@link UserId}.  Not {@code null}.
     * @throws NullPointerException if any parameter is {@code null}.
     */
    public GetEmailAddressResult(UserId userId, Optional<EmailAddress> emailAddress) {
        this.userId = checkNotNull(userId);
        this.emailAddress = checkNotNull(emailAddress).orElse(null);
    }

    /**
     * Gets the {@link UserId}.
     * @return The {@link UserId}. Not {@code null}.
     */
    public UserId getUserId() {
        return userId;
    }

    /**
     * Gets the {@link EmailAddress}.
     * @return The {@link EmailAddress}.  An absent value indicates that the email for the specified user id
     * does not exist.  Not {@code null}.
     */
    public Optional<EmailAddress> getEmailAddress() {
        return Optional.ofNullable(emailAddress);
    }


}

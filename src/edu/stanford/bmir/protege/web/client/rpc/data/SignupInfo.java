package edu.stanford.bmir.protege.web.client.rpc.data;

import edu.stanford.bmir.protege.web.client.ui.verification.HumanVerificationServiceProvider;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class SignupInfo implements Serializable {

    private EmailAddress emailAddress;
    
    private String password;
    
    private String userName;

    private transient HumanVerificationServiceProvider verificationServiceProvider;

    /**
     * Private constructor for serialization purposes.
     */
    private SignupInfo() {
    }

    /**
     * Creates a SignupInfo with information required for sign up and information (provided by ReCaptcha) required to
     * validate that a real human is signing up, and not a bot.
     * @param emailAddress The email address.  Must not be {@code null}.
     * @param password The password.  Must not be {@code null}.
     * @param userName The user name for the user signing up for the account. Must not be {@code null}.  Must not be
     * equal to the guest user name.  See {@link edu.stanford.bmir.protege.web.shared.user.UserId#getGuest()}
     * @param verificationServiceProvider A {@link edu.stanford.bmir.protege.web.client.ui.verification.HumanVerificationServiceProvider} which should be used to do human user
     * verification.
     * @throws NullPointerException if any parameter is {@code null}.
     *
     */
    public SignupInfo(EmailAddress emailAddress, String userName, String password, HumanVerificationServiceProvider verificationServiceProvider) {
        this.emailAddress = checkNotNull(emailAddress);
        this.userName = checkNotNull(userName);
        this.password = checkNotNull(password);
        this.verificationServiceProvider = verificationServiceProvider;
    }

    /**
     * Gets the email address.
     * @return The email address.   Not {@code null}.
     */
    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    /**
     * Gets the password.
     * @return The password.  Not {@code null}.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user name
     * @return The user name.  Not {@code null}.
     */
    public String getUserName() {
        return userName;
    }

    public HumanVerificationServiceProvider getVerificationServiceProvider() {
        return verificationServiceProvider;
    }

    @Override
    public int hashCode() {
        return emailAddress.hashCode() + password.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SignupInfo)) {
            return false;
        }
        SignupInfo other = (SignupInfo) obj;
        return emailAddress.equals(other.emailAddress) && password.equals(other.password);
    }
}

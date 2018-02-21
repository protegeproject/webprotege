package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class SignInDetails {

    private final String userName;

    private final String clearTextPassword;

    public SignInDetails(String userName, String clearTextPassword) {
        this.userName = checkNotNull(userName);
        this.clearTextPassword = checkNotNull(clearTextPassword);
    }

    public String getUserName() {
        return userName;
    }

    public String getClearTextPassword() {
        return clearTextPassword;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userName, clearTextPassword);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SignInDetails)) {
            return false;
        }
        SignInDetails other = (SignInDetails) obj;
        return this.userName.equals(other.userName) &&
                this.clearTextPassword.equals(other.clearTextPassword);
    }


    @Override
    public String toString() {
        return toStringHelper("SignInDetails")
                .add("userName", userName)
                .add("password", "***")
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class ChangePasswordResult extends AbstractAuthenticationResult {

    /**
     * For serialization only
     */
    private ChangePasswordResult() {
    }

    public ChangePasswordResult(AuthenticationResponse response) {
        super(response);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getResponse());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ChangePasswordResult)) {
            return false;
        }
        ChangePasswordResult other = (ChangePasswordResult) obj;
        return this.getResponse() == other.getResponse();
    }


    @Override
    public String toString() {
        return toStringHelper("ChangePasswordResult")
                .addValue(getResponse())
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.auth;

import com.google.common.base.Objects;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class PerformLoginResult extends AbstractAuthenticationResult {

    /**
     * For serialization purposes only
     */
    private PerformLoginResult() {
    }

    public PerformLoginResult(AuthenticationResponse result) {
        super(result);
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
        if (!(obj instanceof PerformLoginResult)) {
            return false;
        }
        PerformLoginResult other = (PerformLoginResult) obj;
        return this.getResponse() == other.getResponse();
    }


    @Override
    public String toString() {
        return toStringHelper("PerformLoginResult")
                .addValue(getResponse())
                .toString();
    }
}

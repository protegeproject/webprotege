package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.auth.AbstractAuthenticationResult;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class CreateUserAccountResult extends AbstractAuthenticationResult {

    @Override
    public int hashCode() {
        return Objects.hashCode("CreateUserAccountResult");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof CreateUserAccountResult;
    }


    @Override
    public String toString() {
        return toStringHelper("CreateUserAccountResult")
                .toString();
    }
}

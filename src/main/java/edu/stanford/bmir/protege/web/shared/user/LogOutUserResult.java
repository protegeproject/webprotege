package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class LogOutUserResult implements Result {

    @Override
    public int hashCode() {
        return Objects.hashCode("LogOutUserResult");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof LogOutUserResult;
    }


    @Override
    public String toString() {
        return toStringHelper("LogOutUserResult")
                .toString();
    }
}

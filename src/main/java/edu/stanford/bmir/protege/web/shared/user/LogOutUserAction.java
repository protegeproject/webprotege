package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class LogOutUserAction implements Action<LogOutUserResult> {

    @Override
    public int hashCode() {
        return Objects.hashCode("LogOutUserAction");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof LogOutUserAction;
    }


    @Override
    public String toString() {
        return toStringHelper("LogOutUserAction")
                .toString();
    }
}

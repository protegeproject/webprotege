package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
public class GetUserIdsAction implements Action<GetUserIdsResult> {

    public GetUserIdsAction() {
    }

    @Override
    public int hashCode() {
        return "GetUserIdsAction".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof GetUserIdsAction;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("GetUserIdsAction")
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.auth;


import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class GetChapSessionResult implements Result {

    private ChapSession chapSession;

    private GetChapSessionResult() {
    }

    public GetChapSessionResult(ChapSession chapSession) {
        this.chapSession = checkNotNull(chapSession);
    }

    public ChapSession getChapSession() {
        return chapSession;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(chapSession);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetChapSessionResult)) {
            return false;
        }
        GetChapSessionResult other = (GetChapSessionResult) obj;
        return this.chapSession.equals(other.chapSession);
    }


    @Override
    public String toString() {
        return toStringHelper("GetChapSessionResult")
                .addValue(chapSession)
                .toString();
    }
}

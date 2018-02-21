package edu.stanford.bmir.protege.web.shared.auth;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class GetChapSessionResult implements Result {

    @Nullable
    private ChapSession chapSession;

    @GwtSerializationConstructor
    private GetChapSessionResult() {
    }

    public GetChapSessionResult(Optional<ChapSession> chapSession) {
        this.chapSession = checkNotNull(chapSession).orElse(null);
    }

    public Optional<ChapSession> getChapSession() {
        return Optional.ofNullable(chapSession);
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
        return Objects.equal(this.chapSession, other.chapSession);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetChapSessionResult")
                .addValue(chapSession)
                .toString();
    }
}

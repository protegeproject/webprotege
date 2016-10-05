package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class Comment implements IsSerializable {

    private UserId createdBy;

    private long createdAt;

    @Nullable
    private Long updatedAt;

    private String body;

    public Comment(@Nonnull UserId createdBy,
                   long createdAt,
                   @Nonnull Optional<Long> updatedAt,
                   @Nonnull String body) {
        this(createdBy,
                createdAt,
                checkNotNull(updatedAt).orElse(null),
                body);
    }

    private Comment(@Nonnull UserId createdBy,
                      long createdAt,
                      @Nullable Long updatedAt,
                      @Nonnull String body) {
        this.createdBy = checkNotNull(createdBy);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.body = checkNotNull(body);
    }

    @GwtSerializationConstructor
    private Comment() {
    }


    public UserId getCreatedBy() {
        return createdBy;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Optional<Long> getUpdatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) obj;
        return this.createdBy.equals(other.createdBy)
                && this.createdAt == other.createdAt
                && Objects.equal(this.updatedAt, other.updatedAt)
                && this.body.equals(other.body);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(createdAt, createdBy, updatedAt, body);
    }

    @Override
    public String toString() {
        return toStringHelper("Comment")
                .add("createdBy", createdBy)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("body", body)
                .toString();
    }
}

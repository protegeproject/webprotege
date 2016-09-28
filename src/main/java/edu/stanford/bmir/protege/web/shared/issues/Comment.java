package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
@TypeAlias("Comment")
public class Comment implements IsSerializable {

    @Nonnull
    private UserId createdBy;

    private long createdAt;

    @Nullable
    private Long updatedAt;

    @Nonnull
    private String body;

    @Nonnull
    private Set<Mention> mentions;

    public Comment(@Nonnull UserId createdBy,
                   long createdAt,
                   @Nonnull Optional<Long> updatedAt,
                   @Nonnull String body,
                   @Nonnull ImmutableSet<Mention> mentions) {
        this(createdBy,
                createdAt,
                checkNotNull(updatedAt).orElse(null),
                body,
                mentions);
    }

    @PersistenceConstructor
    private Comment(@Nonnull UserId createdBy,
                      long createdAt,
                      @Nullable Long updatedAt,
                      @Nonnull String body,
                      @Nonnull Set<Mention> mentions) {
        this.createdBy = checkNotNull(createdBy);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.body = checkNotNull(body);
        this.mentions = ImmutableSet.copyOf(checkNotNull(mentions));
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

    public ImmutableSet<Mention> getMentions() {
        return ImmutableSet.copyOf(mentions);
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
                && this.body.equals(other.body)
                && this.mentions.equals(other.mentions);
    }
}

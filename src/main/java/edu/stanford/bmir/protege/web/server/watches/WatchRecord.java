package edu.stanford.bmir.protege.web.server.watches;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
@Entity(noClassnameStored = true)
@Indexes({
                 @Index(fields = {@Field("userId"), @Field(value = "entity")},
                        options = @IndexOptions(unique = true))
         }
)
public class WatchRecord {

    @Id
    @Nullable
    @SuppressWarnings("unused")
    private ObjectId id;

    @Nonnull
    private final UserId userId;

    @Nonnull
    private final OWLEntity entity;

    @Nonnull
    private final WatchType type;


    public WatchRecord(@Nonnull UserId userId,
                       @Nonnull OWLEntity entity,
                       @Nonnull WatchType type) {
        this.userId = checkNotNull(userId);
        this.entity = checkNotNull(entity);
        this.type = checkNotNull(type);
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public WatchType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity, type, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WatchRecord)) {
            return false;
        }
        WatchRecord other = (WatchRecord) obj;
        return this.userId.equals(other.userId)
                && this.entity.equals(other.entity)
                && this.type.equals(other.type);
    }


    @Override
    public String toString() {
        return toStringHelper("WatchRecord")
                .addValue(userId)
                .addValue(entity)
                .addValue(type)
                .toString();
    }
}

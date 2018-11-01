package edu.stanford.bmir.protege.web.server.watches;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.WatchType;
import org.mongodb.morphia.annotations.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.watches.WatchRecord.PROJECT_ID;
import static edu.stanford.bmir.protege.web.server.watches.WatchRecord.USER_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Apr 2017
 */
@Entity(value = "Watches", noClassnameStored = true)
@Indexes({
                 @Index(fields = {
                         @Field(PROJECT_ID),
                         @Field(USER_ID),
                         @Field(value = WatchRecord.ENTITY)},
                        options = @IndexOptions(unique = true))
         }
)
public class WatchRecord {

    public static final String PROJECT_ID = "projectId";

    public static final String USER_ID = "userId";

    public static final String ENTITY = "entity";

    public static final String TYPE = "type";

    @Property(PROJECT_ID)
    private ProjectId projectId;

    @Property(USER_ID)
    private UserId userId;

    @Property(ENTITY)
    private OWLEntity entity;

    @Property(TYPE)
    private WatchType type;

    private WatchRecord() {
    }

    public WatchRecord(@Nonnull ProjectId projectId,
                       @Nonnull UserId userId,
                       @Nonnull OWLEntity entity, @Nonnull WatchType type) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.entity = checkNotNull(entity);
        this.type = checkNotNull(type);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
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
        return Objects.hashCode(
                projectId,
                userId,
                entity,
                type
        );
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
        return this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId)
                && this.entity.equals(other.entity)
                && this.type == other.type;
    }


    @Override
    public String toString() {
        return toStringHelper("WatchRecord")
                .addValue(projectId)
                .addValue(userId)
                .addValue(entity)
                .addValue(type)
                .toString();
    }
}

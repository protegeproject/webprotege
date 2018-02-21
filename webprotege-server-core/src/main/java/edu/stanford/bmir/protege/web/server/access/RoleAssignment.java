package edu.stanford.bmir.protege.web.server.access;


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.access.RoleAssignment.PROJECT_ID;
import static edu.stanford.bmir.protege.web.server.access.RoleAssignment.USER_NAME;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jan 2017
 *
 * A persistence structure for role assignments.  This assumes the persistence is provided by
 * MongoDb, which is access via Morphia.
 */
@Entity(noClassnameStored = true, value = "RoleAssignments")
@Indexes(
        @Index(fields = {
                @Field(USER_NAME),
                @Field(PROJECT_ID)},
                options = @IndexOptions(unique = true))
)
public class RoleAssignment {

    public static final String USER_NAME = "userName";

    public static final String PROJECT_ID = "projectId";

    public static final String ACTION_CLOSURE = "actionClosure";

    public static final String ROLE_CLOSURE = "roleClosure";

    @Id
    @Nullable
    @SuppressWarnings("unused")
    private ObjectId id;

    @Nullable
    private String userName;

    @Nullable
    private String projectId;

    private List<String> assignedRoles = ImmutableList.of();

    private List<String> roleClosure = ImmutableList.of();

    private List<String> actionClosure = ImmutableList.of();


    private RoleAssignment() {
    }

    public RoleAssignment(@Nullable String userName,
                          @Nullable String projectId,
                          @Nonnull List<String> assignedRoles,
                          @Nonnull List<String> roleClosure,
                          @Nonnull List<String> actionClosure) {
        this.userName = userName;
        this.projectId = projectId;
        this.assignedRoles = ImmutableList.copyOf(checkNotNull(assignedRoles));
        this.roleClosure = ImmutableList.copyOf(checkNotNull(roleClosure));
        this.actionClosure = ImmutableList.copyOf(checkNotNull(actionClosure));
    }

    @Nonnull
    public Optional<String> getProjectId() {
        return Optional.ofNullable(projectId);
    }

    @Nonnull
    public Optional<String> getUserName() {
        return Optional.ofNullable(userName);
    }

    @Nonnull
    public List<String> getAssignedRoles() {
        return ImmutableList.copyOf(assignedRoles);
    }

    @Nonnull
    public List<String> getRoleClosure() {
        return ImmutableList.copyOf(roleClosure);
    }

    @Nonnull
    public List<String> getActionClosure() {
        return ImmutableList.copyOf(actionClosure);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userName, projectId, assignedRoles, roleClosure, actionClosure);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RoleAssignment)) {
            return false;
        }
        RoleAssignment other = (RoleAssignment) obj;
        return Objects.equal(userName, other.userName)
                && Objects.equal(projectId, other.projectId)
                && this.assignedRoles.equals(other.assignedRoles)
                && this.roleClosure.equals(other.roleClosure)
                && this.actionClosure.equals(other.actionClosure);
    }

    @Override
    public String toString() {
        return toStringHelper("RoleAssignment")
                .add("userName", userName)
                .add("projectId", projectId)
                .add("assignedRoles", assignedRoles)
                .add("roleClosure", roleClosure)
                .add("actionClosure", actionClosure)
                .toString();
    }
}

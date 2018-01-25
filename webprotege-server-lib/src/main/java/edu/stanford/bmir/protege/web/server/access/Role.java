package edu.stanford.bmir.protege.web.server.access;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 *
 * A role associates a role Id with a set of actions that can be performed.  Hierarchical roles are supported.
 */
@Immutable
public final class Role {

    @Nonnull
    private final RoleId roleId;

    @Nonnull
    private final List<RoleId> parents;

    @Nonnull
    private final List<ActionId> actions;


    public Role(@Nonnull RoleId roleId,
                @Nonnull List<RoleId> parents,
                @Nonnull List<ActionId> actions) {
        this.roleId = checkNotNull(roleId);
        this.parents = ImmutableList.copyOf(checkNotNull(parents));
        this.actions = ImmutableList.copyOf(checkNotNull(actions));
    }

    @Nonnull
    public RoleId getRoleId() {
        return roleId;
    }

    @Nonnull
    public List<RoleId> getParents() {
        return parents;
    }

    @Nonnull
    public List<ActionId> getActions() {
        return actions;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roleId, parents, actions);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Role)) {
            return false;
        }
        Role other = (Role) obj;
        return this.roleId.equals(other.roleId)
                && this.parents.equals(other.parents)
                && this.actions.equals(other.actions);
    }


    @Override
    public String toString() {
        return toStringHelper("Role")
                .addValue(roleId)
                .add("parents", parents)
                .add("actions", actions)
                .toString();
    }
}

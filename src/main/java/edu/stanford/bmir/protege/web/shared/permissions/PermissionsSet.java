package edu.stanford.bmir.protege.web.shared.permissions;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 * <p>
 *     A {@link PermissionsSet} represents an immutable set of {@link Permission} objects.
 * </p>
 */
public final class PermissionsSet implements Serializable, Iterable<Permission> {

    private static final PermissionsSet EMPTY_SET = new PermissionsSet(Collections.<Permission>emptySet());

    private Set<Permission> permissions;

    /**
     * For serialization purposes only
     */
    private PermissionsSet() {
    }

    private PermissionsSet(Set<Permission> permissions) {
        this.permissions = new HashSet<Permission>(permissions);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    ////  Factory methods
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the empty set of permissions.
     * @return A {@link PermissionsSet} that contains no {@link Permission} objects.
     */
    public static PermissionsSet emptySet() {
        return EMPTY_SET;
    }

    /**
     * Gets a {@link PermissionsSet} that contains the specified permissions.
     * @param permissions The set of {@link Permission}s to be contained in the result.  Not {@code null}.
     * @return A {@link PermissionsSet} that contains the specified set of permissions.  Note that modification of
     * {@code permissions} will not alter the returned {@link PermissionsSet}.
     * @throws NullPointerException if {@code permissions} is {@code null}.
     */
    public PermissionsSet get(Set<Permission> permissions) {
        return new PermissionsSet(checkNotNull(permissions));
    }

    /**
     * Gets the {@link Permission} objects contained within this set.
     * @return The set of {@link Permission}s contained within this set.  Not {@code null}.  Note that modification of
     * the returned set will not alter the permissions contained within this set.
     */
    public Set<Permission> getPermissions() {
        return new HashSet<Permission>(permissions);
    }

    /**
     * Returns an iterator over the set of permissions in this {@link PermissionsSet}.
     * @return an Iterator.  Not {@code null}.
     */
    @Override
    public Iterator<Permission> iterator() {
        return Collections.unmodifiableSet(permissions).iterator();
    }

    /**
     * Determines whether or not this {@link PermissionsSet} contains the specified {@link Permission}.
     * @param permission The permission to test for.  Not {@code null}.
     * @return {@code true} if this {@link PermissionsSet} contains the specified {@link Permission} otherwise {@code false}.
     * @throws  NullPointerException if {@code permission} is {@code null}.
     */
    public boolean contains(Permission permission) {
        return permissions.contains(checkNotNull(permission));
    }

    /**
     * Determines if this set contains a {@link Permission} that is equal to the read permission.
     * @return {@code true} if this set contains a {@link Permission} that is equal to the read permission
     * (i.e. a {@link Permission} with a name equal to {@link PermissionName#READ}) otherwise {@code false}.
     */
    public boolean containsReadPermission() {
        return permissions.contains(Permission.getReadPermission());
    }

    /**
     * Determines if this set contains a {@link Permission} that is equal to the read permission.
     * @return {@code true} if this set contains a {@link Permission} that is equal to the comment permission
     * (i.e. a {@link Permission} with a name equal to {@link PermissionName#COMMENT}) otherwise {@code false}.
     */
    public boolean containsCommentPermission() {
        return permissions.contains(Permission.getCommentPermission());
    }

    /**
     * Determines if this set contains a {@link Permission} that is equal to the read permission.
     * @return {@code true} if this set contains a {@link Permission} that is equal to the write permission
     * (i.e. a {@link Permission} with a name equal to {@link PermissionName#WRITE}) otherwise {@code false}.
     */
    public boolean containsWritePermission() {
        return permissions.contains(Permission.getWritePermission());
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets a {@link Builder} that is initialized with the {@link Permission}s contained within this {@link PermissionsSet}.
     * @return A  {@link Builder}.  Not {@code null}.
     */
    public Builder getBuilderFromThis() {
        return new Builder(permissions);
    }

    /**
     * Gets a {@link Builder} that can be used to build a fresh {@link PermissionsSet}.
     * @return A  {@link Builder}.  Not {@code null}.
     */
    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private Set<Permission> permissions = new HashSet<Permission>();

        public Builder() {
        }

        public Builder(Set<Permission> permissions) {
            this.permissions = permissions;
        }

        public Builder addPermission(Permission permission) {
            permissions.add(permission);
            return this;
        }

        public Builder removePermission(Permission permission) {
            permissions.remove(permission);
            return this;
        }


        public Builder removeAllPermissions() {
            permissions.clear();
            return this;
        }

        public PermissionsSet build() {
            return new PermissionsSet(permissions);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    @Override
    public int hashCode() {
        return "PermissionsSet".hashCode() + permissions.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof PermissionsSet)) {
            return false;
        }
        PermissionsSet other = (PermissionsSet) obj;
        return this.permissions.equals(other.permissions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PermissionsSet");
        sb.append("(");
        for(Iterator<Permission> it = permissions.iterator(); it.hasNext();) {
            Permission permission = it.next();
            sb.append(permission);
            if(it.hasNext()) {
                sb.append(" ");
            }
        }
        sb.append(")");
        return sb.toString();
    }



}

package edu.stanford.bmir.protege.web.server.sharing;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.permissions.Permission.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class Permissions {

    public  static Optional<SharingPermission> toSharingPermission(Collection<Permission> permissions) {
        if(permissions.contains(getAdminPermission())) {
            return Optional.of(SharingPermission.MANAGE);
        }
        else if(permissions.contains(getWritePermission())) {
            return Optional.of(SharingPermission.EDIT);
        }
        else if(permissions.contains(getCommentPermission())) {
            return Optional.of(SharingPermission.COMMENT);
        }
        else if(permissions.contains(getReadPermission())) {
            return Optional.of(SharingPermission.VIEW);
        }
        else {
            return Optional.empty();
        }
    }

    public static ImmutableSet<Permission> fromSharingPermission(SharingPermission sharingPermission) {
        switch (sharingPermission) {
            case MANAGE:
                return ImmutableSet.of(getAdminPermission(), getWritePermission(), getCommentPermission(), getReadPermission());
            case EDIT:
                return ImmutableSet.of(getWritePermission(), getCommentPermission(), getReadPermission());
            case COMMENT:
                return ImmutableSet.of(getWritePermission(), getCommentPermission(), getReadPermission());
            case VIEW:
                return ImmutableSet.of(getWritePermission(), getCommentPermission(), getReadPermission());
            default:
                return ImmutableSet.of();
        }
    }
}

package edu.stanford.bmir.protege.web.server.sharing;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class PermissionMapper {

    public  static Optional<SharingPermission> mapToSharingPermission(Collection<Permission> permissions) {
        if(permissions.contains(Permission.getWritePermission())) {
            return Optional.of(SharingPermission.EDIT);
        }
        else if(permissions.contains(Permission.getCommentPermission())) {
            return Optional.of(SharingPermission.COMMENT);
        }
        else if(permissions.contains(Permission.getReadPermission())) {
            return Optional.of(SharingPermission.VIEW);
        }
        else {
            return Optional.absent();
        }
    }

    public static Set<Permission> mapToPermissions(SharingPermission sharingPermission) {
        switch (sharingPermission) {
            case EDIT:
                return Sets.newHashSet(Permission.getWritePermission(), Permission.getCommentPermission(), Permission.getReadPermission());
            case COMMENT:
                return Sets.newHashSet(Permission.getWritePermission(), Permission.getCommentPermission(), Permission.getReadPermission());
            case VIEW:
                return Sets.newHashSet(Permission.getWritePermission(), Permission.getCommentPermission(), Permission.getReadPermission());
            default:
                return Collections.emptySet();
        }
    }
}

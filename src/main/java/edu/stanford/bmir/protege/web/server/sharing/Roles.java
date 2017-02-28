package edu.stanford.bmir.protege.web.server.sharing;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;

import java.util.Collection;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInRole.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class Roles {

    public  static Optional<SharingPermission> toSharingPermission(Collection<RoleId> roles) {
        if(roles.contains(CAN_MANAGE.getRoleId())) {
            return Optional.of(SharingPermission.MANAGE);
        }
        else if(roles.contains(CAN_EDIT.getRoleId())) {
            return Optional.of(SharingPermission.EDIT);
        }
        else if(roles.contains(CAN_COMMENT.getRoleId())) {
            return Optional.of(SharingPermission.COMMENT);
        }
        else if(roles.contains(CAN_VIEW.getRoleId())) {
            return Optional.of(SharingPermission.VIEW);
        }
        else {
            return Optional.empty();
        }
    }

    public static ImmutableSet<RoleId> fromSharingPermission(SharingPermission sharingPermission) {
        switch (sharingPermission) {
            case MANAGE:
                return ImmutableSet.of(CAN_MANAGE.getRoleId());
            case EDIT:
                return ImmutableSet.of(CAN_EDIT.getRoleId());
            case COMMENT:
                return ImmutableSet.of(CAN_COMMENT.getRoleId());
            case VIEW:
                return ImmutableSet.of(CAN_VIEW.getRoleId());
            default:
                return ImmutableSet.of();
        }
    }
}

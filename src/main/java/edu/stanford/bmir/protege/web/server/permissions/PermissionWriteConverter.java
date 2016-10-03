package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.persistence.Converter;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public class PermissionWriteConverter implements Converter<Permission, String> {

    @Override
    public String convert(Permission permission) {
        return permission.getPermissionName();
    }
}

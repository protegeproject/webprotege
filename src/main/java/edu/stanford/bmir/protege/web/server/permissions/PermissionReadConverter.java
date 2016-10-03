package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.persistence.Converter;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
public class PermissionReadConverter implements Converter<String, Permission> {

    @Override
    public Permission convert(String s) {
        return Permission.getPermission(s);
    }
}

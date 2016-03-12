package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
@ReadingConverter
public class PermissionReadConverter implements Converter<String, Permission> {

    @Override
    public Permission convert(String s) {
        return Permission.getPermission(s);
    }
}

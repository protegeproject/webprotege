package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/03/16
 */
@WritingConverter
public class PermissionWriteConverter implements Converter<Permission, String> {

    @Override
    public String convert(Permission permission) {
        return permission.getPermissionName();
    }
}

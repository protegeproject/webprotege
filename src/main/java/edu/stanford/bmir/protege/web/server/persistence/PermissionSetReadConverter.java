package edu.stanford.bmir.protege.web.server.persistence;

import com.google.common.collect.ImmutableSet;
import com.mongodb.BasicDBList;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/03/16
 */
public class PermissionSetReadConverter implements Converter<BasicDBList, ImmutableSet<Permission>> {

    @Override
    public ImmutableSet<Permission> convert(BasicDBList objects) {
        ImmutableSet.Builder<Permission> builder = ImmutableSet.builder();
        for(Object o : objects) {
            builder.add(Permission.getPermission(o.toString()));
        }
        return builder.build();
    }
}

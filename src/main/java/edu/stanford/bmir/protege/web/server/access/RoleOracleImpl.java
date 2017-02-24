package edu.stanford.bmir.protege.web.server.access;

import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.access.BuiltInRole;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 */
public class RoleOracleImpl implements RoleOracle {

    private Map<RoleId, Role> closure = new LinkedHashMap<>();

    private RoleOracleImpl() {

    }

    public static RoleOracleImpl get() {
        RoleOracleImpl impl = new RoleOracleImpl();
        for(BuiltInRole builtInRole : BuiltInRole.values()) {
            List<RoleId> parentRoles = builtInRole.getParents().stream()
                                                  .map(BuiltInRole::getRoleId)
                                                  .collect(toList());
            List<ActionId> actions = builtInRole.getActions().stream()
                                                .map(BuiltInAction::getActionId)
                                                .collect(toList());
            impl.addRole(new Role(builtInRole.getRoleId(), parentRoles, actions));
        }
        return impl;
    }

    @Nonnull
    @Override
    public Collection<Role> getRoleClosure(@Nonnull RoleId roleId) {
        Set<Role> result = new HashSet<>();
        add(roleId, result);
        return result;
    }

    private void add(RoleId roleId, Set<Role> result) {
        Role role = closure.get(roleId);
        if(role == null) {
            return;
        }
        if(result.add(role)) {
            role.getParents().forEach(pr -> add(pr, result));
        }
    }

    private void addRole(Role role) {
        closure.put(role.getRoleId(), role);
    }
}

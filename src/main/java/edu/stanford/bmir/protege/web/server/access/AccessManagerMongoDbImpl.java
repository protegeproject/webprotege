package edu.stanford.bmir.protege.web.server.access;

import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import static edu.stanford.bmir.protege.web.server.access.RoleAssignment.*;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jan 2017
 */
public class AccessManagerMongoDbImpl implements AccessManager {

    private final RoleOracle roleOracle;

    private final Datastore datastore;

    /**
     * Constructs an {@link AccessManager} that is backed by MongoDb.
     * @param roleOracle An oracle for looking up information about roles.
     * @param datastore A Morphia datastore that is used to access MongoDb.
     */
    public AccessManagerMongoDbImpl(RoleOracle roleOracle, Datastore datastore) {
        this.roleOracle = roleOracle;
        this.datastore = datastore;
    }

    @Override
    public void setAssignedRoles(@Nonnull Subject subject,
                                 @Nonnull Resource resource,
                                 @Nonnull Collection<RoleId> roleIds) {
        String userName = toUserName(subject);
        String projectId = toProjectId(resource);
        List<String> assignedRoles = roleIds.stream().map(RoleId::getId).collect(toList());
        List<String> roleClosure = roleIds.stream()
                                          .flatMap(id -> roleOracle.getRoleClosure(id).stream())
                                          .map(r -> r.getRoleId().getId())
                                          .collect(toList());
        List<String> actionClosure = roleIds.stream()
                                            .flatMap(id -> roleOracle.getRoleClosure(id).stream())
                                            .flatMap(r -> r.getActions().stream())
                                            .map(ActionId::getId)
                                            .collect(toList());
        RoleAssignment assignment = new RoleAssignment(userName,
                                                       projectId,
                                                       assignedRoles,
                                                       roleClosure,
                                                       actionClosure);
        datastore.delete(withUserAndTarget(subject, resource));
        datastore.save(assignment);
    }

    private Query<RoleAssignment> withUserAndTarget(Subject subject, Resource resource) {
        String userName = toUserName(subject);
        String projectId = toProjectId(resource);
        return datastore.createQuery(RoleAssignment.class)
                        .field(USER_NAME)
                        .equal(userName)
                        .field(PROJECT_ID)
                        .equal(projectId);
    }

    @Nonnull
    @Override
    public Collection<RoleId> getAssignedRoles(@Nonnull Subject subject, @Nonnull Resource resource) {
        Query<RoleAssignment> query = withUserAndTarget(subject, resource);
        return query.asList()
                    .stream()
                    .flatMap(ra -> ra.getAssignedRoles().stream())
                    .map(RoleId::new)
                    .distinct()
                    .collect(toList());
    }

    private Query<RoleAssignment> withUserOrAnyUserAndTarget(Subject subject, Resource resource) {
        String userName = toUserName(subject);
        String projectId = toProjectId(resource);

        Query<RoleAssignment> query = datastore.createQuery(RoleAssignment.class)
                                               .field(PROJECT_ID).equal(projectId);
        if (!subject.isGuest()) {
            query.or(
                    query.criteria(USER_NAME).equal(userName),
                    query.criteria(USER_NAME).equal(null)
            );
        }
        else {
            query.field(USER_NAME).equal(userName);
        }
        return query;
    }

    @Nonnull
    @Override
    public Collection<RoleId> getRoleClosure(@Nonnull Subject subject, @Nonnull Resource resource) {
        Query<RoleAssignment> query = withUserOrAnyUserAndTarget(subject,
                                                                 resource);
        return query.asList()
                    .stream()
                    .flatMap(ra -> ra.getRoleClosure().stream())
                    .distinct()
                    .map(RoleId::new)
                    .collect(toList());
    }

    @Nonnull
    @Override
    public Collection<ActionId> getActionClosure(@Nonnull Subject subject, @Nonnull Resource resource) {
        Query<RoleAssignment> query = withUserOrAnyUserAndTarget(subject,
                                                                 resource);
        return query.asList()
                    .stream()
                    .flatMap(ra -> ra.getActionClosure().stream())
                    .map(ActionId::new)
                    .collect(toList());
    }

    @Override
    public boolean hasPermission(@Nonnull Subject subject, @Nonnull Resource resource, @Nonnull ActionId actionId) {
        Query<RoleAssignment> query = withUserOrAnyUserAndTarget(subject, resource)
                .field(ACTION_CLOSURE).equal(actionId.getId());
        return query.count() > 0;
    }


    /**
     * Converts the specified subject to a user name or a null value if the specified subject does not
     * represent a user.
     * @param subject The subject.
     * @return The user name for the subject.
     */
    @Nullable
    private static String toUserName(@Nonnull Subject subject) {
        return subject.getUserName().orElse(null);
    }

    @Nullable
    private static String toProjectId(Resource resource) {
        return resource.getProjectId().map(ProjectId::getId).orElse(null);
    }

}

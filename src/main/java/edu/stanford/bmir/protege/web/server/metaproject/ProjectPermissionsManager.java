package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.shared.permissions.GroupId;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.Operation;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectPermissionsManager {

    boolean isUserAdmin(UserId userId);

    Set<GroupId> getUserGroups(UserId userId);

    List<ProjectDetails> getListableReadableProjects(UserId userId);

    Collection<Operation> getAllowedOperations(String project, String userName);

    PermissionsSet getPermissionsSet(ProjectId projectId, UserId userId);
}

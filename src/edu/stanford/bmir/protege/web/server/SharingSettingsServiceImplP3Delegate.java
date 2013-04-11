package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain.Invitation;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.smi.protege.server.metaproject.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 */
public class SharingSettingsServiceImplP3Delegate {

    public static final String WORLD_GROUP_NAME = "World";

    public static enum OperationName {
        
        READ(MetaProjectConstants.OPERATION_READ.getName()),
        
        COMMENT("Comment"),
        
        WRITE(MetaProjectConstants.OPERATION_WRITE.getName()),
        
        DISPLAY_IN_PROJECT_List(MetaProjectConstants.OPERATION_DISPLAY_IN_PROJECT_LIST.getName());

        private String name;
        
        private OperationName(String name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }
    }
 

    public static final String READERS_GROUP_NAME_SUFFIX = "_Readers";

    public static final String COMMENTERS_GROUP_NAME_SUFFIX = "_Commenters";

    public static final String WRITERS_GROUP_NAME_SUFFIX = "_Writers";

    public static final String NONE_GROUP_NAME_SUFFIX = "_None";

    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        MetaProjectManager mpm = MetaProjectManager.getManager();
        MetaProject metaProject = mpm.getMetaProject();
        ProjectInstance projectInstance = metaProject.getProject(projectId.getId());
        Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
        List<UserSharingSetting> result = new ArrayList<UserSharingSetting>();
        Set<User> usersWithPermissionsOnProject = new HashSet<User>();
        SharingSetting defaultSharingSetting = SharingSetting.getDefaultSharingSetting();
        for (GroupOperation groupOperation : groupOperations) {
            if (!isWorld(groupOperation.getAllowedGroup())) {
                usersWithPermissionsOnProject.addAll(groupOperation.getAllowedGroup().getMembers());
            }
            else {
                defaultSharingSetting = getSharingSettingFromOperations(groupOperation.getAllowedOperations());
            }
        }
        for (User user : usersWithPermissionsOnProject) {
            Collection<Operation> operations = mpm.getAllowedOperations(projectId.getId(), user.getName());
            SharingSetting sharingSetting = getSharingSettingFromOperations(operations);
            UserSharingSetting userSharingSetting = new UserSharingSetting(UserId.getUserId(user.getName()), sharingSetting);
            result.add(userSharingSetting);
        }
        Collections.sort(result);

        return new ProjectSharingSettings(projectId, defaultSharingSetting, result);
    }

    private SharingSetting getSharingSettingFromOperations(Collection<Operation> operations) {
        SharingSetting sharingSetting;
        if (isWriteable(operations)) {
            sharingSetting = SharingSetting.EDIT;
        }
        else if (isCommentable(operations)) {
            sharingSetting = SharingSetting.COMMENT;
        }
        else if (isReadable(operations)) {
            sharingSetting = SharingSetting.VIEW;
        }
        else {
            sharingSetting = SharingSetting.NONE;
        }
        return sharingSetting;
    }

    private boolean isWorld(Group group) {
        return group.getName().equals("World");
    }


    private boolean isReadable(Collection<Operation> operations) {
        for (Operation operation : operations) {
            if (operation.getName().equals(OperationName.READ.getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isCommentable(Collection<Operation> operations) {
        for (Operation operation : operations) {
            if (operation.getName().equals(OperationName.COMMENT.getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isWriteable(Collection<Operation> operations) {
        for (Operation operation : operations) {
            if (operation.getName().equals(OperationName.WRITE.getName())) {
                return true;
            }
        }
        return false;
    }

    public void updateSharingSettings(HttpServletRequest request, ProjectSharingSettings projectSharingSettings) {

        MetaProjectManager mpm = MetaProjectManager.getManager();
        MetaProject metaProject = mpm.getMetaProject();
        ProjectId projectId = projectSharingSettings.getProjectId();
        ProjectInstance projectInstance = metaProject.getProject(projectId.getId());

        // TODO: Check we are allowed to manage projects permissions

        Map<SharingSetting, Set<User>> usersBySharingSetting = createUsersBySharingSettingMap(request, projectSharingSettings, metaProject);

        Set<GroupOperation> allowedGroupOperations = createAllowedGroupOperationsFromSharingSettings(metaProject, projectId, usersBySharingSetting);

        getWorldAllowedOperations(projectSharingSettings, metaProject, allowedGroupOperations);

        projectInstance.setAllowedGroupOperations(allowedGroupOperations);

        OWLAPIMetaProjectStore.getStore().saveMetaProject(mpm);

    }

    private Set<GroupOperation> createAllowedGroupOperationsFromSharingSettings(MetaProject metaProject, ProjectId projectId, Map<SharingSetting, Set<User>> usersBySharingSetting) {
        Set<GroupOperation> allowedGroupOperations = new HashSet<GroupOperation>();
        for(SharingSetting sharingSetting : SharingSetting.values()) {
            Group sharingSettingGroup = getOrCreateGroup(metaProject, projectId, sharingSetting);
            Set<User> sharingSettingUsers = usersBySharingSetting.get(sharingSetting);
            sharingSettingGroup.setMembers(sharingSettingUsers);
            GroupOperation groupOperation = metaProject.createGroupOperation();
            Set<Operation> sharingSettingOperatations = getOperationsForSharingSetting(metaProject, sharingSetting);
            groupOperation.setAllowedOperations(sharingSettingOperatations);
            groupOperation.setAllowedGroup(sharingSettingGroup);
            allowedGroupOperations.add(groupOperation);
        }
        return allowedGroupOperations;
    }

    private Map<SharingSetting, Set<User>> createUsersBySharingSettingMap(HttpServletRequest request, ProjectSharingSettings projectSharingSettings, MetaProject metaProject) {
        Map<SharingSetting, Set<User>> usersBySharingSetting = createSharingSettingMap();

        for (UserSharingSetting userSharingSetting : projectSharingSettings.getSharingSettings()) {
            UserId userId = userSharingSetting.getUserId();
            if (!userId.isGuest()) {
                User user = getUserFromUserId(metaProject, userId);
                if (user != null) {
                    usersBySharingSetting.get(userSharingSetting.getSharingSetting()).add(user);
                }
                else {
                    if(userId.getUserName().contains("@")) {
                        // Assume it's an email invitation
                        sendEmailInvitation(request, projectSharingSettings, userSharingSetting);
                        User freshUser = getUserFromUserId(metaProject, userId);
                        usersBySharingSetting.get(userSharingSetting.getSharingSetting()).add(freshUser);
                    }
                }
            }
        }
        return usersBySharingSetting;
    }

    private void sendEmailInvitation(HttpServletRequest request, ProjectSharingSettings projectSharingSettings, UserSharingSetting userSharingSetting) {
        UserId userId = userSharingSetting.getUserId();
        // Email invitation
        List<Invitation> invitations = new ArrayList<Invitation>();
        final Invitation invitation = new Invitation();
        invitation.setEmailId(userId.getUserName());
        invitation.setWriter(userSharingSetting.getSharingSetting() == SharingSetting.EDIT);
        invitations.add(invitation);
        String baseURL = request.getHeader("referer");

        AccessPolicyManager.get().createTemporaryAccountForInvitation(projectSharingSettings.getProjectId(), baseURL, invitations);
    }

    private void getWorldAllowedOperations(ProjectSharingSettings projectSharingSettings, MetaProject metaProject, Set<GroupOperation> allowedGroupOperations) {
        SharingSetting defaultSharingSetting = projectSharingSettings.getDefaultSharingSetting();
        Group worldGroup = getOrCreateGroup(metaProject, WORLD_GROUP_NAME);
        GroupOperation worldGroupOperation = metaProject.createGroupOperation();
        worldGroupOperation.setAllowedGroup(worldGroup);
        worldGroupOperation.setAllowedOperations(getOperationsForSharingSetting(metaProject, defaultSharingSetting));
        allowedGroupOperations.add(worldGroupOperation);
    }

    private Map<SharingSetting, Set<User>> createSharingSettingMap() {
        Map<SharingSetting, Set<User>> usersBySharingSetting = new HashMap<SharingSetting, Set<User>>();
        for(SharingSetting sharingSetting : SharingSetting.values()) {
            usersBySharingSetting.put(sharingSetting, new HashSet<User>());
        }
        return usersBySharingSetting;
    }

    private User getUserFromUserId(MetaProject metaProject, UserId userId) {
        return metaProject.getUser(userId.getUserName());
    }

    private Group getOrCreateGroup(MetaProject metaProject, ProjectId projectId, SharingSetting sharingSetting) {
        String groupName = getGroupName(projectId, sharingSetting);
        return getOrCreateGroup(metaProject, groupName);
    }
    
    private Group getOrCreateGroup(MetaProject metaProject, String groupName) {
        Group group = metaProject.getGroup(groupName);
        if (group == null) {
            group = metaProject.createGroup(groupName);
        }
        return group;
    }
    
    private String getGroupName(ProjectId projectId, SharingSetting sharingSetting) {
        return projectId.getId() + getGroupNameSuffix(sharingSetting);
    }

    /**
     * Gets the group name suffix for a given sharing setting.
     * @param sharingSetting The sharing setting
     * @return A string representing the group name suffix for a given sharing setting
     */
    private String getGroupNameSuffix(SharingSetting sharingSetting) {
        switch (sharingSetting) {
            case VIEW:
                return READERS_GROUP_NAME_SUFFIX;
            case COMMENT:
                return COMMENTERS_GROUP_NAME_SUFFIX;
            case EDIT:
                return WRITERS_GROUP_NAME_SUFFIX;
            default:
                return NONE_GROUP_NAME_SUFFIX;
        }
    }


    private Set<Operation> getOperationsForSharingSetting(MetaProject metaProject, SharingSetting sharingSetting) {
        switch (sharingSetting) {
            case NONE:
                return Collections.emptySet();
            case VIEW:
                Set<Operation> viewOps = new HashSet<Operation>();
                viewOps.add(getReadOperation(metaProject));
                viewOps.add(getDisplayInProjectListOperation(metaProject));
                return viewOps;
            case COMMENT:
                Set<Operation> commentOps = new HashSet<Operation>();
                commentOps.add(getReadOperation(metaProject));
                commentOps.add(getCommentOperation(metaProject));
                commentOps.add(getDisplayInProjectListOperation(metaProject));
                return commentOps;
            case EDIT:
                Set<Operation> editOps = new HashSet<Operation>();
                editOps.add(getReadOperation(metaProject));
                editOps.add(getWriteOperation(metaProject));
                editOps.add(getDisplayInProjectListOperation(metaProject));
                return editOps;
            default:
                return Collections.emptySet();
        }
    }

    private Operation getWriteOperation(MetaProject metaProject) {
        return metaProject.getOperation(OperationName.WRITE.getName());
    }
    
    private Operation getCommentOperation(MetaProject metaProject) {
        Operation operation = metaProject.getOperation(OperationName.COMMENT.getName());
        if(operation == null) {
            throw new RuntimeException("The '" + OperationName.COMMENT.getName() + "' is not an instance in the meta-project.");
        }
        return operation;
    }

    private Operation getDisplayInProjectListOperation(MetaProject metaProject) {
        return metaProject.getOperation(OperationName.DISPLAY_IN_PROJECT_List.getName());
    }

    private Operation getReadOperation(MetaProject metaProject) {
        return metaProject.getOperation(OperationName.READ.getName());
    }






}

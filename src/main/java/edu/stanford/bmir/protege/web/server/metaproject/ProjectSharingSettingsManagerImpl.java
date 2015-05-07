package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.SharingSetting;
import edu.stanford.bmir.protege.web.client.rpc.data.UserSharingSetting;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.*;

import javax.inject.Inject;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {


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


    private MetaProject metaProject;

    private ProjectPermissionsManager projectPermissionsManager;

    @Inject
    public ProjectSharingSettingsManagerImpl(MetaProject metaProject, ProjectPermissionsManager projectPermissionsManager) {
        this.metaProject = metaProject;
        this.projectPermissionsManager = projectPermissionsManager;
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        ProjectInstance projectInstance = metaProject.getProject(projectId.getId());
        Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
        List<UserSharingSetting> result = new ArrayList<>();
        Set<User> usersWithPermissionsOnProject = new HashSet<>();
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
            Collection<Operation> operations = projectPermissionsManager.getAllowedOperations(projectId.getId(), user.getName());
            SharingSetting sharingSetting = getSharingSettingFromOperations(operations);
            UserSharingSetting userSharingSetting = new UserSharingSetting(UserId.getUserId(user.getName()), sharingSetting);
            result.add(userSharingSetting);
        }
        Collections.sort(result);

        return new ProjectSharingSettings(projectId, defaultSharingSetting, result);
    }


    @Override
    public void setProjectSharingSettings(ProjectSharingSettings projectSharingSettings) {
        ProjectId projectId = projectSharingSettings.getProjectId();
        ProjectInstance projectInstance = metaProject.getProject(projectId.getId());

        Map<SharingSetting, Set<User>> usersBySharingSetting = createUsersBySharingSettingMap(projectSharingSettings);

        Set<GroupOperation> allowedGroupOperations = createAllowedGroupOperationsFromSharingSettings(projectId, usersBySharingSetting);

        getWorldAllowedOperations(projectSharingSettings, allowedGroupOperations);

        projectInstance.setAllowedGroupOperations(allowedGroupOperations);

        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
    }

    /**
     * Applies the default sharing setting to a project.  The default sharing settings are that the project is private,
     * but the signed in user is an editor.
     * @param projectId The project id that identifies the project to apply sharing settings to.
     */
    @Override
    public void applyDefaultSharingSettings(ProjectId projectId, UserId userId) {
        List<UserSharingSetting> userSharingSettings = new ArrayList<>();
        if (!userId.isGuest()) {
            userSharingSettings.add(new UserSharingSetting(userId, SharingSetting.EDIT));
        }
        ProjectSharingSettings sharingSettings = new ProjectSharingSettings(projectId, SharingSetting.NONE, userSharingSettings);
        this.setProjectSharingSettings(sharingSettings);
    }


    private Set<GroupOperation> createAllowedGroupOperationsFromSharingSettings(ProjectId projectId, Map<SharingSetting, Set<User>> usersBySharingSetting) {
        Set<GroupOperation> allowedGroupOperations = new HashSet<>();
        for(SharingSetting sharingSetting : SharingSetting.values()) {
            Group sharingSettingGroup = getOrCreateGroup(projectId, sharingSetting);
            Set<User> sharingSettingUsers = usersBySharingSetting.get(sharingSetting);
            sharingSettingGroup.setMembers(sharingSettingUsers);
            GroupOperation groupOperation = metaProject.createGroupOperation();
            Set<Operation> sharingSettingOperatations = getOperationsForSharingSetting(sharingSetting);
            groupOperation.setAllowedOperations(sharingSettingOperatations);
            groupOperation.setAllowedGroup(sharingSettingGroup);
            allowedGroupOperations.add(groupOperation);
        }
        return allowedGroupOperations;
    }

    private Map<SharingSetting, Set<User>> createUsersBySharingSettingMap(ProjectSharingSettings projectSharingSettings) {
        Map<SharingSetting, Set<User>> usersBySharingSetting = createSharingSettingMap();

        for (UserSharingSetting userSharingSetting : projectSharingSettings.getSharingSettings()) {
            UserId userId = userSharingSetting.getUserId();
            if (!userId.isGuest()) {
                User user = getUserFromUserId(userId);
                if (user != null) {
                    usersBySharingSetting.get(userSharingSetting.getSharingSetting()).add(user);
                }
                else {
                    if(userId.getUserName().contains("@")) {
                        // Assume it's an email invitation
                        User freshUser = getUserFromUserId(userId);
                        usersBySharingSetting.get(userSharingSetting.getSharingSetting()).add(freshUser);
                    }
                }
            }
        }
        return usersBySharingSetting;
    }

//    private void sendEmailInvitation(ProjectSharingSettings projectSharingSettings, UserSharingSetting userSharingSetting) {
//        UserId userId = userSharingSetting.getUserId();
//        // Email invitation
//        List<Invitation> invitations = new ArrayList<Invitation>();
//        final Invitation invitation = new Invitation();
//        invitation.setEmailId(userId.getUserName());
//        invitation.setWriter(userSharingSetting.getSharingSetting() == SharingSetting.EDIT);
//        invitations.add(invitation);
//        String baseURL = "http://" + WebProtegeProperties.get().getApplicationHostName();
//
//        AccessPolicyManager.get().createTemporaryAccountForInvitation(projectSharingSettings.getProjectId(), baseURL, invitations);
//    }

    private void getWorldAllowedOperations(ProjectSharingSettings projectSharingSettings, Set<GroupOperation> allowedGroupOperations) {
        SharingSetting defaultSharingSetting = projectSharingSettings.getDefaultSharingSetting();
        Group worldGroup = getOrCreateGroup(WORLD_GROUP_NAME);
        GroupOperation worldGroupOperation = metaProject.createGroupOperation();
        worldGroupOperation.setAllowedGroup(worldGroup);
        worldGroupOperation.setAllowedOperations(getOperationsForSharingSetting(defaultSharingSetting));
        allowedGroupOperations.add(worldGroupOperation);
    }

    private Map<SharingSetting, Set<User>> createSharingSettingMap() {
        Map<SharingSetting, Set<User>> usersBySharingSetting = new HashMap<>();
        for(SharingSetting sharingSetting : SharingSetting.values()) {
            usersBySharingSetting.put(sharingSetting, new HashSet<User>());
        }
        return usersBySharingSetting;
    }

    private User getUserFromUserId(UserId userId) {
        return metaProject.getUser(userId.getUserName());
    }

    private Group getOrCreateGroup(ProjectId projectId, SharingSetting sharingSetting) {
        String groupName = getGroupName(projectId, sharingSetting);
        return getOrCreateGroup(groupName);
    }

    private Group getOrCreateGroup(String groupName) {
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


    private Set<Operation> getOperationsForSharingSetting(SharingSetting sharingSetting) {
        switch (sharingSetting) {
            case NONE:
                return Collections.emptySet();
            case VIEW:
                Set<Operation> viewOps = new HashSet<>();
                viewOps.add(getReadOperation());
                viewOps.add(getDisplayInProjectListOperation());
                return viewOps;
            case COMMENT:
                Set<Operation> commentOps = new HashSet<>();
                commentOps.add(getReadOperation());
                commentOps.add(getCommentOperation());
                commentOps.add(getDisplayInProjectListOperation());
                return commentOps;
            case EDIT:
                Set<Operation> editOps = new HashSet<>();
                editOps.add(getReadOperation());
                editOps.add(getWriteOperation());
                editOps.add(getDisplayInProjectListOperation());
                return editOps;
            default:
                return Collections.emptySet();
        }
    }

    private Operation getWriteOperation() {
        return metaProject.getOperation(OperationName.WRITE.getName());
    }

    private Operation getCommentOperation() {
        Operation operation = metaProject.getOperation(OperationName.COMMENT.getName());
        if(operation == null) {
            throw new RuntimeException("The '" + OperationName.COMMENT.getName() + "' is not an instance in the meta-project.");
        }
        return operation;
    }

    private Operation getDisplayInProjectListOperation() {
        return metaProject.getOperation(OperationName.DISPLAY_IN_PROJECT_List.getName());
    }

    private Operation getReadOperation() {
        return metaProject.getOperation(OperationName.READ.getName());
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


}

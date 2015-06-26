package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.*;

import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {


    public static final String WORLD_GROUP_NAME = "World";

    private final WebProtegeLogger logger;

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


    private final MetaProject metaProject;

    private final ProjectPermissionsManager projectPermissionsManager;

    private final HasGetUserByUserIdOrEmail userLookupManager;

    @Inject
    public ProjectSharingSettingsManagerImpl(MetaProject metaProject,  HasGetUserByUserIdOrEmail userLookupManager, ProjectPermissionsManager projectPermissionsManager, WebProtegeLogger logger) {
        this.metaProject = checkNotNull(metaProject);
        this.userLookupManager = checkNotNull(userLookupManager);
        this.projectPermissionsManager = checkNotNull(projectPermissionsManager);
        this.logger = checkNotNull(logger);
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        ProjectInstance projectInstance = metaProject.getProject(projectId.getId());
        Set<GroupOperation> groupOperations = projectInstance.getAllowedGroupOperations();
        List<SharingSetting> result = new ArrayList<>();
        Set<User> usersWithPermissionsOnProject = new HashSet<>();
        SharingPermission defaultSharingPermission = SharingPermission.getDefaultSharingSetting();
        for (GroupOperation groupOperation : groupOperations) {
            if (!isWorld(groupOperation.getAllowedGroup())) {
                usersWithPermissionsOnProject.addAll(groupOperation.getAllowedGroup().getMembers());
            }
            else {
                defaultSharingPermission = getSharingSettingFromOperations(groupOperation.getAllowedOperations());
            }
        }
        for (User user : usersWithPermissionsOnProject) {
            Collection<Operation> operations = projectPermissionsManager.getAllowedOperations(projectId.getId(), user.getName());
            SharingPermission sharingPermission = getSharingSettingFromOperations(operations);
            SharingSetting sharingSetting = new SharingSetting(new PersonId(user.getName()), sharingPermission);
            result.add(sharingSetting);
        }
        Collections.sort(result);

        return new ProjectSharingSettings(projectId, defaultSharingPermission, result);
    }


    @Override
    public void setProjectSharingSettings(ProjectSharingSettings projectSharingSettings) {
        ProjectId projectId = projectSharingSettings.getProjectId();
        ProjectInstance projectInstance = metaProject.getProject(projectId.getId());

        Multimap<SharingPermission, User> usersBySharingSetting = createUsersBySharingSettingMap(projectSharingSettings);

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
        List<SharingSetting> userSharingSettings = new ArrayList<>();
        if (!userId.isGuest()) {
            userSharingSettings.add(new SharingSetting(new PersonId(userId.getUserName()), SharingPermission.EDIT));
        }
        ProjectSharingSettings sharingSettings = new ProjectSharingSettings(projectId, SharingPermission.NONE, userSharingSettings);
        this.setProjectSharingSettings(sharingSettings);
    }


    private Set<GroupOperation> createAllowedGroupOperationsFromSharingSettings(ProjectId projectId, Multimap<SharingPermission, User> usersBySharingSetting) {
        Set<GroupOperation> allowedGroupOperations = new HashSet<>();
        for(SharingPermission sharingPermission : SharingPermission.values()) {
            Group sharingSettingGroup = getOrCreateGroup(projectId, sharingPermission);
            Collection<User> sharingSettingUsers = usersBySharingSetting.get(sharingPermission);
            sharingSettingGroup.setMembers(sharingSettingUsers);
            GroupOperation groupOperation = metaProject.createGroupOperation();
            Set<Operation> sharingSettingOperatations = getOperationsForSharingSetting(sharingPermission);
            groupOperation.setAllowedOperations(sharingSettingOperatations);
            groupOperation.setAllowedGroup(sharingSettingGroup);
            allowedGroupOperations.add(groupOperation);
        }
        return allowedGroupOperations;
    }

    private Multimap<SharingPermission, User> createUsersBySharingSettingMap(ProjectSharingSettings projectSharingSettings) {
        Multimap<SharingPermission, User> usersBySharingSetting = HashMultimap.create();
        for (SharingSetting sharingSetting : projectSharingSettings.getSharingSettings()) {
            PersonId personId = sharingSetting.getPersonId();
            UserId personIdAsUserId = UserId.getUserId(personId.getId());
            if (!personIdAsUserId.isGuest()) {
                Optional<User> user = userLookupManager.getUserByUserIdOrEmail(personId.getId());
                if (user.isPresent()) {
                    usersBySharingSetting.put(sharingSetting.getSharingPermission(), user.get());
                }
                else {
                    logger.info("Cannot share project with %s because this person is not a registered WebProtege user.", personId);
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
        SharingPermission defaultSharingPermission = projectSharingSettings.getDefaultSharingPermission();
        Group worldGroup = getOrCreateGroup(WORLD_GROUP_NAME);
        GroupOperation worldGroupOperation = metaProject.createGroupOperation();
        worldGroupOperation.setAllowedGroup(worldGroup);
        worldGroupOperation.setAllowedOperations(getOperationsForSharingSetting(defaultSharingPermission));
        allowedGroupOperations.add(worldGroupOperation);
    }

    private Group getOrCreateGroup(ProjectId projectId, SharingPermission sharingPermission) {
        String groupName = getGroupName(projectId, sharingPermission);
        return getOrCreateGroup(groupName);
    }

    private Group getOrCreateGroup(String groupName) {
        Group group = metaProject.getGroup(groupName);
        if (group == null) {
            group = metaProject.createGroup(groupName);
        }
        return group;
    }

    private String getGroupName(ProjectId projectId, SharingPermission sharingPermission) {
        return projectId.getId() + getGroupNameSuffix(sharingPermission);
    }

    /**
     * Gets the group name suffix for a given sharing setting.
     * @param sharingPermission The sharing setting
     * @return A string representing the group name suffix for a given sharing setting
     */
    private String getGroupNameSuffix(SharingPermission sharingPermission) {
        switch (sharingPermission) {
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


    private Set<Operation> getOperationsForSharingSetting(SharingPermission sharingPermission) {
        switch (sharingPermission) {
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



    private SharingPermission getSharingSettingFromOperations(Collection<Operation> operations) {
        SharingPermission sharingPermission;
        if (isWriteable(operations)) {
            sharingPermission = SharingPermission.EDIT;
        }
        else if (isCommentable(operations)) {
            sharingPermission = SharingPermission.COMMENT;
        }
        else if (isReadable(operations)) {
            sharingPermission = SharingPermission.VIEW;
        }
        else {
            sharingPermission = SharingPermission.NONE;
        }
        return sharingPermission;
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

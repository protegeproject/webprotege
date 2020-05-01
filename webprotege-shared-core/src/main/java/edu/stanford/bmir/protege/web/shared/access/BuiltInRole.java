package edu.stanford.bmir.protege.web.shared.access;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 */
public enum BuiltInRole {

    // Application Roles

    PROJECT_CREATOR(CREATE_EMPTY_PROJECT),

    PROJECT_UPLOADER(UPLOAD_PROJECT),

    ACCOUNT_CREATOR(CREATE_ACCOUNT),

    USER_ADMIN(ACCOUNT_CREATOR,
               VIEW_ANY_USER_DETAILS,
               DELETE_ANY_ACCOUNT,
               RESET_ANY_USER_PASSWORD),

    SYSTEM_ADMIN(USER_ADMIN,
                 MOVE_ANY_PROJECT_TO_TRASH,
                 SUBSTITUTE_USER,
                 EDIT_APPLICATION_SETTINGS,
                 REBUILD_PERMISSIONS),


    // Project Roles

    PROJECT_DOWNLOADER(DOWNLOAD_PROJECT),



    ISSUE_VIEWER(VIEW_ANY_ISSUE),

    ISSUE_COMMENTER(ISSUE_VIEWER,
                    COMMENT_ON_ISSUE),

    ISSUE_CREATOR(ISSUE_COMMENTER,
                  CREATE_ISSUE,
                  ASSIGN_OWN_ISSUE_TO_SELF,
                  CLOSE_OWN_ISSUE),

    ISSUE_MANAGER(ISSUE_CREATOR,
                  ASSIGN_ANY_ISSUE_TO_ANYONE,
                  CLOSE_ANY_ISSUE,
                  UPDATE_ANY_ISSUE_TITLE,
                  UPDATE_ANY_ISSUE_BODY),



    PROJECT_VIEWER(VIEW_PROJECT,
                   VIEW_OBJECT_COMMENT,
                   EDIT_OWN_OBJECT_COMMENT,
                   ADD_OR_REMOVE_VIEW,
                   ADD_OR_REMOVE_PERSPECTIVE,
                   VIEW_CHANGES,
                   WATCH_CHANGES),

    OBJECT_COMMENTER(PROJECT_VIEWER,
                     CREATE_OBJECT_COMMENT,
                     EDIT_OWN_OBJECT_COMMENT,
                     SET_OBJECT_COMMENT_STATUS,
                     EDIT_ENTITY_TAGS),

    PROJECT_EDITOR(OBJECT_COMMENTER,
                   EDIT_ONTOLOGY,
                   EDIT_ONTOLOGY_ANNOTATIONS,
                   CREATE_CLASS,
                   DELETE_CLASS,
                   MERGE_ENTITIES,
                   CREATE_PROPERTY,
                   DELETE_PROPERTY,
                   CREATE_INDIVIDUAL,
                   DELETE_INDIVIDUAL,
                   CREATE_DATATYPE,
                   DELETE_DATATYPE,
                   REVERT_CHANGES),

    LAYOUT_EDITOR(ADD_OR_REMOVE_PERSPECTIVE,
                  ADD_OR_REMOVE_VIEW),

    PROJECT_MANAGER(PROJECT_EDITOR,
                    LAYOUT_EDITOR,
                    SAVE_DEFAULT_PROJECT_LAYOUT,
                    EDIT_PROJECT_SETTINGS,
                    EDIT_DEFAULT_VISUALIZATION_SETTINGS,
                    EDIT_SHARING_SETTINGS,
                    EDIT_NEW_ENTITY_SETTINGS,
                    EDIT_PROJECT_PREFIXES,
                    UPLOAD_AND_MERGE,
                    EDIT_PROJECT_TAGS,
                    EDIT_FORMS,
                    UPLOAD_AND_MERGE_ADDITIONS,
                    EDIT_PROJECT_TAGS),


    // Roles that relate to the UI

    CAN_VIEW(PROJECT_VIEWER, ISSUE_VIEWER, PROJECT_DOWNLOADER),

    CAN_COMMENT(CAN_VIEW, ISSUE_CREATOR, OBJECT_COMMENTER),

    CAN_EDIT(PROJECT_EDITOR, CAN_COMMENT),

    CAN_MANAGE(CAN_EDIT, PROJECT_MANAGER, ISSUE_MANAGER)

    ;






    private final RoleId roleId;

    private final ImmutableList<BuiltInRole> parents;

    private final ImmutableList<BuiltInAction> actions;



    BuiltInRole(ImmutableList<BuiltInRole> parents, ImmutableList<BuiltInAction> actions) {
        this.roleId = new RoleId(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name()));
        this.parents = parents;
        this.actions = actions;
    }

    BuiltInRole(BuiltInAction... actions) {
        this(ImmutableList.of(), ImmutableList.copyOf(Arrays.asList(actions)));
    }


    BuiltInRole(BuiltInRole parentRole, BuiltInAction... actions) {
        this(ImmutableList.of(parentRole), ImmutableList.copyOf(Arrays.asList(actions)));
    }

    BuiltInRole(BuiltInRole parentRole1, BuiltInRole parentRole2, BuiltInAction... actions) {
        this(ImmutableList.of(parentRole1, parentRole2), ImmutableList.copyOf(Arrays.asList(actions)));
    }

    BuiltInRole(BuiltInRole parentRole1, BuiltInRole parentRole2, BuiltInRole parentRole3, BuiltInAction... actions) {
        this(ImmutableList.of(parentRole1, parentRole2, parentRole3), ImmutableList.copyOf(Arrays.asList(actions)));
    }

    public RoleId getRoleId() {
        return roleId;
    }

    public ImmutableList<BuiltInRole> getParents() {
        return parents;
    }

    public ImmutableList<BuiltInAction> getActions() {
        return actions;
    }
}

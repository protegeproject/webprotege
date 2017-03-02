package edu.stanford.bmir.protege.web.shared.access;

import com.google.common.base.CaseFormat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Jan 2017
 */
public enum BuiltInAction {


    CREATE_USER,

    CREATE_EMPTY_PROJECT,

    UPLOAD_PROJECT,

    VIEW_ANY_USER_DETAILS,

    DELETE_ANY_USER,

    RESET_ANY_USER_PASSWORD,

    MOVE_ANY_PROJECT_TO_TRASH,

    SUBSTITUTE_USER,


    VIEW_PROJECT,

    VIEW_CHANGES,

    WATCH_CHANGES,

    DOWNLOAD_PROJECT,

    REVERT_CHANGES,

    EDIT_ONTOLOGY,

    EDIT_ONTOLOGY_ANNOTATIONS,

    CREATE_CLASS,

    DELETE_CLASS,

    CREATE_PROPERTY,

    DELETE_PROPERTY,

    CREATE_INDIVIDUAL,

    DELETE_INDIVIDUAL,

    CREATE_DATATYPE,

    DELETE_DATATYPE,


    CREATE_ISSUE,

    CLOSE_OWN_ISSUE,

    ASSIGN_OWN_ISSUE_TO_SELF,

    ASSIGN_ANY_ISSUE_TO_ANYONE,

    COMMENT_ON_ISSUE,

    VIEW_ANY_ISSUE,

    UPDATE_ANY_ISSUE_TITLE,

    UPDATE_ANY_ISSUE_BODY,

    CLOSE_ANY_ISSUE,


    VIEW_OBJECT_COMMENTS,

    CREATE_OBJECT_COMMENT,

    EDIT_OWN_OBJECT_COMMENT,

    EDIT_ANY_OBJECT_COMMENT,

    SET_OBJECT_COMMENT_STATUS,


    ADD_OR_REMOVE_VIEW,

    ADD_OR_REMOVE_PERSPECTIVE,

    SAVE_DEFAULT_PROJECT_LAYOUT,

    EDIT_PROJECT_SETTINGS,

    EDIT_SHARING_SETTINGS,

    EDIT_NEW_ENTITY_SETTINGS,

    UPLOAD_AND_MERGE
//    MANAGE_PROJECT

    ;




    private final ActionId actionId;

    BuiltInAction() {
        this.actionId = new ActionId(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name()));
    }

    public ActionId getActionId() {
        return actionId;
    }
}

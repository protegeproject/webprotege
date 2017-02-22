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

    DOWNLOAD_PROJECT,

    ADD_AXIOM,

    REMOVE_AXIOM,

    ADD_ONTOLOGY_ANNOTATION,

    REMOVE_ONTOLOGY_ANNOTATION,

    CREATE_CLASS,

    DELETE_CLASS,

    CREATE_OBJECT_PROPERTY,

    DELETE_OBJECT_PROPERTY,

    CREATE_DATA_PROPERTY,

    DELETE_DATA_PROPERTY,

    CREATE_ANNOTATION_PROPERTY,

    DELETE_ANNOTATION_PROPERTY,

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


    SAVE_OWN_PROJECT_LAYOUT,

    SAVE_DEFAULT_PROJECT_LAYOUT;




    private final ActionId actionId;

    BuiltInAction() {
        this.actionId = new ActionId(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name()));
    }

    public ActionId getActionId() {
        return actionId;
    }
}

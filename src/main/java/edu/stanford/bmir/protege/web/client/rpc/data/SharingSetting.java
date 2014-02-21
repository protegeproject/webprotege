package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 */
public enum SharingSetting implements Serializable {

    /**
     * A user can view a project.
     */
    VIEW,

    /**
     * A user can comment on various structures within a project.
     */
    COMMENT,

    /**
     * A user can edit a project.
     */
    EDIT,

    /**
     * A user doesn't have any sharing rights for a project.
     */
    NONE;


    /**
     * Gets the default sharing setting for a project.
     * @return The SharingSetting that represent the default sharing setting.
     */
    public static SharingSetting getDefaultSharingSetting() {
        return EDIT;
    }
}

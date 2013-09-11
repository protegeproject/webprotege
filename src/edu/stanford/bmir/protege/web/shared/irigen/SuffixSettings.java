package edu.stanford.bmir.protege.web.shared.irigen;


import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 * <p>
 *     The settings for generating a suffix for an IRI (from a supplied human readable "local" name).
 * </p>
 */
public abstract class SuffixSettings implements Serializable {

    private EntityCrudKitId kitId;

    /**
     * For serialization only
     */
    protected SuffixSettings() {

    }


    protected SuffixSettings(EntityCrudKitId kitId) {
        this.kitId = kitId;
    }

    public EntityCrudKitId getId() {
        return kitId;
    }

}

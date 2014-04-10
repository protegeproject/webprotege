package edu.stanford.bmir.protege.web.shared.obo;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOSynonymType implements Serializable {

    private String id;

    private String name;

    private OBOTermSynonymScope scope;

    private OBOSynonymType() {

    }

    public OBOSynonymType(String id, String name, OBOTermSynonymScope scope) {
        this.id = id;
        this.name = name;
        this.scope = scope;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OBOTermSynonymScope getScope() {
        return scope;
    }
}

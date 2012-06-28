package edu.stanford.bmir.protege.web.client.rpc.data.obo;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermSubset implements Serializable {

    private String name;
    
    private String description;

    private OBOTermSubset() {
        
    }
    
    public OBOTermSubset(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

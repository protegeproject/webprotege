package edu.stanford.bmir.protege.web.shared.obo;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public enum OBOTermSynonymScope implements Serializable {

    RELATED("Related"),

    EXACT("Exact"),

    BROADER("Broader"),

    NARROWER("Narrower");
    
    
    private String displayText;

    OBOTermSynonymScope(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}

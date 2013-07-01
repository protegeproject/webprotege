package edu.stanford.bmir.protege.web.shared.obo;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermSynonym extends OBOTermMetaData implements Serializable {

    private String name;
    
    private OBOTermSynonymScope scope;


    public OBOTermSynonym() {
        name = "";
        scope = OBOTermSynonymScope.RELATED;
    }

    public OBOTermSynonym(List<OBOXRef> xrefs, String name, OBOTermSynonymScope scope) {
        super(xrefs);
        this.name = name;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public OBOTermSynonymScope getScope() {
        return scope;
    }
    
    public boolean isEmpty() {
        return name.isEmpty();
    }
}

package edu.stanford.bmir.protege.web.client.rpc.data.obo;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermDefinition extends OBOTermMetaData implements Serializable {

    public static final OBOTermDefinition EMPTY = new OBOTermDefinition(Collections.<OBOXRef>emptyList(), "");

    private String definition;

    private OBOTermDefinition() {
        super();
    }

    public static OBOTermDefinition empty() {
        return EMPTY;
    }

    public OBOTermDefinition(List<OBOXRef> xrefs, String definition) {
        super(xrefs);
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }
}

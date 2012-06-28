package edu.stanford.bmir.protege.web.client.rpc.data.obo;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.WebProtegeIRI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public abstract class OBOTermMetaData implements Serializable {

    private List<OBOXRef> xrefs = new ArrayList<OBOXRef>();

    protected OBOTermMetaData() {
    }

    protected OBOTermMetaData(List<OBOXRef> xrefs) {
        this.xrefs = new ArrayList<OBOXRef>(xrefs);
    }

    public List<OBOXRef> getXRefs() {
        return new ArrayList<OBOXRef>(xrefs);
    }
}

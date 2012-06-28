package edu.stanford.bmir.protege.web.client.rpc.data.obo;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermDefinition extends OBOTermMetaData implements Serializable {

//    private VisualEntity entity;

    private String definition;

    private OBOTermDefinition() {
        super();
    }

    public OBOTermDefinition(List<OBOXRef> xrefs, String definition) {
        super(xrefs);
//        this.entity = entity;
        this.definition = definition;
    }

//    public VisualEntity getEntity() {
//        return entity;
//    }

    public String getDefinition() {
        return definition;
    }
}

package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2012
 */
public class AnonymousIndividual implements AnnotationPropertyEdgeSubject, AnnotationPropertyEdgeValue, Serializable {

    
    private String nodeId;

    public AnonymousIndividual(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }
}

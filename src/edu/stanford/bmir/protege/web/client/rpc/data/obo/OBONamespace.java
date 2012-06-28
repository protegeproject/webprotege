package edu.stanford.bmir.protege.web.client.rpc.data.obo;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBONamespace implements Serializable {

    private String namespace;

    public OBONamespace() {
        namespace = "";
    }

    public OBONamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }
    
    public boolean isEmpty() {
        return namespace.isEmpty();
    }

    @Override
    public int hashCode() {
        return namespace.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OBONamespace)) {
            return false;
        }
        return ((OBONamespace) obj).namespace.equals(this.namespace);
    }
}

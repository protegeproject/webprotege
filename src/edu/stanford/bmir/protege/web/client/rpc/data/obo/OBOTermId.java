package edu.stanford.bmir.protege.web.client.rpc.data.obo;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBOTermId implements Serializable {

    private String id;
    
    private String name;
    
    private String namespace;
    
    private OBOTermId() {
        id = "";
        name = "";
        namespace = "";
    }

    public OBOTermId(String id, String name) {
        this(id, name, "");
    }

    public OBOTermId(String id, String name, String namespace) {
        this.id = id;
        this.name = name;
        this.namespace = namespace;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public boolean isEmpty() {
        return id.isEmpty() && name.isEmpty() && namespace.isEmpty();
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode() + namespace.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OBOTermId)) {
            return false;
        }
        OBOTermId other = (OBOTermId) obj;
        return id.equals(other.id) && name.equals(other.name) && namespace.equals(other.namespace);
    }
}

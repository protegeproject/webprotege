package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectType implements Serializable {

    /**
     * The name of the project type.
     */
    private String name;

    /**
     * For serialization purposes
     */
    private ProjectType() {
    }

    /**
     * Constructs a project type by specifying the type name.
     * @param name The type name of the project type.  Must not be null.
     * @throws NullPointerException if name is null.
     */
    public ProjectType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectType)) {
            return false;
        }
        ProjectType other = (ProjectType) obj;
        return other.name.equals(this.name);
    }
}

package edu.stanford.bmir.protege.web.client.ui.hierarchy;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/06/2012
 */
public class HierarchyId implements Serializable {

    private String hierarchyName;

    private HierarchyId() {
    }

    public HierarchyId(String hierarchyName) {
        this.hierarchyName = hierarchyName;
    }

    public String getHierarchyName() {
        return hierarchyName;
    }
}

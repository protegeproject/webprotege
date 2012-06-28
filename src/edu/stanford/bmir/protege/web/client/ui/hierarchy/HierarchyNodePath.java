package edu.stanford.bmir.protege.web.client.ui.hierarchy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/06/2012
 */
public class HierarchyNodePath implements Serializable {

    private List<HierarchyNode> path;

    private HierarchyNodePath() {
    }

    public HierarchyNodePath(List<HierarchyNode> path) {
        this.path = new ArrayList<HierarchyNode>(path);
    }

    public List<HierarchyNode> getPath() {
        return path;
    }
}

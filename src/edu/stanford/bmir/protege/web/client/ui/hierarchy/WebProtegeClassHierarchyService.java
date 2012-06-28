package edu.stanford.bmir.protege.web.client.ui.hierarchy;

import com.google.gwt.user.client.rpc.RemoteService;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/06/2012
 */
public interface WebProtegeClassHierarchyService extends RemoteService  {

    
    List<HierarchyNode> getRoots(HierarchyId hierarchyId);

    HierarchyNode getParent(HierarchyId hierarchyId, HierarchyNode hierarchyNode);
    
    List<HierarchyNode> getChildren(HierarchyId hierarchyId, HierarchyNode parent);
    
    List<HierarchyNodePath> getPathsToRoot(HierarchyId hierarchyId, HierarchyNode pathTerminal);


}

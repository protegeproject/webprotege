package edu.stanford.bmir.protege.web.client.ui.hierarchy;


import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface WebProtegeClassHierarchyServiceAsync {


    void getRoots(HierarchyId hierarchyId, AsyncCallback<List<HierarchyNode>> async);

    void getChildren(HierarchyId hierarchyId, HierarchyNode parent, AsyncCallback<List<HierarchyNode>> async);

    void getParent(HierarchyId hierarchyId, HierarchyNode hierarchyNode, AsyncCallback<HierarchyNode> async);

    void getPathsToRoot(HierarchyId hierarchyId, HierarchyNode pathTerminal, AsyncCallback<List<HierarchyNodePath>> async);
}

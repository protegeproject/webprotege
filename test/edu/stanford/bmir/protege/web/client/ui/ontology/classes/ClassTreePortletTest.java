package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import com.google.gwt.junit.client.GWTTestCase;
import com.gwtext.client.widgets.tree.TreeNode;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;


/**
 * @author Jack Elliott <jacke@stanford.edu>
 */
public class ClassTreePortletTest extends GWTTestCase {
    public String getModuleName() {
        return "edu.stanford.bmir.protege.web.WebProtegeTest";
    }                              

    public void testTextIsComputed(){
        Project project = new Project(new ProjectData("description", "location", "name", "owner"));
        ClassTreePortlet unit = new ClassTreePortlet(project, false, true, true, false, null);
        EntityData data = new EntityData("name", "browserText");
        data.setChildrenAnnotationsCount(20);
        data.setLocalAnnotationsCount(50);

        final TreeNode node = new TreeNode();
        node.setUserObject(data);
        final String result = unit.computeText(node);
        assertTrue(result.contains("There are 50 notes on this category"));
        assertTrue(result.contains("There are 20 notes on the children of this category"));
        
    }

}

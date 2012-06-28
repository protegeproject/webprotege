package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

/**
 * A class tree portlet that creates a class, setting a property to a value set by a user.
 *
 * Useful when the browser text is configured to read in a property, and we want to display that value to a user in
 * place of the messy, ugly class name.
 *
 * @author Jack Elliott <jacke@stanford.edu>
 */
public class LabelingClassTreePortlet extends ClassTreePortlet {
    public static final String PROPERTY_NAME = "property_name";
    public static final String PROPERTY_NAME_DEFAULT = "http://www.w3.org/2000/01/rdf-schema#label";

    public LabelingClassTreePortlet(Project project) {
        super(project);
    }

    public LabelingClassTreePortlet(Project project, boolean showToolbar, boolean showTitle, boolean showTools, boolean allowsMultiSelection, String topClass) {
        super(project, showToolbar, showTitle, showTools, allowsMultiSelection, topClass);
    }


    protected boolean isValidClassName(String value) {
        return value != null && value.length() > 0;
    }

    protected void createCls(final String className) {
        String superClsName = null;
        final EntityData currentSelection = getSingleSelection();
        if (currentSelection != null) {
            superClsName = currentSelection.getName();
        }
        String propertyName = getPropertyName();
        EntityData propertyValue = new EntityData(className);
        OntologyServiceManager.getInstance().createClsWithProperty(project.getProjectName(), null, superClsName, propertyName, propertyValue,
                GlobalSettings.getGlobalSettings().getUserName(), getCreateClsDescription() + " " + className,
                getCreateClassAsyncHandler(superClsName, className));
    }

    private String getPropertyName(){
        return getPortletConfiguration().getStringProperty(PROPERTY_NAME, PROPERTY_NAME_DEFAULT);
    }

}

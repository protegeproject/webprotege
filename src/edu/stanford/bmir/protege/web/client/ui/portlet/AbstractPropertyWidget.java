package edu.stanford.bmir.protege.web.client.ui.portlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormConstants;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public abstract class AbstractPropertyWidget implements PropertyWidget {

    private static final String PART_OF_WRITE_ACCESS_GROUP_SESSION_PROP = "partOfWriteAccessGroup";

    private Project project;
    private EntityData subject;
    private PropertyEntityData property;
    private Map<String, Object> widgetConfiguration;

    private EntityData oldDisplayedSubject; //Optimization: only load values if new subject, and widget is visible
    private boolean isLoading;


    public AbstractPropertyWidget(Project project) {
        this.project = project;
    }

    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        setProperty(propertyEntityData);
        setWidgetConfiguration(widgetConfiguration);

        createComponent();
    }


    public abstract Component getComponent(); //TODO: should be Widget?

    public abstract Component createComponent();

    protected String getLabelHtml(String label, String helpURL, String tooltip) {
        String labelHtml = label;
        if (label != null) {
            tooltip = (tooltip == null ? "" : tooltip.replaceAll("\"", "&quot;"));
            String helpImage = "";
            if (helpURL != null && helpURL.length() > 0) {
                helpImage = UIUtil.getHelpImageHtml(helpURL, "Help");
                String tooltipSuffix = "&#10;&#13;(click on the 'HELP' icon to learn more)";
                tooltip += tooltipSuffix;
            }
            labelHtml = "<span title=\"" + tooltip + "\">" + label + helpImage + "</span>";
        }
        return labelHtml;
    }

    public Collection<EntityData> getValues() {
        return new ArrayList<EntityData>();
    }

    public EntityData getSubject() {
        return subject;
    }

    public void setValues(Collection<EntityData> values) {}

    public void setSubject(EntityData subject) {
        this.subject = subject;
    }

    public boolean isDisplayed() {
        return (getComponent()).getEl() != null && getComponent().getEl().isVisible(true);
    }

    /**
     * Load new values only if the new subject is different than the old subject and the widget is currently displayed.
     */
    public void fillValues() {
        if (!isSameSubject()) {
            List<String> subjects = new ArrayList<String>();
            if (getSubject() != null) {
                subjects.add(getSubject().getName());
            }

            List<String> props = new ArrayList<String>();
            props.add(getProperty().getName());

            setLoadingStatus(true);

            fillValues(subjects, props);
        }
    }

    protected void fillValues(List<String> subjects, List<String> props) {
        OntologyServiceManager.getInstance().getEntityTriples(getProject().getProjectName(), subjects, props,
                new GetValuesHandler(getSubject()));
    }

    protected boolean isSameSubject() {
        return oldDisplayedSubject != null && getSubject() != null && oldDisplayedSubject.equals(getSubject()) ||
        (oldDisplayedSubject == null && getSubject() == null);
    }

    public void setProperty(PropertyEntityData property) {
        this.property = property;
    }

    public PropertyEntityData getProperty() {
        return property;
    }

    public Project getProject() {
        return project;
    }

    public void setWidgetConfiguration(Map<String, Object> widgetConfiguration) {
        this.widgetConfiguration = widgetConfiguration; //TODO: maybe need to save
    }

    public Map<String, Object> getWidgetConfiguration() {
        return widgetConfiguration;
    }

    public boolean userPartOfWriteAccessGroup() {
        if (!GlobalSettings.getGlobalSettings().isLoggedIn()) {
            return false;
        }
        //checked if this is cached
        String isPartOfUsers = GlobalSettings.getGlobalSettings().getSessionProperty(getSessionWriteAccessProperty());
        if (isPartOfUsers != null) {
            return Boolean.valueOf(isPartOfUsers);
        }

        List<String> writeAccessGroups = UIUtil.getListConfigurationProperty(widgetConfiguration, FormConstants.WRITE_ACCESS_GROUPS);
        if (writeAccessGroups == null) {
            setPartOfWriteAccessGroup(true);
            return true;
        }
        Collection<String> userGroups = GlobalSettings.getGlobalSettings().getUser().getGroups();
        if (userGroups == null) {
            setPartOfWriteAccessGroup(false);
            return false;
        }
        for (String writeGroup : writeAccessGroups) {
            if (userGroups.contains(writeGroup)) {
                setPartOfWriteAccessGroup(true);
                return true;
            }
        }
        setPartOfWriteAccessGroup(false);
        return false;
    }

    private void setPartOfWriteAccessGroup(Boolean isPartOfWriteAccessGroup) {
        GlobalSettings.getGlobalSettings().setSessionProperty(getSessionWriteAccessProperty(), isPartOfWriteAccessGroup.toString());
    }

    private String getSessionWriteAccessProperty() {
        String id = getComponent().getElement().getId();
        return PART_OF_WRITE_ACCESS_GROUP_SESSION_PROP + id;
    }

    public boolean isWriteOperationAllowed() {
        return isWriteOperationAllowed(true);
    }
    
    public boolean isWriteOperationAllowed(boolean showUserAlerts) {
        if (!UIUtil.checkOperationAllowed(getProject(), showUserAlerts)) {
            return false;
        }
        if (isReadOnly()) {
            if (showUserAlerts) {
                MessageBox.alert("This property is read only.");
            }
            return false;
        }
         boolean hasWriteAccess = userPartOfWriteAccessGroup();
         if (!hasWriteAccess) {
             if (showUserAlerts) {
                 MessageBox.alert("You do not have permission to change this property.");
             }
         }
         return hasWriteAccess;
    }

    public boolean isReadOnly() {
        return UIUtil.getBooleanConfigurationProperty(widgetConfiguration, FormConstants.READ_ONLY, false);
    }

    public void setReadOnly(boolean readOnly) {
        UIUtil.setConfigurationValue(widgetConfiguration, FormConstants.READ_ONLY, readOnly);
    }

    public boolean isDisabled() {
        return UIUtil.getBooleanConfigurationProperty(widgetConfiguration, FormConstants.DISABLED, false);
    }

    public void setDisabled(boolean disabled) {
        UIUtil.setConfigurationValue(widgetConfiguration, FormConstants.DISABLED, disabled);
    }

    public String getTooltipText() {
        return widgetConfiguration == null ? null : (String) widgetConfiguration.get(FormConstants.TOOLTIP);
    }

    public String getHelpURL() {
        return widgetConfiguration == null ? null : (String) widgetConfiguration.get(FormConstants.HELP);
    }

    public void setLoadingStatus(boolean loading) {
        isLoading = loading;
    }

    protected void setOldDisplayedSubject(EntityData oldSubject) {
        oldDisplayedSubject = oldSubject;
    }

    protected EntityData getOldDisplayedSubject() {
        return oldDisplayedSubject;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void refresh() {
        oldDisplayedSubject = null;
        fillValues();
    }

    /*
     * Remote calls
     */

    class GetValuesHandler extends AbstractAsyncHandler<List<Triple>> {

        private EntityData mySubject;

        public GetValuesHandler(EntityData subject) {
            this.mySubject = subject;
        }

        @Override
        public void handleFailure(Throwable caught) {
            setLoadingStatus(false);
            GWT.log("Error at getting values for " + mySubject + " and " + getProperty(), caught);
            //TODO: notify the user somehow
        }

        @Override
        public void handleSuccess(List<Triple> triples) { //TODO - make a call to get only the prop values
            /*
             * This check is necessary because of the async nature of the call.
             * We should never add values to a widget, if the subject has already changed.
             */
            if (!UIUtil.equals(mySubject, getSubject())) {  return; }
            Collection<EntityData> values = new ArrayList<EntityData>();
            if (triples != null) {
                for (Triple triple : triples) {
                    if (triple.getValue() != null) {
                        values.add(triple.getValue());
                    }
                }
            }
            setValues(values);

            setOldDisplayedSubject(mySubject);
            setLoadingStatus(false);
        }
    }

}

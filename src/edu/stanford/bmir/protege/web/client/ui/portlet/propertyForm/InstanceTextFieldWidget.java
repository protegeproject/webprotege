package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class InstanceTextFieldWidget extends TextFieldWidget {
	private String property;
	private Map<EntityData, EntityData> valuesToDisplayValuesMap;

	public InstanceTextFieldWidget(Project project) {
		super(project);
		valuesToDisplayValuesMap = new HashMap<EntityData, EntityData>();
	}

	protected String getDisplayProperty() {
	    return property;
	}
	
    @Override
    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        super.setup(widgetConfiguration, propertyEntityData);
        String instProperty = (String) widgetConfiguration.get(FormConstants.PROPERTY);
        if (instProperty == null) {
            GWT.log("Wrong widget configuration for " + propertyEntityData.getName()
            		+ ". Property \"" + FormConstants.PROPERTY + "\" must be specified!", null);
            //FIXME this is only for experimenting purposes.
            //See if we can set a good default value on the client or we deal with this case on the server
            instProperty = ":NAME";
        }
        property = instProperty;
    }

    @Override
    public Field createFieldComponent() {
    	TextField textfield = new TextField();
    	return textfield;
    }

    @Override
    protected void displayValues(Collection<EntityData> values) {
        List<EntityData> dispValues = new ArrayList<EntityData>();
        if (values != null) {
            for (EntityData value : values) {
                EntityData displayValue = valuesToDisplayValuesMap.get(value);
                if (displayValue != null) {
                    dispValues.add(displayValue);
                }
                else {
                    GWT.log("No display text for instance property value: " + value);
                }
            }
        }
        super.displayValues(dispValues);
    }

    @Override
    protected void fillValues(List<String> subjects, List<String> props) {
        displayValues(null);
        List<String> reifiedProps = new ArrayList<String>();
        reifiedProps.add(property);
        OntologyServiceManager.getInstance().getEntityTriples(getProject().getProjectName(), subjects, props, reifiedProps,
                new GetTriplesHandler(getSubject()));
    }


    /*
     * Remote calls
     */

    class GetTriplesHandler extends AbstractAsyncHandler<List<Triple>> {

        private EntityData mySubject;

        public GetTriplesHandler(EntityData subject) {
            this.mySubject = subject;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Instance Text Field Widget: Error at getting triples for " + mySubject, caught);
        }

        @Override
        public void handleSuccess(List<Triple> triples) {
            valuesToDisplayValuesMap.clear();
            Set<EntityData> subjects = null;
            if (triples != null) {
                subjects = new HashSet<EntityData>();
                for (Triple triple : triples) {
                    subjects.add(triple.getEntity());
                    valuesToDisplayValuesMap.put(triple.getEntity(), triple.getValue());
                }
            }
            setOldDisplayedSubject(mySubject);
            setValues(subjects);
            setLoadingStatus(false);
        }
    }


    protected EntityData findInstanceForValue(EntityData displayValueEntityData) {
        EntityData instanceValueEntityData = null;
        //try to find the value that has the display text
        for (EntityData value : getValues()) {
            if (valuesToDisplayValuesMap.get(value).equals(displayValueEntityData)) {
                instanceValueEntityData = value;
                break;
            }
        }
        //as a fallback return the first instance attached as value 
        if (instanceValueEntityData == null) {
            instanceValueEntityData = UIUtil.getFirstItem(getValues());
        }
        return instanceValueEntityData;
    }

    @Override
    protected void deletePropertyValue(EntityData subject, String propName, ValueType propValueType,
            EntityData oldEntityData, Object oldBrowserText, String operationDescription) {
        
        EntityData oldInstanceEntityData = findInstanceForValue(oldEntityData);
        
        //overwrite the oldEntityData that has been created from the display text,
        //with a (hopefully correctly identified) instance value
        if (oldInstanceEntityData != null) {
            oldEntityData = oldInstanceEntityData;  //FIXME Check this!!!
        }
        
        propertyValueUtil.deletePropertyValue(getProject().getProjectName(), subject.getName(), propName, propValueType,
                oldEntityData.getName(), GlobalSettings.getGlobalSettings().getUserName(), operationDescription,
                new RemoveInstancePropertyValueHandler(subject, oldEntityData, getValues()));
    }

    @Override
    protected void replacePropertyValue(EntityData subject, String propName, ValueType propValueType,
            EntityData oldEntityData, EntityData newEntityData, Object oldDisplayedValue, String operationDescription) {
        
        EntityData oldInstanceEntityData = findInstanceForValue(oldEntityData);
        
        if (oldInstanceEntityData != null) {
            propertyValueUtil.replacePropertyValue(getProject().getProjectName(), oldInstanceEntityData.getName(),
                    property, null, oldEntityData.getName(), newEntityData.getName(),
                    GlobalSettings.getGlobalSettings().getUserName(), operationDescription, 
                    new ReplaceInstancePropertyValueHandler(subject, oldInstanceEntityData, 
                            oldEntityData, newEntityData, getValues()));
        }
    }

    @Override
    //TODO: assume value value type is the same as the property value type, fix later
    protected void addPropertyValue(EntityData subject, String propName, ValueType propValueType,
            EntityData newEntityData, Object oldDisplayValue, String operationDescription) {
        List<EntityData> allowedValues = getProperty().getAllowedValues();
        String type = null;
        if (allowedValues != null && !allowedValues.isEmpty()) {
            type = allowedValues.iterator().next().getName();
        }

        OntologyServiceManager.getInstance().createInstanceValueWithPropertyValue(getProject().getProjectName(), null, type,
        		subject.getName(), propName, new PropertyEntityData(property), newEntityData,
        		GlobalSettings.getGlobalSettings().getUserName(), operationDescription, 
        		new AddInstanceValueWithPropertyValueHandler(subject, newEntityData, getValues()));
    }

    /*
     * Remote calls
     */
    class RemoveInstancePropertyValueHandler extends RemovePropertyValueHandler {

        public RemoveInstancePropertyValueHandler(EntityData subject, 
                EntityData oldEntityData, Collection<EntityData> oldValues) {
            super(subject, oldEntityData, oldValues);
        }

        @Override
        public void handleSuccess(Void result) {
            if (subject.equals(getSubject())) {
                valuesToDisplayValuesMap.remove(oldEntityData);
                super.handleSuccess(result);
            }
        }

    }

    protected class ReplaceInstancePropertyValueHandler extends ReplacePropertyValueHandler {

        protected EntityData changeSubject;
        
        public ReplaceInstancePropertyValueHandler(EntityData subject, EntityData changeSubject,
                EntityData oldEntityData, EntityData newEntityData, Collection<EntityData> oldValues) {
            super(subject, oldEntityData, newEntityData, oldValues);
            this.changeSubject = changeSubject;
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("Success at setting value for " + getProperty().getBrowserText() + " and "
                    + subject.getBrowserText() + " (" + changeSubject.getBrowserText() + ")", null);
            if (subject.equals(getSubject())) {
                valuesToDisplayValuesMap.put(changeSubject, newEntityData);
                displayValues();    //FIXME: either delete or fix the local override, or add parameters to this call
            }
        }
    }

    class AddInstanceValueWithPropertyValueHandler extends AbstractAsyncHandler<EntityData> {

        protected EntityData subject;
        protected EntityData newEntityData;
        protected Collection<EntityData> oldValues;

        public AddInstanceValueWithPropertyValueHandler(EntityData subject, 
                EntityData newEntityData, Collection<EntityData> oldValues) {
            this.subject = subject;
            this.newEntityData = newEntityData;
            this.oldValues = oldValues;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at adding instance property for " + getProperty().getBrowserText() + " and "
                    + subject.getBrowserText(), caught);
            MessageBox.alert("There was an error at adding the instance property value for " + subject.getBrowserText()
                    + ".");
        }

        @Override
        public void handleSuccess(EntityData newInstanceEntityData) {
            if (newInstanceEntityData == null) {
                GWT.log("Success at adding instance property for " + getProperty().getBrowserText() + " and "
                        + subject.getBrowserText(), null);
                return;
            }

            if (subject.equals(getSubject())) {
                valuesToDisplayValuesMap.put(newInstanceEntityData, this.newEntityData);
                //displayValues();    //FIXME: either delete or fix the local override, or add parameters to this call
                
                Collection<EntityData> newValues;
                if (!oldValues.isEmpty()) {
                    newValues = oldValues;
                    newValues.add(newInstanceEntityData);
                }
                else {
                    newValues = new ArrayList<EntityData>();
                    newValues.add(newInstanceEntityData);
                }
                setValues(newValues);
            }
        }
    }

}

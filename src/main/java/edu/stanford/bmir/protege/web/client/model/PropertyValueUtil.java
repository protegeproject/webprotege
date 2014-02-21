package edu.stanford.bmir.protege.web.client.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PropertyValueUtil {

    public void deletePropertyValue(ProjectId projectId, String entityName, String propName, ValueType propValueType,
            String value, UserId userId, String operationDescription, AsyncCallback<Void> asyncCallback) {
        EntityData oldEntityData = new EntityData(value);
        oldEntityData.setValueType(propValueType);
        OntologyServiceManager.getInstance().removePropertyValue(projectId, entityName,
                new PropertyEntityData(propName), oldEntityData, userId, operationDescription,
                new RemovePropertyValueHandler(entityName, propName, asyncCallback));
    }

    //TODO: assume value value type is the same as the property value type, fix later
    public void replacePropertyValue(ProjectId projectId, String entityName, String propName, ValueType propValueType,
            String oldValue, String newValue, UserId userId, String operationDescription,
            AsyncCallback<Void> asyncCallback) {
        EntityData oldEntityData = new EntityData(oldValue);
        oldEntityData.setValueType(propValueType);
        EntityData newEntityData = new EntityData(newValue);
        newEntityData.setValueType(propValueType);
        OntologyServiceManager.getInstance().replacePropertyValue(projectId, entityName,
                new PropertyEntityData(propName), oldEntityData, newEntityData, userId, operationDescription,
                new ReplacePropertyValueHandler(entityName, propName, newEntityData, asyncCallback));
    }

    //TODO: assume value value type is the same as the property value type, fix later
    public void setPropertyValues(ProjectId projectId, String entityName, String propName, ValueType propValueType,
            Collection<String> newValues, UserId userId, String operationDescription,
            AsyncCallback<Void> asyncCallback) {
        ArrayList<EntityData> newEntityData = new ArrayList<EntityData>();
        if (newValues != null && newValues.size() > 0) {
            for (String newValue : newValues) {
                newEntityData.add(new EntityData(newValue));
            }
        }
        else {
            newEntityData = null;
        }
        OntologyServiceManager.getInstance().setPropertyValues(projectId, entityName,
                new PropertyEntityData(propName), newEntityData, userId, operationDescription,
                new SetPropertyValuesHandler(entityName, propName, newEntityData, asyncCallback));
    }

    //TODO: assume value value type is the same as the property value type, fix later
    public void addPropertyValue(ProjectId projectId, String entityName, String propName, ValueType propValueType,
            String newValue, UserId userId, String operationDescription, AsyncCallback<Void> asyncCallback) {
        EntityData newEntityData = new EntityData(newValue);
        newEntityData.setValueType(propValueType);
        OntologyServiceManager.getInstance().addPropertyValue(projectId, entityName,
                new PropertyEntityData(propName), newEntityData, userId, operationDescription,
                new AddPropertyValueHandler(entityName, propName, asyncCallback));
    }

    abstract class AbstractPropertyHandler<T> extends AbstractAsyncHandler<T> {
        private AsyncCallback<T> asyncCallback;
        private String subject;
        private String property;

        public AbstractPropertyHandler(String subject, String property, AsyncCallback<T> asyncCallback) {
            this.asyncCallback = asyncCallback;
            this.subject = subject;
            this.property = property;
        }

        public String getSubject() {
            return subject;
        }

        public String getProperty() {
            return property;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at removing value for " + getProperty() + " and " + getSubject(), caught);
            MessageBox.showAlert("There was an error at removing the property value for " + getProperty() + " and " + getSubject() + ".");
            if (asyncCallback != null) {
                asyncCallback.onFailure(caught);
            }
        }

        @Override
        public void handleSuccess(T result) {
            if (asyncCallback != null) {
                asyncCallback.onSuccess(result);
            }
        }
    }

    /*
     * Remote calls
     */
    class RemovePropertyValueHandler extends AbstractPropertyHandler<Void> {

        public RemovePropertyValueHandler(String subject, String property, AsyncCallback<Void> asyncCallback) {
            super(subject, property, asyncCallback);
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("* Success at removing value for " + getProperty() + " and " + getSubject(), null);
            super.handleSuccess(result);
        }

    }

    class ReplacePropertyValueHandler extends AbstractPropertyHandler<Void> {

        private EntityData newEntityData;

        public ReplacePropertyValueHandler(String subject, String property, EntityData newEntityData,
                AsyncCallback<Void> asyncCallback) {
            super(subject, property, asyncCallback);
            this.newEntityData = newEntityData;
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("Success at replacing value for " + getProperty() + " and " + getSubject() + " with: " + newEntityData, null);
            super.handleSuccess(result);
        }
    }

    class AddPropertyValueHandler extends AbstractPropertyHandler<Void> {

        public AddPropertyValueHandler(String subject, String property, AsyncCallback<Void> asyncCallback) {
            super(subject, property, asyncCallback);
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("Success at adding value for " + getProperty() + " and " + getSubject(), null);
            super.handleSuccess(result);
        }
    }

    class SetPropertyValuesHandler extends AbstractPropertyHandler<Void> {

        private List<EntityData> newEntityData;

        public SetPropertyValuesHandler(String subject, String property, List<EntityData> newEntityData, AsyncCallback<Void> asyncCallback) {
            super(subject, property, asyncCallback);
            this.newEntityData = newEntityData;
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("Success at setting value for " + getProperty() + " and " + getSubject() + " to: " + newEntityData, null);
            super.handleSuccess(result);
        }
    }

}

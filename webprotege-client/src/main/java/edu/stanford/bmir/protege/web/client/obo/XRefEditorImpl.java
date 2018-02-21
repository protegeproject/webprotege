package edu.stanford.bmir.protege.web.client.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/06/2013
 */
public class XRefEditorImpl extends Composite implements XRefEditor {

    interface XRefEditorImplUiBinder extends UiBinder<HTMLPanel, XRefEditorImpl> {

    }

    private static XRefEditorImplUiBinder ourUiBinder = GWT.create(XRefEditorImplUiBinder.class);


    @UiField
    protected TextBoxBase databaseNameField;

    @UiField
    protected TextBoxBase databaseIdField;

    @UiField
    protected TextBoxBase descriptionField;


    private boolean dirty = false;


    public XRefEditorImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @UiHandler("databaseNameField")
    protected void handleDatabaseNameFieldChanged(ValueChangeEvent<String> event) {
        dirty = true;
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }


    @UiHandler("databaseIdField")
    protected void handleDatabaseIdFieldChanged(ValueChangeEvent<String> event) {
        dirty = true;
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }


    @UiHandler("descriptionField")
    protected void handleDescriptionFieldChanged(ValueChangeEvent<String> event) {
        dirty = true;
        fireEvent(new DirtyChangedEvent());
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public boolean isWellFormed() {
        return (getDatabaseName().isEmpty() && getDatabaseId().isEmpty() && getDescription().isEmpty()) || (!getDatabaseName().isEmpty() && !getDatabaseId().isEmpty());
    }

    @Override
    public void setValue(OBOXRef object) {
        databaseNameField.setText(object.getDatabaseName());
        databaseIdField.setText(object.getDatabaseId());
        descriptionField.setText(object.getDescription());
        dirty = false;
    }

    @Override
    public void clearValue() {
        databaseNameField.setText("");
        databaseIdField.setText("");
        descriptionField.setText("");
    }

    @Override
    public Optional<OBOXRef> getValue() {
        String databaseName = getDatabaseName();
        if(databaseName.isEmpty()) {
            return Optional.empty();
        }
        String databaseId = getDatabaseId();
        if(databaseId.isEmpty()) {
            return Optional.empty();
        }
        String description = getDescription();
        return Optional.of(new OBOXRef(databaseName, databaseId, description));
    }


    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OBOXRef>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }



    private String getDescription() {
        return descriptionField.getValue().trim();
    }

    private String getDatabaseId() {
        return databaseIdField.getValue().trim();
    }

    private String getDatabaseName() {
        return databaseNameField.getValue().trim();
    }
}
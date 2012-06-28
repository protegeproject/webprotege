package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.model.PropertyValueUtil;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractPropertyWidgetWithNotes;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public abstract class AbstractFieldWidget extends AbstractPropertyWidgetWithNotes {

    protected static final String LABEL_SEPARATOR = "";
    protected static final String DELETE_ICON_STYLE_STRING = "style=\"position:relative; top:4px;\"";

    private Panel wrappingPanel;
    private Field field;

    private Anchor deleteLink;
    private Anchor commentLink;

    private Collection<EntityData> values;
    protected PropertyValueUtil propertyValueUtil;
    protected List<String> allowedValueNames;

    public AbstractFieldWidget(Project project) {
        super(project);
        propertyValueUtil = new PropertyValueUtil();
    }


    @Override
    public Component createComponent() {
        wrappingPanel = new Panel();
        wrappingPanel.setPaddings(5);
        wrappingPanel.setLayout(new ColumnLayout());

        field = createField();
        deleteLink = createDeleteHyperlink();
        commentLink = createCommentHyperLink();

        FormPanel formPanel = new FormPanel();
        formPanel.add(field, new AnchorLayoutData("100%"));

        wrappingPanel.add(formPanel, new ColumnLayoutData(1));

        Collection<Widget> suffixComponents = createSuffixComponents();
        for (Widget widget : suffixComponents) {
            wrappingPanel.add(widget);
        }

        wrappingPanel.add(deleteLink);
        wrappingPanel.add(commentLink);
        return wrappingPanel;
    }

    protected AbstractFieldWidget(Project project, List<String> allowedValueNames) {
        this(project);
        //FIXME This is a temporary hack. Get rid of the second argument and the field
        this.allowedValueNames = allowedValueNames;
    }

    protected Collection<Widget> createSuffixComponents() { //TODO: might need better name
        return new ArrayList<Widget>();
    }

    protected Anchor createDeleteHyperlink() {
        deleteLink = new Anchor("&nbsp<img src=\"images/delete.png\" " + AbstractFieldWidget.DELETE_ICON_STYLE_STRING + "></img>", true);
        deleteLink.setWidth("22px");
        deleteLink.setHeight("22px");
        deleteLink.setTitle("Delete this value");
        deleteLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            	//we have to check the read-only status at the time of the mouse click,
            	//because at the time of the widget creation (and initialization) we don't
            	//know the value of the read_only property (it will be set only later in the setup method)
            	if (isReadOnly() || isDisabled()) {
            		deleteLink.setTitle("Delete operation is not allowed!");
            		return;
            	}
                if (isWriteOperationAllowed()) {
                    MessageBox.confirm("Confirm", "Are you sure you want to delete this value?",
                        new MessageBox.ConfirmCallback() {
                            public void execute(String btnID) {
                                if (btnID.equals("yes")) {
                                    deletePropertyValue(getSubject(), getProperty().getName(),
                                            getProperty().getValueType(), UIUtil.getFirstItem(values),
                                            field.getRawValue(), getDeleteValueOperationDescription());
                                }
                            }
                        });
                }
            }
        });
        return deleteLink;
    }

	@Override
    public void refresh() {
	    //TODO activate this and test it after the server side will provide the local annotation count for all instance values
	    //commentLink = createCommentHyperLink();
		wrappingPanel.doLayout();
	}


    //TODO - check if this is ICD specific
    protected String getDeleteValueOperationDescription() {
        String deletedValueDesc = new String("(");
        for (EntityData value : values) {
            String valueStr = UIUtil.getDisplayText(value);
            deletedValueDesc = deletedValueDesc + (field == null ? "empty" : valueStr) + ", ";
        }
        deletedValueDesc = deletedValueDesc.substring(0, deletedValueDesc.length() - 2);
        deletedValueDesc = deletedValueDesc + ")";

        return UIUtil.getAppliedToTransactionString("Deleted " + UIUtil.getShortName(getProperty().getBrowserText())
                + " from " + getSubject().getBrowserText() + ". Deleted value: "
                + (values == null || values.toString().length() == 0 ? "(empty)" : deletedValueDesc), getSubject()
                .getName());
    }

    protected Field createField() {
        field = createFieldComponent();
        attachFieldChangeListener();
        return field;
    }

    protected void attachFieldChangeListener() {
        field.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onChange(Field field, Object newVal, Object oldVal) {
                onChangeValue(getSubject(), oldVal, newVal);
            }
        });
    }

    protected void onChangeValue(EntityData subj, Object oldVal, Object newVal) {
        if (!isWriteOperationAllowed()) {
            displayValues();
            return;
        }
        GWT.log("on change before invoke " + field.getFieldLabel(), null);

        //fix oldVal if it was the display text of a collection of more than 1 values
        //      (i.e. if it contained '{' or other special characters)
        if (values != null && values.size() > 1 &&
                UIUtil.getDisplayText(values).equals(oldVal)) {
            oldVal = UIUtil.getFirstItem(values);
        }

        if (UIUtil.getDisplayText(oldVal).length() == 0) {
            if (UIUtil.getDisplayText(newVal).length() > 0) {
                EntityData newValEntityData = convertToEntityData(newVal);
                addPropertyValue(subj, getProperty().getName(), getProperty().getValueType(),
                        newValEntityData, oldVal, getAddValueOperationDescription(subj, newValEntityData));
            }
        }
        else if (UIUtil.getDisplayText(newVal).length() == 0) {
            EntityData oldValEntityData = convertToEntityData(oldVal);
            deletePropertyValue(subj, getProperty().getName(), getProperty().getValueType(),
                    oldValEntityData, oldVal, getDeleteValueOperationDescription());
        }
        else {
            EntityData oldValEntityData = convertToEntityData(oldVal);
            EntityData newValEntityData = convertToEntityData(newVal);

            replacePropertyValue(subj, getProperty().getName(), getProperty().getValueType(),
                    oldValEntityData, newValEntityData, oldVal,
                    getReplaceValueOperationDescription(subj,
                            oldValEntityData, newValEntityData));
        }
    }


    protected EntityData convertToEntityData(Object fieldValue) {
        if (fieldValue instanceof EntityData) {
            return (EntityData) fieldValue;
        }

        if (fieldValue instanceof Collection<?>) {
            Collection<?> collectionFieldValue = (Collection<?>)fieldValue;
            if (collectionFieldValue.isEmpty()) {
                return convertToEntityData(null);
            }
            else {
                return convertToEntityData(collectionFieldValue.iterator().next());
            }
        }

        return new EntityData(UIUtil.getDisplayText(fieldValue));
    }


    //TODO - check if this is ICD specific
    protected String getAddValueOperationDescription(EntityData subject, EntityData newVal) {
        return UIUtil.getAppliedToTransactionString("Added a new "
                + UIUtil.getDisplayText(getProperty()) + " to " + UIUtil.getDisplayText(subject)  +
                ". Added value: " + UIUtil.getDisplayText(newVal),
                subject.getName());
    }

    //TODO - check if this is ICD specific
    protected String getReplaceValueOperationDescription(EntityData subject, Object oldValue, Object newValue) {
        return UIUtil.getAppliedToTransactionString("Replaced " + UIUtil.getDisplayText(getProperty())
                + " of " + UIUtil.getDisplayText(subject) + ". Old value: "
                + (oldValue == null || oldValue.toString().length() == 0 ? "(empty)" : UIUtil.getDisplayText(oldValue)) + " "
                + ". New value: " + (newValue == null || newValue.toString().length() == 0 ? "(empty)" : UIUtil.getDisplayText(newValue)),
                subject.getName());
    }

    protected Anchor createCommentHyperLink() {
        String text = "<img src=\"images/comment.gif\" title=\""
                + "Add a comment on this value\" " + AbstractPropertyWidgetWithNotes.COMMENT_ICON_STYLE_STRING + "></img>";
        EntityData value = UIUtil.getFirstItem(values);
        int annotationsCount = (value == null ? 0 : value.getLocalAnnotationsCount());
        if (annotationsCount > 0) {
            text = "<img src=\"images/comment.gif\" title=\""
                    + UIUtil.getNiceNoteCountText(annotationsCount)
                    + " on this value. \nClick on the icon to see existing or to add new note(s).\" " + AbstractPropertyWidgetWithNotes.COMMENT_ICON_STYLE_STRING + "></img>"
                    + "<span style=\"vertical-align:super;font-size:95%;color:#15428B;font-weight:bold;\">"
                    + "&nbsp;" + annotationsCount + "</span>";
        }
        commentLink = new Anchor(text, true);
        commentLink.setWidth("40px");
        commentLink.setHeight("22px");
        commentLink.addClickHandler(new ClickHandler() {
    		double timeOfLastClick = 0;

            public void onClick(ClickEvent event) {
				double eventTime = new Date().getTime(); //take current time since there is no way to get time from a ClickEvent
				if (eventTime - timeOfLastClick > 500) { //not the second click in a double click
					onCellClickOrDblClick(event);
        		};

        		//Set new value for timeOfLastClick the time the last click was handled.
        		//We use the current time (and not eventTime), because some time may have passed since eventTime
        		//while executing the onCellClickOrDblClick method.
        		timeOfLastClick = new Date().getTime();
        	}

            private void onCellClickOrDblClick(ClickEvent event) {
                if (UIUtil.confirmIsLoggedIn()) {
                    onEditNotes();
                }
            }
        });
        return commentLink;
    }

    protected void onEditNotes() {
//        // TODO Auto-generated method stub
//        super.onEditNotes(value);
        String annotEntityName = null;
        PropertyEntityData property = getProperty();
        if (property.getValueType() == ValueType.Instance) {
            //FIXME This solution will not work when multiple property values are specified:
            //  it will add the comment always to the first instance.
            //  This method should be generalized OR overwritten in subclasses that can deal
            //  with multiple property values.
            EntityData firstValue = UIUtil.getFirstItem(getValues());
            if (firstValue != null) {
                annotEntityName = firstValue.getName();
            }
            onEditNotes(annotEntityName);
        }
        else {
            //TODO this is a hack. Fix it when the Changes API will provide
            // ways to add notes to property values
            // Until then we add the notes to the selected class itself
            annotEntityName = getSubject().getName();
            onEditNotes(annotEntityName, "[" + AbstractFieldWidget.this.getProperty().getBrowserText() + "] ", "");
        }
    }

    protected Field createFieldComponent() {
        TextField textField = new TextField();
        return textField;
    }

    public void setLabel(String label, String helpURL, String tooltip) {
       //FIXME: workarounfd! Field should never be null!!
        if (field != null) {
            field.setLabel(getLabelHtml(label, helpURL, tooltip));
            field.setLabelSeparator(AbstractFieldWidget.LABEL_SEPARATOR);
        }

    }

    @Override
    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        super.setup(widgetConfiguration, propertyEntityData);
        String tooltip = getTooltipText();
		getField().setTitle(tooltip);

        String label = UIUtil.getStringConfigurationProperty(widgetConfiguration, FormConstants.LABEL, "");
        setLabel(label != null ? label : (propertyEntityData != null ? propertyEntityData.getBrowserText() : ""), getHelpURL(), tooltip );

        //If the property does not have a browserText, use the label of the field. This is experimental to see if it has the desirable behavior.
        getProperty().setBrowserText(label);

        String width = UIUtil.getStringConfigurationProperty(widgetConfiguration, FormConstants.WIDTH, null);
        if (width != null) {
        	field.setWidth(width);
        }
        String height = UIUtil.getStringConfigurationProperty(widgetConfiguration, FormConstants.HEIGHT, null);
        if (height != null) {
        	field.setHeight(height);
        }

        boolean readOnly = isReadOnly();
        getField().setReadOnly(readOnly);
        boolean disabled = isDisabled();
        getField().setDisabled(disabled);

        if (readOnly || disabled) {
            deleteLink.setHTML("&nbsp<img src=\"images/delete_grey.png\" " + AbstractFieldWidget.DELETE_ICON_STYLE_STRING + "></img>");
            deleteLink.setTitle("Delete value is not allowed");
        }
    }

    @Override
    public Collection<EntityData> getValues() {
        return values;
    }

    @Override
    public void setValues(Collection<EntityData> values) {
        this.values = values != null ? values : new ArrayList<EntityData>();
        displayValues();
    }

    protected void displayValues() {
        displayValues(values);
    }

    protected void displayValues(Collection<EntityData> values) {
        if (values == null || values.size() == 0) {
            field.setValue("");
        } else {
            if (values.size() == 1) {
                EntityData value = values.iterator().next();
                String displayText = UIUtil.getDisplayText(value);
                field.setValue(displayText);
            } else {
                field.setValue(UIUtil.prettyPrintList(values));
            }
        }
    }

    public Field getField() {
        return field;
    }

    @Override
    public Component getComponent() {
        return getWrappingPanel();
    }

    public Panel getWrappingPanel() {
        return wrappingPanel;
    }

    protected void deletePropertyValue(EntityData subject, String propName, ValueType propValueType,
            EntityData oldEntityData, Object oldDisplayedValue, String operationDescription) {
        propertyValueUtil.deletePropertyValue(getProject().getProjectName(), subject.getName(), propName, propValueType,
                oldEntityData.getName(), GlobalSettings.getGlobalSettings().getUserName(), operationDescription,
                new RemovePropertyValueHandler(subject, oldEntityData, values));
    }

    protected void replacePropertyValue(EntityData subject, String propName, ValueType propValueType,
            EntityData oldEntityData, EntityData newEntityData, Object oldDisplayedValue, String operationDescription) {
        propertyValueUtil.replacePropertyValue(getProject().getProjectName(), subject.getName(), propName, propValueType,
                oldEntityData.getName(), newEntityData.getName(), GlobalSettings.getGlobalSettings().getUserName(),
                operationDescription, new ReplacePropertyValueHandler(subject, oldEntityData, newEntityData, values));
    }

    //TODO: assume value value type is the same as the property value type, fix later
    protected void addPropertyValue(EntityData subject, String propName, ValueType propValueType,
            EntityData newEntityData, Object oldDisplayedValue, String operationDescription) {
        propertyValueUtil.addPropertyValue(getProject().getProjectName(), subject.getName(), propName, propValueType,
                newEntityData.getName(), GlobalSettings.getGlobalSettings().getUserName(), operationDescription,
                new AddPropertyValueHandler(subject, newEntityData, values));
    }

    /*
     * Remote calls
     */
    class RemovePropertyValueHandler extends AbstractAsyncHandler<Void> {

        protected EntityData subject;
        protected EntityData oldEntityData;
        protected Collection<EntityData> oldValues;

        public RemovePropertyValueHandler(EntityData subject, EntityData oldEntityData, Collection<EntityData> oldValues) {
            this.subject = subject;
            this.oldEntityData = oldEntityData;
            this.oldValues = oldValues;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at removing value for " + getProperty().getBrowserText() + " and "
                    + subject.getBrowserText(), caught);
            Window.alert("There was an error at removing the property value for " + getProperty().getBrowserText()
                    + " and " + subject.getBrowserText() + ".");
            if (subject.equals(getSubject())) {
                setValues(oldValues);
            }
        }

        @Override
        public void handleSuccess(Void result) {
            if (subject.equals(getSubject())) {
                Collection<EntityData> newValues;
                if (oldValues != null && oldValues.contains(oldEntityData)) {
                    newValues = oldValues;
                    newValues.remove(oldEntityData);
                }
                else {
                    newValues = null;
                }
                setValues(newValues);
            }
        }

    }

    protected class ReplacePropertyValueHandler extends AbstractAsyncHandler<Void> {

        protected EntityData subject;
        protected EntityData oldEntityData;
        protected EntityData newEntityData;
        protected Collection<EntityData> oldValues;

        public ReplacePropertyValueHandler(EntityData subject,
                EntityData oldEntityData, EntityData newEntityData, Collection<EntityData> oldValues) {
            this.subject = subject;
            this.oldEntityData = oldEntityData;
            this.newEntityData = newEntityData;
            this.oldValues = oldValues;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at replace property for " + getProperty().getBrowserText() + " and "
                    + subject.getBrowserText(), caught);
            Window.alert("There was an error at setting the property value for " + subject.getBrowserText() + ".");
            if (subject.equals(getSubject())) {
                setValues(oldValues);
            }
        }

        @Override
        public void handleSuccess(Void result) {
            //FIXME: we need a reload method
            if (subject.equals(getSubject())) {
                Collection<EntityData> newValues;
                if (oldValues != null && oldValues.contains(oldEntityData)) {
                    newValues = oldValues;
                    newValues.remove(oldEntityData);
                    newValues.add(newEntityData);
                }
                else {
                    newValues = new ArrayList<EntityData>();
                    newValues.add(newEntityData);
                }
                setValues(newValues);
            }
        }
    }

    class AddPropertyValueHandler extends AbstractAsyncHandler<Void> {

        protected EntityData subject;
        protected EntityData newEntityData;
        protected Collection<EntityData> oldValues;

        public AddPropertyValueHandler(EntityData subject,
                EntityData newEntityData, Collection<EntityData> oldValues) {
            this.subject = subject;
            this.newEntityData = newEntityData;
            this.oldValues = oldValues;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at add property for " + getProperty().getBrowserText() + " and "
                    + subject.getBrowserText(), caught);
            Window.alert("There was an error at adding the property value for " + subject.getBrowserText() + ".");
            if (subject.equals(getSubject())) {
                setValues(oldValues);
            }
        }

        @Override
        public void handleSuccess(Void result) {
            //FIXME: we need a reload method
            if (subject.equals(getSubject())) {
                Collection<EntityData> newValues;
                if (oldValues != null && !oldValues.isEmpty()) {
                    newValues = oldValues;
                    newValues.add(newEntityData);
                }
                else {
                    newValues = new ArrayList<EntityData>();
                    newValues.add(newEntityData);
                }
                setValues(newValues);
            }
        }
    }

}

package edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.HierarchyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.client.ui.util.ClassSelectionField;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.client.ui.util.field.TextAreaField;

public class RetireClassPanel extends FormPanel implements Selectable {

    private Project project;
    private ClassSelectionField retiredClassesField;
    private ClassSelectionField newParentField;
    private Checkbox retireChildrenCheckbox;
    private TextAreaField reasonField;
    private String topClass;


    public RetireClassPanel(Project project) {
        this.project = project;
        buildUI();
    }

    private void buildUI() {
        setHeight(400);

        Label title = new Label("RETIRE CLASS");
        title.setStylePrimaryName("hierarchy-title");
        add(title, new AnchorLayoutData("100% - 53"));

        HTML explanationHtml = new HTML("Please select one or several <b>classes to retire</b> by clicking on " +
        		"the &nbsp <img src=\"../images/add.png\"></img> &nbsp icon in the <i>Classes to retire</i> field.<br />" +
        		"If the retired class has children, you have the option <b>to retire also all the children</b>, or <b>to move the children under a different parent</b>. " +
        		"Retired classes are not displayed in the class hierarchy.<br />" +
        		"Operations are performed after clicking on the <i>Retire</i> button.");
        explanationHtml.setStylePrimaryName("explanation");
        add(explanationHtml);

        retiredClassesField = new ClassSelectionField(project, "Class(es) to retire");
        add(retiredClassesField, new AnchorLayoutData("100% - 53"));

        retireChildrenCheckbox = new Checkbox("Retire also all children of the selected class(es).");
        add(retireChildrenCheckbox, new AnchorLayoutData("100% - 53"));
        retireChildrenCheckbox.addListener(new CheckboxListenerAdapter() {
            @Override
            public void onCheck(Checkbox field, boolean checked) {
                newParentField.setDisabled(checked);
            }
        });

        newParentField = new ClassSelectionField(project, "New parent class of the children", false, null);
        add(newParentField, new AnchorLayoutData("100% - 53"));

        reasonField = new TextAreaField();
        reasonField.setLabel("Reason for change:");
        ((TextArea)reasonField.getFieldComponent()).setHeight(120);
        add(reasonField, new AnchorLayoutData("100% - 53"));

        Button retireButton = new Button("Retire");
        retireButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                if (project.hasWritePermission()) {
                    onRetire();
                } else {
                    MessageBox.alert("No permission", "You do not have enough privileges to retire classes.");
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                clear();
                destroy();

            }
        });

        addButton(retireButton);
        addButton(cancelButton);

    }


    public void setParentsClses(Collection<EntityData> parents) {
        retiredClassesField.setClsValues(parents);
    }

    private void onRetire() {
        Collection<EntityData> classesToRetire = retiredClassesField.getClsValues();
        EntityData newParent = newParentField.getClsValue();
        final String reasonForChange = reasonField.getValueAsString();
        boolean retireChildren = retireChildrenCheckbox.getValue();

        if (classesToRetire == null || classesToRetire.size() == 0) {
            MessageBox.alert("No classes to retire selected", "No class to retire was selected." +
                    "<br />Please select at least one class to retire in the <i>Classes to retire</i> field.");
            return;
        }

        for (EntityData classToRetire : classesToRetire) {
            String clsName = classToRetire == null ? null : classToRetire.getName();
            if (clsName != null && clsName.equals(topClass)) {
                MessageBox.alert("Operation not allowed", "It is not allowed to retire the top level class." +
                        "<br />Please select classes other than the top level class in the <i>Class(es) to retire</i> field.");
                return;
            }
        }

        if (reasonForChange == null || reasonForChange.length() == 0) {
            MessageBox.alert("No reason for change", "A reason for the change was not provided.<br />" +
                    "Please fill in the <i>Reason for change</i> field.");
            return;
        }

        if (!retireChildren && newParent == null) {
            MessageBox.alert("No parent for children", "You have selected the option to move children of retired class(es) under a new parent,<br />" +
            		"but no parent was selected. Please select a parent.");
            return;
        }

        MessageBox.confirm("Retire class(es)?", getRetireConfirmMessage(), new ConfirmCallback() {
            public void execute(String btnID) {
                if (btnID.equalsIgnoreCase("yes")) {
                    UIUtil.mask(RetireClassPanel.this.getEl(), "Performing retire...", true, 10);
                    performRetire();
                }
            }
        });
    }

    private String getRetireConfirmMessage() {
        String message = "The following class(es) will be retired:<br />";

        Collection<EntityData> classesToRetire = retiredClassesField.getClsValues();
        for (EntityData entityData : classesToRetire) {
            message = message + entityData.getBrowserText();
        }

        message = message + "<br /><br />";

        if (retireChildrenCheckbox.getValue()) {
            message = message + "All children of retired classes will also be retired.<br />";
        } else {
            message = message + "Children of retired classes will be moved under a new parent: " + UIUtil.getDisplayText(newParentField.getClsValue());
        }

        message += "<br /><b>Are you sure you want to perform these operations?</b>";

        return message;
    }

    private void performRetire() {
        String newParent = newParentField.getClsValue() == null ? null : newParentField.getClsValue().getName();
        HierarchyServiceManager.getInstance().retireClasses(project.getProjectName(), UIUtil.getStringCollection(retiredClassesField.getClsValues()),
                retireChildrenCheckbox.getValue(), newParent, reasonField.getValueAsString(), getOperationDescription(),
                GlobalSettings.getGlobalSettings().getUserName(), new RetireHandler());
    }

    private String getOperationDescription() {
        return "Retire classes: " + UIUtil.prettyPrintList(retiredClassesField.getClsValues()) + ". " +
        (retireChildrenCheckbox.getValue() ?
                "Children of retired classes are also retired." :
                    "Children of retired classes are moved under new parent: " + UIUtil.getDisplayText(newParentField.getClsValue()));
    }

    public String getTopClass() {
        return topClass;
    }

    public void setTopClass(String topClass) {
        this.topClass = topClass;
        retiredClassesField.setTopClass(topClass);
        newParentField.setTopClass(topClass);
    }

    protected void refreshFromServer() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                project.forceGetEvents();
            }
        };
        timer.schedule(500);
    }

    public void addSelectionListener(SelectionListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Collection<EntityData> getSelection() {
        return retiredClassesField.getClsValues();
    }

    public void notifySelectionListeners(SelectionEvent selectionEvent) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void removeSelectionListener(SelectionListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setSelection(Collection<EntityData> selection) {
        retiredClassesField.setClsValues(selection);
    }

    /*
     * Remote calls
     */

    class RetireHandler extends AbstractAsyncHandler<Void> {

        @Override
        public void handleFailure(Throwable caught) {
            getEl().unmask();
            GWT.log("Error at retiring classes " + retiredClassesField.getClsValues(), caught);
            MessageBox.alert("Error at retire", "There was an error at performing the retire classes operation.<br />" +
                    "Message: " + caught.getMessage());
        }

        @Override
        public void handleSuccess(Void result) {
            getEl().unmask();
            MessageBox.alert("Success", "Retire classes operation performed successfully.");
            retiredClassesField.setClsValues(new ArrayList<EntityData>());
            refreshFromServer();
        }

    }

}

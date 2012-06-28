package edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
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

public class ChangeParentPanel extends FormPanel implements Selectable {

    private Project project;
    private ClassSelectionField classField;
    private ParentsPanel parentsPanel;
    private SelectionListener selectionListener;
    //hack for IE 6, which does not show the text area
    private TextAreaField reasonField;
    private String topClass;


    public ChangeParentPanel(Project project) {
        this.project = project;
        buildUI();
    }

    private void buildUI() {
        setHeight(430);

        Label title = new Label("CHANGE PARENTS (add or remove parents, move in hierarchy)");
        title.setStylePrimaryName("hierarchy-title");
        add(title, new AnchorLayoutData("100% - 53"));

        HTML explanationHtml = new HTML("Please select a class that you want to move in the hierarchy.<br />" +
                "Then <b>add</b> or <b>remove parents</b> in the <i>Parents</i> field.<br />" +
                "Operations are performed only after clicking on the <i>Move in hierarchy</i> button." );
        explanationHtml.setStylePrimaryName("explanation");
        add(explanationHtml);

        classField = new ClassSelectionField(project, "Class to move in hierarchy", false, topClass);
        add(classField, new AnchorLayoutData("100% - 53"));
        classField.addSelectionListener(selectionListener = new SelectionListener() {
            public void selectionChanged(SelectionEvent event) {
                parentsPanel.setClsEntity(UIUtil.getFirstItem(classField.getClsValues()));
            }
        });


        parentsPanel = new ParentsPanel(project);
        add(parentsPanel,  new AnchorLayoutData("100% - 53"));

        reasonField = new TextAreaField();
        reasonField.setLabel("Reason for change:");
        ((TextArea)reasonField.getFieldComponent()).setHeight(120);
        add(reasonField, new AnchorLayoutData("100% - 53"));

        Button createButton = new Button("Move in hierarchy");
        createButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                if (UIUtil.confirmOperationAllowed(project)) {
                    onMove();
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

        addButton(createButton);
        addButton(cancelButton);

    }


    public void setParentsClses(Collection<EntityData> parents) {
        classField.setClsValues(parents);
        parentsPanel.setClsEntity(UIUtil.getFirstItem(parents));
        doLayout();
    }

    private void onMove() {
        final Set<EntityData> parentsToAdd = parentsPanel.getParentsToAdd();
        final Set<EntityData> parentsToRemove = parentsPanel.getParentsToRemove();
        EntityData clsValue = classField.getClsValue();
        final String clsName = clsValue == null ? null : clsValue.getName();
        final String reasonForChange = reasonField.getValueAsString();

        if (clsName == null) {
            MessageBox.alert("No class selected", "No class to move in hierarchy is selected." +
            		"<br />Please select a class in the <i>Class to move in hierarchy</i> field.");
            return;
        }

        if (clsName.equals(topClass)) {
            MessageBox.alert("Operation not allowed", "It is not allowed to move the top level class in the hierarchy." +
                    "<br />Please select another class in the <i>Class to move in hierarchy</i> field.");
            return;
        }

        if (reasonForChange == null || reasonForChange.length() == 0) {
            MessageBox.alert("No reason for change", "A reason for the change was not provided.<br />" +
            		"Please fill in the <i>Reason for change</i> field.");
            return;
        }

        if (parentsToAdd.size() == 0 && parentsToRemove.size() == 0) {
            MessageBox.alert("No changes", "No changes detected. No operation will be performed.");
            return;
        }

        if (parentsPanel.getFinalParents().size() == 0) {
            MessageBox.alert("No parents","Class " + UIUtil.getDisplayText(clsValue) + " has no parents. Please add at least on parent.");
            return;
        }


        MessageBox.confirm("Confirm", getMoveConfirmMessage(), new ConfirmCallback() {
            public void execute(String btnID) {
                if (btnID.equalsIgnoreCase("yes")) {
                    UIUtil.mask(ChangeParentPanel.this.getEl(), "Performing change in hierarchy", true, 10);
                    performMove(clsName, UIUtil.getStringCollection(parentsToAdd),
                            UIUtil.getStringCollection(parentsToRemove), reasonForChange);
                }
            }
        });
    }


    private String getMoveConfirmMessage() {
        Set<EntityData> parentsToAdd = parentsPanel.getParentsToAdd();
        Set<EntityData> parentsToRemove = parentsPanel.getParentsToRemove();

        String message = "The following operations will be performed on <b>" +
            UIUtil.getDisplayText(classField.getClsValue()) + "</b>:<br /><br />";

        if (parentsToAdd.size() > 0) {
            message = message + "<b>Parents added: </b><br />";
            for (EntityData parent : parentsToAdd) {
                message += parent.getBrowserText() + "<br />";
            }
        }

        if (parentsToRemove.size() > 0) {
            message = message + "<br /><b>Parents removed:</b><br /> ";
            for (EntityData parent : parentsToRemove) {
                message += parent.getBrowserText() + "<br />";
            }
        }

        message += "<br /><b>Are you sure you want to perform these operations?</b>";
        return message;
    }

    protected void performMove(String clsName, Collection<String> parentsToAdd, Collection<String> parentsToRemove, String reasonForChange) {
        HierarchyServiceManager.getInstance().changeParent(project.getProjectName(), clsName, parentsToAdd, parentsToRemove,
                GlobalSettings.getGlobalSettings().getUserName(), UIUtil.getAppliedToTransactionString(getOperationDescription(), clsName),
                reasonForChange, new MoveHandler());
    }

    protected String getOperationDescription() {
        Set<EntityData> parentsToAdd = parentsPanel.getParentsToAdd();
        Set<EntityData> parentsToRemove = parentsPanel.getParentsToRemove();

        String message = "Change in hierarchy for class: " + UIUtil.getDisplayText(classField.getClsValue()) + "." +
        		(parentsToAdd.size() == 0 ? "" : " Parents added: (" + UIUtil.prettyPrintList(parentsToAdd) + ").") +
        		(parentsToRemove.size() == 0 ? "" : " Parents removed: (" + UIUtil.prettyPrintList(parentsToRemove) + ").");

        return message;
    }

    public void setTopClass(String topClass) {
        this.topClass = topClass;
        classField.setTopClass(topClass);
        parentsPanel.setTopClass(topClass);
    }

    public String getTopClass() {
        return topClass;
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

    @Override
    protected void onDestroy() {
        classField.removeSelectionListener(selectionListener);
        super.onDestroy();
    }

    public void addSelectionListener(SelectionListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Collection<EntityData> getSelection() {
        return classField.getClsValues();
    }

    public void notifySelectionListeners(SelectionEvent selectionEvent) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void removeSelectionListener(SelectionListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setSelection(Collection<EntityData> selection) {
        classField.setClsValues(selection);
    }

    /*
     * Remote methods
     */

    class MoveHandler extends AbstractAsyncHandler<List<EntityData>> {

        @Override
        public void handleFailure(Throwable caught) {
            getEl().unmask();
            GWT.log("Error at move in hierarchy for class " + classField.getClsValue(), caught);
            MessageBox.alert("Error", "There was an error at performing the change parents operation.<br />" +
            		"Message: " + caught.getMessage());
        }

        @Override
        public void handleSuccess(List<EntityData> result) {
            getEl().unmask();
            if (result == null) {
                MessageBox.alert("Success", "Change in hierarchy operation performed successfully.");
            }
            else {
                String warningMsg = "<B>WARNING! There is a cycle in the hierarchy: </B><BR><BR>";
                for (EntityData p : result) {
                    warningMsg += "&nbsp;&nbsp;&nbsp;&nbsp;" + p.getBrowserText() + "<BR>";
                }
                warningMsg += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...";
                MessageBox.alert("Warning", "Change in hierarchy operation performed successfully.<BR>" +
                		"<BR>" +
                		warningMsg);
            }
            parentsPanel.setClsEntity(classField.getClsValue());
            refreshFromServer();
        }

    }


}

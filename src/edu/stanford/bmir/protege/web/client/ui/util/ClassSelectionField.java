package edu.stanford.bmir.protege.web.client.ui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TriggerField;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;


public class ClassSelectionField extends TriggerField implements Selectable {

    private Project project;
    private Collection<EntityData> values = new HashSet<EntityData>();
    private Window selectWindow;
    private boolean allowMultipleSelection;
    private ClassTreePortlet classTreePortlet;
    private String topClass;

    private Collection<SelectionListener> listeners = new ArrayList<SelectionListener>();


    public ClassSelectionField(Project project, String label) {
        this(project, label, true, null);
    }

    public ClassSelectionField(Project project, String label, boolean allowMultipleSelection, String topClass) {
        super();
        this.project = project;
        this.allowMultipleSelection = allowMultipleSelection;
        this.topClass = topClass;

        setLabel(label);
        setReadOnly(true);
        setTitle("Select parent. Multiple selection works");

        setTriggerClass("select-link");
    }

    public void setClsValues(Collection<EntityData> newValues) {
        this.values = newValues;
        super.setValue(values == null ? "" : UIUtil.prettyPrintList(values));
        notifySelectionListeners(new SelectionEvent(this));
    }

    public Collection<EntityData> getClsValues() {
        return values;
    }

    public EntityData getClsValue() {
        return UIUtil.getFirstItem(values);
    }

    public ClassTreePortlet getSelectable() {
        if (classTreePortlet == null) {
            classTreePortlet = createSelectable();
        }
        return classTreePortlet;
    }

    protected ClassTreePortlet createSelectable() {
        ClassTreePortlet treePortlet = new ClassTreePortlet(project, false, false, false, allowMultipleSelection, topClass);
        treePortlet.setDraggable(false);
        treePortlet.setClosable(false);
        treePortlet.setCollapsible(false);
        treePortlet.setHeight(300);
        treePortlet.setWidth(450);

        return treePortlet;
    }

    protected void onSelectEntity() {
        if (selectWindow == null) {
            selectWindow = new com.gwtext.client.widgets.Window();
            selectWindow.setTitle(getFieldLabel());
            selectWindow.setWidth(600);
            selectWindow.setHeight(480);
            selectWindow.setMinWidth(300);
            selectWindow.setMinHeight(350);
            selectWindow.setLayout(new FitLayout());
            selectWindow.setPaddings(5);
            selectWindow.setButtonAlign(Position.CENTER);

            //window.setCloseAction(Window.HIDE);
            selectWindow.setPlain(true);

            com.gwtext.client.widgets.Button cancelButton = new com.gwtext.client.widgets.Button("Cancel");
            cancelButton.addListener(new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    selectWindow.hide();
                }
            });

            com.gwtext.client.widgets.Button selectButton = new com.gwtext.client.widgets.Button("Select");
            selectButton.addListener(new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    Collection<EntityData> selection = getSelectable().getSelection();
                    if (selection == null || selection.size() == 0) {
                        MessageBox.alert("No selection", "No class selected. Please select a class from the tree.");
                        return;
                    }
                    setClsValues(selection);
                    selectWindow.hide();
                }
            });


            selectWindow.add(getSelectable());
            selectWindow.addButton(selectButton);
            selectWindow.addButton(cancelButton);
        }

        if (!selectWindow.isVisible()) {
            selectWindow.show();
            selectWindow.center();
        }
    }


    @Override
    protected void onTriggerClick(EventObject event) {
        if (!isDisabled()) {
            classTreePortlet = getSelectable();
            onSelectEntity();
        }
    }

    public String getTopClass() {
        return topClass;
    }

    public void setTopClass(String topClass) {
        this.topClass = topClass;
    }

    @Override
    protected void onDestroy() {
        listeners.clear();
        if (selectWindow != null) {
            selectWindow.destroy();
        }
        super.onDestroy();
    }

    public void addSelectionListener(SelectionListener listener) {
        listeners.add(listener);
    }

    public Collection<EntityData> getSelection() {
        return values;
    }

    public void notifySelectionListeners(SelectionEvent selectionEvent) {
        for (SelectionListener listener : listeners) {
            listener.selectionChanged(selectionEvent);
        }

    }

    public void removeSelectionListener(SelectionListener listener) {
        listeners.remove(listener);
    }

    public void setSelection(Collection<EntityData> selection) {
        setClsValues(selection);
    }

}

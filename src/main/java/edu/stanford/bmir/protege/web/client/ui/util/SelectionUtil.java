package edu.stanford.bmir.protege.web.client.ui.util;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

import java.util.Collection;
import java.util.HashSet;

public class SelectionUtil {

    public static void selectClses(Project project, boolean allowMultiple, final SelectionCallback callback) {
        final Collection<EntityData> selection = new HashSet<EntityData>();

        final ClassSelectionPanel classSelectionPanel = new ClassSelectionPanel(project, allowMultiple);
        final Window selectWindow = new com.gwtext.client.widgets.Window();
        selectWindow.setTitle("Select class");
        selectWindow.setWidth(600);
        selectWindow.setHeight(480);
        selectWindow.setMinWidth(300);
        selectWindow.setMinHeight(350);
        selectWindow.setLayout(new FitLayout());
        selectWindow.setPaddings(5);
        selectWindow.setButtonAlign(Position.CENTER);

        selectWindow.setPlain(true);

        com.gwtext.client.widgets.Button cancelButton = new com.gwtext.client.widgets.Button("Cancel");
        cancelButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                selectWindow.hide();
                selectWindow.destroy();
            }
        });

        com.gwtext.client.widgets.Button selectButton = new com.gwtext.client.widgets.Button("Select");
        selectButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                Collection<EntityData> tmpselection = classSelectionPanel.getSelection();
                if (tmpselection == null || tmpselection.size() == 0) {
                    MessageBox.alert("No selection", "No class selected. Please select a class from the tree.");
                    return;
                } else {
                    for (EntityData sel : tmpselection) {
                        selection.add(sel);
                    }
                    if (callback != null) {
                        callback.onSelect(selection);
                    }
                }
                selectWindow.hide();
                selectWindow.destroy();
            }
        });

        selectWindow.add((Component)classSelectionPanel.getSelectable());
        selectWindow.addButton(selectButton);
        selectWindow.addButton(cancelButton);

        selectWindow.show();
        selectWindow.center();
    }


    public static void selectIndividuals(Project project, Collection<EntityData> clses, boolean allowMultiple, boolean showClsesPanel, final SelectionCallback callback) {
        final IndividualsWithClassSelectionPanel classSelectionPanel = new IndividualsWithClassSelectionPanel(project, clses, allowMultiple, showClsesPanel);
        final Window selectWindow = new com.gwtext.client.widgets.Window();
        selectWindow.setTitle("Select individuals");
        selectWindow.setWidth(800);
        selectWindow.setHeight(500);
        selectWindow.setLayout(new FitLayout());
        selectWindow.setPaddings(5);
        selectWindow.setButtonAlign(Position.CENTER);
        selectWindow.setPlain(true);

        com.gwtext.client.widgets.Button cancelButton = new com.gwtext.client.widgets.Button("Cancel");
        cancelButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                selectWindow.hide();
                selectWindow.destroy();
            }
        });

        com.gwtext.client.widgets.Button selectButton = new com.gwtext.client.widgets.Button("Select");
        selectButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                Collection<EntityData> tmpselection = classSelectionPanel.getSelection();
                if (tmpselection == null || tmpselection.size() == 0) {
                    MessageBox.alert("No selection", "No entity selected. Please make a selection in the right panel.");
                    return;
                } else {
                    final Collection<EntityData> selection = new HashSet<EntityData>();
                    for (EntityData sel : tmpselection) {
                        selection.add(sel);
                    }
                    if (callback != null) {
                        callback.onSelect(selection);
                    }
                }
                selectWindow.hide();
                selectWindow.destroy();
            }
        });

        selectWindow.add(classSelectionPanel);
        selectWindow.addButton(selectButton);
        selectWindow.addButton(cancelButton);

        selectWindow.show();
        selectWindow.center();
    }


    public interface SelectionCallback {
        void onSelect(Collection<EntityData> selection);
    }

}

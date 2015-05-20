package edu.stanford.bmir.protege.web.client.ui.util;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import java.util.Collection;

public class ClassSelectionPanel {

    private Project project;
    private boolean allowMultipleSelection;
    private Selectable selectable;

    public ClassSelectionPanel(Project project, boolean allowMultipleSelection) {
        this.project = project;
        this.allowMultipleSelection = allowMultipleSelection;
        selectable = getSelectable();
    }

    public Selectable getSelectable() {
        if (selectable == null) {
            SelectionModel selectionModel = SelectionModel.create();
            ClassTreePortlet selectableTree = new ClassTreePortlet(selectionModel, project, false, false, false, allowMultipleSelection, null);
            selectableTree.setDraggable(false);
            selectableTree.setClosable(false);
            selectableTree.setCollapsible(false);
            selectableTree.setHeight(300);
            selectableTree.setWidth(450);
            // TODO: SELECTION
//            selectable = selectableTree;
        }
        return selectable;
    }

    public Collection<EntityData> getSelection() {
        return selectable.getSelection();
    }

}

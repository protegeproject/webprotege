package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * A portlet that displays the {@link AllPropertiesGrid} (see for more
 * documentation). Shows all properties of an entity and supports basic editing.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class AllPropertiesPortlet extends AbstractOWLEntityPortlet {

    protected AllPropertiesGrid propGrid;

    public AllPropertiesPortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
    }

    @Override
    public void initialize() {
        setTitle("Properties");
        this.propGrid = new AllPropertiesGrid(getProject());
        add(propGrid);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        if (entityData.isPresent()) {
            setTitle("Properties for " + entityData.get().getBrowserText());
        }
        GWT.log("NOT IMPLEMENTED");
//        propGrid.setEntity(getEntity());
    }

    @Override
    protected void onRefresh() {
        propGrid.refresh();
    }

    @Override
    public void onPermissionsChanged() {
        propGrid.updateButtonStates();
    }

}

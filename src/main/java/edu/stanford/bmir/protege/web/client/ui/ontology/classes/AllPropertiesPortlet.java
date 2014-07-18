package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

import java.util.Collection;

/**
 * A portlet that displays the {@link AllPropertiesGrid} (see for more
 * documentation). Shows all properties of an entity and supports basic editing.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class AllPropertiesPortlet extends AbstractOWLEntityPortlet {

    protected AllPropertiesGrid propGrid;

    public AllPropertiesPortlet(Project project) {
        super(project);
    }

    @Override
    public void setEntity(EntityData newEntity) {
        super.setEntity(newEntity);
    }

    @Override
    public void reload() {
        if (_currentEntity != null) {
            setTitle("Properties for " + _currentEntity.getBrowserText());
        }

        propGrid.setEntity(_currentEntity);
    }

    @Override
    public void initialize() {
        setTitle("Properties");
        this.propGrid = new AllPropertiesGrid(getProject());
        add(propGrid);
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

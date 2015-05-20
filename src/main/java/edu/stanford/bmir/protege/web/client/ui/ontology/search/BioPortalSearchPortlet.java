package edu.stanford.bmir.protege.web.client.ui.ontology.search;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

public class BioPortalSearchPortlet extends AbstractOWLEntityPortlet {

    private BioPortalSearchComponent searchComp;

    public BioPortalSearchPortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
    }

    @Override
    public void initialize() {
        setTitle("BioPortal Search");
        searchComp = new BioPortalSearchComponent(getProjectId(), false);
        add(searchComp);
    }

    @Override
    public void setPortletConfiguration(PortletConfiguration portletConfiguration) {
        super.setPortletConfiguration(portletConfiguration);
        searchComp.setConfigProperties(portletConfiguration.getProperties());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        if (!entityData.isPresent()) {
            return;
        }
        setTitle("BioPortal search results for " + entityData.get().getBrowserText());
        GWT.log("NOT IMPLEMENTED");
//        searchComp.setEntity(getEntity());
    }
}

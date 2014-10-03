package edu.stanford.bmir.protege.web.client.ui.ontology.search;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.util.ArrayList;

public class BioPortalSearchPortlet extends AbstractOWLEntityPortlet {

    private BioPortalSearchComponent searchComp;

    public BioPortalSearchPortlet(Project project) {
        super(project);
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
        searchComp.setEntity(getEntity());
    }
}

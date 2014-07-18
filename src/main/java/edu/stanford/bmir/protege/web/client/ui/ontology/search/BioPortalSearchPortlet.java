package edu.stanford.bmir.protege.web.client.ui.ontology.search;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

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
        reload();
    }

    @Override
    public void setPortletConfiguration(PortletConfiguration portletConfiguration) {
        super.setPortletConfiguration(portletConfiguration);
        searchComp.setConfigProperties(portletConfiguration.getProperties());
    }

    @Override
    public void reload() {
        if (_currentEntity == null) {
            return;
        }
        setTitle("BioPortal search results for " + _currentEntity.getBrowserText());
        searchComp.setEntity(_currentEntity);
    }
}

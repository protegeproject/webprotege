package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

import java.util.ArrayList;

public class BioPortalImportPortlet extends AbstractOWLEntityPortlet {

    private BioPortalImportComponent importComp;

    public BioPortalImportPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setTitle("BioPortal Search");
        importComp = new BioPortalImportComponent(getProjectId(), false);
        add(importComp);
        reload();
    }

    @Override
    public void setPortletConfiguration(PortletConfiguration portletConfiguration) {
        super.setPortletConfiguration(portletConfiguration);
        importComp.setConfigProperties(portletConfiguration.getProperties());
    }

    @Override
    public void reload() {
        if (_currentEntity == null) {
            return;
        }
        setTitle("BioPortal search results for " + _currentEntity.getBrowserText());
        importComp.setEntity(_currentEntity);
    }
}

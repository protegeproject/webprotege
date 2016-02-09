package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 08/02/16
 */
public class ProjectLayoutConfigurationBuilder {

    private ProjectId projectId;

    private List<AbstractTab> tabs;


    public ProjectLayoutConfigurationBuilder(ProjectId projectId, List<AbstractTab> tabs) {
        this.projectId = projectId;
        this.tabs = new ArrayList<>(tabs);
    }

    public ProjectLayoutConfiguration build() {
        ProjectLayoutConfiguration config = new ProjectLayoutConfiguration();
        for(AbstractTab tab : tabs) {
                TabConfiguration tabConfiguration = new TabConfiguration();
                tabConfiguration.setName(UserDefinedTab.class.getName());
                tabConfiguration.setLabel(tab.getLabel());
                config.addTab(tabConfiguration);
                List<TabColumnConfiguration> columnConfigs = new ArrayList<>();
                for(int columnIndex = 0; columnIndex < tab.getColumnCount(); columnIndex++) {
                    TabColumnConfiguration columnConfig = new TabColumnConfiguration();
                    columnConfigs.add(columnConfig);
                    columnConfig.setWidth(tab.getColumnWidth(columnIndex));
                    List<PortletConfiguration> portletConfigurations = buildPortletConfigurations(tab, columnIndex);
                    columnConfig.setPortlets(portletConfigurations);
                }
                tabConfiguration.setColumns(columnConfigs);
        }
        config.setProjectId(projectId);
        return config;
    }

    private List<PortletConfiguration> buildPortletConfigurations(AbstractTab tab, int columnIndex) {
        List<PortletConfiguration> portletConfigurations = new ArrayList<>();
        int portletCount = tab.getPortletCount(columnIndex);
        for(int portletIndex = 0; portletIndex < portletCount; portletIndex++) {
            EntityPortlet entityPortlet = tab.getPortletAt(columnIndex, portletIndex);
            PortletConfiguration config = new PortletConfiguration();
            portletConfigurations.add(config);
            config.setName(entityPortlet.getClass().getName());
            config.setHeight(entityPortlet.getHeight());
        }
        return portletConfigurations;
    }
}

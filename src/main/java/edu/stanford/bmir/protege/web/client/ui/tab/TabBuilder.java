package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.portal.PortalColumn;
import com.gwtext.client.widgets.portal.Portlet;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public class TabBuilder {

    private ProjectId projectId;

    private AbstractTab tab;

    private TabConfiguration tabConfiguration;

    public TabBuilder(ProjectId projectId, AbstractTab tab, TabConfiguration tabConfiguration) {
        this.projectId = projectId;
        this.tab = tab;
        this.tabConfiguration = tabConfiguration;
    }

    public void build() {
        tab.setLabel(tabConfiguration.getLabel());
        tab.setClosable(tabConfiguration.getClosable());
        int index = 0;
        for (final TabColumnConfiguration tabColumnConfiguration : tabConfiguration.getColumns()) {
            tab.addColumn(tabColumnConfiguration.getWidth());
            addPortletsToColumn(tabColumnConfiguration, index);
            index++;
        }
    }

    private void addPortletsToColumn(TabColumnConfiguration tabColumnConfiguration, int columnIndex) {
        List<PortletConfiguration> portlets = tabColumnConfiguration.getPortlets();
        portlets = getSortedPortlets(portlets);
        tabColumnConfiguration.setPortlets(portlets);
        for (final PortletConfiguration portletConfiguration : portlets) {
            addPortletToColumn(tabColumnConfiguration, portletConfiguration, false, columnIndex);
        }
    }

    private List<PortletConfiguration> getSortedPortlets(List<PortletConfiguration> portlets) {
        Collections.sort(portlets, new PortletConfigurationComparator());
        return portlets;
    }

    private void addPortletToColumn(final TabColumnConfiguration tabColumnConfiguration,
                                    final PortletConfiguration portletConfiguration, final boolean updateColConfig, int index) {
        // configuration
        final EntityPortlet portlet = createPortlet(portletConfiguration);
        if (portlet == null) {
            return;
        }
        addPortletToColumn(portlet, tabColumnConfiguration, portletConfiguration, index, updateColConfig);
    }

    private void addPortletToColumn(final EntityPortlet portlet,
                                      final TabColumnConfiguration tabColumnConfiguration,
                                      final PortletConfiguration portletConfiguration,
                                      final int columnIndex,
                                      final boolean updateColConfig) {


        ((AbstractEntityPortlet) portlet).setPortletConfiguration(portletConfiguration);
        tab.addPortletToColumn(portlet, columnIndex);
        ((AbstractEntityPortlet) portlet).setTab(tab);

        if (updateColConfig) {
            tabColumnConfiguration.addPortelt(portletConfiguration);
            adjustPortletsIndex(tabColumnConfiguration);
        }
    }

    private void adjustPortletsIndex(TabColumnConfiguration tabColumnConfiguration){
        List<PortletConfiguration> portlets = tabColumnConfiguration.getPortlets();
        int portletsCount = portlets.size();
        for (int i=0; i < portletsCount; i++)  {
            PortletConfiguration pc = portlets.get(i);
            pc.setIndex(String.valueOf(portletsCount - i - 1));
        }
    }

    private EntityPortlet createPortlet(final PortletConfiguration portletConfiguration) {
        final String portletClassName = portletConfiguration.getName();
        final UIFactory uiFactory = WebProtegeClientInjector.getUiFactory(projectId);
        final EntityPortlet portlet =  uiFactory.createPortlet(portletClassName);
        if (portlet == null) {
            GWT.log("[TabBuilder] The UIFactory returned a null value when asked to create an instance of the portlet: " + portletClassName);
            return null;
        }
        final int height = portletConfiguration.getHeight();
        GWT.log("[TabBuilder] Setting the " + portlet.getClass().getSimpleName() + " portlet height: " + height);
        if (height == 0) {
            portlet.setAutoHeight(true);
        } else {
            portlet.setHeight(height);
        }
        // MH: I don't see any reason not always have auto-width
        portlet.setAutoWidth(true);
        return portlet;
    }
}

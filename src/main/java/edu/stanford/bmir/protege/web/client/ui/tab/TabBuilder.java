package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

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
        for (int i = portlets.size() - 1; i > -1; i--) {
            PortletConfiguration portletConfiguration = portlets.get(i);
            insertPortletInColumn(portletConfiguration, columnIndex);
        }
    }

    /**
     * Inserts a portlet into a column.  Portlets are inserted into the top of the column.
     * @param portletConfiguration The portlet to insert.
     * @param columnIndex The column which should take the portlet.
     */
    private void insertPortletInColumn(final PortletConfiguration portletConfiguration, int columnIndex) {
        // configuration
        final EntityPortlet portlet = createPortlet(portletConfiguration);
        if (portlet == null) {
            return;
        }
        addPortletToColumn(portlet, portletConfiguration, columnIndex);
    }

    private void addPortletToColumn(final EntityPortlet portlet,
                                      final PortletConfiguration portletConfiguration,
                                      final int columnIndex) {


        ((AbstractEntityPortlet) portlet).setPortletConfiguration(portletConfiguration);
        tab.addPortletToColumn(portlet, columnIndex);
        ((AbstractEntityPortlet) portlet).setTab(tab);
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

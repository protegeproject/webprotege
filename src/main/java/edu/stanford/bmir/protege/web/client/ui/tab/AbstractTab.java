package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListener;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.portal.Portal;
import com.gwtext.client.widgets.portal.PortalColumn;
import com.gwtext.client.widgets.portal.Portlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;

import java.util.*;

public abstract class AbstractTab implements PortletContainer, IsWidget {

    private final Portal baseContainer = new Portal();

    private final Portal portal = new Portal();

    private final ListMultimap<PortalColumn, EntityPortlet> column2Portlets = ArrayListMultimap.create();

    private final Map<EntityPortlet, PortalColumn> portlet2Column = new HashMap<>();

    private final List<PortalColumn> columns = new ArrayList<>();

    private final PanelListener portletDestroyListener = new PanelListenerAdapter() {
        @Override
        public void onDestroy(Component component) {
            if (component instanceof EntityPortlet) {
                removePortlet((EntityPortlet) component);
            }
        }
    };

    private TabId tabId;

    public AbstractTab(final TabId tabId) {
        super();
        this.tabId = tabId;
        baseContainer.addListener(new PanelListenerAdapter() {
            @Override
            public void onActivate(Panel panel) {
                activatePortlets();
            }
        });
        baseContainer.setLayout(new FitLayout());
        baseContainer.setHideBorders(true);
        baseContainer.add(portal);
    }

    public TabId getTabId() {
        return tabId;
    }

    private void activatePortlets() {
        for(EntityPortlet entityPortlet : getPortlets()) {
            if(entityPortlet instanceof AbstractEntityPortlet) {
                ((AbstractEntityPortlet) entityPortlet).handleActivated();
            }
        }
    }

    @Override
    public List<EntityPortlet> getPortlets() {
        return new ArrayList<>(column2Portlets.values());
    }

    private void removePortlet(final EntityPortlet portlet) {
        PortalColumn column = portlet2Column.remove(portlet);
        if(column == null) {
            return;
        }
        column2Portlets.removeAll(column);
        ((Portlet) portlet).hide();
        ((Portlet) portlet).destroy();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public int getPortletCount(int columnIndex) {
        PortalColumn column = columns.get(columnIndex);
        return column2Portlets.get(column).size();
    }

    @Override
    public EntityPortlet getPortletAt(int columnIndex, int portletIndex) {
        PortalColumn column = columns.get(columnIndex);
        return column2Portlets.get(column).get(portletIndex);
    }

    @Override
    public double getColumnWidth(int columnIndex) {
        return columns.get(columnIndex).getWidth();
    }

    @Override
    public void addColumn(double width) {
        final PortalColumn portalColumn = new PortalColumn();
        portalColumn.setPaddings(10, 10, 10, 10);
        portal.add(portalColumn, new ColumnLayoutData(width));
        columns.add(portalColumn);
    }


    @Override
    public void addPortletToColumn(EntityPortlet entityPortlet, int columnIndex) {
        PortalColumn portalColumn = columns.get(columnIndex);
        portalColumn.add((Portlet) entityPortlet);
        column2Portlets.put(portalColumn, entityPortlet);
        portlet2Column.put(entityPortlet, portalColumn);
        ((Portlet) entityPortlet).addListener(portletDestroyListener);
    }

    public String getLabel() {
        return baseContainer.getTitle();
    }

    public void setLabel(final String label) {
        baseContainer.setTitle(label);
    }

    public void setClosable(boolean closable) {
        baseContainer.setClosable(closable);
    }

    public boolean isVisible() {
        return baseContainer.isVisible();
    }

    @Override
    public Widget asWidget() {
        return baseContainer;
    }
}

package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ContainerListenerAdapter;
import com.gwtext.client.widgets.event.PanelListener;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.ContainerLayout;
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

    private final List<EntityPortlet> entityPortlets = new ArrayList<>();

    private final List<ColumnInfo> columns = new ArrayList<>();

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
        return new ArrayList<>(entityPortlets);
    }

    private void removePortlet(final EntityPortlet portlet) {
        entityPortlets.remove(portlet);
        if (portlet instanceof Portlet) {
            ((Portlet) portlet).hide();
        }
        portlet.dispose();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public int getPortletCount(int columnIndex) {
        return getEntityPortlets(columnIndex).size();
    }

    private PortalColumn getColumnAt(int columnIndex) {
        return columns.get(columnIndex).getColumn();
    }

    @Override
    public EntityPortlet getPortletAt(int columnIndex, int portletIndex) {
        List<EntityPortlet> portlets = getEntityPortlets(columnIndex);
        EntityPortlet component = portlets.get(portletIndex);
        GWT.log("[AbstractTab] Portlet At (" + columnIndex + ", " + portletIndex + "): " + component);
        return component;
    }

    private List<EntityPortlet> getEntityPortlets(int columnIndex) {
        PortalColumn column = getColumnAt(columnIndex);
        List<EntityPortlet> portlets = new ArrayList<>();
        for(Component c : column.getComponents()) {
            EntityPortlet entityPortlet = (EntityPortlet) c;
            if (entityPortlets.contains(entityPortlet)) {
                portlets.add(entityPortlet);
            }
        }
        Collections.sort(portlets, new Comparator<EntityPortlet>() {
            @Override
            public int compare(EntityPortlet o1, EntityPortlet o2) {
                int y1 = o1.asWidget().getAbsoluteTop();
                int y2 = o2.asWidget().getAbsoluteTop();
                return y1 - y2;
            }
        });
        return portlets;
    }

    @Override
    public double getColumnWidth(int columnIndex) {
        double totalWidth = getSumOfColumnWidths();
        return columns.get(columnIndex).getWidth() / totalWidth;
    }

    private double getSumOfColumnWidths() {
        double totalWidth = 0;
        for(ColumnInfo portalColumn : columns) {
            totalWidth += portalColumn.getWidth();
        }
        return totalWidth;
    }

    @Override
    public void addColumn(double width) {
        final PortalColumn portalColumn = new PortalColumn();
        portalColumn.setPaddings(10, 10, 10, 10);
        portal.add(portalColumn, new ColumnLayoutData(width));
        columns.add(new ColumnInfo(portalColumn, width));
    }


    @Override
    public void addPortletToColumn(final EntityPortlet entityPortlet, final int columnIndex) {
        entityPortlets.add(entityPortlet);
        final PortalColumn portalColumn = columns.get(columnIndex).getColumn();
        portalColumn.add((Portlet) entityPortlet.asWidget());
        if (entityPortlet instanceof Portlet) {
            ((Portlet) entityPortlet).addListener(portletDestroyListener);
        }
        GWT.log("[AbstractTab] Added portlet to column: " + entityPortlet);
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


    private static class ColumnInfo {

        private final PortalColumn column;

        private final double width;

        public ColumnInfo(PortalColumn column, double width) {
            this.column = column;
            this.width = width;
        }

        public PortalColumn getColumn() {
            return column;
        }

        public double getWidth() {
            return width;
        }
    }
}

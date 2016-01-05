package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListener;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.portal.Portal;
import com.gwtext.client.widgets.portal.PortalColumn;
import com.gwtext.client.widgets.portal.Portlet;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import java.util.*;

/**
 * Abstract class that should be extended by all tabs that can be added to the
 * main UI of WebProtege.
 * <p/>
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
// TODO: reordering of portlets in a column at drang-n-drop does not work yet
public abstract class AbstractTab extends Portal implements PortletContainer {

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

    private final SelectionModel selectionModel;

    private final ProjectId projectId;

    private TabId tabId;

    public AbstractTab(final TabId tabId, final SelectionModel selectionModel, ProjectId projectId, ProjectManager projectManager) {
        super();
        this.tabId = tabId;
        this.projectId = projectId;
        this.selectionModel = selectionModel;
        addListener(new PanelListenerAdapter(){
            @Override
            public void onActivate(Panel panel) {
                // Tell the portlets to update themselves
                for(EntityPortlet entityPortlet : getPortlets()) {
                    if(entityPortlet instanceof AbstractEntityPortlet) {
                        ((AbstractEntityPortlet) entityPortlet).handleActivated();
                    }
                }
            }
        });
        setLayout(new FitLayout());
        setHideBorders(true);
        add(portal);
    }

    public TabId getTabId() {
        return tabId;
    }

    public SelectionModel getSelectionModel() {
        return selectionModel;
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
        return getTitle();
    }

    public void setLabel(final String label) {
        setTitle(label);
    }
    
    public String getHeaderClass() {
        return null;
    }
}

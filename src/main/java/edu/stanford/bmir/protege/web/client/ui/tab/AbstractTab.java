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
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
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

    private String tabId;

    public AbstractTab(final String tabId, final SelectionModel selectionModel, ProjectId projectId, ProjectManager projectManager) {
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
        portalColumn.add((AbstractEntityPortlet) entityPortlet);
        column2Portlets.put(portalColumn, entityPortlet);
        portlet2Column.put(entityPortlet, portalColumn);
        ((Portlet) entityPortlet).addListener(portletDestroyListener);
    }

    public String getLabel() {
        return getTitle();
    }




    /**
     * Overwrite this method to provide a default tab configuration for a tab.
     * For example, the ClassesTab may set up in the default
     * configuration the default portlets to be shown in the UI (e.g., classes
     * tree portlet, properties portlet and restriction portlet). The default
     * tab configuration will be used if the user has not performed any
     * customization to this tab.
     * <p>
     * This method will not se the tab configuration to the return value of this
     * method. It is responsability of the calling code to make so.
     * </p>
     * <p>
     * The default implementation will return an empty tab.
     * </p>
     *
     * @return the default configuration of this tab
     */
    public TabConfiguration getDefaultTabConfiguration() {
        final TabConfiguration tabConfiguration = new TabConfiguration();
        final List<TabColumnConfiguration> colList = new ArrayList<TabColumnConfiguration>();
        final TabColumnConfiguration column = new TabColumnConfiguration();
        column.setWidth((float) 1.0);
        colList.add(column);
        tabConfiguration.setColumns(colList);
        tabConfiguration.setName(tabId);
        return tabConfiguration;
    }


    public void setLabel(final String label) {
        setTitle(label);
    }
    
    public String getHeaderClass() {
        return null;
    }
}

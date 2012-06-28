 package edu.stanford.bmir.protege.web.client.ui.portlet;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.Timer;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Resizable;
import com.gwtext.client.widgets.ResizableConfig;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.ResizableListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.portal.Portlet;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.client.ui.util.AbstractValidatableTab;
import edu.stanford.bmir.protege.web.client.ui.util.ValidatableTab;

/**
 * Abstract class that should be extended by all portlets that should be
 * available in the user interface of a tab. This class takes care of the common
 * initializations for portlet (e.g. resizing, dragging, etc.) and the life
 * cycle of a portlet.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public abstract class AbstractEntityPortlet extends Portlet implements EntityPortlet {

    protected Project project;
    protected EntityData _currentEntity;
    private int revision;

    private AbstractTab tab;
    private PortletConfiguration portletConfiguration;
    private ArrayList<SelectionListener> _selectionListeners = new ArrayList<SelectionListener>();

    public AbstractEntityPortlet(Project project) {
        this(project, true);
    }

    public AbstractEntityPortlet(Project project, boolean initialize) {
        super();
        this.project = project;

        setTitle(""); // very important
        setLayout(new FitLayout());

        ResizableConfig config = new ResizableConfig();
        config.setHandles(Resizable.SOUTH_EAST);

        final Resizable resizable = new Resizable(this, config);
        resizable.addListener(new ResizableListenerAdapter() {
            @Override
            public void onResize(Resizable self, int width, int height) {
                doOnResize(width, height);
            }
        });

        if (initialize) {
            setTools(getTools());
            initialize();
        }

       updateIcon(isControllingPortlet());
    }

    protected void doOnResize(int width, int height){
        setWidth(width);
        setHeight(height);
        doLayout();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * edu.stanford.bmir.protege.web.client.ui.EntityPortlet#setEntity(edu.stanford
     * .bmir.protege.web.client.util.EntityData)
     */
    public void setEntity(EntityData newEntity) {
        _currentEntity = newEntity;
        reload();
        // doLayout();
    }

    /*
     * (non-Javadoc)
     *
     * @see edu.stanford.bmir.protege.web.client.ui.EntityPortlet#getEntity()
     */
    public EntityData getEntity() {
        return _currentEntity;
    }


    public Project getProject() {
        return project;
    }

    public int getRevision() {
        return revision;
    }

    /**
     * Should only be called after an event has been processed and
     * the portlet shows a new revision.
     *
     * @param revision
     */
    public void setRevision(int revision) {
        this.revision = revision;
    }

    /*
     * (non-Javadoc)
     *
     * @see edu.stanford.bmir.protege.web.client.ui.EntityPortlet#reload()
     */
    public abstract void reload();

    /*
     * (non-Javadoc)
     *
     * @see edu.stanford.bmir.protege.web.client.ui.EntityPortlet#intialize()
     */
    public abstract void initialize();

    /*
     * Tools methods
     */
    protected Tool[] getTools() {
        // create some shared tools
        Tool refresh = new Tool(Tool.REFRESH, new Function() {
            public void execute() {
                onRefresh();
            }
        });
        Tool gear = new Tool(Tool.GEAR, new Function() {
            public void execute() {
                onConfigure();
            }
        });

        Tool close = new Tool(Tool.CLOSE, new Function() {
            public void execute() {
                onClose();
            }
        });

        Tool[] tools = new Tool[] { refresh, gear, close };

        return tools;
    }

    protected void onRefresh() {
        reload();
    }

    protected void onConfigure() {
        final Window window = new Window();
        window.setSize(550, 250);
        window.setTitle("Configure");
        window.setLayout(new FitLayout());
        window.setClosable(true);
        window.setPaddings(7);
        window.setCloseAction(Window.CLOSE);
        final TabPanel configPanel = getConfigurationPanel();
        window.add(configPanel);
        window.setButtonAlign(Position.CENTER);

        window.addButton(new Button("OK", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                if (isValidConfiguration(configPanel)) {
                    saveConfiguration(configPanel);
                    window.close();
                } else {
                    MessageBox.alert("Invalid configuration", "The configuration is not valid. Please fix it, and try again");
                }
            }
        }) );

        window.addButton(new Button("Cancel", new ButtonListenerAdapter(){
            @Override
            public void onClick(Button button, EventObject e) {
                window.close();
            }
        }) );

        window.show();
    }

    protected boolean isValidConfiguration(TabPanel configPanel) {
        Component[] tabs = configPanel.getComponents();
        for (int i = 0; i < tabs.length; i++) {
            Component comp = tabs[i];
            if (comp instanceof ValidatableTab) {
                ValidatableTab tab = (ValidatableTab)comp;
                if (!tab.isValid()) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void saveConfiguration(TabPanel configPanel) {
        Component[] tabs = configPanel.getComponents();
        for (int i = 0; i < tabs.length; i++) {
            Component comp = tabs[i];
            if (comp instanceof ValidatableTab) {
                ValidatableTab tab = (ValidatableTab)comp;
                tab.onSave();
            }
        }
    }

    protected TabPanel getConfigurationPanel() {
        TabPanel tabPanel = new TabPanel();
        tabPanel.add(createGeneralConfigPanel());
        return tabPanel;
    }

    protected Panel createGeneralConfigPanel() {
        final Checkbox isControllingPortletCheckbox = new Checkbox("Set as controlling portlet (the controlling portlet sets the selection for the entire tab)");
        isControllingPortletCheckbox.setChecked(isControllingPortlet());

        Panel generalPanel = new AbstractValidatableTab() {
            @Override
            public void onSave() {
                if (isControllingPortletCheckbox.getValue()) { //true - is checked
                    setAsControllingPortlet();
                } else {
                    if (isControllingPortlet()) {
                        AbstractTab tab = getTab();
                        if (tab == null) { return; }
                        tab.setControllingPortlet(null);
                    }
                }
            }

            @Override
            public boolean isValid() {
                return true;
            }
        };

        generalPanel.setTitle("General");
        generalPanel.setPaddings(10);

        generalPanel.add(isControllingPortletCheckbox, new AnchorLayoutData("100%"));
        return generalPanel;
    }



    public boolean isControllingPortlet() {
        AbstractTab tab = getTab();
        if (tab == null) { return false; }
        return this.equals(tab.getControllingPortlet());
    }

    public void setAsControllingPortlet() {
        AbstractTab tab = getTab();
        if (tab == null) { return; }
        EntityPortlet oldCtrlPortlet = tab.getControllingPortlet();
        if (!( this.equals(oldCtrlPortlet))) {
            tab.setControllingPortlet(this);
        }
    }

    public void updateIcon(boolean isControlling) {
        setIconCls(isControlling ? "ctrl_portlet" : null);
        if (isRendered() && !isControlling) {
            ExtElement header = this.getHeader();
            if (header != null) {
                header.removeClass("x-panel-icon");
            }
        }
    }

    protected void onClose() {
        this.hide();
        this.destroy();
    }

    public void onLogin(String userName) {}

    public void onLogout(String userName) {}

    public void onPermissionsChanged(Collection<String> permissions) {}

    public void refreshFromServer(int delay) {
        Timer timer = new Timer() {
            @Override
            public void run() {
                project.forceGetEvents();
            }
        };

        timer.schedule(delay);
    }

    /*
     * Selectable methods
     */

    /*
     * Should be implemented by subclasses
     */
    public void setSelection(Collection<EntityData> selection) {
    }

    public void addSelectionListener(SelectionListener selectionListener) {
        _selectionListeners.add(selectionListener);
    }

    public void removeSelectionListener(SelectionListener selectionListener) {
        _selectionListeners.remove(selectionListener);
    }

    public void notifySelectionListeners(SelectionEvent selectionEvent) {
        for (SelectionListener listener : _selectionListeners) {
            listener.selectionChanged(selectionEvent);
        }
    }

    /**
     * Does not do anything to the UI, only stores the new configuration in this
     * portlet
     *
     * @param portletConfiguration
     */
    public void setPortletConfiguration(PortletConfiguration portletConfiguration) {
        this.portletConfiguration = portletConfiguration;
    }

    public PortletConfiguration getPortletConfiguration() {
        return portletConfiguration;
    }

    public AbstractTab getTab() {
        return tab;
    }

    public void setTab(AbstractTab tab) {
        this.tab = tab;
    }
}

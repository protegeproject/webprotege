package edu.stanford.bmir.protege.web.client.ui;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Container;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.TabPanelListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;
import com.gwtext.client.widgets.portal.Portlet;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.model.SystemEventManager;
import edu.stanford.bmir.protege.web.client.model.event.LoginEvent;
import edu.stanford.bmir.protege.web.client.model.event.PermissionEvent;
import edu.stanford.bmir.protege.web.client.model.listener.SystemListener;
import edu.stanford.bmir.protege.web.client.model.listener.SystemListenerAdapter;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.ProjectConfigurationServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.client.ui.tab.UserDefinedTab;
import edu.stanford.bmir.protege.web.client.ui.util.GlobalSelectionManager;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * The main user interface for displaying one ontology.
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class Ontology extends TabPanel {
    public static final String SHOW_ONTOLOGY_TOOLBAR = "showOntologyToolbar";

    protected Project project;

    protected SystemListener systemListener;

    private Map<String, String> shortToLongPortletNameMap;
    private Map<String, String> shortToLongTabNameMap;

    private List<AbstractTab> tabs;


    public Ontology(Project project) {
        this.project = project;
        this.systemListener = getSystemListener();

        setTitle(getLabel());
        setTopToolbar(new Toolbar()); //TODO: make it configurable
        setEnableTabScroll(true);
        buildUI();

        SystemEventManager.getSystemEventManager().addSystemListener(project, getSystemListener());

        //TODO: use this to remove tab from config
        addListener(new TabPanelListenerAdapter() {
            @Override
            public void onRemove(Container self, Component component) {
                GWT.log("Ont on remove" );
            }

        });
    }


    public void buildUI() {
        tabs = new ArrayList<AbstractTab>();
        setActiveTab(0);
    }

    protected SystemListener getSystemListener() {
        if (systemListener == null) {
            this.systemListener = new SystemListenerAdapter() {
                @Override
                public void onLogin(LoginEvent loginEvent) {
                    Ontology.this.onLogin(loginEvent.getUser());
                }

                @Override
                public void onLogout(LoginEvent loginEvent) {
                    Ontology.this.onLogout(loginEvent.getUser());
                }

                @Override
                public void onPermissionsChanged(PermissionEvent permissionEvent) {
                    Ontology.this.onPermissionsChanged(permissionEvent.getPermissions());
                }
            };
        }
        return systemListener;
    }

    protected void onLogin(String user) {
        requestPermissions();
        for (AbstractTab tab : tabs) {
            tab.onLogin(user);
        }
    }

    protected void onLogout(String user) {
        requestPermissions();
        for (AbstractTab tab : tabs) {
            tab.onLogout(user);
        }
    }

    protected void onPermissionsChanged(Collection<String> permissions) {
        for (AbstractTab tab : tabs) {
            tab.onPermissionsChanged(permissions);
        }
    }

    protected void requestPermissions() {
        SystemEventManager.getSystemEventManager().requestPermissions(project,
                GlobalSettings.getGlobalSettings().getUserName());
    }

    protected void getProjectConfiguration(Project project) {
        UIUtil.showLoadProgessBar("Loading " + project.getProjectName() + " configuration", "Loading");
        ProjectConfigurationServiceManager.getInstance().getProjectConfiguration(project.getProjectName(),
                GlobalSettings.getGlobalSettings().getUserName(), new GetProjectConfigurationHandler(project));

        //FIXME delete this hack once we figure it out how to make it the ComboBox fill-in mechanism work properly
        //      in NoteInputPanel at display time
        ChAOServiceManager.getInstance().getAvailableNoteTypes(project.getProjectName(),
                new AsyncCallback<Collection<EntityData>>() {
            public void onFailure(Throwable caught) {
                GWT.log("Error getting the list of available note types", caught);
            }

            public void onSuccess(Collection<EntityData> result) {
                if (result != null) {
                    String[] noteTypes = new String[result.size()];
                    int i = 0;
                    for (EntityData noteType : result) {
                        noteTypes[i++] = noteType.getBrowserText();
                    }
                    UIUtil.setNoteTypes(noteTypes);
                }
            }
        });
    }

    public void layoutProject() {
        getProjectConfiguration(getProject());
    }

    protected void createOntolgyForm() {
        List<AbstractTab> tabs = project.getLayoutManager().createTabs(project.getProjectConfiguration());
        for (AbstractTab tab : tabs) {
            addTab(tab);
            updateTabStyle(tab);
        }
        if (tabs.size() > 0) {
            activate(0);
        }
        if (UIUtil.getBooleanConfigurationProperty(project.getProjectConfiguration(), SHOW_ONTOLOGY_TOOLBAR, true) ) {
            createToolbarButtons();
        }
        doLayout();
    }


    private void updateTabStyle(AbstractTab tab) {
        String tabHeaderClass = tab.getHeaderClass();
        if (tabHeaderClass != null) {
            Element tabEl = getTabEl(tab);
            tabEl.addClassName(tabHeaderClass);
        }
    }

    protected void addTab(AbstractTab tab) {
        tabs.add(tab);
        add(tab);
    }

    protected void createToolbarButtons() {
       Toolbar toolbar = getTopToolbar();
        toolbar.addFill();
        toolbar.addButton(getAddPortletButton());
        toolbar.addSpacer();
        toolbar.addButton(getAddTabButton());
        toolbar.addSpacer();
        toolbar.addButton(getSaveConfigurationButton());
    }


    protected ToolbarMenuButton getAddPortletButton() {
        shortToLongPortletNameMap = UIFactory.getAvailablePortletNameMap();

        Menu addPortletMenu = new Menu();
        for (String portletName : shortToLongPortletNameMap.keySet()) {
            Item item = new Item(portletName);
            addPortletMenu.addItem(item);
            item.addListener(new BaseItemListenerAdapter() {
                @Override
                public void onClick(BaseItem item, EventObject e) {
                    String javaClassName = shortToLongPortletNameMap.get(((Item) item).getText());
                    onPortletAdded(javaClassName);
                }
            });
        }
        ToolbarMenuButton addPortletButton = new ToolbarMenuButton("Add content to this tab", addPortletMenu);
        addPortletButton.setIcon("images/portlet_add.gif");
        return addPortletButton;
    }

    protected void onPortletRemoved(String javaClassName) {
        AbstractTab activeTab = getActiveOntologyTab();
        List<EntityPortlet> comps = activeTab.getPortlets();
        for (EntityPortlet entityPortlet2 : comps) {
            EntityPortlet entityPortlet = entityPortlet2;
            if (entityPortlet.getClass().getName().equals(javaClassName)) {
                ((Portlet) entityPortlet).setVisible(false);
                ((Portlet) entityPortlet).destroy();
            }
        }
    }

    protected void onPortletAdded(String javaClassName) {
        EntityPortlet portlet = UIFactory.createPortlet(project, javaClassName);
        if (portlet == null) {
            return;
        }
        AbstractTab activeTab = getActiveOntologyTab();
        activeTab.addPortlet(portlet, activeTab.getColumnCount() - 1);
        doLayout();
    }

    protected ToolbarMenuButton getAddTabButton() {
        shortToLongTabNameMap = UIFactory.getAvailableTabNameMap();

        List<String> enabledTabs = new ArrayList<String>();
        for (AbstractTab tab : tabs) {
            enabledTabs.add(tab.getClass().getName());
        }

        Menu addTabMenu = new Menu();
        for (String tabName : shortToLongTabNameMap.keySet()) {
            CheckItem item = new CheckItem(tabName, enabledTabs.contains(shortToLongTabNameMap.get(tabName)));
            addTabMenu.addItem(item);
            item.addListener(new CheckItemListenerAdapter() {
                @Override
                public void onCheckChange(CheckItem item, boolean checked) {
                    String javaClassName = shortToLongTabNameMap.get(item.getText());
                    if (checked) {
                        onTabAdded(javaClassName);
                    } else {
                        onTabRemoved(javaClassName);
                    }
                }
            });
        }

        // Add the "Add user defined tab"
        addTabMenu.addSeparator();
        Item item = new Item("Add your own tab");
        addTabMenu.addItem(item);
        item.addListener(new BaseItemListenerAdapter() {
            @Override
            public void onClick(BaseItem item, EventObject e) {
                onUserDefinedTabAdded();
            }
        });

        ToolbarMenuButton addTabButton = new ToolbarMenuButton("Add tab", addTabMenu);
        addTabButton.setIcon("images/tab_add.gif");
        return addTabButton;
    }

    protected ToolbarButton getSaveConfigurationButton() {
        ToolbarButton saveConfigButton = new ToolbarButton(null,  new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                MessageBox.confirm("Save project layout?", "Save the current project layout for future sessions?", new ConfirmCallback() {
                    public void execute(String btnID) {
                        if (btnID.equalsIgnoreCase("yes")) {
                            saveProjectConfiguration();
                        }
                    }
                });
            }
        });
        saveConfigButton.setIcon("images/page_save.png");
        saveConfigButton.setTooltip("Saves the current project layout for later sessions.");
        return saveConfigButton;
    }

    protected void onUserDefinedTabAdded() {
        final Window window = new Window();
        window.setTitle("Create your own tab");
        window.setClosable(true);
        window.setPaddings(7);
        window.setCloseAction(Window.HIDE);
        window.add(new CreateUserDefinedTabForm(window));
        window.show();
    }

    protected void onTabRemoved(String javaClassName) {
        AbstractTab tab = getTabByClassName(javaClassName);
        if (tab == null) {
            return;
        }
        project.getLayoutManager().removeTab(tab);
        tabs.remove(tab);
        hideTabStripItem(tab);
        remove(tab);
        tab.hide();
        doLayout();
    }

    protected void onTabAdded(String javaClassName) {
        AbstractTab tab = project.getLayoutManager().addTab(javaClassName);
        if (tab == null) {
            return;
        }
        addTab(tab);
        // doLayout();
    }

    public AbstractTab getTabByClassName(String javaClassName) {
        for (AbstractTab tab : tabs) {
            if (tab.getClass().getName().equals(javaClassName)) {
                return tab;
            }
        }
        return null;
    }

    public AbstractTab getActiveOntologyTab() {
        Panel panel = getActiveTab();
        return (panel instanceof AbstractTab) ? (AbstractTab) panel : null;
    }

    protected void saveProjectConfiguration() {
        if (GlobalSettings.getGlobalSettings().getUserName() == null) {
            MessageBox.alert("Not logged in", "To save the layout, you need to login first.");
            return;
        }
        ProjectConfiguration config = project.getProjectConfiguration();
        config.setOntologyName(project.getProjectName());
        ProjectConfigurationServiceManager.getInstance().saveProjectConfiguration(project.getProjectName(),
                GlobalSettings.getGlobalSettings().getUserName(), config, new SaveConfigHandler());
    }

    public String getLabel() {
        return project.getProjectName();
    }

    public Project getProject() {
        return project;
    }


    /*
     * Remote calls
     */

    class SaveConfigHandler extends AbstractAsyncHandler<Void> {
        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error in saving configurations (UI Layout)", caught);
            MessageBox.alert("There were problems at saving the project layout. Please try again later.");
        }

        @Override
        public void handleSuccess(Void result) {
            MessageBox.alert("Project layout saved successfully. This layout will be used the next time you log in.");
        }
    }

    /*
     * Internal class
     */

    class CreateUserDefinedTabForm extends FormPanel {
        private final Window parent;
        private final TextField colNoTextField;
        private int colNo;
        private final TextField[] colTextFields = new TextField[10];
        private final TextField labelTextField;

        public CreateUserDefinedTabForm(Window parent) {
            super();
            this.parent = parent;

            setWidth(400);
            setLabelWidth(150);
            setPaddings(15);

            labelTextField = new TextField("Tab Label", "label");
            labelTextField.setValue("New Tab");
            add(labelTextField);

            colNoTextField = new TextField("Number of columns", "noCols");
            colNoTextField.setValue("2");
            add(colNoTextField);
            colNoTextField.addListener(new TextFieldListenerAdapter() {
                @Override
                public void onChange(Field field, Object newVal, Object oldVal) {
                    showColFields();
                }
            });

            showColFields();

            Button createButton = new Button("Create", new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    AbstractTab tab = createTab();
                    addTab(tab);
                    CreateUserDefinedTabForm.this.parent.close();
                    activate(tab.getId());
                }
            });

            addButton(createButton);
        }

        private void showColFields() {
            removeExistingFields();
            String noOfColsStr = colNoTextField.getValueAsString();
            try {
                colNo = Integer.parseInt(noOfColsStr);
            } catch (NumberFormatException e) {
            }

            int defaultWidth = colNo == 0 ? 100 : 100 / colNo;
            String defaultWidthStr = new Integer(defaultWidth).toString();
            for (int i = 0; i < colNo; i++) {
                colTextFields[i] = new TextField("Width column " + i + " (%)", "col" + i);
                colTextFields[i].setValue(defaultWidthStr);
                add(colTextFields[i]);
            }
            doLayout();

        }

        private void removeExistingFields() {
            for (int i = 0; i < colNo; i++) {
                TextField tf = colTextFields[i];
                if (tf != null) {
                    remove(tf);
                    colTextFields[i] = null;
                }
            }
            doLayout();
        }

        public AbstractTab createTab() {
            UserDefinedTab userDefinedTab = new UserDefinedTab(project);
            TabConfiguration userDefinedTabConfiguration = getUserDefinedTabConfiguration();
            project.getLayoutManager().setupTab(userDefinedTab, userDefinedTabConfiguration);
            project.getProjectConfiguration().addTab(userDefinedTabConfiguration);
            return userDefinedTab;
        }

        private TabConfiguration getUserDefinedTabConfiguration() {
            TabConfiguration tabConfiguration = new TabConfiguration();
            tabConfiguration.setLabel(labelTextField.getValueAsString().trim());
            List<TabColumnConfiguration> tabColConf = new ArrayList<TabColumnConfiguration>(colNo);
            // TabColumnConfiguration[] tabColConf = new
            // TabColumnConfiguration[colNo];
            for (int i = 0; i < colNo; i++) {
                TabColumnConfiguration tabCol = new TabColumnConfiguration();
                String widthStr = colTextFields[i].getValueAsString();
                int width = 0;
                try {
                    width = Integer.parseInt(widthStr);
                } catch (NumberFormatException e) {
                }
                // tabColConf[i].setWidth(width == 0 ? 0 : 1/width);
                tabCol.setWidth(width == 0 ? 0 : (float) width / 100);
                tabColConf.add(tabCol);
            }
            tabConfiguration.setColumns(tabColConf);
            tabConfiguration.setName(UserDefinedTab.class.getName());
            return tabConfiguration;
        }
    }

    public void setInitialSelection() {
        final String ontologyName = com.google.gwt.user.client.Window.Location.getParameter("ontology");
        if (ontologyName == null || !getProject().getProjectName().equals(ontologyName)) {
            return;
        }

        final String tabNameToSelect = com.google.gwt.user.client.Window.Location.getParameter("tab");
        if (tabNameToSelect == null) {
            return;
        }

        for (AbstractTab tab : tabs) {
            String tabName = tab.getClass().getName();
            tabName = tabName.substring(tabName.lastIndexOf(".") + 1);
            if (tabName.equals(tabNameToSelect)) {
                activate(tab.getId());
                doLayout();

                String selection = com.google.gwt.user.client.Window.Location.getParameter("id");
                if (selection != null) {
                    selection = URL.decodeComponent(selection);
                    GlobalSelectionManager.setGlobalSelection(ontologyName, UIUtil.createCollection(new EntityData(selection)));

                    tab.setSelection(UIUtil.createCollection(new EntityData(selection)));
                    break;
                }
            }
        }
    }

    class GetProjectConfigurationHandler extends AbstractAsyncHandler<ProjectConfiguration> {
        private final Project project;

        public GetProjectConfigurationHandler(Project project) {
            this.project = project;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("There were errors at loading project configuration for " + project.getProjectName(), caught);
            UIUtil.hideLoadProgessBar();
            com.google.gwt.user.client.Window.alert("Load project configuration for " + project.getProjectName()
                    + " failed. " + " Message: " + caught.getMessage());
        }

        @Override
        public void handleSuccess(ProjectConfiguration config) {
            project.setProjectConfiguration(config);
            createOntolgyForm();
            doLayout();

            setInitialSelection();
            UIUtil.hideLoadProgessBar();
        }
    }

}

package edu.stanford.bmir.protege.web.client.ui;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.*;
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
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.project.ProjectManager;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabColumnConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.YesNoHandler;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.client.ui.tab.UserDefinedTab;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The main user interface for displaying one ontology.
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class ProjectDisplayImpl extends TabPanel implements ProjectDisplay {

    public static final String SHOW_ONTOLOGY_TOOLBAR = "showOntologyToolbar";

    public static final String PLACE_COORDINATE_NAME = "perspective";

    private final ProjectId projectId;

    private Map<String, String> shortToLongPortletNameMap;

    private Map<String, String> shortToLongTabNameMap;

    private List<AbstractTab> tabs;

    private final SelectionModel selectionModel;

    public ProjectDisplayImpl(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
        this.selectionModel = SelectionModel.create();
        setTitle(getLabel());
        setTopToolbar(new Toolbar()); //TODO: make it configurable
        setEnableTabScroll(true);
        buildUI();


        //TODO: use this to remove tab from config
        addListener(new TabPanelListenerAdapter() {
            @Override
            public void onTabChange(TabPanel source, Panel tab) {
//                EventBusManager.getManager().postEvent(new PlaceCoordinateChangedEvent(ProjectDisplayImpl.this, getPlaceCoordinate()));
                // TODO:  PLACE HAS CHANGED!!!
            }
        });


        EventBusManager.getManager().registerHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
            @Override
            public void onPlaceChange(PlaceChangeEvent event) {
                updateFromPlace();
            }
        });
        updateFromPlace();
    }


    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the project for this tab.
     *
     * @return The {@link Project}.  Not {@code null}.
     */
    public Project getProject() {
        Optional<Project> project = ProjectManager.get().getProject(projectId);
        if (!project.isPresent()) {
            throw new IllegalStateException("Unknown project: " + project);
        }
        return project.get();
    }

    private void updateFromPlace() {
//        WebProtegePlace place = Application.get().getPlaceManager().getCurrentPlace();
//        final Optional<ProjectId> placeProjectId = place.getProjectId();
//        if(!placeProjectId.isPresent()) {
//            return;
//        }
//        if(!placeProjectId.get().equals(projectId)) {
//            return;
//        }
//        Optional<String> perspectiveName = place.getCoordinateValue(PLACE_COORDINATE_NAME);
//        if(!perspectiveName.isPresent()) {
//            setActiveTab(0);
//        }
//        else {
//            GWT.log("Set active tab: " + perspectiveName.get());
//            for(AbstractTab tab : tabs) {
//                String title = tab.getTitle();
//                if(perspectiveName.get().equals(title)) {
//                    setActiveTab(tab.getId());
//                    break;
//                }
//            }
//        }


    }

    public void buildUI() {
        tabs = new ArrayList<>();
    }

    private void getProjectConfiguration() {
        GWT.log("Getting the project configuration");
        DispatchServiceManager.get().execute(new GetUIConfigurationAction(projectId),
                new DispatchServiceCallbackWithProgressDisplay<GetUIConfigurationResult>() {
                    @Override
                    public void handleSuccess(GetUIConfigurationResult result) {
                        getProject().setProjectLayoutConfiguration(result.getConfiguration());
                        createOntolgyForm();
                        doLayout();
                        setInitialSelection();
                    }

                    @Override
                    public String getProgressDisplayTitle() {
                        return "Loading project";
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return "Loading user interface configuration";
                    }

                    @Override
                    protected String getErrorMessage(Throwable throwable) {
                        return "There was an error loading the UI configuration";
                    }
                });
    }

    public void layoutProject() {
        getProjectConfiguration();
    }


    private void createOntolgyForm() {
        Project project = ProjectManager.get().getProject(projectId).get();
        List<AbstractTab> tabs = project.getLayoutManager().createTabs(selectionModel, project.getProjectLayoutConfiguration());
        GWT.log("Creating the ontology form from " + tabs.size() + " tabs");
        for (AbstractTab tab : tabs) {
            GWT.log("Attempting to add tab " + tab.getLabel());
            addTab(tab);
            updateTabStyle(tab);
        }
        if (tabs.size() > 0) {
            activate(0);
        }
        if (UIUtil.getBooleanConfigurationProperty(project.getProjectLayoutConfiguration(), SHOW_ONTOLOGY_TOOLBAR, true)) {
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

    private void addTab(AbstractTab tab) {
        GWT.log("Add Tab: " + tab.getLabel());
        if(tabs.contains(tab)) {
            throw new RuntimeException("Tab already present");
        }
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

    protected void onPortletAdded(final String javaClassName) {
        AbstractTab activeTab = getActiveOntologyTab();
        EntityPortlet portlet = UIFactory.createPortlet(activeTab.getSelectionModel(), getProject(), javaClassName);
        if (portlet == null) {
            return;
        }
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
        ToolbarButton saveConfigButton = new ToolbarButton(null, new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                MessageBox.showYesNoConfirmBox("Save project layout?", "Save the current project layout for future sessions?", new YesNoHandler() {
                    @Override
                    public void handleYes() {
                        saveProjectConfiguration();
                    }

                    @Override
                    public void handleNo() {
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
        getProject().getLayoutManager().removeTab(tab);
        tabs.remove(tab);
        hideTabStripItem(tab);
        remove(tab);
        tab.hide();
        doLayout();
    }

    protected void onTabAdded(String javaClassName) {
        AbstractTab tab = getProject().getLayoutManager().addTab(selectionModel, javaClassName);
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
        UserId userId = Application.get().getUserId();
        if (userId.isGuest()) {
            MessageBox.showAlert("You are not signed in", "To save the layout, you need to sign in first.");
            return;
        }
        ProjectLayoutConfiguration config = getProject().getProjectLayoutConfiguration();
        config.setProjectId(projectId);
        DispatchServiceManager.get().execute(new SetUIConfigurationAction(projectId, config),
                new DispatchServiceCallbackWithProgressDisplay<SetUIConfigurationResult>() {
                    @Override
                    public void handleSuccess(SetUIConfigurationResult setUIConfigurationResult) {
                        MessageBox.showMessage("Project layout saved",
                                "Project layout saved successfully. This layout will be used the next time you log in.");
                    }

                    @Override
                    public String getProgressDisplayTitle() {
                        return "Saving configuration";
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return "Saving the current project layout.";
                    }

                });
    }

    public String getLabel() {
        return ProjectManager.get().getProject(projectId).get().getProjectDetails().getDisplayName();
    }




    /*
     * Remote calls
     */

    class SaveConfigHandler implements AsyncCallback<Void> {
        @Override
        public void onFailure(Throwable caught) {
            GWT.log("Error in saving configurations (UI Layout)", caught);
            MessageBox.showAlert("Error saving project layout", "There were problems at saving the project layout. Please try again later.");
        }

        @Override
        public void onSuccess(Void result) {
            MessageBox.showAlert("Project layout saved", "Project layout saved successfully. This layout will be used the next time you log in.");
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
            final Project project = getProject();
            UserDefinedTab userDefinedTab = new UserDefinedTab(selectionModel, project);
            TabConfiguration userDefinedTabConfiguration = getUserDefinedTabConfiguration();
            project.getLayoutManager().setupTab(userDefinedTab, userDefinedTabConfiguration);
            project.getProjectLayoutConfiguration().addTab(userDefinedTabConfiguration);
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

    private void setInitialSelection() {
        final String ontologyName = com.google.gwt.user.client.Window.Location.getParameter("ontology");
        if (ontologyName == null || !projectId.getId().equals(ontologyName)) {
            return;
        }

        final String tabNameToSelect = com.google.gwt.user.client.Window.Location.getParameter("tab");
        if (tabNameToSelect == null) {
            return;
        }

        selectTabWithName(tabNameToSelect);
    }

    private void selectTabWithName(String tabNameToSelect) {
        for (AbstractTab tab : tabs) {
            String tabName = tab.getClass().getName();
            tabName = tabName.substring(tabName.lastIndexOf(".") + 1);
            if (tabName.equals(tabNameToSelect)) {
                activate(tab.getId());
                doLayout();
            }
        }
    }
}

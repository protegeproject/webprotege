package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.*;
import com.gwtext.client.data.event.StoreListener;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.grid.event.RowSelectionListenerAdapter;
import com.gwtext.client.widgets.layout.*;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AdminServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.ui.library.common.Filter;
import edu.stanford.bmir.protege.web.client.ui.library.common.Refreshable;
import edu.stanford.bmir.protege.web.client.ui.library.sidebar.SideBar;
import edu.stanford.bmir.protege.web.client.ui.library.sidebar.SideBarItem;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.projectlist.ProjectListDisplayImpl;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class OntologiesPortlet extends AbstractEntityPortlet implements Refreshable {

    private static String LINK_TO_PROTEGE_JNLP = "<a href=\"" + GWT.getModuleBaseURL() + "collabProtege_OWL.jnlp" + "\">here</a>";

    public static final String CREATE_ONTOLOGY_BUTTON_TEXT = "Create ontology...";

    public static final String UPLOAD_ONTOLOGY_BUTTON_TEXT = "Upload ontology...";

    public static final String MOVE_TO_TRASH_BUTTON_TEXT = "Move to trash...";

    public static final String DOWNLOAD_ONTOLOGY_BUTTON_TEXT = "Download ontology...";

    public static final String NAME_FIELD = "name";

    public static final String DESC_FIELD = "desc";

    public static final String OWNER_FIELD = "owner";
    
    public static final String LAST_MODIFIED_FIELD = "lastModified";



    protected GridPanel ontologiesGrid;

    protected HashMap<String, ProjectData> ontologies = new HashMap<String, ProjectData>();

    protected RecordDef recordDef;

    protected Store store;

    protected List<EntityData> currentSelection;

    // A list of components that should only be enabled for logged in users.
    private List<ObjectStateUpdater> updatableComponents;

    private SideBar<ProjectDataListSideBarItem> sideBar;

    public OntologiesPortlet() {
        super(null);
    }

    @Override
    public void reload() {
    }

    @Override
    public void initialize() {
        updatableComponents = new ArrayList<ObjectStateUpdater>();
        setTitle("Ontologies");
        setLayout(new FitLayout());

        Panel borderLayout = new Panel();
        borderLayout.setLayout(new BorderLayout());
        borderLayout.setSize(500, 500);
        add(borderLayout);


        sideBar = new SideBar<ProjectDataListSideBarItem>();
        rebuildSideBar();


        sideBar.addSelectionHandler(new SelectionHandler<ProjectDataListSideBarItem>() {
            public void onSelection(com.google.gwt.event.logical.shared.SelectionEvent<ProjectDataListSideBarItem> sideBarItemSelectionEvent) {
                refreshProjectList();
            }
        });
        sideBar.setWidth("200px");

        borderLayout.add(sideBar, new BorderLayoutData(RegionPosition.WEST));
//
        createGrid();
        borderLayout.add(ontologiesGrid, new BorderLayoutData(RegionPosition.CENTER));
        //add(createJNLPPanel()) ;
        createToolbar();

    }

    private void rebuildSideBar() {
        System.out.println("Rebuilding side bar");
        sideBar.clearItems();
        sideBar.addItem(new HomeItem());
        if (GlobalSettings.getGlobalSettings().getUserName() != null) {
            sideBar.addItem(new OwnedByMeItem());
            sideBar.addItem(new TrashItem());
        }
    }


    private void createToolbar() {
        

        Toolbar toolbar = new Toolbar();
        setTopToolbar(toolbar);
        ToolbarButton createOntologyButton = new ToolbarButton(CREATE_ONTOLOGY_BUTTON_TEXT);
        createOntologyButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                handleCreateOntology();
            }
        });
        toolbar.addButton(createOntologyButton);
        updatableComponents.add(new LoggedInWidgetStateUpdater(createOntologyButton));

        ToolbarButton uploadOntologyButton = new ToolbarButton(UPLOAD_ONTOLOGY_BUTTON_TEXT);
        uploadOntologyButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                handleUploadOntology();
            }
        });
        toolbar.addButton(uploadOntologyButton);
        updatableComponents.add(new LoggedInWidgetStateUpdater(uploadOntologyButton));

        ToolbarButton moveToTrashButton = new ToolbarButton(MOVE_TO_TRASH_BUTTON_TEXT);
        moveToTrashButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                handleMoveOntologyToTrash();
            }
        });
        toolbar.addButton(moveToTrashButton);
        updatableComponents.add(new TrashableSelectionUpdater(moveToTrashButton));

        ToolbarButton downloadButton = new ToolbarButton(DOWNLOAD_ONTOLOGY_BUTTON_TEXT);
        downloadButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                handleDownload();
            }
        });
        toolbar.addButton(downloadButton);
        updatableComponents.add(new DownloadableSelectionUpdater(downloadButton));

        updateLoginDependentComponents();

    }


    /**
     * Refreshes this refreshable.
     */
    public void refresh() {
        refreshProjectList();
    }

    /**
     * Handles a request to create a new ontology.
     */
    private void handleCreateOntology() {
        NewProjectDialog dlg = new NewProjectDialog(this);
        dlg.show();
    }

    /**
     * Handles a request to upload an ontology
     */
    private void handleUploadOntology() {
        UploadProjectDialog dlg = new UploadProjectDialog(this);
        dlg.show();
    }

    private void handleMoveOntologyToTrash() {
        List<String> selection = getSelectedOntologies();
        Set<ProjectId> projectIds = new HashSet<ProjectId>();
        
        for(final String selectedOntology : selection) {
            projectIds.add(new ProjectId(selectedOntology));
        }


        ProjectManagerServiceAsync service = GWT.create(ProjectManagerService.class);
        service.moveProjectsToTrash(projectIds, new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                MessageBox.alert("Could not move selected ontologies to trash.  Reason: " + caught.getMessage());
            }

            public void onSuccess(Void result) {
                refreshProjectList();
            }
        });
    }

    private void handleDownload() {
        List<String> selection = getSelectedOntologies();
        if(selection.isEmpty()) {
            return;
        }
        String projectName = selection.get(0);
        String encodedProjectName = URL.encode(projectName);
        com.google.gwt.user.client.Window.open("download?ontology=" + encodedProjectName, "Download ontology", "");
    }
    
    private List<String> getSelectedOntologies() {
        List<String> result = new ArrayList<String>();
        for(Record record : ontologiesGrid.getSelectionModel().getSelections()) {
            String ontologyName = record.getAsString(NAME_FIELD);
            result.add(ontologyName);
        }
        return result;
    }


    private HTML createJNLPPanel() {
        // FIXME: the <br> is a hack - fix in css
        return new HTML("<html><br><br><span class=\"jnlpPanel\">To open the same copy of an ontology" + " in the Prot&eacute;g&eacute rich client, click " + LINK_TO_PROTEGE_JNLP + " .</span><br><br></html>");
    }

    protected void createGrid() {
        ontologiesGrid = new GridPanel();
        ontologiesGrid.setAutoScroll(true);
        ontologiesGrid.setStripeRows(true);
        ontologiesGrid.setAutoExpandColumn("desc");
//        ontologiesGrid.setAutoHeight(true);
        ontologiesGrid.setHeight(500);
        ontologiesGrid.getSelectionModel().addListener(new RowSelectionListenerAdapter() {
            @Override
            public void onSelectionChange(RowSelectionModel sm) {
                updateLoginDependentComponents();
            }
        });


        createColumns();

        recordDef = new RecordDef(new FieldDef[]{new StringFieldDef(NAME_FIELD), new StringFieldDef(DESC_FIELD), new StringFieldDef(OWNER_FIELD)
//                new StringFieldDef(LAST_MODIFIED_FIELD)
                // new StringFieldDef("action")
        });


        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy dataProxy = new MemoryProxy(new Object[][]{});
        store = new Store(dataProxy, reader);
        store.load();
        ontologiesGrid.setStore(store);

        ontologiesGrid.addGridCellListener(new GridCellListenerAdapter() {
            @Override
            public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
                if (grid.getColumnModel().getDataIndex(colindex).equals("name")) {
                    Record record = grid.getStore().getAt(rowIndex);
                    String projectName = record.getAsString("name");

                    currentSelection = new ArrayList<EntityData>();
                    currentSelection.add(new EntityData(projectName));
                    GWT.log(projectName + " selected in OntologiesPortlet", null);
                    notifySelectionListeners(new SelectionEvent(OntologiesPortlet.this));
                }
            }
        });
    }

    @Override
    public void onPermissionsChanged(Collection<String> permissions) {
        super.onPermissionsChanged(permissions);
        updateLoginDependentComponents();
    }

    /**
     * Gets the owners of the selected ontologies
     * @return A set of strings representing the user names of the owners of the selected ontologies.
     */
    private Set<String> getSelectedProjectOwners() {
        Record[] selectedRecords = ontologiesGrid.getSelectionModel().getSelections();
        Set<String> selectedOwners = new HashSet<String>();
        for (Record selectedRecord : selectedRecords) {
            String owner = selectedRecord.getAsString(OWNER_FIELD);
            selectedOwners.add(owner);
        }
        return selectedOwners;
    }
    
    private Set<String> getSelectedProjectNames() {
        Record[] selectedRecords = ontologiesGrid.getSelectionModel().getSelections();
        Set<String> projectNames = new HashSet<String>();
        for (Record selectedRecord : selectedRecords) {
            String projectName = selectedRecord.getAsString(NAME_FIELD);
            projectNames.add(projectName);
        }
        return projectNames;
    }


    public void addStoreListener(StoreListener listener) {
        store.addStoreListener(listener);
    }

    protected void createColumns() {
        ColumnConfig nameCol = new ColumnConfig();
        nameCol.setHeader("Name");
        nameCol.setId("name");
        nameCol.setDataIndex("name");
        nameCol.setResizable(true);
        nameCol.setSortable(true);
        nameCol.setWidth(200);

        Renderer nameColRenderer = new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                return new String("<a href=\"#" + value.toString() + "\">" + value.toString() + "</a>");
            }
        };
        nameCol.setRenderer(nameColRenderer);

        ColumnConfig descCol = new ColumnConfig();
        descCol.setHeader("Description");
        descCol.setId("desc");
        descCol.setDataIndex("desc");
        descCol.setResizable(true);
        descCol.setSortable(true);

        ColumnConfig ownerCol = new ColumnConfig();
        ownerCol.setHeader("Owner");
        ownerCol.setId("owner");
        ownerCol.setDataIndex("owner");
        ownerCol.setResizable(true);
        ownerCol.setSortable(true);

//        ColumnConfig lastModifiedCol = new ColumnConfig();
//        lastModifiedCol.setHeader("Last Modified");
//        lastModifiedCol.setId("lastModified");
//        lastModifiedCol.setDataIndex(LAST_MODIFIED_FIELD);
//        lastModifiedCol.setResizable(true);
//        lastModifiedCol.setSortable(true);

        /*
         * ColumnConfig actionCol = new ColumnConfig();
         * actionCol.setHeader("Action"); actionCol.setId("action");
         * actionCol.setDataIndex("action"); actionCol.setResizable(true);
         * actionCol.setSortable(false);
         * actionCol.setTooltip("Open the same copy of the ontology in" +
         * "\n Collaborative Protege using Java Web Start");
         * actionCol.setWidth(200);
         */
        /*
         * Renderer actionColRenderer = new Renderer() { public String
         * render(Object value, CellMetadata cellMetadata, Record record, int
         * rowIndex, int colNum, Store store) { return new
         * String("<a href=\"\">view recent changes</a>"); } };
         * actionCol.setRenderer(actionColRenderer);
         */

        ColumnConfig[] columns = new ColumnConfig[]{nameCol, descCol, ownerCol};
        ColumnModel columnModel = new ColumnModel(columns);
        ontologiesGrid.setColumnModel(columnModel);
    }

    public ProjectData getOntologyData(String projectName) {
        return ontologies.get(projectName);
    }

    public List<EntityData> getSelection() {
        return currentSelection;
    }

    @Override
    protected void afterRender() {
        refreshProjectList();
    }

    @Override
    protected void onRefresh() {
        AdminServiceManager.getInstance().refreshMetaproject(new RefreshMetaProjectHandler());
    }

    public void refreshProjectList() {
        String userName = GlobalSettings.getGlobalSettings().getUserName();
        ProjectManagerServiceAsync pm = GWT.create(ProjectManagerService.class);
        UserId userId = UserId.getUserId(userName);
        pm.getProjects(userId, new GetProjectsHandler());
    }

    @Override
    public void onLogin(String userName) {
        rebuildSideBar();
        refreshProjectList();
        updateLoginDependentComponents();
    }

    @Override
    public void onLogout(String userName) {
        rebuildSideBar();
        refreshProjectList();
        updateLoginDependentComponents();
    }

    public void updateLoginDependentComponents() {
        doLayout();
        for(ObjectStateUpdater updater : updatableComponents) {
            updater.updateState();
        }

    }


    /*
     * Remote calls
     */

    class GetProjectsHandler extends AbstractAsyncHandler<List<ProjectData>> {

        @Override
        public void handleFailure(Throwable caught) {
            store.removeAll();
            GWT.log("RPC error getting ontologies from server", caught);
            MessageBox.alert("Error", "There was an error retrieving ontologies from server: " + caught.getMessage());
        }

        @Override
        public void handleSuccess(List<ProjectData> projectsData) {
            store.removeAll();
            for (ProjectData data : projectsData) {
                ProjectDataListSideBarItem item = sideBar.getSelectedItem();
                if(item.isIncluded(data)) {
                    ontologies.put(data.getName(), data);
//                    String modificationHistoryRendering = " - ";
//                    if (data.getLastModified() != 0) {
//                        Date lastModifiedDate = new Date(data.getLastModified());
//                        modificationHistoryRendering = DateTimeFormat.getFormat("EEE dd MMMM 'at' HH:mm").format(lastModifiedDate);
//                        if(!data.getLastModifiedBy().isEmpty()) {
//                            modificationHistoryRendering += "<span style=\" color:grey;\"> by " + data.getLastModifiedBy() + "</span>";
//                        }
//                    }
                    Record record = recordDef.createRecord(new Object[]{data.getName(), data.getDescription(), data.getOwner()});
                    store.add(record);
                }
            }
            updateLoginDependentComponents();
        }
    }

    class RefreshMetaProjectHandler extends AbstractAsyncHandler<Void> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("RPC error at refreshing metaproject on the server", caught);
            com.google.gwt.user.client.Window.alert("There were errors at refreshing metaproject information from server.");
        }

        @Override
        public void handleSuccess(Void result) {
            refreshProjectList();
        }

    }


    private interface ProjectDataListSideBarItem extends SideBarItem, Filter<ProjectData> {

    }


    private class HomeItem implements ProjectDataListSideBarItem {

        public boolean isIncluded(ProjectData object) {
            return !object.isInTrash();
        }

        /**
         * Gets the label of this thing.
         * @return A string representing the label
         */
        public String getLabel() {
            return "Home";
        }
    }
    
    private class OwnedByMeItem implements ProjectDataListSideBarItem {

        private String label = "Owned by me";
        
        private OwnedByMeItem() {
        }

        public boolean isIncluded(ProjectData object) {
            if(object.isInTrash()) {
                return false;
            }
            String userName = GlobalSettings.getGlobalSettings().getUserName();
            if(userName == null) {
                return false;
            }
            String projectOwner = object.getOwner();
            return projectOwner != null && projectOwner.equals(userName);
        }

        /**
         * Gets the label of this thing.
         * @return A string representing the label
         */
        public String getLabel() {
            return label;
        }
    }
    

    private class TrashItem implements ProjectDataListSideBarItem {

        public boolean isIncluded(ProjectData object) {
            String loggedInUser = GlobalSettings.getGlobalSettings().getUserName();
            String owner = object.getOwner();
            boolean ownedByLoggedInUser = loggedInUser != null && owner != null && owner.equals(loggedInUser);
            return ownedByLoggedInUser && object.isInTrash();
        }

        /**
         * Gets the label of this thing.
         * @return A string representing the label
         */
        public String getLabel() {
            return "Trash";
        }
    }




    private interface ObjectStateUpdater {

        void updateState();
    }



    private class LoggedInWidgetStateUpdater implements ObjectStateUpdater {

        private Component component;

        private LoggedInWidgetStateUpdater(Component component) {
            this.component = component;
        }

        public void updateState() {
            boolean loggedIn = GlobalSettings.getGlobalSettings().isLoggedIn();
            component.setDisabled(!loggedIn);
        }
    }
    
    
    private class TrashableSelectionUpdater implements ObjectStateUpdater {

        private Component component;

        private TrashableSelectionUpdater(Component component) {
            this.component = component;
        }

        public void updateState() {
            component.setDisabled(true);
            boolean loggedIn = GlobalSettings.getGlobalSettings().isLoggedIn();
            if(!loggedIn) {
                return;
            }
            Set<String> ownersOfSelectedOntologies = getSelectedProjectOwners();
            if(ownersOfSelectedOntologies.size() != 1) {
                return;
            }
            String userName = GlobalSettings.getGlobalSettings().getUserName();
            if(!ownersOfSelectedOntologies.contains(userName)) {
                return;
            }
            for(String projectName : getSelectedProjectNames()) {
                ProjectData data = ontologies.get(projectName);
                if(data == null) {
                    return;
                }
                if(data.isInTrash()) {
                    return;
                }
            }
            component.setDisabled(false);
        }
    }
    
    
    private class DownloadableSelectionUpdater implements ObjectStateUpdater {

        private Component component;

        private DownloadableSelectionUpdater(Component component) {
            this.component = component;
        }

        public void updateState() {
            component.setDisabled(true);
            Set<String> selectedProjectNames = getSelectedProjectNames();
            component.setDisabled(selectedProjectNames.size() != 1);
        }
    }
    
}

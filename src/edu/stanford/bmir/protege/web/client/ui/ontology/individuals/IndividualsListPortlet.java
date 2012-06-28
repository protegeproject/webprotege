package edu.stanford.bmir.protege.web.client.ui.ontology.individuals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.event.StoreListener;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.search.SearchUtil;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * Portlet for showing a list of individuals. The list is filled with the
 * instances of the class given as argument to <code>setEntity</code> method.
 * Normally, it is used together with the class tree portlet. The portlet can
 * also be configured in the configuration file to show always only the
 * instances of a certain class by setting a property of the portlet
 * <code>showOnlyClass</code> to point to a class.
 *
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class IndividualsListPortlet extends AbstractEntityPortlet {

    private static final String PRECONFIGURED_CLASS = "showOnlyClass";

    protected GridPanel individualsGrid;
    protected RecordDef recordDef;
    protected Store store;
    protected GridRowListener gridRowListener;

    /*
     * Retrieved from the project configuration. If it is set,
     * then the individuals list will always display the instances
     * of the preconfigured class.
     */
    protected EntityData preconfiguredClass;

    private ToolbarButton createButton;
    private ToolbarButton deleteButton;
    private ToolbarButton watchButton;

    private Renderer renderer = new Renderer() {
        public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                Store store) {
            String val = UIUtil.getDisplayText(value);;
            if (isWatched(record)) {
                return val + " (W)";
            } else {
                return val;
            }
        }
    };

    public IndividualsListPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setLayout(new FitLayout());
        setTitle("Individuals");

        createGrid();
        add(individualsGrid);
        individualsGrid.addGridRowListener(getRowListener());

        addToolbarButtons();

        initConfiguration();
        if (preconfiguredClass != null) {
            setEntity(preconfiguredClass);
        }
    }

    @Override
    public void setPortletConfiguration(PortletConfiguration portletConfiguration) {
        super.setPortletConfiguration(portletConfiguration);
        initConfiguration();
        if (preconfiguredClass != null) {
            setEntity(preconfiguredClass);
        }
    }

    private void initConfiguration() {
        PortletConfiguration config = getPortletConfiguration();
        if (config == null) {
            return;
        }
        Map<String, Object> properties = config.getProperties();
        if (properties == null) {
            return;
        }
        String preconfiguredClassName = (String) properties.get(PRECONFIGURED_CLASS);
        preconfiguredClass = new EntityData(preconfiguredClassName);
    }

    @Override
    public void reload() {
        if (_currentEntity != null) {
            setTitle("Individuals for " + _currentEntity.getBrowserText());
        }

        setEntity(_currentEntity);
    }

    @Override
    public void setEntity(EntityData newEntity) {
        setTitle(newEntity == null ? "Individuals (nothing selected)" : " Individuals for " + newEntity.getBrowserText());
        if (preconfiguredClass != null) {
            newEntity = preconfiguredClass;
        }
        if (_currentEntity != null) {
            store.removeAll();
        }
        _currentEntity = newEntity;
        if (_currentEntity == null) {
            return;
        }

        OntologyServiceManager.getInstance().getIndividuals(project.getProjectName(), _currentEntity.getName(),
                new GetIndividuals());
    }

    protected GridRowListener getRowListener() {
        if (gridRowListener == null) {
            gridRowListener = new GridRowListenerAdapter() {
                @Override
                public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                    notifySelectionListeners(new SelectionEvent(IndividualsListPortlet.this));
                    super.onRowClick(grid, rowIndex, e);
                }
            };
        }
        return gridRowListener;
    }

    protected void createGrid() {
        recordDef = new RecordDef(new FieldDef[] { new ObjectFieldDef("individuals"), new BooleanFieldDef("watched") });

        individualsGrid = new GridPanel();
        createColumns();

        individualsGrid.setHeight(590);
        individualsGrid.setAutoWidth(true);
        individualsGrid.setAutoExpandColumn("individuals");

        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy dataProxy = new MemoryProxy(new Object[][] {});
        store = new Store(dataProxy, reader);
        store.load();
        individualsGrid.setStore(store);
    }

    protected void createColumns() {
        ColumnConfig indCol = new ColumnConfig();
        indCol.setHeader("Name");
        indCol.setId("individuals");
        indCol.setDataIndex("individuals");
        indCol.setResizable(true);
        indCol.setSortable(true);

        ColumnConfig watchedCol = new ColumnConfig();
        watchedCol.setHeader("Watched");
        watchedCol.setId("watched");
        watchedCol.setDataIndex("watched");
        watchedCol.setHidden(true);

        //indCol.setRenderer(renderer); //TODO: does not work - bug in gwt-ext

        ColumnConfig[] columns = new ColumnConfig[] { indCol, watchedCol };

        ColumnModel columnModel = new ColumnModel(columns);
        individualsGrid.setColumnModel(columnModel);
    }

    private void setWatched(Record record, boolean watched) {
        record.set("watched",watched);
        //workaround for not being able to set custom renderer
        EntityData ed = (EntityData) record.getAsObject("individuals");
        if (ed != null) {
            ed.setBrowserText(ed.getBrowserText() + (watched ? " (W)" : "")); //TODO this will not work fine
            Record copyRec = recordDef.createRecord(new Object[] { ed, watched });
            int index = store.indexOf(record);
            store.remove(record);
            store.insert(index, copyRec);
        }
    }

    private boolean isWatched(Record record) {
        return record.getAsBoolean("watched");
    }

    protected void addToolbarButtons() {
        setTopToolbar(new Toolbar());
        Toolbar toolbar = getTopToolbar();

        createButton = new ToolbarButton("Create");
        createButton.setCls("toolbar-button");
        createButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                onCreateIndividual();
            }
        });
        createButton.setDisabled(!project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName()));
        toolbar.addButton(createButton);


        deleteButton = new ToolbarButton("Delete");
        deleteButton.setCls("toolbar-button");
        deleteButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                onDeleteIndividual();
            }
        });
        deleteButton.setDisabled(!project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName()));
        toolbar.addButton(deleteButton);

        watchButton = new ToolbarButton("Watch");
        watchButton.setCls("toolbar-button");
        watchButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                onWatchIndividual();
            }
        });
        watchButton.setDisabled(!GlobalSettings.getGlobalSettings().isLoggedIn());
        toolbar.addButton(watchButton);

        Component searchField = createSearchField();
        if (searchField != null) {
            toolbar.addFill();
            toolbar.addSeparator();
            toolbar.addText("&nbsp<i>Search</i>:&nbsp&nbsp");
            toolbar.addElement(searchField.getElement());
        }
    }

    protected void onWatchIndividual() {
        Record selectedRec = individualsGrid.getSelectionModel().getSelected();

        if (selectedRec == null || isWatched(selectedRec) ) {
            return;
        }

        EntityData entityData = (EntityData) selectedRec.getAsObject("individuals");
        if (entityData != null) {
            ChAOServiceManager.getInstance().addWatchedEntity(project.getProjectName(),
                    GlobalSettings.getGlobalSettings().getUserName(), entityData.getName(),
                    new AddWatchedIndividual(selectedRec));
        }
    }

    protected void onDeleteIndividual() {
        final Record selRecord = individualsGrid.getSelectionModel().getSelected();
        if (selRecord == null) {
            Window.alert("Please select first an individual to delete.");
            return;
        }

        final EntityData currentSelection = (EntityData) selRecord.getAsObject("individuals");
        final String indName = currentSelection.getName();

        MessageBox.confirm("Confirm", "Are you sure you want to delete individual <br> " + indName + " ?",
                new MessageBox.ConfirmCallback() {
            public void execute(String btnID) {
                if (btnID.equals("yes")) {
                    deleteIndividual(currentSelection, selRecord);
                }
            }
        });
    }

    protected void deleteIndividual(EntityData indEntity, Record selRecord) {
        if (indEntity == null) { return; }

        OntologyServiceManager.getInstance().deleteEntity(project.getProjectName(), indEntity.getName(),
                GlobalSettings.getGlobalSettings().getUserName(), "Deleted individual " + indEntity.getBrowserText(),
                new DeleteIndividualHandler(selRecord));

        refreshFromServer(500);
    }

    protected void onCreateIndividual() {
        if (_currentEntity == null) {
            MessageBox.prompt("No type selected", "No type selected for the new individual. <br />" +
                    "If no type is selected, the new individual will have as type the root concept. <br />" +
                    "Do you want to continue?", new PromptCallback() {
                public void execute(String btnID, String text) {
                    if (!btnID.equalsIgnoreCase("ok")) {
                        return;
                    }
                }
            });
        }

        MessageBox.prompt("Name", "Please enter an individual name (if no name is specified, one will be autogenerated):", new MessageBox.PromptCallback() {
            public void execute(String btnID, String text) {
                if (text != null && text.length() == 0) {
                    text = null;
                }

                OntologyServiceManager.getInstance().createInstance(getProject().getProjectName(), text,
                        _currentEntity == null ? null : _currentEntity.getName(),
                        GlobalSettings.getGlobalSettings().getUserName(),
                        getCreateOperationDescription(text, _currentEntity), new CreateIndividualHandler());
            }
        });
    }

    protected String getCreateOperationDescription(String individualName, EntityData typeEntity) {
        return "Created new individual " + (individualName == null ? "" : individualName) + " of type " + (typeEntity == null ? " root concept" : typeEntity.getBrowserText()) ;
    }

    protected Component createSearchField() {
        final TextField searchField = new TextField("Search: ", "search");
        searchField.setAutoWidth(true);
        searchField.setEmptyText("Type search string");
        searchField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onSpecialKey(Field field, EventObject e) {
                if (e.getKey() == EventObject.ENTER) {
                    SearchUtil su = new SearchUtil(project, IndividualsListPortlet.this);
                    //su.setBusyComponent(searchField);  //this does not seem to work
                    su.setBusyComponent(getTopToolbar());
                    su.setSearchedValueType(ValueType.Instance);
                    su.search(searchField.getText());
                }
            }
        });
        return searchField;
    }

    public void addStoreListener(StoreListener listener){
        store.addStoreListener(listener);
    }

    @Override
    public void setSelection(Collection<EntityData> selection) {
    	ArrayList<Record> selectedRecords = new ArrayList<Record>();
    	Record[] allRecords = individualsGrid.getStore().getRecords();
    	if (selection != null && allRecords != null) {
			for (Record record : allRecords) {
	    		if (selection.contains(record.getAsObject("individuals"))) {
	    			selectedRecords.add(record);
	    		}
	    	}
	    }
    	individualsGrid.getSelectionModel().selectRecords(selectedRecords.toArray(new Record[0]));
    	notifySelectionListeners(new SelectionEvent(IndividualsListPortlet.this));
    }

    public List<EntityData> getSelection() {
        return getMultipleSelection();
    }

    public EntityData getSingleSelection() {
        Record selRecord = individualsGrid.getSelectionModel().getSelected();
        if (selRecord == null) { return null; }
        return (EntityData) selRecord.getAsObject("individuals");
    }

    public List<EntityData> getMultipleSelection() {
        Record[] selRecords = individualsGrid.getSelectionModel().getSelections();
        if (selRecords == null || selRecords.length == 0) {
            return null;
        }
        List<EntityData> selection = new ArrayList<EntityData>();
        for (int i = 0; i < selRecords.length; i++) {
            selection.add((EntityData) selRecords[i].getAsObject("individuals"));
        }
        return selection;
    }

    @Override
    public void onPermissionsChanged(Collection<String> permissions) {
        updateButtonStates();
    }

    public void updateButtonStates() {
        if (project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName())) {
            createButton.enable();
            deleteButton.enable();
        } else {
            createButton.disable();
            deleteButton.disable();
        }
        if (GlobalSettings.getGlobalSettings().isLoggedIn()) {
            watchButton.enable();
        } else {
            watchButton.disable();
        }
    }

    /*
     * Remote calls
     */

    class GetIndividuals extends AbstractAsyncHandler<List<EntityData>> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at getting individuals for " + _currentEntity, caught);
        }

        @Override
        public void handleSuccess(List<EntityData> indData) {
            for (EntityData instData : indData) {
                Record record = recordDef.createRecord(new Object[] { instData, false });
                store.add(record);
            }
        }
    }

    class AddWatchedIndividual extends AbstractAsyncHandler<EntityData> {

        private Record record;

        public AddWatchedIndividual(Record record) {
            this.record = record;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at add watched entity", caught);
            MessageBox.alert("Error", "There was an error at adding the new watched entity. Pleas try again later.");
        }

        @Override
        public void handleSuccess(EntityData entityData) {
            setWatched(record, true);
        }
    }

    class CreateIndividualHandler extends AbstractAsyncHandler<EntityData> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at creating individual", caught);
            MessageBox.alert("Error", "There was an error at creating the individual.<br />" +
            		"Please try again later.");
            reload();
        }

        @Override
        public void handleSuccess(EntityData individualEntity) {
            Record record = recordDef.createRecord(new Object[] { individualEntity, false });
            store.add(record);
            individualsGrid.getSelectionModel().selectRecords(record);
            notifySelectionListeners(new SelectionEvent(IndividualsListPortlet.this));
        }
    }

    class DeleteIndividualHandler extends AbstractAsyncHandler<Void> {

        private Record indRecord;

        public DeleteIndividualHandler(Record indRecord) {
            this.indRecord = indRecord;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at deleting individual", caught);
            MessageBox.alert("Error", "There was an error at deleting the individual.<br />" +
                "Please try again later.");
            reload();
        }

        @Override
        public void handleSuccess(Void result) {
            store.remove(indRecord);
        }
    }

}

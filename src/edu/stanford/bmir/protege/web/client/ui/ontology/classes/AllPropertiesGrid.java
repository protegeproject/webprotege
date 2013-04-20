package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.*;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.grid.event.*;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.model.PropertyValueUtil;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.ontology.properties.PropertiesTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormConstants;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.util.SelectionUtil;
import edu.stanford.bmir.protege.web.client.ui.util.SelectionUtil.SelectionCallback;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A grid that shows all the properties of an entity. Can be used with classes,
 * properties, individuals. Works both for OWL and Frames ontologies.
 *
 * In current implementation supports basic editing of string values by
 * double-clicking on a cell.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class AllPropertiesGrid extends EditorGridPanel {

    private static final String PROPERTY = "property";
    private static final String VALUE = "value";
   // only used as a workaround: the grid does not allow a renderer on an object field...
    private static final String VALUE_BROWSER_TEXT = "valueBt";
    private static final String LANGUAGE = "language";

    private Project project;
    private EntityData _currentEntity;
    private RecordDef recordDef;
    private GroupingStore store;

    private GridRowListener gridRowListener;
    private GridCellListener gridCellListener;
    private EditorGridListener editorGridListener;

    private Collection<EntityData> currentSelection;
    private PropertyValueUtil propertyValueUtil;

    public AllPropertiesGrid(Project project) {
        this.project = project;
        this.propertyValueUtil = new PropertyValueUtil();

        createGrid();

        addGridRowListener(getRowListener());
        addGridCellListener(getGridCellListerner());
        addEditorGridListener(getEditorGridListener());
    }


    public void setEntity(EntityData newEntity) {
        if (_currentEntity != null && _currentEntity.equals(newEntity)) {
            return;
        }
        _currentEntity = newEntity;
        refresh();
    }

    public void refresh() {

        store.removeAll();
        // store.clearGrouping();
        if (_currentEntity == null) {
            return;
        }
        reload();
    }

    protected GridCellListener getGridCellListerner() {
       if (gridCellListener == null) {
           gridCellListener = new GridCellListenerAdapter() {
              @Override
            public void onCellDblClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
                 onCellDoubleClick(rowIndex, colindex);
              };
           };
       }
        return gridCellListener;
    }

    protected void onCellDoubleClick(int rowIndex, int colindex) {
        //do something - for future use
    }


    protected void handleAddInstanceValue(final PropertyEntityData prop) {
        if (!(ValueType.Instance.equals(prop.getValueType()))) {
            return;
        }
        SelectionUtil.selectIndividuals(project, prop.getAllowedValues(), true, true, new SelectionCallback() {
            public void onSelect(Collection<EntityData> selection) {
                if (selection == null) { return; }
                //TODO: optimize: make one call for all values
                for (EntityData instVal : selection) {
                    addPropertyValue(_currentEntity.getName(), prop.getName(), prop.getValueType(), instVal.getName(), getAddValueOpDescription(prop, instVal));
                }
            }
        });
    }
    
    protected void handleAddClassValue(final PropertyEntityData prop) {
        if(!(ValueType.Instance.equals(prop.getValueType()))) {
            return;
        }
        SelectionUtil.selectClses(project, true, new SelectionCallback() {
            public void onSelect(Collection<EntityData> selection) {
                if(selection == null) {
                    for(EntityData entityData : selection) {
                        addPropertyValue(_currentEntity.getName(), prop.getName(), prop.getValueType(), entityData.getName(), getAddValueOpDescription(prop, entityData));
                    }
                }
            }
        });
    }

    private String getAddValueOpDescription(PropertyEntityData prop, EntityData value) {
        return UIUtil.getAppliedToTransactionString("Added " + UIUtil.getDisplayText(value) + " as value for property " + UIUtil.getDisplayText(prop) + " at " +
                UIUtil.getDisplayText(_currentEntity), _currentEntity.getName());
    }


    protected void addPropertyValue(String entityName, String propName, ValueType propValueType, String newValue, String operationDescription) {
        propertyValueUtil.addPropertyValue(project.getProjectId(), entityName, propName, propValueType, newValue,
                Application.get().getUserId(), operationDescription, new ReplacePropertyValueHandler());
    }

    protected GridRowListener getRowListener() {
        if (gridRowListener == null) {
            gridRowListener = new GridRowListenerAdapter() {
                @Override
                public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                    PropertyEntityData property = (PropertyEntityData)(store.getAt(rowIndex).getAsObject(PROPERTY));
                    currentSelection = new ArrayList<EntityData>();
                    currentSelection.add(property);

                    super.onRowClick(grid, rowIndex, e);
                }
            };
        }
        return gridRowListener;
    }

    private boolean isStringOrAnyValueType(EntityData prop) {
        ValueType vt = prop.getValueType();
        return vt == null || ValueType.String.equals(vt) || ValueType.Literal.equals(vt) || ValueType.Any.equals(vt);
    }

    protected EditorGridListener getEditorGridListener() {
        if (editorGridListener == null) {
            editorGridListener = new EditorGridListenerAdapter() {
                @Override
                public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex,
                        int colIndex) {

                    if (!project.hasWritePermission(Application.get().getUserId())) {
                        return false;
                    } // this editor only handles the editing of strings and any values
                    return isStringOrAnyValueType((PropertyEntityData) (store.getAt(rowIndex).getAsObject(PROPERTY)));
                }

                @Override
                public void onAfterEdit(GridPanel grid, Record record, String field, Object newValueO, Object oldValueO,
                        int rowIndex, int colIndex) {
                    // special handling rdfs:Literal

                    // TODO:
                    EntityData rowEntity = (EntityData) record.getAsObject(VALUE);
                    ValueType valueTypeT = rowEntity == null ? null : rowEntity.getValueType();

                    String oldPropValue = null;
                    String newPropValue = null;

                    String newValueS = newValueO.toString();
                    String oldValueS = oldValueO.toString();

                    if (field.equals(VALUE_BROWSER_TEXT)) { //edit the property value
                        String lang = record.getAsString(LANGUAGE);
                        if (lang != null && lang.length() > 0) {
                            newPropValue = "~#" + lang + " " + newValueS;
                            oldPropValue = "~#" + lang + " " + oldValueS;
                            valueTypeT = ValueType.Literal;
                        } else {
                            newPropValue = newValueS;
                            oldPropValue = oldValueS;
                        }
                    } else if (field.equals(LANGUAGE)) { //edit the language
                        String propValue = record.getAsString(VALUE_BROWSER_TEXT);
                        if (oldValueO == null || oldValueS.length() == 0) {
                            oldPropValue = propValue;
                        } else {
                            oldPropValue = "~#" + oldValueS + " " + propValue;
                        }
                        if (newValueO == null || newValueS.length() == 0) {
                            newPropValue = propValue;
                        } else {
                            newPropValue = "~#" + newValueS + " " + propValue;
                        }
                        valueTypeT = ValueType.Literal;
                    }

                    PropertyEntityData prop = (PropertyEntityData) (store.getAt(rowIndex).getAsObject(PROPERTY));
                    replacePropertyValue(_currentEntity.getName(), prop.getName(), valueTypeT,
                            oldPropValue, newPropValue, null); //no op description
                }
            };
        }
        return editorGridListener;
    }

    // TODO: assume value value type is the same as the property value type, fix
    // later
    protected void replacePropertyValue(String entityName, String propName, ValueType propValueType, String oldValue,
            String newValue, String operationDescription) {
        propertyValueUtil.replacePropertyValue(project.getProjectId(), entityName, propName, propValueType, oldValue,
                newValue, Application.get().getUserId(), operationDescription,
                new ReplacePropertyValueHandler());
    }

    protected void deletePropertyValue(String entityName, String propName, ValueType propValueType, String value,
            String operationDescription) {
        propertyValueUtil.deletePropertyValue(project.getProjectId(), entityName, propName, propValueType, value,
                Application.get().getUserId(), operationDescription,
                new RemovePropertyValueHandler());

    }

    protected void createGrid() {
        createColumns();

        recordDef = new RecordDef(new FieldDef[] {  new ObjectFieldDef(PROPERTY),
                new ObjectFieldDef(VALUE), new StringFieldDef(VALUE_BROWSER_TEXT), new StringFieldDef(LANGUAGE) });

        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy dataProxy = new MemoryProxy(new Object[][] {});
        store = new GroupingStore(dataProxy, reader);
        store.setSortInfo(new SortState(PROPERTY, SortDir.ASC));
        setStore(store);


        createButton = new ToolbarButton("Add property value");
        createButton.setCls("toolbar-button");
        createButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                onAddPropertyValue();
            }
        });
        if (!project.hasWritePermission(Application.get().getUserId())) {
            createButton.disable();
        }

        deleteButton = new ToolbarButton("Delete property value");
        deleteButton.setCls("toolbar-button");
        if (!project.hasWritePermission(Application.get().getUserId())) {
            deleteButton.disable();
        }
        deleteButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                int selectedRow = getCellSelectionModel().getSelectedCell()[0];
                if (selectedRow < 0) {
                    return;
                }
                onDelete(store.getAt(selectedRow));
            }
        });
        setTopToolbar(new Button[] { createButton, deleteButton });


        /*
         * GroupingView gridView = new GroupingView();
         * gridView.setForceFit(true);gridView.setGroupTextTpl(
         * "{text} ({[values.rs.length]} {[values.rs.length > 1 ? \"Values\" : \"Value\"]})"
         * );
         */

        GridView gridView = new GridView();
        gridView.setAutoFill(true);
        //gridView.setScrollOffset(0);

        setHeight(200);
        setAutoWidth(true);
        setLoadMask("Loading properties");

        setStripeRows(true);
        setAutoExpandColumn(VALUE_BROWSER_TEXT);
        setAnimCollapse(true);
        setClicksToEdit(
                UIUtil.getIntegerConfigurationProperty(
                        project.getProjectLayoutConfiguration(),
                        FormConstants.CLICKS_TO_EDIT, 
                        FormConstants.DEFAULT_CLICKS_TO_EDIT));
        setView(gridView);

        store.load();

    }

    protected void createColumns() {
        ColumnConfig propCol = new ColumnConfig();
        propCol.setHeader("Property");
        propCol.setId(PROPERTY);
        propCol.setDataIndex(PROPERTY);
        propCol.setResizable(true);
        propCol.setSortable(true);
       // propCol.setRenderer(propRenderer);

        ColumnConfig valueCol = new ColumnConfig();
        valueCol.setHeader("Value");
        valueCol.setId(VALUE_BROWSER_TEXT);
        valueCol.setDataIndex(VALUE_BROWSER_TEXT);
        valueCol.setResizable(true);
        valueCol.setSortable(true);
        valueCol.setCss("word-wrap: break-word ;");
        valueCol.setRenderer(valueRenderer);
        valueCol.setEditor(new GridEditor(new TextField()));

        ColumnConfig languageCol = new ColumnConfig();
        languageCol.setHeader("Lang");
        languageCol.setId(LANGUAGE);
        languageCol.setDataIndex(LANGUAGE);
        languageCol.setResizable(true);
        languageCol.setSortable(true);
        languageCol.setWidth(30);
        languageCol.setEditor(new GridEditor(new TextField()));

        ColumnConfig[] columns = new ColumnConfig[] { propCol, valueCol, languageCol };

        ColumnModel columnModel = new ColumnModel(columns);
        setColumnModel(columnModel);

    }

    Renderer valueRenderer = new Renderer() {
        public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                Store store) {
            if (value == null) {
                return "";
            }
            EntityData ed = ((EntityData) record.getAsObject(VALUE));
            PropertyEntityData prop = (PropertyEntityData) store.getAt(rowIndex).getAsObject(PROPERTY);
            if (ValueType.Instance.equals(prop.getValueType())) {
                return Format.format(
                        "<img src=\"../images/tree/class.gif\" /><style type=\"text/css\">.x-grid3-cell-inner, .x-grid3-hd-inner { white-space:normal !important; }</style> {0}",
                        new String[] { ed.getBrowserText() });
            } else {
                return Format.format(
                        "<style type=\"text/css\">.x-grid3-cell-inner, .x-grid3-hd-inner { white-space:normal !important; }</style> {0}",
                        new String[] {  value == null ? "" : value.toString() });
            }
        }
    };

//    Renderer propRenderer = new Renderer() {
//        public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
//                Store store) {
//            return Format.format(
//                    "<style type=\"text/css\">.x-grid3-cell-inner, .x-grid3-hd-inner { white-space:normal !important; }</style> {0}",
//                    new String[] { ((EntityData)record.getAsObject(PROPERTY)).getBrowserText() });
//        }
//    };


    private ToolbarButton createButton;
    private ToolbarButton deleteButton;

    public void updateButtonStates() {
        if (project.hasWritePermission(Application.get().getUserId())) {
            createButton.enable();
            deleteButton.enable();
        } else {
            createButton.disable();
            deleteButton.disable();
        }
    }


    public void reload() {
        store.removeAll();
        OntologyServiceManager.getInstance().getEntityTriples(project.getProjectId(), _currentEntity.getName(),
                new GetTriplesHandler());
    }

    public Collection<EntityData> getSelection() {
        return currentSelection;
    }

    protected void onDelete(Record record) {
        EntityData valueEd = ((EntityData) record.getAsObject(VALUE));
        String value = valueEd.getName();
        if (valueEd == null || value == null) {
            return; //nothing to delete
        }

        ValueType valueType = valueEd.getValueType();
        String lang = record.getAsString(LANGUAGE);
        if (lang != null && lang.length() > 0) {
            valueType = ValueType.Literal; //TODO: check if necessary
        }
        PropertyEntityData prop = (PropertyEntityData) (record.getAsObject(PROPERTY));

        deletePropertyValue(_currentEntity.getName(), prop.getName(), valueType, value, null); //TODO: how to handle operationDescription
    }

    protected void onAddPropertyValue() {
        final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window();
        window.setTitle("Select value");
        window.setClosable(true);
        window.setPaddings(7);
        window.setWidth(250);
        window.setHeight(350);
        window.setLayout(new FitLayout());
        window.setCloseAction(com.gwtext.client.widgets.Window.HIDE);
        window.add(new SelectionDialog(window, createSelectable()));
        window.show();
    }

    public Selectable createSelectable() {
        PropertiesTreePortlet propertiesTreePortlet = new PropertiesTreePortlet(project);
        return propertiesTreePortlet;
    }

    protected void addEmptyPropertyRow(PropertyEntityData prop) {
        Record record = recordDef.createRecord(new Object[] { prop, null, "", null });
        stopEditing();
        store.insert(0, record);
        if(ValueType.Cls.equals(_currentEntity.getValueType())) {
//            if (isStringOrAnyValueType(prop)) {
//                startEditing(0, 1);
            if (ValueType.Instance.equals(prop.getValueType())){
                handleAddClassValue(prop);
            }
        }
        else {
            if (isStringOrAnyValueType(prop)) {
                startEditing(0, 1);
            } else if (ValueType.Instance.equals(prop.getValueType())){
                handleAddInstanceValue(prop);
            }
        }
        

    }

    /*
     * Remote calls
     */

    class GetTriplesHandler extends AbstractAsyncHandler<List<Triple>> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at getting triples for " + _currentEntity, caught);
        }

        @Override
        public void handleSuccess(List<Triple> triples) {
            store.removeAll();
            if (triples == null) {return;}
            for (Triple triple : triples) {
                EntityData value = triple.getValue();
                String str = value.getName();
                String lan = "";
                if (isLiteralWithLang(str)) {
                    lan = getLang(str);
                    str = getText(str);
                } else {
                    str = UIUtil.getDisplayText(value);
                }
                str = UIUtil.replaceEOLWithBR(str);

                Record record = recordDef.createRecord(new Object[] {triple.getProperty(), value, str, lan});
                store.add(record);
            }
        }

        private boolean isLiteralWithLang(String str) {
            int indexOfSpace = str.indexOf(" ");
            return str.indexOf("~#") == 0 && indexOfSpace > 0;
        }

        private String getLang(String str) {
            int indexOfSpace = str.indexOf(" ");
            return str.substring(2, indexOfSpace);
        }

        private String getText(String str) {
            int indexOfSpace = str.indexOf(" ");
            return str.substring(indexOfSpace + 1);
        }

    }

    class ReplacePropertyValueHandler extends AbstractAsyncHandler<Void> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at replace property value for " + _currentEntity, caught);
            Window.alert("There was an error at setting the property value for " + _currentEntity.getBrowserText()
                    + ".<br>Please try again later.");
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("Success at setting property value for " + _currentEntity.getBrowserText(), null);
            refresh();
        }
    }

    class RemovePropertyValueHandler extends AbstractAsyncHandler<Void> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at removing property value for " + _currentEntity, caught);
            Window.alert("There was an error at removing the property value for " + _currentEntity.getBrowserText()
                    + ".<br>Please try again later.");
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("Success at removing property value for " + _currentEntity.getBrowserText(), null);
            refresh();
        }

    }

    /*
     * Internal class - to be refactored
     */

    class SelectionDialog extends Panel {
        private com.gwtext.client.widgets.Window parent;
        private Selectable selectable;

        public SelectionDialog(com.gwtext.client.widgets.Window parent, Selectable selectable) {
            super();
            this.parent = parent;
            this.selectable = selectable;

            Button selectButton = new Button("Select", new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    Collection<EntityData> selection = SelectionDialog.this.selectable.getSelection();
                    if (selection != null && selection.size() > 0) {
                        EntityData singleSelection = selection.iterator().next();
                        SelectionDialog.this.parent.close();
                        addEmptyPropertyRow((PropertyEntityData) singleSelection);
                    }
                }
            });

            setLayout(new FitLayout());
            add((Widget) selectable);
            addButton(selectButton);
        }
    }
    


}

package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.ComboBoxCallback;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListener;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.model.PropertyValueUtil;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityPropertyValues;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractPropertyWidgetWithNotes;
import edu.stanford.bmir.protege.web.client.ui.util.SelectionUtil;
import edu.stanford.bmir.protege.web.client.ui.util.SelectionUtil.SelectionCallback;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class InstanceGridWidget extends AbstractPropertyWidgetWithNotes {

    protected static String INSTANCE_FIELD_NAME = "@instance@";
    protected static String DELETE_FIELD_NAME = "@delete@";
    protected static String COMMENT_FIELD_NAME = "@comment@";

    private static int OFFSET_DELETE_COLUMN = 1;   //use -1 if not present
    private static int OFFSET_COMMENT_COLUMN = 2;
    private static int OFFSET_MAX_COLUMN = OFFSET_COMMENT_COLUMN; //use 0 if all other column offsets are -1

    private Panel wrappingPanel;
    private EditorGridPanel grid;

    private String labelText;
    private HTML loadingIcon;

    protected RecordDef recordDef;
    protected Store store;

    protected List<Radio> gridRadios = null;
    protected List<String> radioIDs = null;

    protected GridRowListener gridRowListener;
    protected EditorGridListener editorGridListener;

    protected List<String> properties = new ArrayList<String>(); //stores the order of cols
    protected Map<String, Integer> prop2Index = new HashMap<String, Integer>();
    protected String[] columnEditorConfigurations;
    protected String autoExpandColId;

    private String fieldNameSorted = null;

    protected boolean multiValue = true;
    private Anchor addExistingLink;
    private Anchor addNewLink;
    private Anchor replaceExistingLink;
    private Anchor replaceNewLink;
    private com.google.gwt.user.client.ui.Panel labelPanel;

    protected PropertyValueUtil propertyValueUtil;


    public InstanceGridWidget(Project project) {
        super(project);
        propertyValueUtil = new PropertyValueUtil();
    }

    @Override
    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        super.setup(widgetConfiguration, propertyEntityData);

        if (getProperty() != null) {
            //If the property does not have a browserText, use the label of the field. This is experimental to see if it has the desirable behavior.
            String label = UIUtil.getStringConfigurationProperty(widgetConfiguration, FormConstants.LABEL, getProperty().getBrowserText());
            getProperty().setBrowserText(label);
        }
    }

    @Override
    public Component createComponent() {
        wrappingPanel = createWrappingPanel();

        labelPanel = createLabelPanel();

        grid = createGrid();
        grid.addEditorGridListener(getEditorGridListener());

        wrappingPanel.add(labelPanel);
        wrappingPanel.add(grid, new ColumnLayoutData(1));

        return wrappingPanel;
    }

    @Override
    public Component getComponent() {
        return wrappingPanel;
    }

    protected Panel createWrappingPanel() {
        Panel panel = new Panel();
        panel.setLayout(new ColumnLayout());
        panel.setPaddings(5);
        return panel;
    }

    protected com.google.gwt.user.client.ui.Panel createLabelPanel() {
        HorizontalPanel horizLabelPanel = new HorizontalPanel();
        labelText = UIUtil.getStringConfigurationProperty(getWidgetConfiguration(), FormConstants.LABEL, getProperty().getBrowserText());
        Label label = new Label();
        label.setHtml(getLabelHtml(labelText, getHelpURL(), getTooltipText()) + AbstractFieldWidget.LABEL_SEPARATOR);
        horizLabelPanel.add(label);
        loadingIcon = new HTML("<img src=\"images/invisible12.png\"/>");
        loadingIcon.setStyleName("loading-img");
        horizLabelPanel.add(loadingIcon);
        horizLabelPanel.setStyleName("form_label");

        labelPanel = new VerticalPanel();

        labelPanel.setStyleName("action_link");

        labelPanel.add(horizLabelPanel);
        createActionLinks();

        return labelPanel;
    }

    /*
     * Create action links
     */

    protected void createActionLinks() {
        if (InstanceGridWidgetConstants.showAddExistingActionLink(getWidgetConfiguration(), getProject().getProjectConfiguration())) {
            addExistingLink = createAddExistingHyperlink();
            if (addExistingLink != null) {
                labelPanel.add(addExistingLink);
            }
        }

        if (InstanceGridWidgetConstants.showAddNewActionLink(getWidgetConfiguration(), getProject().getProjectConfiguration())) {
            addNewLink = createAddNewValueHyperlink();
            if (addNewLink != null) {
                labelPanel.add(addNewLink);
            }
        }

        if (InstanceGridWidgetConstants.showReplaceExistingActionLink(getWidgetConfiguration(), getProject().getProjectConfiguration())) {
            replaceExistingLink = createReplaceExistingHyperlink();
            if (replaceExistingLink != null && isReplace()) {
                labelPanel.add(replaceExistingLink);
            }
        }

        if (InstanceGridWidgetConstants.showReplaceNewActionLink(getWidgetConfiguration(), getProject().getProjectConfiguration())) {
            replaceNewLink = createReplaceNewValueHyperlink();
            if (replaceNewLink != null && isReplace()) {
                labelPanel.add(replaceNewLink);
            }
        }
    }

    protected Anchor createAddNewValueHyperlink() {
        Anchor addNewLink = new Anchor(
                InstanceGridWidgetConstants.getAddNewLink(getWidgetConfiguration(), getProject().getProjectConfiguration()), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()) {
                    onAddNewValue();
                }
            }
        });
        return addNewLink;
    }

    protected Anchor createAddExistingHyperlink() {
        Anchor addExistingLink = new Anchor(
                InstanceGridWidgetConstants.getAddExistingLink(getWidgetConfiguration(), getProject().getProjectConfiguration()), true);
        addExistingLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()) {
                    onAddExistingValue();
                }
            }
        });
        return addExistingLink;
    }

    protected Anchor createReplaceNewValueHyperlink() {
        Anchor replaceNewLink = new Anchor(
                InstanceGridWidgetConstants.getReplaceNewLink(getWidgetConfiguration(), getProject().getProjectConfiguration()), true);
        replaceNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()) {
                    onReplaceNewValue();
                }
            }
        });
        return replaceNewLink;
    }

    protected Anchor createReplaceExistingHyperlink() {
        Anchor replaceExistingLink = new Anchor(
                InstanceGridWidgetConstants.getReplaceExistingLink(getWidgetConfiguration(), getProject().getProjectConfiguration()), true);
        replaceExistingLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()) {
                    onReplaceExisitingValue();
                }
            }
        });
        return replaceExistingLink;
    }


    protected void updateActionLinks(boolean isReplace) {
        if (addExistingLink != null) {
            if (isReplace) { labelPanel.remove(addExistingLink);}
                else { labelPanel.add(addExistingLink); }
        }
        if (addNewLink != null) {
            if (isReplace) { labelPanel.remove(addNewLink);}
                else { labelPanel.add(addNewLink); }
        }
        if (replaceExistingLink != null) {
            if (isReplace) { labelPanel.add(replaceExistingLink);}
                else { labelPanel.remove(replaceExistingLink); }
        }
        if (replaceNewLink != null) {
            if (isReplace) { labelPanel.add(replaceNewLink);}
                else { labelPanel.remove(replaceNewLink); }
        }
    }


    /*
     * Grid actions (on add, on replace, on delete, etc.)
     */

    protected void onAddNewValue() {
        List<EntityData> allowedValues = getProperty().getAllowedValues();
        String type = null;
        if (allowedValues != null && !allowedValues.isEmpty()) {
            type = allowedValues.iterator().next().getName();
        }

        OntologyServiceManager.getInstance().createInstanceValue(getProject().getProjectName(), null, type,
                getSubject().getName(), getProperty().getName(), GlobalSettings.getGlobalSettings().getUserName(),
                getAddValueOperationDescription(), new AddPropertyValueHandler());
    }


    protected void onAddExistingValue() {
        String type = UIUtil.getStringConfigurationProperty(getWidgetConfiguration(), FormConstants.ONT_TYPE, null);
        if (type == null) { return;  } //TODO: not type specified, maybe use range of property

        SelectionUtil.selectIndividuals(getProject(), UIUtil.createCollection(new EntityData(type)), true, false, new SelectionCallback() {
            public void onSelect(Collection<EntityData> selection) {
                addExistingValues(selection);
            }
        });
    }

    protected void addExistingValues(Collection<EntityData> values) {
        //TODO: later optimize this in a single remote call
        for (EntityData value : values) {
            OntologyServiceManager.getInstance().addPropertyValue(getProject().getProjectName(), getSubject().getName(), getProperty(), value,
                    GlobalSettings.getGlobalSettings().getUserName(), getAddExistingOperationDescription(value), new AddExistingValueHandler(getSubject()));
        }
    }

    protected void onReplaceNewValue() {
        onDelete(0);
        onAddNewValue();
    }

    protected void onReplaceExisitingValue() {
        onDelete(0);
        onAddExistingValue();
    }

    protected void onEditNotes(int index) {
        Record record = store.getAt(index);
        String value = record.getAsString(INSTANCE_FIELD_NAME);
        super.onEditNotes(value);
    }

    protected void onDelete(int index) {
        Record record = store.getAt(index);
        String value = record.getAsString(INSTANCE_FIELD_NAME);
        if (value != null) {
            propertyValueUtil.deletePropertyValue(getProject().getProjectName(), getSubject().getName(),
                    getProperty().getName(), ValueType.Instance, value, GlobalSettings.getGlobalSettings()
                    .getUserName(), getDeleteValueOperationDescription(index), new RemovePropertyValueHandler(
                            index));

        }
    }


    /*
     * Operation descriptions
     */
    protected String getAddValueOperationDescription() {
        return UIUtil.getAppliedToTransactionString("Added a new "
                + UIUtil.getShortName(getProperty().getBrowserText()) + " to " + getSubject().getBrowserText(),
                getSubject().getName());
    }

    protected String getAddExistingOperationDescription(EntityData value) {
        return UIUtil.getAppliedToTransactionString("Added  " + UIUtil.getDisplayText(value) + " as "
                + UIUtil.getShortName(getProperty().getBrowserText()) + " to " + getSubject().getBrowserText(),
                getSubject().getName());
    }

    protected String getReplaceValueOperationDescription(int colIndex, Object oldValue, Object newValue) {
        String header = grid.getColumnModel().getColumnHeader(colIndex);
        header = header == null ? "(no header)" : header;

        oldValue = UIUtil.getDisplayText(oldValue);
        oldValue = ((String)oldValue).length() == 0 ? "(empty)" : oldValue;
        newValue = UIUtil.getDisplayText(newValue);
        newValue = ((String)newValue).length() == 0 ? "(empty)" : newValue;

        return UIUtil.getAppliedToTransactionString("Replaced '" + header + "' for '"
                + UIUtil.getDisplayText(getProperty()) + "' of " + getSubject().getBrowserText()
                + ". Old value: " + oldValue +
                ". New value: " + newValue,
                getSubject().getName());
    }

    protected String getDeleteValueOperationDescription(int index) {
        Record record = store.getAt(index);
        String value = record.getAsString(INSTANCE_FIELD_NAME);
        String deletedValueDesc = new String("(");
        String[] fields = record.getFields();
        if (fields.length > getMaxColumnOffset()) {
            for (int i = 0; i < fields.length - getMaxColumnOffset(); i++) {
                if (!grid.getColumnModel().isHidden(i)) {
                    String fieldValue = record.getAsString(fields[i]);
                    String fieldHeader = grid.getColumnModel().getColumnHeader(i);
                    String fieldValuePair = (fieldHeader == null ? "no field header" : fieldHeader) + ": " + (fieldValue == null ? "empty" : fieldValue);
                    deletedValueDesc = deletedValueDesc + fieldValuePair + ", ";
                }
            }
            deletedValueDesc = deletedValueDesc.substring(0, deletedValueDesc.length() - 2);
        }
        deletedValueDesc = deletedValueDesc + ")";

        return UIUtil.getAppliedToTransactionString("Deleted " + UIUtil.getShortName(getProperty().getBrowserText())
                + " from " + getSubject().getBrowserText() + ". Deleted value: "
                + (value == null || value.toString().length() == 0 ? "(empty)" : deletedValueDesc), getSubject()
                .getName());
    }


    /*
     * Grid creation
     */

    protected EditorGridPanel createGrid() {
        grid = new EditorGridPanel();

        grid.setCls("form_grid");
        grid.setAutoWidth(true);
        grid.setStripeRows(true);
        int clicksToEdit = UIUtil.getIntegerConfigurationProperty(
                getProject().getProjectConfiguration(), 
                FormConstants.CLICKS_TO_EDIT, 
                FormConstants.DEFAULT_CLICKS_TO_EDIT);
        grid.setClicksToEdit(clicksToEdit);
        grid.setFrame(true);

        Map<String, Object> widgetConfig = getWidgetConfiguration();
        if (widgetConfig != null) {
            String heigthStr = (String) widgetConfig.get(FormConstants.HEIGHT);
            if (heigthStr != null) {
                grid.setHeight(Integer.parseInt(heigthStr));
            } else {
                grid.setHeight(110);
            }

            multiValue = UIUtil.getBooleanConfigurationProperty(widgetConfig, FormConstants.MULTIPLE_VALUES_ALLOWED, true);
        }

        createColumns();
        createStore();
        attachListeners();

        if (autoExpandColId != null) {
            grid.setAutoExpandColumn(autoExpandColId);
        }

        grid.getView().setScrollOffset(25);

        //default height of grid header (25) + default height of a row (25) + default height of horiz. scrollbar (20)
        if (grid.getHeight() < 25 + 25 + 20) {
            grid.setAutoScroll(false);
        }

        return grid;
    }

    protected void createStore() {
        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy dataProxy = new MemoryProxy(new Object[][] {});
        store = new Store(dataProxy, reader);
        grid.setStore(store);
        store.load();
    }

    public GridPanel getGridPanel() {
        return grid;
    }

    protected void attachListeners() {
        //TODO: may not work so well.. - check indexes
        grid.addGridCellListener(new GridCellListenerAdapter() {
            double timeOfLastClick = 0;
            int clicksToEdit = UIUtil.getIntegerConfigurationProperty(
                    getProject().getProjectConfiguration(), 
                    FormConstants.CLICKS_TO_EDIT, 
                    FormConstants.DEFAULT_CLICKS_TO_EDIT);
            boolean oneClickComboboxEditingEnabled = UIUtil.getBooleanConfigurationProperty(
                    getProject().getProjectConfiguration(), 
                    FormConstants.ONE_CLICK_COMBOBOX_EDITING, 
                    true);
            
            @Override
            public void onCellClick(final GridPanel grid, final int rowIndex, final int colindex, final EventObject e) {
                double eventTime = e.getTime();
                if (eventTime - timeOfLastClick > 500) { //not the second click in a double click
                    onCellClickOrDblClick(grid, rowIndex, colindex, e);
                };

                /*
                 * Set new value for timeOfLastClick the time the last click was handled
                 * We use the current time (and not eventTime), because some time may have passed since eventTime
                 * while executing the onCellClickOrDblClick method.
                 */
                timeOfLastClick = new Date().getTime();
            }

            private void onCellClickOrDblClick(GridPanel grid, final int rowIndex, int colindex, EventObject e) {
                int offsetDeleteColumn = getOffsetDeleteColumn();
                int offsetCommentColumn = getOffsetCommentColumn();
                if (offsetDeleteColumn != -1 && colindex == properties.size() + offsetDeleteColumn) {
                    onDeleteColumnClicked(rowIndex);
                } else if (offsetCommentColumn != -1 && colindex == properties.size() + offsetCommentColumn) {
                    onCommentColumnClicked(rowIndex);
                } else {
                    if (clicksToEdit == 1) {
                        onValueColumnClicked(grid, rowIndex, colindex);
                    }
                    else {
                        //overriding clicksToEdit behavior: forcing single click to work for specific field types, such as comboboxes
                        checkSpecialColumnsAndStartEditing(grid, rowIndex, colindex);
                    }
                }

            }

            private void checkSpecialColumnsAndStartEditing(GridPanel grid, final int rowIndex, int colindex) {
                if (oneClickComboboxEditingEnabled) {
                    Map<String, Object> widgetConfiguration = getWidgetConfiguration();
                    if (widgetConfiguration != null) {
                        Map<String, Object> columnConfiguration = (Map<String, Object>) widgetConfiguration.get(FormConstants.COLUMN_PREFIX + (colindex+1));
                        if (columnConfiguration != null) {
                            String fieldType = UIUtil.getStringConfigurationProperty(columnConfiguration, FormConstants.FIELD_TYPE, null);
                            if (FormConstants.FIELD_TYPE_COMBOBOX.equals(fieldType)) {
                                ((EditorGridPanel)grid).startEditing(rowIndex, colindex);
                            }
                        }
                    }
                }
            }
            
            @Override
            public void onCellDblClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {
                if (clicksToEdit == 2) {
                    onValueColumnClicked(grid, rowIndex, colIndex);
                }
            }

            @Override
            public void onCellContextMenu(final GridPanel grid, final int rowIndex, final int colindex, final EventObject e) {
                e.stopEvent();
                if (e.getTarget(".checkbox", 1) != null) {
                    onContextMenuCheckboxClicked(rowIndex, colindex, e);
                } else {
                    onContextMenuClicked(rowIndex, colindex, e);
                }
            }
        });
        
        //TODO we should find a solution for removing the cell selection once focus is lost (i.e. another widget is selected) 
//        grid.addListener(Event.ONBLUR, new Function() {
//            public void execute() {
//                System.out.println("Focus out!!!");
//            }
//        });
        
        grid.addGridListener(new GridListenerAdapter(){
            @Override
            public void onKeyPress(EventObject e) {
                int key = e.getKey();
                if (key == EventObject.ENTER && !e.isCtrlKey()) {
                    int[] selectedCell = grid.getCellSelectionModel().getSelectedCell();
                    if (selectedCell != null) {
                        onValueColumnClicked(grid, selectedCell[0], selectedCell[1]);
                    }
                }
            }
        });
    }

    protected void onDeleteColumnClicked(final int rowIndex) {
        Record record = store.getAt(rowIndex);
        if (record != null) {
            boolean value = record.getAsBoolean(DELETE_FIELD_NAME);
            if (value == false) {
                return; //read only value
            }
            if (isWriteOperationAllowed()) {
                MessageBox.confirm("Confirm", "Are you sure you want to delete this value?",
                        new MessageBox.ConfirmCallback() {
                    public void execute(String btnID) {
                        if (btnID.equals("yes")) {
                            onDelete(rowIndex);
                        }
                    }
                });
            }
        }
    }

    protected void onCommentColumnClicked(final int rowIndex) {
        Record record = store.getAt(rowIndex);
        if (record != null) {
            if (UIUtil.confirmOperationAllowed(getProject())) {
                onEditNotes(rowIndex);
            }
        }
    }

    protected void onValueColumnClicked(final GridPanel grid, final int rowIndex, final int colIndex) {
        //To be overridden in subclasses, if needed
        
        final Record record = store.getAt(rowIndex);
        String gridEditorOption = columnEditorConfigurations[colIndex];
        if (record != null && gridEditorOption != null) {
            if (gridEditorOption.equals(FormConstants.FIELD_EDITOR_MULTILINE)) { 
                editWithPopupGridEditor(PopupGridEditor.TEXT_AREA, grid, colIndex, record);
            }
            else if (gridEditorOption.equals(FormConstants.FIELD_EDITOR_HTML)) { 
                editWithPopupGridEditor(PopupGridEditor.HTML, grid, colIndex, record);
            }
        }
    }

    private void editWithPopupGridEditor(final PopupGridEditor editor, final GridPanel grid, final int colIndex, final Record record) {
        final String field = record.getFields()[colIndex];
        final String value = record.getAsString(field);
        editor.show(labelText, grid.getColumnModel().getColumnHeader(colIndex), value, isWriteOperationAllowed(false));
        editor.setCallbackFunction( new Function() {
            public void execute() {
                if (editor.hasValueChanged()) {
                    String newValue = editor.getValue();
                    record.set(field, newValue);
                    changeValue(record, newValue, value, colIndex);
                }
            }
        });
    }

    protected void onContextMenuCheckboxClicked(final int rowIndex, final int colIndex,  final EventObject e) {
        final Record record = store.getAt(rowIndex);
        if (record != null) {
            if (isWriteOperationAllowed()) {
                String field = record.getFields()[colIndex];
                String value = record.getAsString(field);
                if (value != null && !"".equals(value)) {
                    Menu contextMenu = new DeleteContextMenu(
                            "Unset value (i.e. set to 'Unknown')", "images/unknown_check.gif",
                            record, colIndex, ValueType.Boolean);
                    contextMenu.showAt(e.getXY()[0] + 5, e.getXY()[1] + 5);
                }
            }
        }
    }

    protected void onContextMenuClicked(final int rowIndex, final int colIndex,  final EventObject e) {
        final Record record = store.getAt(rowIndex);
        if (record != null) {
            if (isWriteOperationAllowed()) {
                String valueType = record.getAsString("valueType");
                if (valueType == null) { //TODO: should be fixed
                    valueType = ValueType.String.name();
                }
                if (ValueType.Instance.equals(ValueType.valueOf(valueType))) {
                    Menu contextMenu = new DeleteContextMenu(
                            "Remove this value", "images/delete_small_16x16.png",
                            record, colIndex, ValueType.valueOf(valueType));
                    contextMenu.showAt(e.getXY()[0] + 5, e.getXY()[1] + 5);
                }
            }
        }
    }

    protected EditorGridListener getEditorGridListener() {
        if (editorGridListener == null) {
            editorGridListener = new EditorGridListenerAdapter() {
                @Override
                public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex, int colIndex) {
                    if (!isWriteOperationAllowed()) {
                        return false;
                    }
                    String valueType = record.getAsString("valueType");
                    return valueType == null || valueType.equalsIgnoreCase("string") || valueType.equalsIgnoreCase("any"); //TODO: allow only the editing of String values for now
                }

                @Override
                public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue,
                        int rowIndex, int colIndex) {
                    changeValue(record, newValue, oldValue, colIndex);
                }
            };
        }
        return editorGridListener;
    }


    private void changeValue(Record record, Object newValue, Object oldValue, int colIndex) {
        //special handling rdfs:Literal
        String valueType = record.getAsString("valueType");
        if (valueType == null) { //TODO: should be fixed
            valueType = ValueType.String.name();
        }
        String selSubject = record.getAsString(INSTANCE_FIELD_NAME);
        if (selSubject != null) {
            propertyValueUtil.replacePropertyValue(getProject().getProjectName(), selSubject,
                    properties.get(colIndex), ValueType.valueOf(valueType), 
                            oldValue == null ? null : oldValue.toString(), 
                            newValue == null ? null : newValue.toString(),
                            GlobalSettings.getGlobalSettings().getUserName(),
                            getReplaceValueOperationDescription(colIndex, oldValue, newValue),
                            new ReplacePropertyValueHandler(new EntityData(newValue == null ? null : newValue.toString(), newValue == null ? null : newValue.toString())));
        }
    }

    protected void createColumns() {
        Map<String, Object> widgetConfig = getWidgetConfiguration();
        if (widgetConfig == null) {
            return;
        }

        int colCount = 0;

        for (String key : widgetConfig.keySet()) {
            if (key.startsWith(FormConstants.COLUMN_PREFIX)) {
                colCount++;
            }
        }

        FieldDef[] fieldDef = new FieldDef[colCount + getExtraColumnCount()];
        ColumnConfig[] columns = new ColumnConfig[colCount + getExtraColumnCount()];
        String[] props = new String[colCount];
        columnEditorConfigurations = new String[colCount];

        for (String key : widgetConfig.keySet()) {
            if (key.startsWith(FormConstants.COLUMN_PREFIX)) {
                createColumn((Map<String, Object>) widgetConfig.get(key), fieldDef, columns, props);
            }
        }

        properties = Arrays.asList(props);
        for (int i = 0; i < props.length; i++) {
            prop2Index.put(props[i], i);
        }

        createInstanceColumn(fieldDef, columns, colCount);
        createActionColumns(fieldDef, columns, colCount);

        recordDef = new RecordDef(fieldDef);

        ColumnModel columnModel = new ColumnModel(columns);
        grid.setColumnModel(columnModel);
    }

    protected void createInstanceColumn(FieldDef[] fieldDef, ColumnConfig[] columns, int colCount) {
        ColumnConfig instCol = new ColumnConfig("", INSTANCE_FIELD_NAME, 25);
        instCol.setTooltip("Attached instance name");
        instCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
                String strValue = (String) value;
                return (strValue != null && strValue.contains("#") ? strValue.substring(strValue.lastIndexOf("#")) : strValue);
            }
        });
        instCol.setHidden(true);

        fieldDef[colCount] = new StringFieldDef(INSTANCE_FIELD_NAME);
        columns[colCount] = instCol;
    }

    protected void createActionColumns(FieldDef[] fieldDef, ColumnConfig[] columns, int colCount) {
        int offsetDeleteColumn = getOffsetDeleteColumn();
        if (offsetDeleteColumn != -1) {
            ColumnConfig deleteCol = createDeleteColumn();
            fieldDef[colCount + offsetDeleteColumn] = new BooleanFieldDef(DELETE_FIELD_NAME);
            columns[colCount + offsetDeleteColumn] = deleteCol;
        }

        int offsetCommentColumn = getOffsetCommentColumn();
        if (offsetCommentColumn != -1) {
            ColumnConfig commentCol = createCommentsColumn();
            fieldDef[colCount + offsetCommentColumn] = new IntegerFieldDef(COMMENT_FIELD_NAME);
            columns[colCount + offsetCommentColumn] = commentCol;
        }
    }

    protected ColumnConfig createDeleteColumn() {
        ColumnConfig deleteCol = new ColumnConfig("", DELETE_FIELD_NAME, 25);
        deleteCol.setTooltip("Delete this value");

        deleteCol.setRenderer(createDeleteColumnRenderer());
        return deleteCol;
    }

    protected Renderer createDeleteColumnRenderer() {
        return new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                boolean isDeleteEnabled = (Boolean) value;
                return isDeleteEnabled ?
                        "<img src=\"images/delete.png\" title=\" Click on the icon to remove value.\"></img>" :
                        "<img src=\"images/delete_grey.png\" title=\" Delete is disabled for this value.\"></img>"  ;
            }
        };
    }

    //TODO: refactor
    protected ColumnConfig createCommentsColumn() {
        ColumnConfig commentCol = new ColumnConfig("", COMMENT_FIELD_NAME, 40);
        commentCol.setTooltip("Add a comment on this value");
        commentCol.setRenderer(createCommentColumnRenderer());
        return commentCol;
    }

    protected Renderer createCommentColumnRenderer() {
        return new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
                // TODO: add a css for this
                String text = "<img src=\"images/comment.gif\" title=\""
                    + " Click on the icon to add new note(s).\"></img>";
                int annotationsCount = (value == null ? 0 : ((Integer) value));
                if (annotationsCount > 0) {
                    text = "<img src=\"images/comment.gif\" title=\""
                        + UIUtil.getNiceNoteCountText(annotationsCount)
                        + " on this value. \nClick on the icon to see existing or to add new note(s).\"></img>"
                        + "<span style=\"vertical-align:super;font-size:95%;color:#15428B;font-weight:bold;\">"
                        + "&nbsp;" + annotationsCount + "</span>";
                }
                return text;
            }
        };
    }

    //FIXME: protect against invalid config xml
    protected ColumnConfig createColumn(Map<String, Object> config, FieldDef[] fieldDef, ColumnConfig[] columnConfigs, String[] props) {
        ColumnConfig colConfig = new ColumnConfig();

        String header = (String) config.get(FormConstants.HEADER);
        colConfig.setHeader(header == null ? "" : header);

        String tooltip = (String) config.get(FormConstants.TOOLTIP);
        if (tooltip != null) {
            colConfig.setTooltip(tooltip);
        }

        String property = (String) config.get(FormConstants.PROPERTY); //better not be null
        String indexStr = (String) config.get(FormConstants.INDEX);

        int index = Integer.parseInt(indexStr); //better be valid
        props[index] = property;
        colConfig.setDataIndex(property);

        String widthStr = (String) config.get(FormConstants.WIDTH);
        if (widthStr != null) {
            if (widthStr.equalsIgnoreCase(FormConstants.WIDTH_ALL)) {
                autoExpandColId = HTMLPanel.createUniqueId();
                colConfig.setId(autoExpandColId);
            } else {
                int width = Integer.parseInt(widthStr);
                colConfig.setWidth(width);
            }
        }

        boolean hidden = UIUtil.getBooleanConfigurationProperty(config, FormConstants.HIDDEN, false);
        if (hidden) {
            colConfig.setHidden(true);
        }

        colConfig.setResizable(true);
        colConfig.setSortable(true);
        colConfig.setCss("word-wrap: break-word ;");

        String fieldType = (String) config.get(FormConstants.FIELD_TYPE);
        colConfig.setRenderer(getColumnRenderer(fieldType, config));

        String fieldTextAlign = (String) config.get(FormConstants.FIELD_ALIGN);
        if (fieldTextAlign != null) {
            colConfig.setAlign(getTextAlign(fieldTextAlign));
        }
        //we could center checkboxes and radio buttons by default...
        //else if (FormConstants.FIELD_TYPE_CHECKBOX.equals(fieldType)
        //        || FormConstants.FIELD_TYPE_RADIO.equals(fieldType)) {
        //    colConfig.setAlign(getTextAlign("center"));
        //}

        GridEditor editor = getGridEditor(fieldType, config);
        if (editor != null) {
            colConfig.setEditor(editor);
        }
        else {
            columnEditorConfigurations[index] = (String) config.get(FormConstants.FIELD_EDITOR);
        }

        //TODO: support other types as well
        fieldDef[index] = new StringFieldDef(property);
        columnConfigs[index] = colConfig;

        Boolean sorted = (Boolean) config.get(FormConstants.SORTED);
        if (Boolean.TRUE.equals(sorted)) {
            fieldNameSorted = colConfig.getDataIndex();
        }

        return colConfig;
    }

    protected int getIndexOfProperty(String prop) {
        return prop2Index.get(prop);
    }

    protected TextAlign getTextAlign(String fieldTextAlign) {
        if (fieldTextAlign == null ||
                fieldTextAlign.equalsIgnoreCase("left")) {
            return TextAlign.LEFT;
        }
        if (fieldTextAlign.equalsIgnoreCase("center")) {
            return TextAlign.CENTER;
        }
        if (fieldTextAlign.equalsIgnoreCase("right")) {
            return TextAlign.RIGHT;
        }
        if (fieldTextAlign.equalsIgnoreCase("justify")) {
            return TextAlign.JUSTIFY;
        }
        return TextAlign.LEFT;
    }


    protected GridEditor getGridEditor(final String fieldType, final Map<String, Object> config) {

        if (fieldType != null) {
            if (fieldType.equals(FormConstants.FIELD_TYPE_COMBOBOX)) {
                Map<String, String> allowedValues = UIUtil.getAllowedValuesConfigurationProperty(config);
                System.out.println("Allowed Values Map: " + allowedValues);
                String[][] displayValues;
                if (allowedValues == null) {
                    displayValues = new String[][]{};
                }
                else {
                    displayValues = new String[allowedValues.size()][2];
                    int i=0;
                    for (String key : allowedValues.keySet()) {
                        displayValues[i][0] = key;
                        displayValues[i][1] = allowedValues.get(key);
                        i++;
                    }
                }
                SimpleStore cbStore = new SimpleStore(new String[]{"displayText", "value"}, displayValues);
                cbStore.load();

                ComboBox cb = new ComboBox();
                cb.setStore(cbStore);
                cb.setDisplayField("displayText");
                cb.setValueField("value");
                boolean allowedValuesOnly = UIUtil.getBooleanConfigurationProperty(config, FormConstants.ALLOWED_VALUES_ONLY, true);
                System.out.println("Allowed Values Only: " + allowedValuesOnly);
                cb.setForceSelection(allowedValuesOnly);
                if (! allowedValuesOnly) {
                    //Apparently we need to add this because the 'setForceSelection(boolean)' method 
                    //is not doing what it suppose to by itself, and we need to enforce the raw value to be used.
                    cb.addListener(new ComboBoxListenerAdapter() {
                        @Override
                        public boolean doBeforeQuery(ComboBox comboBox, ComboBoxCallback cbcb) {
                            String lastQueried = comboBox.getRawValue();
                            //System.out.println("In doBeforeQuery. rawValue: " + lastQueried + " getValue: " + comboBox.getValue());
                            comboBox.setValue(lastQueried);
                            //System.out.println("New getValue: " + comboBox.getValue());
                            return true;
                        }
                    });
                }
                return new GridEditor(cb);
            }
        }

        //TODO - use a text area as the default editor for now, support more later
        String gridEditorOption = (String) config.get(FormConstants.FIELD_EDITOR);
        
        if (gridEditorOption == null    //default
                || gridEditorOption.equals(FormConstants.FIELD_EDITOR_INLINE)) {
            return new GridEditor(new TextField());
        }
        else if (gridEditorOption.equals(FormConstants.FIELD_EDITOR_MULTILINE)) { 
            return null;
        }
        else if (gridEditorOption.equals(FormConstants.FIELD_EDITOR_HTML)) { 
            return null;
        }

        //in other cases: create text field or text area depending on the size of the grid.
        //This behavior probably does not make sense anymore, and we should treat all the 
        //different valid editor options above
        TextField textEditor;
        if (grid.getHeight() < 25 + 50) {	//default height of grid header (25) + default height of a TextArea (50)
            textEditor = new TextField();
        }
        else {
            textEditor = new TextArea();
        }

        return new GridEditor(textEditor);
    }

    protected Renderer getColumnRenderer(final String fieldType, Map<String, Object> config) {
        Map<String, String> valueToDisplayTextMap = null;
        if (FormConstants.FIELD_TYPE_COMBOBOX.equals(fieldType)) {
            Map<String, String> allowedValues = UIUtil.getAllowedValuesConfigurationProperty(config);
            if (allowedValues != null) {
                valueToDisplayTextMap = new HashMap<String, String>();
                for (String key : allowedValues.keySet()) {
                    valueToDisplayTextMap.put(allowedValues.get(key), key);
                }
            }
        }
        ColumnRenderer renderer = new ColumnRenderer(fieldType, valueToDisplayTextMap);
        return renderer;
    }

    protected String preRenderColumnContent(String content, String fieldType) {
        return content;
    }

    @Override
    public void setValues(Collection<EntityData> values) {
        //This method is not invoked by this widget. It bypasses the parent mechanism for retrieving
        //the widget values and makes an optimized call.
    }

    @Override
    protected void fillValues(List<String> subjects, List<String> props) {
        store.removeAll();
        OntologyServiceManager.getInstance().getEntityPropertyValues(getProject().getProjectName(), subjects, props, properties,
                new GetTriplesHandler(getSubject()));
    }

    /**
     * Fills the extra column values for a data row, based on an EntityPropertyValues.
     * <B>Important note:</B> Please make sure that both this method and all the
     * methods that override this method will correctly handle the situation when
     * the property-value map in <code>epv</code> is empty, in case when a new instance
     * is created in the grid.
     *
     * @param datarow a grid data row
     * @param epv an EntityPropertyValues instance.
     */
    protected void setExtraColumnValues(Object[] datarow, EntityPropertyValues epv) {
        setExtraColumnValues(datarow, epv.getSubject());
    }

    protected Object[][] createDataArray(List<EntityPropertyValues> entityPropertyValues) {
        int i = 0;
        Object[][] data = new Object[entityPropertyValues.size()][properties.size() + getExtraColumnCount()];
        for (EntityPropertyValues epv : entityPropertyValues) {
            for (PropertyEntityData ped : epv.getProperties()) {
                data[i][getIndexOfProperty(ped.getName())] = getCellText(epv, ped);
            }
            setExtraColumnValues(data[i], epv);
            i++;
        }
        return data;
    }

    protected void setExtraColumnValues(Object[] datarow, EntityData subject) {
        //add the name of the subject instance
        datarow[properties.size()] = subject.getName();
        //add delete and comment icons
        int offsetDeleteColumn = getOffsetDeleteColumn();
        if (offsetDeleteColumn != -1) {
            datarow[properties.size() + offsetDeleteColumn] = true;
        }

        int offsetCommentColumn = getOffsetCommentColumn();
        if (offsetCommentColumn != -1) {
            datarow[properties.size() + offsetCommentColumn] = new Integer(subject.getLocalAnnotationsCount());
        }
    }

    protected boolean isReplace() {
        if (store == null){
            return false;
        }
        if (store.getRecords() == null){
            return false;
        }
        return store.getRecords().length > 0 && !multiValue;
    }

    @Override
    public void setLoadingStatus(boolean loading) {
        super.setLoadingStatus(loading);
        loadingIcon.setHTML(loading ? "<img src=\"images/loading.gif\"/>" : "<img src=\"images/invisible12.png\"/>");
    }


    protected Store getStore() {
        return store;
    }

    protected int getOffsetDeleteColumn() {
        return OFFSET_DELETE_COLUMN;
    }

    protected int getOffsetCommentColumn() {
        return OFFSET_COMMENT_COLUMN;
    }

    protected int getMaxColumnOffset() {
        return OFFSET_MAX_COLUMN;
    }

    protected int getExtraColumnCount() {
        return OFFSET_MAX_COLUMN + 1;   //1 for the instance field
    }


    /*
     * Remote calls
     */

    protected class GetTriplesHandler extends AbstractAsyncHandler<List<EntityPropertyValues>> {

        private EntityData mySubject = null;

        public GetTriplesHandler(EntityData subject) {
            mySubject = subject;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Instance Grid Widget: Error at getting triples for " + getSubject(), caught);
            updateActionLinks(isReplace());
        }

        @Override
        public void handleSuccess(List<EntityPropertyValues> entityPropertyValues) {
            if (!UIUtil.equals(mySubject, getSubject())) {  return; }
            store.removeAll();

            if (entityPropertyValues != null) {
                Object[][] data = createDataArray(entityPropertyValues);
                store.setDataProxy(new MemoryProxy(data));
                store.load();

                if (fieldNameSorted != null) {
                    store.sort(fieldNameSorted, SortDir.ASC);   //WARNING! This seems to be very slow
                }
            }

            setOldDisplayedSubject(getSubject());

            updateActionLinks(isReplace());
            setLoadingStatus(false);

        }
    }

    protected String getCellText(EntityPropertyValues epv, PropertyEntityData ped) {
        return UIUtil.prettyPrintList(epv.getPropertyValues(ped));
    }


    class RemovePropertyValueHandler extends AbstractAsyncHandler<Void> {
        private int removeInd;

        public RemovePropertyValueHandler(int removeIndex) {
            this.removeInd = removeIndex;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at removing value for " + getProperty().getBrowserText() + " and "
                    + getSubject().getBrowserText(), caught);
            MessageBox.alert("There was an error at removing the property value for " + getProperty().getBrowserText()
                    + " and " + getSubject().getBrowserText() + ".");
            updateActionLinks(isReplace());
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("Success at removing value for " + getProperty().getBrowserText() + " and "
                    + getSubject().getBrowserText(), null);
            Record recordToRemove = store.getAt(removeInd);
            if (recordToRemove != null) {
                store.remove(recordToRemove);
                updateActionLinks(isReplace());
            }
        }
    }


    protected class ReplacePropertyValueHandler extends AbstractAsyncHandler<Void> {

        private EntityData newEntityData;

        public ReplacePropertyValueHandler(EntityData newEntityData) {
            this.newEntityData = newEntityData;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at replace property for " + getProperty().getBrowserText() + " and "
                    + getSubject().getBrowserText(), caught);
            MessageBox.alert("There was an error at setting the property value for " + getSubject().getBrowserText() + ".");
            InstanceGridWidget.this.refresh();
            updateActionLinks(isReplace());
        }

        @Override
        public void handleSuccess(Void result) {
            InstanceGridWidget.this.grid.getStore().commitChanges();
            updateActionLinks(isReplace());
        }
    }


    class AddPropertyValueHandler extends AbstractAsyncHandler<EntityData> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at add property for " + getProperty().getBrowserText() + " and "
                    + getSubject().getBrowserText(), caught);
            MessageBox.alert("There was an error at adding the property value for " + getSubject().getBrowserText() + ".");
            updateActionLinks(isReplace());
        }

        @Override
        public void handleSuccess(EntityData newInstance) {
            if (newInstance == null) {
                GWT.log("Error at add property for " + getProperty().getBrowserText() + " and "  + getSubject().getBrowserText(), null);
                updateActionLinks(isReplace());
                return;
            }

            Object[] empty = new Object[properties.size() + getExtraColumnCount()];
            empty[properties.size()] = newInstance.getName();
            setExtraColumnValues(empty, new EntityPropertyValues(newInstance));

            updateActionLinks(isReplace());

            Record plant = recordDef.createRecord(empty);
            grid.stopEditing();
            store.insert(0, plant);
            if (columnEditorConfigurations[0] == null) {
                grid.startEditing(0, 0);
            }
            else {
                onValueColumnClicked(grid, 0, 0);
            }
        }
    }


    protected class AddExistingValueHandler extends AbstractAsyncHandler<Void> {

        private EntityData newEntityData;

        public AddExistingValueHandler(EntityData newEntityData) {
            this.newEntityData = newEntityData;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at adding property for " + getProperty().getBrowserText() + " and "  + getSubject().getBrowserText(), caught);
            MessageBox.alert("There was an error at adding the property value(s) for " + getSubject().getBrowserText() + ".");
            InstanceGridWidget.this.refresh();
            updateActionLinks(isReplace());
        }

        @Override
        public void handleSuccess(Void result) {
            InstanceGridWidget.this.refresh();
            updateActionLinks(isReplace());
        }
    }


    /*
     * Inner classes
     */

    class ColumnRenderer implements Renderer {
        private String type = "";
        private Map<String, String> valueToDisplayTextMap = null;

        public ColumnRenderer(final String fieldType) {
            this(fieldType, null);
        }

        public ColumnRenderer(final String fieldType, Map<String, String> valueToDisplayTextMap) {
            this.type = fieldType;
            this.valueToDisplayTextMap = valueToDisplayTextMap;
        }

        public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                Store store) {
            String field = record.getAsString(store.getFields()[colNum]);

            if (type != null) {
                if (type.equals(FormConstants.FIELD_TYPE_LINK_ICON)) {
                    return renderLinkIcon(value, cellMetadata, record, rowIndex, colNum, store);
                }
                else if (type.equals(FormConstants.FIELD_TYPE_CHECKBOX)) {
                    return renderCheckBox(value, cellMetadata, record, rowIndex, colNum, store);
                }
                else if (type.equals(FormConstants.FIELD_TYPE_RADIO)) {
                    return renderRadioButton(value, cellMetadata, record, rowIndex, colNum, store);
                }
            }

            if (valueToDisplayTextMap != null && field != null) {
                String newFieldValue = valueToDisplayTextMap.get(field);
                if (newFieldValue != null) {
                    field = newFieldValue;
                }
            }
            if (field == null) {
                field = "";
            }
            field = preRenderColumnContent(field, type);
            return Format.format(
                    "<style type=\"text/css\">.x-grid3-cell-inner, .x-grid3-hd-inner { white-space:normal !important; }</style> {0}",
                    new String[] { (field) });
        }

        private String renderLinkIcon(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                Store store) {
            if (value == null || value.toString().length() == 0) {
                return "";
            } else {
                return "<a href= \"" + value + "\" target=\"_blank\">"
                + "<img src=\"images/world_link.png\"></img>" + "</a>";
            }
        }

        private String renderCheckBox(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                Store store) {
            boolean checked = false;
            boolean unknown = (value == null);
            try {
                if (value instanceof Boolean) {
                    checked = ((Boolean)value).booleanValue();
                }
                else {
                    checked = new Boolean((String)value).booleanValue();
                }
            }
            catch (Exception e) {
                unknown = true;
            }

            if (unknown) {
                return "<img class=\"checkbox\" src=\"images/unknown_check.gif\"/>";//<div style=\"text-align: center;\"> IMG_TAG </div>;
            }

            return "<img class=\"checkbox\" " +
            		"src=\"js/ext/resources/images/default/menu/" +
                            (checked ? "checked.gif" : "unchecked.gif") + "\"/>";//<div style=\"text-align: center;\"> IMG_TAG </div>;
        }

        private String renderRadioButton(final Object value, final CellMetadata cellMetadata,
                final Record record, final int rowIndex, final int colNum, final Store store) {
            boolean checked = false;
            boolean unknown = (value == null);
            try {
                if (value instanceof Boolean) {
                    checked = ((Boolean)value).booleanValue();
                }
                else {
                    checked = new Boolean((String)value).booleanValue();
                }
            }
            catch (Exception e) {
                unknown = true;
            }

            if (unknown) {
                checked = false;
            }

            final boolean oldValue = checked;

            final Radio btn = new Radio("", new CheckboxListenerAdapter(){
                @Override
                public void onCheck(Checkbox field, boolean checked) {
                    if (checked != oldValue) {
                        if (isWriteOperationAllowed()) {
                            onRadioButtonChecked(checked, value, cellMetadata, record, rowIndex, colNum, store);
                        }
                        else {
                            record.set(record.getFields()[colNum], oldValue);
                            record.commit();
                        }
                    }
                }
            });
            final String id = Ext.generateId();
            btn.setRenderToID(id);
            btn.setChecked(checked);
            Scheduler.get().scheduleDeferred(new Command() {
                public void execute() {
                    if (DOM.getElementById(id) != null) {
                        btn.render(id);
                    }
                }
            });
            return Format.format(
                    "<div id='{0}' style='padding:0px;width:100%;height:20px;'></div>", id);

        }
    }

    protected void onRadioButtonChecked(boolean checked, Object value, CellMetadata cellMetadata,
            Record record, int rowIndex, int colNum, Store store) {
        //override this in subclasses
    }


    final class DeleteContextMenu extends Menu{
        public DeleteContextMenu(String menuText, String menuIcon, final Record record, final int colindex, final ValueType valueType) {
            MenuItem item = new MenuItem();
            item.setText(menuText);
            item.setIcon(menuIcon);
            item.addListener(new BaseItemListenerAdapter() {
                @Override
                public void onClick(BaseItem item, EventObject e) {
                    super.onClick(item, e);
                    String field = record.getFields()[colindex];
                    record.set(field, (String)null);
                }
            });
            addItem(item);
        }
    }
}
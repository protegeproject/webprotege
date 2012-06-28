package edu.stanford.bmir.protege.web.client.ui.icd;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.ICDServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityPropertyValues;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormConstants;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.InstanceGridWidget;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class ICDLinearizationWidget extends InstanceGridWidget {

    private static final String TOP_CLASS_PROP = "topClass";

    protected static String PARENT_DISPLAY_FIELD_NAME = "@parent@";

    private static int OFFSET_PARENT_ENTITY_COLUMN = 1;

    private static int OFFSET_DELETE_COLUMN = -1;
    private static int OFFSET_COMMENT_COLUMN = 2;
    private static int OFFSET_MAX_COLUMN = OFFSET_COMMENT_COLUMN;

    private String fieldNameParent = null;
    private int colIndexParent = -1;

    private Record currentRecord;
    private Window selectWindow;
    private Selectable selectable;
    private String topClass;

    public ICDLinearizationWidget(Project project) {
        super(project);
    }

    @Override
    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        super.setup(widgetConfiguration, propertyEntityData);
        topClass = UIUtil.getStringConfigurationProperty(getWidgetConfiguration(), TOP_CLASS_PROP, null);
    }

    @Override
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
                ColumnConfig colConfig = createColumn((Map<String, Object>) widgetConfig.get(key), fieldDef, columns,
                        props);

                //do not allow users to rearrange instance order
                colConfig.setSortable(false);

                //hide and extract info from column storing the parent instance name
                if (colConfig.getHeader().toLowerCase().contains("parent")) {
                    fieldNameParent = colConfig.getDataIndex();
                    //either this, or there is a better way to get colIndexParent using prop2Index (see below)
                    //colIndexParent = Integer.parseInt(key.substring(FormConstants.COLUMN_PREFIX.length())) - 1; //Column1 -> index 0, Column2 -> index 1, etc.
                    
                    //hide parent column only when we create the parent display column below,
                    //because we want to know what is the user specified setting 
                    //for the hidden status of this column
                    //colConfig.setHidden(true);
                }

                //hide and extract info from column storing the sequence number
                if (colConfig.getHeader().toLowerCase().contains("#")) {
                    colConfig.setHidden(true);
                }

                //make sorting label column editable
                if (colConfig.getHeader().toLowerCase().contains("sort") ||
                        colConfig.getHeader().toLowerCase().contains("label") ) {
                    colConfig.setEditor(new GridEditor(new TextField()));
                }
            }
        }

        properties = Arrays.asList(props);
        for (int i = 0; i < props.length; i++) {
            prop2Index.put(props[i], i);
        }
        //set the colIndexParent
        if (fieldNameParent != null) {
            Integer fieldNameParentIndex = prop2Index.get(fieldNameParent);
            colIndexParent = fieldNameParentIndex == null ? -1 : fieldNameParentIndex.intValue();
        }

        createInstanceColumn(fieldDef, columns, colCount);
        createParentEntityColumn(fieldDef, columns, colCount);
        createActionColumns(fieldDef, columns, colCount);

        recordDef = new RecordDef(fieldDef);

        ColumnModel columnModel = new ColumnModel(columns);
        getGridPanel().setColumnModel(columnModel);
    }

    @Override
    protected Anchor createAddNewValueHyperlink() {
        return null;
    }

    @Override
    protected Anchor createAddExistingHyperlink() {
        return null;
    }

    protected void createParentEntityColumn(FieldDef[] fieldDef, ColumnConfig[] columns, int colCount) {
        ColumnConfig instCol = new ColumnConfig("", PARENT_DISPLAY_FIELD_NAME, 25);
        instCol.setTooltip("The parent of the category in a certain linearization.\n "
                + "Each class must have one and only one parent in every linearization.\n "
                + "Click on the cell to select a (new) parent.");
        if (fieldNameParent != null) {
            ColumnConfig userSpecParentCol = null;
            for (int i = 0; i < fieldDef.length; i++) {
                if (fieldDef[i] != null && fieldDef[i].getName().equals(fieldNameParent)) {
                    userSpecParentCol = columns[i];
                    break;
                }
            }
            if (userSpecParentCol != null) {
                instCol.setHeader(userSpecParentCol.getHeader());
                userSpecParentCol.setHeader(instCol.getHeader() + " Instance");
                String userSpecTooltip = userSpecParentCol.getTooltip();
                if (userSpecTooltip != null && userSpecTooltip.length() > 0) {
                    instCol.setTooltip(userSpecTooltip);
                }
                //apply the user's "hidden" preference for the "parent instance" column 
                //to the "parent display" column, then hide the "parent instance" column 
                instCol.setHidden(userSpecParentCol.getHidden());
                userSpecParentCol.setHidden(true);

                //if user-specified parent column has an id, and it is set to be the auto-expanded column id,
                //make the "parent display" column auto-expanded instead of the "parent instance" column
                String userSpecParentColId = userSpecParentCol.getId();
                if (userSpecParentColId != null && userSpecParentColId.equals(autoExpandColId)) {
                    autoExpandColId = HTMLPanel.createUniqueId();
                    instCol.setId(autoExpandColId);
                }
                else {
                    instCol.setWidth(userSpecParentCol.getWidth());
                }
            }
        }
        instCol.setRenderer(new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                String entityDataValue = (String) value;
                if (entityDataValue == null ) {
                    return "<DIV style=\"color:GRAY\">Click here to select a parent</DIV>";
                } else if (entityDataValue.startsWith("[") && entityDataValue.endsWith("]") ) {
                    return "<DIV style=\"color:GRAY\">" + entityDataValue + "</DIV>";
                } else {
                    return entityDataValue;
                }
            }
        });

        fieldDef[colCount + OFFSET_PARENT_ENTITY_COLUMN] = new StringFieldDef(PARENT_DISPLAY_FIELD_NAME);
        columns[colCount + OFFSET_PARENT_ENTITY_COLUMN] = instCol;
    }

    @Override
    protected void attachListeners() {
        getGridPanel().addGridCellListener(new GridCellListenerAdapter() {
            double timeOfLastClick = 0;

            @Override
            public void onCellClick(final GridPanel grid, final int rowIndex, final int colindex, final EventObject e) {
                double eventTime = e.getTime();
                if (eventTime - timeOfLastClick > 500) { //not the second click in a double click
                    onCellClickOrDblClick(grid, rowIndex, colindex, e);
                }
                /*
                 * Set new value for timeOfLastClick the time the last click was handled
                 * We use the current time (and not eventTime), because some time may have passed since eventTime
                 * while executing the onCellClickOrDblClick method.
                 */
                timeOfLastClick = new Date().getTime();
            }

            private void onCellClickOrDblClick(GridPanel grid, final int rowIndex, int colindex, EventObject e) {
                // int offsetDeleteColumn = getOffsetDeleteColumn();
                int offsetCommentColumn = getOffsetCommentColumn();
                if (e.getTarget(".checkbox", 1) != null) {
                    Record record = store.getAt(rowIndex);
                    if (record != null) {
                        if (isWriteOperationAllowed()) {
                            String field = record.getFields()[colindex];
                            String value = record.getAsString(field);
                            if (Boolean.parseBoolean(value) == true) {
                                record.set(field, Boolean.FALSE.toString());
                            } else {
                                record.set(field, Boolean.TRUE.toString());
                            }
                            updateInstanceValue(record, colindex, value == null ? "" : value, record.getAsString(field), ValueType.Boolean, false);
                        }
                    }
                } else if (colindex == properties.size() + OFFSET_PARENT_ENTITY_COLUMN) {
                    Record record = store.getAt(rowIndex);
                    if (record != null) {
                        if (isWriteOperationAllowed()) {
                            String field = record.getFields()[colindex];
                            selectNewParents(record, field);
                        }
                    }
                } else if (offsetCommentColumn != -1 && colindex == properties.size() + offsetCommentColumn) {
                    onCommentColumnClicked(rowIndex);
                } else {
                    Record record = store.getAt(rowIndex);
                    if (record != null) {
                        String valueType = record.getAsString("valueType");
                        if (valueType != null && (!valueType.equalsIgnoreCase("string"))) {
                            //TODO - implement later
                        }
                    }
                }
            }

            @Override
            public void onCellContextMenu(final GridPanel grid, final int rowIndex, final int colindex, final EventObject e) {
                e.stopEvent();
                if (e.getTarget(".checkbox", 1) != null) {
                    final Record record = store.getAt(rowIndex);
                    if (record != null) {
                        if (isWriteOperationAllowed()) {
                            String field = record.getFields()[colindex];
                            String value = record.getAsString(field);
                            if (value != null && !"".equals(value)) {
                                Menu contextMenu = new DeleteContextMenu(
                                    "Unset value (i.e. set to 'Unknown')", "images/unknown_check.gif",
                                    record, colindex, ValueType.Boolean, false);
                                contextMenu.showAt(e.getXY()[0] + 5, e.getXY()[1] + 5);
                            }
                        }
                    }
                } else if (colindex == properties.size() + OFFSET_PARENT_ENTITY_COLUMN) {
                    Record record = store.getAt(rowIndex);
                    if (record != null) {
                        if (isWriteOperationAllowed()) {
                            if (colIndexParent >= 0) {
                                String field = record.getFields()[colIndexParent];
                                String value = record.getAsString(field);
                                if (value != null && !"".equals(value)) {
                                    Menu contextMenu = new DeleteContextMenu(
                                            "Remove linearization parent", "images/delete_small_16x16.png",
                                            record, colIndexParent, ValueType.Instance, true);
                                    contextMenu.showAt(e.getXY()[0] + 5, e.getXY()[1] + 5);
                                }
                            }
                        }
                    }
                }
            }

            final class DeleteContextMenu extends Menu{
                public DeleteContextMenu(String menuText, String menuIcon, final Record record, final int colindex, final ValueType valueType, final boolean resetParentDisplayName) {
                    MenuItem item = new MenuItem();
                    item.setText(menuText);
                    item.setIcon(menuIcon);
                    item.addListener(new BaseItemListenerAdapter() {
                        @Override
                        public void onClick(BaseItem item, EventObject e) {
                          super.onClick(item, e);
                          String field = record.getFields()[colindex];
                          String value = record.getAsString(field);
                          record.set(field, (String)null);
                          updateInstanceValue(record, colindex, value == null ? "" : value, record.getAsString(field), valueType, resetParentDisplayName);
                          //alternative solution, if we don't want to refresh the widget, but prefer to display a non-precise message
//                          if (resetParentDisplayName) {
//                              record.set(ICD_CATEGORY_DISPLAY_FIELD_NAME, (String)null); //this is optimistic and not precise
//                          }
                        }
                    });
                    addItem(item);
                }
            }
        });
    }

    protected void selectNewParents(Record record, String field) {
        currentRecord = record;
        selectWindow = getSelectionWindow();
        if (!selectWindow.isVisible()) {
            selectWindow.show();
            selectWindow.center();
        }
    }

    protected void updateInstanceValue(Record record, int colIndex, Object oldValue, Object newValue,
            ValueType valueType, final boolean updateLinParentName) {
        if (valueType == null) { //TODO: should be fixed
            valueType = ValueType.String;
        }
        String selSubject = record.getAsString(INSTANCE_FIELD_NAME);
        if (selSubject != null) {
            Object oldValueId = (oldValue == null ? "" : oldValue);
            oldValueId = (oldValueId instanceof EntityData) ? ((EntityData)oldValueId).getName() : oldValueId.toString();

            Object newValueId = newValue == null ? null :
                (newValue instanceof EntityData) ? ((EntityData)newValue).getName() : newValue.toString();

            propertyValueUtil.replacePropertyValue(getProject().getProjectName(), selSubject,
                    properties .get(colIndex), valueType, (String)oldValueId, (String)newValueId, GlobalSettings.getGlobalSettings().getUserName(),
                    getReplaceValueOperationDescription(colIndex, oldValue, newValue),
                    new ReplacePropertyValueHandlerForICDLinearizationWidget(new EntityData(
                            newValue == null ? null : newValue.toString(),
                                    newValue == null ? null : newValue.toString()), updateLinParentName));
        }
    }

    class ReplacePropertyValueHandlerForICDLinearizationWidget extends ReplacePropertyValueHandler {
        private boolean updateLinParentName;

        public ReplacePropertyValueHandlerForICDLinearizationWidget(EntityData newEntityData, boolean updateLinParentName) {
            super(newEntityData);
            this.updateLinParentName = updateLinParentName;
        }

        @Override
        public void onSuccess(Void result) {
            super.onSuccess(result);
            if (updateLinParentName) {
                refresh();
            }
        }
    }

    @Override
    protected GridEditor getGridEditor(final String fieldType, final Map<String, Object> config) {
        if (FormConstants.FIELD_TYPE_CHECKBOX.equals(fieldType)) {
            return null;
        }
        return null;
    }

    @Override
    protected int getOffsetDeleteColumn() {
        return OFFSET_DELETE_COLUMN;
    }

    @Override
    protected int getOffsetCommentColumn() {
        return OFFSET_COMMENT_COLUMN;
    }

    @Override
    protected int getMaxColumnOffset() {
        return OFFSET_MAX_COLUMN;
    }

    @Override
    protected int getExtraColumnCount() {
        return OFFSET_MAX_COLUMN + 1; //1 for the instance field
    }

    protected Window getSelectionWindow() {
        if (selectWindow == null) {
            selectWindow = new com.gwtext.client.widgets.Window();
            selectWindow.setTitle("Select parent");
            selectWindow.setWidth(600);
            selectWindow.setHeight(480);
            selectWindow.setMinWidth(300);
            selectWindow.setMinHeight(350);
            selectWindow.setLayout(new FitLayout());
            selectWindow.setPaddings(5);
            selectWindow.setButtonAlign(Position.CENTER);

            selectWindow.setCloseAction(Window.HIDE);
            selectWindow.setPlain(true);

            com.gwtext.client.widgets.Button cancelButton = new com.gwtext.client.widgets.Button("Cancel");
            cancelButton.addListener(new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    selectWindow.hide();
                }
            });

            com.gwtext.client.widgets.Button selectButton = new com.gwtext.client.widgets.Button("Select");
            selectButton.addListener(new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    Collection<EntityData> selection = getSelectable().getSelection();
                    if (selection == null || selection.size() == 0) {
                        MessageBox.alert("No selection", "No class selected. Please select a class from the tree.");
                        return;
                    }

                    if (currentRecord != null) {
                        EntityData firstSelectedParent = UIUtil.getFirstItem(selection);

                        EntityData oldValue = new EntityData(currentRecord.getAsString(fieldNameParent), currentRecord.getAsString(PARENT_DISPLAY_FIELD_NAME));

                        currentRecord.set(PARENT_DISPLAY_FIELD_NAME, firstSelectedParent.getBrowserText()); //this is optimistic
                        if (fieldNameParent != null) {
                            currentRecord.set(fieldNameParent, firstSelectedParent.getName());
                        }
                        if (colIndexParent >= 0) { //this col is wrong
                          //  int colIndex = getGridPanel().getColumnModel().getColumnCount() + OFFSET_PARENT_ENTITY_COLUMN - OFFSET_MAX_COLUMN - 1; //TT: not sure this works
                            updateInstanceValue(currentRecord, colIndexParent, oldValue, firstSelectedParent, ValueType.Instance, false); //false - because above we have already set the lin. parent name optimistically
                        }
                    }

                    selectWindow.hide();
                }
            });

            selectWindow.add((Component) getSelectable());
            selectWindow.addButton(selectButton);
            selectWindow.addButton(cancelButton);
        }
        return selectWindow;
    }


    public Selectable getSelectable() {
        if (selectable == null) {
            ClassTreePortlet selectableTree = new ICDClassTreePortlet(getProject(), true, false, false, true, topClass);
            selectableTree.setDraggable(false);
            selectableTree.setClosable(false);
            selectableTree.setCollapsible(false);
            selectableTree.setHeight(300);
            selectableTree.setWidth(450);
            selectable = selectableTree;
        }
        return selectable;
    }

    @Override
    protected void setExtraColumnValues(Object[] datarow, EntityPropertyValues epv) {
        super.setExtraColumnValues(datarow, epv);

        //add parent display text
        String parentName = null;
        for (PropertyEntityData ped : epv.getProperties()) {
            //datarow[getIndexOfProperty(ped.getName())] = UIUtil.commaSeparatedList(epv.getPropertyValues(ped));
            if (ped.getName().equals(fieldNameParent)) {
                List<EntityData> values = epv.getPropertyValues(ped);
                if (values != null && values.size() > 0) {
                    parentName = UIUtil.getDisplayText(values.get(0));
                    break;
                }
            }
        }

        datarow[properties.size() + OFFSET_PARENT_ENTITY_COLUMN] = parentName;
    }

    @Override
    protected void fillValues(List<String> subjects, List<String> props) {
        store.removeAll();
        ICDServiceManager.getInstance().getEntityPropertyValuesForLinearization(getProject().getProjectName(), subjects, props, properties,
                new GetTriplesHandler(getSubject()));
    }

    @Override
    protected String getCellText(EntityPropertyValues epv, PropertyEntityData ped) {
        if (ped.getName().equals(fieldNameParent)) {
            List<EntityData> values = epv.getPropertyValues(ped);
            if (values != null && values.size() > 0) {
                return values.get(0).getName();
            }
            return null;
        } else {
            return super.getCellText(epv, ped);
        }
    }

}

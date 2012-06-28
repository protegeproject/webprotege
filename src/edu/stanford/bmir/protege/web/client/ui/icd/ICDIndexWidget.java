package edu.stanford.bmir.protege.web.client.ui.icd;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ICDServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityPropertyValues;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormConstants;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.InstanceGridWidget;

public class ICDIndexWidget extends InstanceGridWidget {

    //private static final String TOP_CLASS_PROP = "topClass";

    protected static String SYNONYM_CLASS_NAME = "http://who.int/icd#SynonymTerm";
    protected static String NARROWER_CLASS_NAME = "http://who.int/icd#NarrowerTerm";
    protected static String INCLUSION_CLASS_NAME = "http://who.int/icd#BaseInclusionTerm";

    protected static String SYNONYM_FIELD_NAME = "@synonym@";
    protected static String NARROWER_FIELD_NAME = "@narrower@";
    protected static String INCLUSION_FIELD_NAME = "@inclusion@";

    private static int OFFSET_SYNONYM_COLUMN = 1;
    private static int OFFSET_NARROWER_COLUMN = 2;
    private static int OFFSET_INCLUSION_COLUMN = 3;

    private static int OFFSET_DELETE_COLUMN = 4;
    private static int OFFSET_COMMENT_COLUMN = 5;
    private static int OFFSET_MAX_COLUMN = OFFSET_COMMENT_COLUMN;

    private String fieldNameType = null;
    private int colIndexType = -1;

    public ICDIndexWidget(Project project) {
        super(project);
    }


    @Override
    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        super.setup(widgetConfiguration, propertyEntityData);
        //topClass = UIUtil.getStringConfigurationProperty(getWidgetConfiguration(), TOP_CLASS_PROP, null);
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
//
//                //do not allow users to rearrange instance order
//                colConfig.setSortable(false);

                //hide and extract info from column storing the parent instance name
                if (colConfig.getHeader().toLowerCase().contains("type")) {
                    fieldNameType = colConfig.getDataIndex();
                    //either this, or there is a better way to get colIndexParent using prop2Index (see below)
                    //colIndexParent = Integer.parseInt(key.substring(FormConstants.COLUMN_PREFIX.length())) - 1; //Column1 -> index 0, Column2 -> index 1, etc.
                    colConfig.setHidden(true);
                }
            }
        }

        properties = Arrays.asList(props);
        for (int i = 0; i < props.length; i++) {
            prop2Index.put(props[i], i);
        }
        //set the colIndexParent
        if (fieldNameType != null) {
            Integer fieldNameParentIndex = prop2Index.get(fieldNameType);
            colIndexType = fieldNameParentIndex == null ? -1 : fieldNameParentIndex.intValue();
        }

        createInstanceColumn(fieldDef, columns, colCount);
        createCheckboxColumns(fieldDef, columns, colCount);
        createActionColumns(fieldDef, columns, colCount);

        recordDef = new RecordDef(fieldDef);

        ColumnModel columnModel = new ColumnModel(columns);
        getGridPanel().setColumnModel(columnModel);
    }


    private void createCheckboxColumns(FieldDef[] fieldDef, ColumnConfig[] columns, int colCount) {
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


        if (OFFSET_SYNONYM_COLUMN != -1) {
            ColumnConfig synonymCol = createSynonymColumn();
            fieldDef[colCount + OFFSET_SYNONYM_COLUMN] = new BooleanFieldDef(SYNONYM_FIELD_NAME);
            columns[colCount + OFFSET_SYNONYM_COLUMN] = synonymCol;
        }

        if (OFFSET_NARROWER_COLUMN != -1) {
            ColumnConfig narrowerCol = createNarrowerColumn();
            fieldDef[colCount + OFFSET_NARROWER_COLUMN] = new BooleanFieldDef(NARROWER_FIELD_NAME);
            columns[colCount + OFFSET_NARROWER_COLUMN] = narrowerCol;
        }

        if (OFFSET_INCLUSION_COLUMN != -1) {
            ColumnConfig inclusionCol = createInclusionColumn();
            fieldDef[colCount + OFFSET_INCLUSION_COLUMN] = new BooleanFieldDef(INCLUSION_FIELD_NAME);
            columns[colCount + OFFSET_INCLUSION_COLUMN] = inclusionCol;
        }

    }

    protected ColumnConfig createSynonymColumn() {
        ColumnConfig synonymCol = new ColumnConfig("Synonym", SYNONYM_FIELD_NAME, 75);
        synonymCol.setTooltip("Check this if index is a synonym");
        synonymCol.setAlign(getTextAlign("center"));

        //TODO continue here

        synonymCol.setRenderer(getColumnRenderer(FormConstants.FIELD_TYPE_RADIO, null));

//        synonymCol.setRenderer(new Renderer() {
//            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
//                    Store store) {
//                return "<img src=\"images/delete.png\" title=\""
//                        + " Click on the icon to remove value.\"></img>";
//            }
//        });
        return synonymCol;
    }


    protected ColumnConfig createNarrowerColumn() {
        //TODO write this method!!!!!!
        ColumnConfig narrowerCol = new ColumnConfig("Narrower", NARROWER_FIELD_NAME, 75);
        narrowerCol.setTooltip("Check this if index is a narrower term");
        narrowerCol.setAlign(getTextAlign("center"));

        narrowerCol.setRenderer(getColumnRenderer(FormConstants.FIELD_TYPE_RADIO, null));

        return narrowerCol;
    }

    protected ColumnConfig createInclusionColumn() {
        //TODO write this method!!!!!!
        ColumnConfig inclusionCol = new ColumnConfig("Inclusion", INCLUSION_FIELD_NAME, 75);
        inclusionCol.setTooltip("Check this if index is an inclusion term");
        inclusionCol.setAlign(getTextAlign("center"));

        inclusionCol.setRenderer(getColumnRenderer(FormConstants.FIELD_TYPE_CHECKBOX, null));

        return inclusionCol;
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
        return OFFSET_MAX_COLUMN + 1;   //1 for the instance field
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
                int offsetDeleteColumn = getOffsetDeleteColumn();
                int offsetCommentColumn = getOffsetCommentColumn();
                if (e.getTarget(".checkbox", 1) != null) {
                    Record record = store.getAt(rowIndex);
                    if (record != null) {
                        if (isWriteOperationAllowed()) {
                            String field = record.getFields()[colindex];
                            String value = record.getAsString(field);

                            if (colindex == properties.size() + OFFSET_INCLUSION_COLUMN) {
                                String selSubject = record.getAsString(INSTANCE_FIELD_NAME);
                                if (selSubject != null) {
                                    if (Boolean.parseBoolean(value) == true) {
                                        record.set(field, false);
                                    } else {
                                        record.set(field, true);
                                    }

                                    ICDServiceManager.getInstance().changeInclusionFlagForIndex(
                                            getProject().getProjectName(), getSubject().getName(), selSubject, properties, record.getAsBoolean(INCLUSION_FIELD_NAME),
                                            new ChangeInclusionFlagForIndex(getSubject(), record));
                                }
                            }
                            else {
                                if (Boolean.parseBoolean(value) == true) {
                                    record.set(field, Boolean.FALSE.toString());
                                } else {
                                    record.set(field, Boolean.TRUE.toString());
                                }

                                updateInstanceValue(record, colindex, value == null ? "" : value, record.getAsString(field), ValueType.Boolean, false);
                            }
                        }
                    }
//                } else if (colindex == properties.size() + OFFSET_PARENT_ENTITY_COLUMN) {
//                    Record record = store.getAt(rowIndex);
//                    if (record != null) {
//                        if (UIUtil.confirmOperationAllowed(getProject())) {
//                            String field = record.getFields()[colindex];
//                            selectNewParents(record, field);
//                        }
//                    }
                } else if (offsetDeleteColumn != -1 && colindex == properties.size() + offsetDeleteColumn) {
                    onDeleteColumnClicked(rowIndex);
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

        });
    }


    protected class ChangeInclusionFlagForIndex extends AbstractAsyncHandler<EntityPropertyValues> {

        private EntityData mySubject;
        private Record record;

        public ChangeInclusionFlagForIndex(EntityData subject, Record record) {
            this.mySubject = subject;
            this.record = record;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("ICD Index Widget: Error at getting information about the updated index " + record.getAsObject(INSTANCE_FIELD_NAME) + " for " + getSubject(), caught);
            //reset check box and remove dirty flag
            record.set(INCLUSION_FIELD_NAME, ! record.getAsBoolean(INCLUSION_FIELD_NAME));
            record.commit();
        }

        @Override
        public void handleSuccess(EntityPropertyValues result) {
            if (mySubject.equals(getSubject())) {
                for (PropertyEntityData ped : result.getProperties()) {
                    if (ped.getName().equals(fieldNameType)) {
                        int propIndex = getIndexOfProperty(ped.getName());
                        String fieldName = record.getFields()[propIndex];
                        String types = typeFullNamesToString(result.getPropertyValues(ped));
                        record.set(fieldName, getCellText(result, ped)); //result.getPropertyValues(ped).toString()); //

                        record.set(INCLUSION_CLASS_NAME, types.contains(INCLUSION_CLASS_NAME));

                        record.commit();
                    }
                }
                //refresh();
                //ICDIndexWidget.this.getGridPanel().getView().refresh();
            }
        }
    }


    protected void updateInstanceValue(Record record, int colIndex, Object oldValue, Object newValue,
            ValueType valueType, final boolean refreshAlsoOtherColumns) {
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
                    new ReplacePropertyValueHandlerForICDIndexWidget(new EntityData(
                            newValue == null ? null : newValue.toString(),
                                    newValue == null ? null : newValue.toString()), refreshAlsoOtherColumns));

        }
    }

    class ReplacePropertyValueHandlerForICDIndexWidget extends ReplacePropertyValueHandler {
        private boolean refreshAlsoOtherColumns;

        public ReplacePropertyValueHandlerForICDIndexWidget(EntityData newEntityData, boolean refreshAlsoOtherColumns) {
            super(newEntityData);
            this.refreshAlsoOtherColumns = refreshAlsoOtherColumns;
        }

        @Override
        public void onSuccess(Void result) {
            super.onSuccess(result);
            if (refreshAlsoOtherColumns) {
                refresh();
            }
        }
    }

    @Override
    protected void onRadioButtonChecked(boolean checked,
            Object value, CellMetadata cellMetadata, Record record,
            int rowIndex, int colNum, Store store) {
        //TODO remove this line
        GWT.log("Radio checked in col: " + colNum + ". (Synonym col = " + (properties.size() + OFFSET_SYNONYM_COLUMN) + ")");
        if (colNum == properties.size() + OFFSET_SYNONYM_COLUMN) {
            String selSubject = record.getAsString(INSTANCE_FIELD_NAME);
            if (selSubject != null) {
                //add dirty flags
                record.set(SYNONYM_FIELD_NAME, true);
                record.set(NARROWER_FIELD_NAME, false);

                ICDServiceManager.getInstance().changeIndexType(
                        getProject().getProjectName(), getSubject().getName(), selSubject, properties,SYNONYM_CLASS_NAME,
                        new ChangeIndexTypeHandler(getSubject(), record));
            }
        }
        else if (colNum == properties.size() + OFFSET_NARROWER_COLUMN) {
            String selSubject = record.getAsString(INSTANCE_FIELD_NAME);
            if (selSubject != null) {
                //add dirty flags
                record.set(SYNONYM_FIELD_NAME, false);
                record.set(NARROWER_FIELD_NAME, true);

                ICDServiceManager.getInstance().changeIndexType(
                        getProject().getProjectName(), getSubject().getName(), selSubject, properties, NARROWER_CLASS_NAME,
                        new ChangeIndexTypeHandler(getSubject(), record));
            }
        }
    };

    protected class ChangeIndexTypeHandler extends AbstractAsyncHandler<EntityPropertyValues> {

        private EntityData mySubject;
        private Record record;

        public ChangeIndexTypeHandler(EntityData subject, Record record) {
            this.mySubject = subject;
            this.record = record;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("ICD Index Widget: Error at getting information about the updated index " + record.getAsObject(INSTANCE_FIELD_NAME) + " for " + getSubject(), caught);
            //reset radio buttons and remove dirty flag
            if (record.isModified(SYNONYM_FIELD_NAME)) {
                record.set(SYNONYM_FIELD_NAME, ! record.getAsBoolean(SYNONYM_FIELD_NAME));
            }
            if (record.isModified(NARROWER_FIELD_NAME)) {
                record.set(NARROWER_FIELD_NAME, ! record.getAsBoolean(NARROWER_FIELD_NAME));
            }
            record.commit();
        }

        @Override
        public void handleSuccess(EntityPropertyValues result) {
            if (mySubject.equals(getSubject())) {
                for (PropertyEntityData ped : result.getProperties()) {
                    if (ped.getName().equals(fieldNameType)) {
                        int propIndex = getIndexOfProperty(ped.getName());
                        String fieldName = record.getFields()[propIndex];
                        String types = typeFullNamesToString(result.getPropertyValues(ped));
                        record.set(fieldName, getCellText(result, ped)); //result.getPropertyValues(ped).toString()); //

                        record.set(SYNONYM_FIELD_NAME, types.contains(SYNONYM_CLASS_NAME));
                        record.set(NARROWER_FIELD_NAME, types.contains(NARROWER_CLASS_NAME));

                        record.commit();
                    }
                }
                //refresh();
                //ICDIndexWidget.this.getGridPanel().getView().refresh();
            }
        }
    }

    @Override
    protected void setExtraColumnValues(Object[] datarow, EntityPropertyValues epv) {
        super.setExtraColumnValues(datarow, epv);

        //set type flags
        String types = null;
        for (PropertyEntityData ped : epv.getProperties()) {
            //data[i][getIndexOfProperty(ped.getName())] = UIUtil.commaSeparatedList(epv.getPropertyValues(ped));
            if (ped.getName().equals(fieldNameType)) {
                List<EntityData> values = epv.getPropertyValues(ped);
                types = typeFullNamesToString(values);
            }
        }
        if (types == null) {
            types = "";
        }

        datarow[properties.size() + OFFSET_SYNONYM_COLUMN] = types.contains(SYNONYM_CLASS_NAME);
        datarow[properties.size() + OFFSET_NARROWER_COLUMN] = types.contains(NARROWER_CLASS_NAME);
        datarow[properties.size() + OFFSET_INCLUSION_COLUMN] = types.contains(INCLUSION_CLASS_NAME);
    }


    private String typeFullNamesToString(List<EntityData> typeValues) {
        String types;
        types = "";
        boolean first = true;
        if (typeValues != null && typeValues.size() > 0) {
            for (EntityData value : typeValues) {
                if (first) {
                    first = false;
                }
                else {
                    types +=", ";
                }
                types += value.getName();
            }
            //types = UIUtil.getDisplayText(values.get(0));
        }
        return types;
    }

}

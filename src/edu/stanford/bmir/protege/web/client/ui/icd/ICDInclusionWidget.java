package edu.stanford.bmir.protege.web.client.ui.icd;

import java.util.Arrays;
import java.util.Map;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.Renderer;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormConstants;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.InstanceGridWidget;

public class ICDInclusionWidget extends InstanceGridWidget {

    //private static final String TOP_CLASS_PROP = "topClass";

    protected static String ICD_CATEGORY_DISPLAY_FIELD_NAME = "@icd_category@";

    private static int OFFSET_ICD_CATEGORY_ENTITY_COLUMN = 1;

    private static int OFFSET_DELETE_COLUMN = 2;
    private static int OFFSET_COMMENT_COLUMN = 3;
    private static int OFFSET_MAX_COLUMN = OFFSET_COMMENT_COLUMN;

    private String fieldNameIcdCategory = null;
    private int colIndexIcdCategory = -1;

    public ICDInclusionWidget(Project project) {
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
                if (colConfig.getHeader().toLowerCase().contains("category")) {
                    fieldNameIcdCategory = colConfig.getDataIndex();
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
        if (fieldNameIcdCategory != null) {
            Integer fieldNameParentIndex = prop2Index.get(fieldNameIcdCategory);
            colIndexIcdCategory = fieldNameParentIndex == null ? -1 : fieldNameParentIndex.intValue();
        }

        createInstanceColumn(fieldDef, columns, colCount);
        createParentEntityColumn(fieldDef, columns, colCount);
        createActionColumns(fieldDef, columns, colCount);

        recordDef = new RecordDef(fieldDef);

        ColumnModel columnModel = new ColumnModel(columns);
        getGridPanel().setColumnModel(columnModel);
    }


    protected void createParentEntityColumn(FieldDef[] fieldDef, ColumnConfig[] columns, int colCount) {
        ColumnConfig instCol = new ColumnConfig("", ICD_CATEGORY_DISPLAY_FIELD_NAME, 25);
        instCol.setTooltip("The parent of the category in a certain linearization.\n "
                + "Each class must have one and only one parent in every linearization.\n "
                + "Click on the cell to select a (new) parent.");
        if (fieldNameIcdCategory != null) {
            ColumnConfig userSpecParentCol = null;
            for (int i = 0; i < fieldDef.length; i++) {
                if (fieldDef[i] != null && fieldDef[i].getName().equals(fieldNameIcdCategory)) {
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

        autoExpandColId = HTMLPanel.createUniqueId();
        instCol.setId(autoExpandColId);

        fieldDef[colCount + OFFSET_ICD_CATEGORY_ENTITY_COLUMN] = new StringFieldDef(ICD_CATEGORY_DISPLAY_FIELD_NAME);
        columns[colCount + OFFSET_ICD_CATEGORY_ENTITY_COLUMN] = instCol;
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

}

package edu.stanford.bmir.protege.web.client.ui.icd;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.Renderer;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.ICDServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityPropertyValues;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormConstants;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.InstanceGridWidget;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 *  ICD specific widget showing the inherited TAGs. Makes a specific service call.
 *  Misuses the {@link InstanceGridWidget}, which needs to be configured in a specific way,
 *  otherwise this widget will not work.
 *
 */
public class InheritedTagsGrid extends InstanceGridWidget {

    /*
     * TODO:
     * - render Display status with flags in checkbox
     *
     */

    //Virtual property names from the configuration of the grid
    private final static String COL_TAG = "tag";
    private final static String COL_INH_FOM = "inheritedFrom";

    public InheritedTagsGrid(Project project) {
        super(project);
    }

    @Override
    protected void fillValues(List<String> subjects, List<String> props) {
        getStore().removeAll();
        ICDServiceManager.getInstance().getSecondaryAndInheritedTags(getProject().getProjectName(), getSubject().getName(),
                new GetTriplesHandler(getSubject()));
    }

    @Override
    protected GridEditor getGridEditor(String fieldType, Map<String, Object> config) {
        return null; //make sure the cells are not editable
    }

    @Override
    protected String getCellText(EntityPropertyValues epv, PropertyEntityData ped) {
        return UIUtil.prettyPrintList(epv.getPropertyValues(ped), "<br />");
    }

    private boolean isInherited(Record record) {
        return !record.isEmpty(COL_INH_FOM);
    }

    @Override
    protected Object[][] createDataArray(List<EntityPropertyValues> entityPropertyValues) {
        int i = 0;
        Object[][] data = new Object[entityPropertyValues.size()][properties.size() + getExtraColumnCount()];
        for (EntityPropertyValues epv : entityPropertyValues) {
            boolean isInherited = false;
            for (PropertyEntityData ped : epv.getProperties()) {
                data[i][getIndexOfProperty(ped.getName())] = getCellText(epv, ped);
                if (ped.getName().equals(COL_INH_FOM)) {
                    Collection<EntityData> values = epv.getPropertyValues(ped);
                    if (values != null && values.size() > 0) {
                        isInherited = true;
                    }
                }
            }
            setExtraColumnValues(data[i], epv);
            //update the delete icon status
            if (isInherited) {
                int offsetDeleteColumn = getOffsetDeleteColumn();
                if (offsetDeleteColumn != -1) {
                    data[i][properties.size() + offsetDeleteColumn] = false;
                }
            }
            i++;
        }
        return data;
    }

    /*
     * Renderer
     */

    @Override
    protected Renderer getColumnRenderer(String fieldType, Map<String, Object> config) {
        String property = (String) config.get(FormConstants.PROPERTY);
        if (COL_TAG.equals(property)) {
            return getTagColumnRenderer();
        } else if (COL_INH_FOM.equals(property)) {
            return getInheritedFromRenderer();
        }
        return super.getColumnRenderer(fieldType, config);
    }

    private Renderer getInheritedFromRenderer() {
        return new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                return getRenderString(record, value);
            }
        };
    }

    private Renderer getTagColumnRenderer() {
        return new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum, Store store) {
                return getRenderString(record, value);
            }
        };
    }

    private String getRenderString(Record record, Object value) {
        if (isInherited(record)) {
            return "<span style=\"font-style:italic; color:grey;\">" + (value == null ? "" : value.toString()) +  "</span>";
        }
        return value == null ? "" : value.toString();
    }
}

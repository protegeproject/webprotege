package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

import java.util.Collection;
import java.util.List;

public class PropertiesViewPortlet extends AbstractEntityPortlet {

    private GridPanel propGrid;
    private RecordDef recordDef;
    private Store store;

    public PropertiesViewPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setTitle("Related Properties");
        createGrid();
        add(propGrid);
    }

    protected void createGrid() {
        propGrid = new GridPanel();

        createColumns();

        recordDef = new RecordDef(new FieldDef[] {new ObjectFieldDef("prop"), new ObjectFieldDef("value"), new StringFieldDef("card")} );

        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy dataProxy = new MemoryProxy(new Object[][] {});
        store = new Store(dataProxy, reader);
        propGrid.setStore(store);

        propGrid.setHeight(150);
        propGrid.setAutoWidth(true);
        propGrid.setLoadMask("Loading properties");
        propGrid.setStripeRows(true);
        propGrid.setAutoExpandColumn("value");
        propGrid.setAnimCollapse(true);

        GridView gridView = new GridView();
        gridView.setAutoFill(true);
        //gridView.setScrollOffset(0);
        propGrid.setView(gridView);

        store.load();
    }

    protected void createColumns() {
        ColumnConfig propCol = new ColumnConfig();
        propCol.setHeader("Property");
        propCol.setId("prop");
        propCol.setDataIndex("prop");
        propCol.setResizable(true);
        propCol.setSortable(true);

        ColumnConfig valueCol = new ColumnConfig();
        valueCol.setHeader("Value");
        valueCol.setId("value");
        valueCol.setDataIndex("value");
        valueCol.setResizable(true);
        valueCol.setSortable(true);

        ColumnConfig cardCol = new ColumnConfig();
        cardCol.setHeader("Cardinality");
        cardCol.setId("card");
        cardCol.setDataIndex("card");
        cardCol.setResizable(true);
        cardCol.setSortable(true);

        ColumnConfig[] columns = new ColumnConfig[] { propCol, valueCol, cardCol};

        ColumnModel columnModel = new ColumnModel(columns);
        propGrid.setColumnModel(columnModel);
    }

    @Override
    public void reload() {
        setTitle("Related properties" + (_currentEntity == null ? " (nothing selected)" : " for " + _currentEntity.getBrowserText()));

        store.removeAll();
        if (_currentEntity != null) {
            OntologyServiceManager.getInstance().getRelatedProperties(getProject().getProjectId(), _currentEntity.getName(),
                new GetTriplesHandler());
        }
    }

    public Collection<EntityData> getSelection() {
        Record selRec = propGrid.getSelectionModel().getSelected();
        return selRec != null ? UIUtil.createCollection(((EntityData)selRec.getAsObject("prop"))) : null;
    }

    @Override
    protected void onRefresh() {
        reload();
    }

    /*
     * Remote calls
     */

    class GetTriplesHandler extends AbstractAsyncHandler<List<Triple>> {

        @Override
        public void handleFailure(Throwable caught) {
           GWT.log("Error at retrieving props in domain for " + _currentEntity, caught);
           propGrid.setTitle("Error at retrieving the related properties for " + _currentEntity.getBrowserText());
        }

        @Override
        public void handleSuccess(List<Triple> triples) {
            for (Triple triple : triples) {
                int maxCardinality  = triple.getProperty().getMaxCardinality();
                Record rec = recordDef.createRecord(new Object[] {triple.getProperty(), triple.getValue(),
                        maxCardinality == 0 || maxCardinality == 1 ? "single" : "multiple"});
                store.add(rec);
            }
        }

    }

}

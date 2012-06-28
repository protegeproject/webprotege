package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import java.util.Date;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.util.PaginationUtil;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 *
 */
public class ChangesGrid extends GridPanel {

    private Project project;
    private RecordDef recordDef;
    private Store store;
    private ChangesProxyImpl proxy;

    public ChangesGrid(Project project) {
        this.project = project;
        createGrid();
    }

    private void createGrid() {
        createColumns();

        setAutoWidth(true);
        setAutoExpandColumn("ChangesGrid_ChangeDescCol");
        setId("changes-grid");
        setStripeRows(true);
        setTitle("Change History");
        setFrame(true);

        recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("desc"), new StringFieldDef("author"),
                new DateFieldDef("timestamp"), new StringFieldDef("applies") });

        ArrayReader reader = new ArrayReader(recordDef);
        proxy = new ChangesProxyImpl();
        store = new Store(proxy, reader);

        PagingToolbar pToolbar = PaginationUtil.getNewPagingToolbar(store, 20);
        this.setBottomToolbar(pToolbar);

        setStore(store);

        if (getStore() == null) {
            setStore(store);
        }

        reload();
    }

    private void createColumns() {
        ColumnConfig changeDescCol = new ColumnConfig("Description", "desc");
        changeDescCol.setId("ChangesGrid_ChangeDescCol");
        changeDescCol.setResizable(true);
        changeDescCol.setSortable(true);

        ColumnConfig authorCol = new ColumnConfig("Author", "author");
        authorCol.setResizable(true);
        authorCol.setSortable(true);

        ColumnConfig timestampCol = new ColumnConfig("Timestamp", "timestamp");
        timestampCol.setResizable(true);
        timestampCol.setSortable(true);

        ColumnConfig appliesToCol = new ColumnConfig("Applies to", "applies");
        appliesToCol.setResizable(true);
        appliesToCol.setSortable(true);
        appliesToCol.setHidden(true);

        ColumnConfig[] columns = new ColumnConfig[] { changeDescCol, authorCol, timestampCol, appliesToCol };
        ColumnModel columnModel = new ColumnModel(columns);
        setColumnModel(columnModel);
    }


    @SuppressWarnings("deprecation")
    public void reload() {
        store.removeAll();

        String projectName = null;

        if (project != null) {
            projectName = project.getProjectName();
        }
        /*
         * Tried using the non-deprecated methods here (i.e., from Calendar),
         * but this results in GWT error: "No source code is available for type
         * java.util.Calendar; did you forget to inherit a required module?".
         * Apparently, the Calendar class is not in the GWT 1.5 JRE emulation
         * library and can't be used on the client-side. Check available classes
         * for client-side code here: http://tinyurl.com/bwrden.
         */
        Date weekBegin = new Date();
        weekBegin.setDate(weekBegin.getDate() - 7);

        proxy.resetParams();
        proxy.setProjectName(projectName);
        proxy.setStartDate(weekBegin);
        proxy.setEndDate(new Date());
        store.load(0, ((PagingToolbar) this.getBottomToolbar()).getPageSize());
    }
}

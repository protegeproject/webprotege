package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.GroupingStore;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GroupingView;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.util.PaginationUtil;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class WatchedEntitiesGrid extends GridPanel {

    private static int INITIAL_PAGE_SIZE = 50;

    private GroupingStore store;
    private final Project project;
    private RecordDef recordDef;
    private WatchedEntitiesProxyImpl proxy;


    public WatchedEntitiesGrid(Project project) {
        this.project = project;
        createGrid();
    }

    private void createGrid() {
        setFrame(true);
        createColumns();

        recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("author"), new StringFieldDef("desc"),
                new StringFieldDef("applies"), new DateFieldDef("timestamp"), new StringFieldDef("entity") });
        ArrayReader reader = new ArrayReader(recordDef);

        store = new GroupingStore();
        store.setReader(reader);
        proxy = new WatchedEntitiesProxyImpl();
        store.setDataProxy(proxy);
        store.setSortInfo(new SortState("author", SortDir.ASC));
        store.setGroupField("entity");

        PagingToolbar pToolbar = PaginationUtil.getNewPagingToolbar(store, INITIAL_PAGE_SIZE);
        pToolbar.setPageSize(INITIAL_PAGE_SIZE);

        setBottomToolbar(pToolbar);

        setAutoWidth(true);
        setStripeRows(true);

        GroupingView gridView = new GroupingView();
        setView(gridView);

        setStore(store);

        setLoadMask("Fetching the watched entities... Please do not click on the refresh icon again.");
        setLoadMask(true);

        reload();
     }

    private void createColumns() {
        ColumnConfig authorCol = new ColumnConfig("Author", "author");
        authorCol.setResizable(true);
        authorCol.setSortable(true);

        ColumnConfig changeDescCol = new ColumnConfig("Change Description", "desc");
        changeDescCol.setResizable(true);
        changeDescCol.setSortable(true);
        changeDescCol.setId("changeDesc");
        setAutoExpandColumn("changeDesc");

        ColumnConfig appliesToCol = new ColumnConfig("Applies to", "applies");
        appliesToCol.setResizable(true);
        appliesToCol.setSortable(true);
        appliesToCol.setHidden(true);

        ColumnConfig timestampCol = new ColumnConfig("Timestamp", "timestamp");
        timestampCol.setResizable(true);
        timestampCol.setSortable(true);

        ColumnConfig entityCol = new ColumnConfig("Entity", "entity");
        entityCol.setResizable(true);
        entityCol.setSortable(true);
        entityCol.setHidden(true);

        ColumnConfig[] columns = new ColumnConfig[] { authorCol, changeDescCol, appliesToCol, timestampCol, entityCol };
        ColumnModel columnModel = new ColumnModel(columns);
        setColumnModel(columnModel);
    }

    public void reload() {
        String projectName = null;

        if (project != null) {
            projectName = project.getProjectName();
        } else {
            return;
        }

        String userName = GlobalSettings.getGlobalSettings().getUserName();

        proxy.resetParams();
        proxy.setProjectName(projectName);
        proxy.setUserName(userName);

        store.removeAll();
        store.load(0, ((PagingToolbar) this.getBottomToolbar()).getPageSize());
    }
}

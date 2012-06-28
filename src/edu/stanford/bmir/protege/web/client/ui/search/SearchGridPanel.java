package edu.stanford.bmir.protege.web.client.ui.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.ObjectFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.util.PaginationUtil;

/**
 * The panel used for displaying the search results.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class SearchGridPanel extends GridPanel {
    private RecordDef recordDef;
    private Store store;
    private GridRowListener gridRowListener;
    private SearchResultsProxyImpl proxy;

    private TextField searchField;
    private Component busyComponent;
    private AsyncCallback<Boolean> asyncCallback;

    public SearchGridPanel(){
        this(null);
    }

    public SearchGridPanel(AsyncCallback<Boolean> asyncCallback){
        this.asyncCallback = asyncCallback;
        createGrid();
        addGridRowListener(getRowListener());
    }

    public void reload(String projectName, String searchText, ValueType valueType) {
        proxy.resetParams();
        proxy.setProjectName(projectName);
        proxy.setSearchText(searchText);
        proxy.setValueType(valueType);

        if (busyComponent != null) {
            busyComponent.getEl().mask("Searching...", true);
        }
        store.load(0, ((PagingToolbar) this.getBottomToolbar()).getPageSize());
    }

    protected GridRowListener getRowListener() {
        if (gridRowListener == null) {
            gridRowListener = new GridRowListenerAdapter() {
                @Override
                public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
                    onEntityDblClick();
                }
            };
        }
        return gridRowListener;
    }

    protected void onEntityDblClick() {
    }

    protected void createGrid() {
        ColumnConfig browserTextCol = new ColumnConfig();
        browserTextCol.setHeader("Results");
        browserTextCol.setId("browserText");
        browserTextCol.setDataIndex("browserText");
        browserTextCol.setResizable(true);
        browserTextCol.setSortable(true);
        browserTextCol.setTooltip("Double click search result to select in tree.");

        ColumnConfig[] columns = new ColumnConfig[] { browserTextCol };

        ColumnModel columnModel = new ColumnModel(columns);
        setColumnModel(columnModel);

        recordDef = new RecordDef(new FieldDef[] { new ObjectFieldDef("entity"), new StringFieldDef("browserText") });

        ArrayReader reader = new ArrayReader(recordDef);

        proxy = new SearchResultsProxyImpl(new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                GWT.log("Problems at search", caught);
                if (busyComponent != null) {
                    busyComponent.getEl().unmask();
                }
                emptyContent();
                if (asyncCallback != null) {
                    asyncCallback.onFailure(caught);
                }
            }

            public void onSuccess(Boolean result) {
                if (result == false) {
                    emptyContent();
                }
                if (busyComponent != null) {
                    busyComponent.getEl().unmask();
                }
                if (asyncCallback != null) {
                    asyncCallback.onSuccess(result);
                }
            }
        });

        store = new Store(proxy, reader);
        setStore(store);

        PagingToolbar pToolbar = PaginationUtil.getNewPagingToolbar(store, 20);
        setBottomToolbar(pToolbar);

        addSearchToolbar();

        setStripeRows(true);
        setAutoExpandColumn("browserText");
        setFrame(true);
    }



    public EntityData getSelection() {
        Record selRecord = getSelectionModel().getSelected();
        if (selRecord == null) { return null; }
        return (EntityData) selRecord.getAsObject("entity");
    }


    private void addSearchToolbar() {
        setTopToolbar(new Toolbar());
        Toolbar toolbar = getTopToolbar();
        final Component searchField = createSearchField();
        if (searchField != null) {
            toolbar.addText("<i>Search</i>:&nbsp&nbsp");
            toolbar.addElement(searchField.getElement());
        }
    }

    protected Component createSearchField() {
        searchField = new TextField("Search: ", "search");
        searchField.setValidateOnBlur(false);
        searchField.setSelectOnFocus(true);
        searchField.setWidth(400);
        //searchField.setAutoWidth(true);
        searchField.setEmptyText("Type search string");
        searchField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onSpecialKey(final Field field, final EventObject e) {
                if (e.getKey() == EventObject.ENTER) {
                    String searchText = searchField.getText();
                    if (searchText != null) {
                        searchText = searchText.trim();
                    }
                    if (searchText.length() > 0) {
                        reload(proxy.getProjectName(), searchText, proxy.getValueType());
                    }
                }
            }

            @Override
            public void onValid(Field field) {
                onSearchTextChange(field.getValueAsString());
            }
        });
        return searchField;
    }

    private void onSearchTextChange(String searchText) {
        if (searchText == null) {
            return;
        }
        searchText = searchText.trim();
        if (searchText.length() > 2) {
            reload(proxy.getProjectName(), searchText, proxy.getValueType());
        } else {
            emptyContent();
        }
    }

    public void setSearchFieldText(String text) {
        searchField.setValue(text);
    }

    public void setProjectName(String projectName) {
        proxy.setProjectName(projectName);
    }

    public void setAsyncCallback(AsyncCallback<Boolean> asyncCallback) {
        this.asyncCallback = asyncCallback;
    }

    public void setBusyComponent(Component busyComponent) {
        this.busyComponent = busyComponent;
    }

    public void emptyContent() {
        store.removeAll();
    }
    
    public SearchResultsProxyImpl getProxy() {
        return proxy;
    }

}

package edu.stanford.bmir.protege.web.client.ui.search;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.portlet.LegacyCompatUtil;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchUtil {

    private ProjectId projectId;

    private SelectionModel selectionModel;

    private ValueType searchedValueType;

    private SearchGridPanel searchGrid;

    private AsyncCallback<Boolean> asyncCallback;


    public SearchUtil(ProjectId projectId, SelectionModel selectionModel, AsyncCallback<Boolean> asyncCallback) {
        this.projectId = projectId;
        this.selectionModel = checkNotNull(selectionModel);
        this.asyncCallback = asyncCallback;
        init();
    }

    private void init() {
        searchGrid = new SearchGridPanel(asyncCallback) {
            @Override
            protected void onEntityDblClick() {
                doSelect(getSelection());
            }
        };
        searchGrid.setAutoScroll(true);
        searchGrid.setStripeRows(true);
        searchGrid.setProjectId(projectId);
    }

    public void search(String text) {
        showSearchResults(text);
    }

    public void setSearchedValueType(ValueType searchedValueType) {
        this.searchedValueType = searchedValueType;
    }

    public ValueType getSearchedValueType() {
        return searchedValueType;
    }

    private void showSearchResults(String searchText) {
        final Window window = new Window();
        window.setTitle("Search results");
        window.setWidth(500);
        window.setHeight(365);
        window.setLayout(new FitLayout());

        FormPanel panel = new FormPanel();

        Button showInTreeButton = new Button("Select in tree", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                doSelect(searchGrid.getSelection());
            }
        });

        Button closeButton = new Button("Close", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                window.close(); // TODO: cancel existing search
                window.destroy();
            }
        });

        panel.add(searchGrid, new AnchorLayoutData("100% 100%"));
        panel.addButton(showInTreeButton);
        panel.addButton(closeButton);

        window.add(panel);

        searchGrid.getProxy().setValueType(getSearchedValueType());
        searchGrid.setSearchFieldText(searchText);

        if (searchText == null || searchText.trim().length() == 0) {
            searchGrid.getStore().removeAll();
        }
        else {
            searchGrid.reload(projectId, searchText, searchedValueType);
        }

        window.show();
    }

    private void doSelect(final EntityData selection) {
        if (selection == null) {
            return;
        }
        Optional<OWLEntityData> sel = LegacyCompatUtil.toOWLEntityData(selection);
        if(sel.isPresent()) {
            selectionModel.setSelection(sel.get().getEntity());
        }

    }

    public SearchGridPanel getSearchGridPanel() {
        return searchGrid;
    }

    public void setBusyComponent(Component busyComponent) {
        searchGrid.setBusyComponent(busyComponent);
    }

}

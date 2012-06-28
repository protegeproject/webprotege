package edu.stanford.bmir.protege.web.client.ui.ontology.reviews;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.client.rpc.data.ReviewData;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class ReviewsGrid extends GridPanel {

    private EntityData currentEntity;
    private Project project;
    private RecordDef recordDef;
    private Store store;
    private Toolbar toolbar;
    private ToolbarButton addReviewButton;
    private ToolbarButton requestReviewButton;

    public ReviewsGrid(Project project) {
        this.project = project;
        initialize();
    }

    private void initialize() {
        createColumns();
        createGrid();
        createToolbar();
    }

    private void createToolbar() {
        toolbar = new Toolbar();

        addReviewButton = new ToolbarButton("Add Review", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                addReviewButton_onClick(button, e);
            }
        });
        addReviewButton.setId("ReviewsGrid_AddReviewButton");
        addReviewButton.disable();

        requestReviewButton = new ToolbarButton("Request Review", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                requestReviewButton_onClick(button, e);
            }
        });
        requestReviewButton.setId("ReviewsGrid_RequestReviewButton");
        requestReviewButton.disable();

        toolbar.addButton(requestReviewButton);
        toolbar.addButton(addReviewButton);
        setTopToolbar(toolbar);
    }

    private void createGrid() {
        recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("subject"), new StringFieldDef("author"),
                new DateFieldDef("date") });

        MemoryProxy proxy = new MemoryProxy(new Object[][] {});
        ArrayReader reader = new ArrayReader(recordDef);
        store = new Store(proxy, reader);
        store.load();
        setStore(store);

        setAutoExpandColumn("ReviewsGrid_subjectCol");
        setAutoWidth(true);
        setId("reviews-grid");
        setStripeRows(true);
        setFrame(true);
    }

    private void createColumns() {
        ColumnConfig subjectCol = new ColumnConfig("Subject", "subject");
        subjectCol.setId("ReviewsGrid_subjectCol");
        subjectCol.setResizable(true);
        subjectCol.setSortable(true);

        ColumnConfig authorCol = new ColumnConfig("Author", "author");
        authorCol.setResizable(true);
        authorCol.setSortable(true);

        ColumnConfig dateCol = new ColumnConfig("Date", "date");
        dateCol.setResizable(true);
        dateCol.setSortable(true);

        ColumnConfig[] columns = new ColumnConfig[] { subjectCol, authorCol, dateCol };
        ColumnModel columnModel = new ColumnModel(columns);
        setColumnModel(columnModel);
    }

    public void reload() {
        store.removeAll();
        ChAOServiceManager.getInstance().getReviews(project.getProjectName(), currentEntity.getName(),
                new GetReviewsHandler());
    }

    public void refresh() {
        if (currentEntity == null) {
            return;
        }
        reload();
    }

    public void setEntity(EntityData newEntity) {
        // Shortcut
        if (currentEntity != null && currentEntity.equals(newEntity)) {
            return;
        }

        currentEntity = newEntity;

        if (addReviewButton.isDisabled()) {
            addReviewButton.enable();
        }

        if (requestReviewButton.isDisabled()) {
            requestReviewButton.enable();
        }

        refresh();
    }

    private void requestReviewButton_onClick(Button button, EventObject e) {
        final RequestReviewWindow window = new RequestReviewWindow(project.getProjectName(), currentEntity);
        window.show(button.getId());
    }

    private void addReviewButton_onClick(Button button, EventObject e) {
        // Shortcut
        if (GlobalSettings.getGlobalSettings().getUserName() == null) {
            MessageBox.alert("You must be logged in to add a review");
            return;
        }

        final AddReviewWindow window = new AddReviewWindow(new AddReviewWindowHandler());
        window.show(button.getId());
    }

    /*
     * Remote calls
     */

    class AddReviewWindowHandler extends AbstractAsyncHandler<NotesData> {

        @Override
        public void handleFailure(Throwable caught) {
        }

        @Override
        public void handleSuccess(NotesData result) {
            getEl().mask("Adding Review...");

            // Add new review item to the changes & annotations ontology.
            result.setAnnotatedEntity(currentEntity);
            result.setType("Review");

            ChAOServiceManager.getInstance().addReview(project.getProjectName(),
                    GlobalSettings.getGlobalSettings().getUserName(), result, new AddReviewHandler(result));
        }
    }

    class AddReviewHandler extends AbstractAsyncHandler<Void> {

        private NotesData data;

        public AddReviewHandler(NotesData data) {
            this.data = data;
        }

        @Override
        public void handleFailure(Throwable caught) {
            getEl().unmask();
            MessageBox.alert("Failed to add review");
            GWT.log("RPC error adding review for " + currentEntity.getBrowserText(), caught);
        }

        @Override
        public void handleSuccess(Void result) {
            getEl().unmask();

            // Add new review item to the grid.
            Record record = recordDef
                    .createRecord(new Object[] { data.getSubject(), data.getAuthor(), data.getCreationDate() });
            store.addSorted(record);
            getView().refresh();
        }
    }

    class GetReviewsHandler extends AbstractAsyncHandler<Collection<ReviewData>> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("RPC error getting reviews for " + currentEntity.getBrowserText(), caught);
        }

        @Override
        public void handleSuccess(Collection<ReviewData> result) {
            store.removeAll();
            for (ReviewData data : result) {
                Record record = recordDef.createRecord(new Object[] { data.getSubject(), data.getAuthor(),
                        data.getCreated() });
                store.add(record);
                store.sort("date", SortDir.DESC);
                getView().refresh();
            }
        }
    }
}

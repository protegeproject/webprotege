package edu.stanford.bmir.protege.web.client.ui.ontology.reviews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class RequestReviewWindow extends Window {

    private GridPanel reviewersGrid;
    private RecordDef recordDef;
    private Store store;
    private EntityData entity;
    private String projectName;

    public RequestReviewWindow(String projectName, EntityData entity) {
        this.projectName = projectName;
        this.entity = entity;
        initialize();
    }

    private void initialize() {
        setTitle("Select Reviewer");
        setWidth(250);
        setHeight(350);
        setLayout(new FitLayout());
        setPaddings(5);
        setButtonAlign(Position.CENTER);
        setCloseAction(Window.HIDE);
        setPlain(true);

        addButtons();
        createGrid();

        FormPanel formPanel = new FormPanel();
        formPanel.setBaseCls("x-plain");
        formPanel.add(reviewersGrid, new AnchorLayoutData("100% 100%"));

        add(formPanel);

        ChAOServiceManager.getInstance().getReviewers(projectName, new GetReviewersHandler());
    }

    private void createGrid() {
        recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("name"), });

        reviewersGrid = new GridPanel();

        MemoryProxy proxy = new MemoryProxy(new Object[][] {});
        ArrayReader reader = new ArrayReader(recordDef);
        store = new Store(proxy, reader);
        store.load();
        reviewersGrid.setStore(store);

        ColumnConfig nameCol = new ColumnConfig("Reviewer Name", "name");
        nameCol.setId("RequestReviewersWindow_NameCol");
        nameCol.setResizable(true);
        nameCol.setSortable(true);

        ColumnConfig[] columns = new ColumnConfig[] { nameCol };
        ColumnModel columnModel = new ColumnModel(columns);
        reviewersGrid.setColumnModel(columnModel);

        reviewersGrid.setAutoWidth(true);
        reviewersGrid.setHideColumnHeader(true);
        reviewersGrid.setAutoExpandColumn("RequestReviewersWindow_NameCol");
    }

    private void addButtons() {
        Button okButton = new Button("OK", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                okButton_onClick();
            }
        });
        addButton(okButton);

        Button cancelButton = new Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                close();
            }
        });
        addButton(cancelButton);
    }

    private void okButton_onClick() {
        ArrayList<String> reviewerNames = new ArrayList<String>();

        Record[] selections = reviewersGrid.getSelectionModel().getSelections();
        ArrayList<Record> reviewers = new ArrayList<Record>(Arrays.asList(selections));
        for (Record reviewer : reviewers) {
            reviewerNames.add(reviewer.getAsString("name"));
        }

        reviewersGrid.getEl().mask("Requesting review...");
        ChAOServiceManager.getInstance().requestReview(projectName, entity.getName(), reviewerNames,
                new RequestReviewHandler());
    }

    protected void showReviewRequestMessage() { //FIXME - do only if ok clicked
        MessageBox.show(new MessageBoxConfig() {
            {
                setTitle("Request review");
                setMsg("Please edit your review request");
                setWidth(500);
                //setHeight(600);
                setDefaultTextHeight(800);
                setButtons(MessageBox.OKCANCEL);
                setMultiline(true);
                setValue(getReviewText());
                setCallback(new MessageBox.PromptCallback() {
                    public void execute(String btnID, String text) {
                        if (btnID.equals("ok")) {
                            MessageBox.alert("Request successfully submitted.");
                        }
                    }
                });
            }
        });
    }

    protected String getReviewText() {
        return "Dear Ms/Dr/Professor ________________,\n\n"
                + "I am contacting you as the Managing Editor for the ICD-11 Topic Advisory Group on  _______________   to request you to review the following proposal made to be included in the ICD-11 for:\n\n"
                + UIUtil.getDisplayText(entity)
                + "\n\n"
                + "The review guide is also included in the appendix and in the iCAT software platform.\n\n"
                + "The basic questions that we would like you to address in this review are as follows: ... \n"
                + "Thank you for agreeing to review this proposal which will greatly contribute to the creation of an international public good."
                + "\n\nYours truly,\n" + GlobalSettings.getGlobalSettings().getUserName() + "\nManaging Editor\n"
                + "For  TAG on   _______________________";
    }

    /*
     * Remote calls
     */
    class GetReviewersHandler extends AbstractAsyncHandler<List<String>> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("RPC error getting reviewers for " + projectName, caught);
        }

        @Override
        public void handleSuccess(List<String> result) {
            store.removeAll();
            for (String str : result) {
                Record record = recordDef.createRecord(new Object[] { str });
                store.add(record);
            }
        }
    }

    class RequestReviewHandler extends AbstractAsyncHandler<Void> {

        @Override
        public void handleFailure(Throwable caught) {
            reviewersGrid.getEl().unmask();
            MessageBox.alert("Failed to submit request");
            GWT.log("RPC error requesting review for " + entity.getName(), caught);
            close();
        }

        @Override
        public void handleSuccess(Void result) {
            reviewersGrid.getEl().unmask();
            showReviewRequestMessage();
            close();
        }
    }
}

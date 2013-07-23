package edu.stanford.bmir.protege.web.client.ui.ontology.restrictions;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Container;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ConditionItem;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEventHandler;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;

import java.util.Collection;
import java.util.List;

public class RestrictionsPortlet extends AbstractOWLEntityPortlet{

    private final static int CONDITION_COL_INDEX = 0;
    private final static int EDIT_COL_INDEX = 1;
    private final static int DELETE_COL_INDEX = 2;

    private final static int NO_CONDITION_MODE = 0;
    private final static int EDIT_CONDITION_MODE = 100;
    private final static int ADD_N_CONDITION_MODE = 200;
    private final static int ADD_NS_CONDITION_MODE = 300;

    private Grid grid;
    private List<ConditionItem> conditionItems;
    private ClickHandler clickHandler;

    private ConditionEditor conditionEditor;
    private Window conditionEditorWindow;
    /*
     *Trying to reuse the same condition editor and window for all
     * operations.. that's why the editMode. Not ideal, but probably faster.
     * This works only because we can have at a time exactly one conditionEditor.
     */
    private int editMode;
    private int lastActionRow = -1;

    public RestrictionsPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setLayout(new AnchorLayout()); //we don't want something that stretches vertically
        setAutoScroll(true);
        setHeight(200);

        grid = new Grid(0,3);
        grid.getColumnFormatter().setWidth(EDIT_COL_INDEX, "24px");
        grid.getColumnFormatter().setWidth(DELETE_COL_INDEX, "24px");

        add(grid, new AnchorLayoutData("100%"));

        clickHandler = createClickHandler();
        conditionEditor = new ConditionEditor(getProject());
        conditionEditorWindow = createConditionEditorWindow();
        grid.setCellPadding(8);

        EventBusManager.getManager().registerHandler(ClassFrameChangedEvent.TYPE, new ClassFrameChangedEventHandler() {
            @Override
            public void classFrameChanged(ClassFrameChangedEvent event) {
                if (isSelected(event.getEntity())) {
                    reload();
                }

            }
        });
    }

    @Override
    public void reload() {
        clearGrid();
        EntityData entity = getEntity();
        if (entity != null) {
            setTitle("Asserted Conditions for " + entity.getBrowserText());
            OntologyServiceManager.getInstance().getClassConditions(getProjectId(), entity.getName(),
                    new GetClassConditionsAsyncHandler(entity));
        } else {
            setTitle("Asserted Conditions (nothing selected)");
        }
    }

    protected void clearGrid() {
        grid.resize(0,3);
        doLayout();
    }

    protected void fillGrid(List<ConditionItem> conditions) {
        grid.resize(conditions.size(), 3);
        for (int i = 0 ; i < conditions.size(); i++) {
            addRow(i, conditions.get(i));
        }
        //not sure it is needed
        doLayout();
    }

    protected void addRow(int row, ConditionItem condition) {
        grid.setHTML(row, 0, condition.getBrowserText());
        if (condition.isNSSeparator()) {
            Anchor newNSAnchor = new Anchor("<img src=\"images/add.png\"  title=\"Add new Necessary & Sufficent condition\"/>", true);
            newNSAnchor.addClickHandler(clickHandler);
            grid.setWidget(row, EDIT_COL_INDEX, newNSAnchor);
        } else if (condition.isNSeparator()) {
            Anchor newNAnchor = new Anchor("<img src=\"images/add.png\"  title=\"Add new Necessary condition\"/>", true);
            newNAnchor.addClickHandler(clickHandler);
            grid.setWidget(row, EDIT_COL_INDEX, newNAnchor);
        } else if (condition.isINHSeparator()) {//do nothing for now

        } else {
            if (condition.getInheritedFromName() != null) {
                return; //no editing for inherited conditions
            }
            String safeCondition = UIUtil.removeHTMLTags(condition.getBrowserText());
            Anchor editAnchor = new Anchor("<img src=\"images/pencil.png\"  title=\"Edit condition: " + safeCondition + "\"/>", true);
            Anchor deleteAnchor = new Anchor("<img src=\"images/delete.png\"  title=\"Delete condition: " + safeCondition +"\"/>", true);

            editAnchor.addClickHandler(clickHandler);
            deleteAnchor.addClickHandler(clickHandler);

            grid.setWidget(row, EDIT_COL_INDEX, editAnchor);
            grid.setWidget(row, DELETE_COL_INDEX, deleteAnchor);
        }
    }

    /*
     * Edit and delete conditions
     */

    protected ClickHandler createClickHandler() {
        return new ClickHandler() {
            public void onClick(ClickEvent event) {
                Cell cellForEvent = grid.getCellForEvent(event);
                int col = cellForEvent.getCellIndex();
                int row = cellForEvent.getRowIndex();
                lastActionRow = row;

                if (!UIUtil.confirmOperationAllowed(getProjectId())) {
                    return;
                }

                ConditionItem conditionItem = conditionItems.get(row);
                if (conditionItem.isNSSeparator()) {
                    onAddNewNNSCondition(true);
                } else if (conditionItem.isNSeparator() ){
                    onAddNewNNSCondition(false);
                } else { //normal conditions
                    if (col == EDIT_COL_INDEX) {
                        onEdit(row);
                    } else if (col == DELETE_COL_INDEX) {
                        onDelete(row);
                    }
                }
            }
        };
    }

    protected void updateButtonStates() {
        boolean hasWritePermission = hasWritePermission();
        for (int i = 0; i < conditionItems.size(); i++) {
            Widget widget1 = grid.getWidget(i, EDIT_COL_INDEX);
            if (widget1 != null) {
                ((Anchor)widget1).setEnabled(hasWritePermission);
            }
            Widget widget2 = grid.getWidget(i, DELETE_COL_INDEX);
            if (widget2 != null) {
                ((Anchor)widget2).setEnabled(hasWritePermission);
            }
        }
    }


    private Window createConditionEditorWindow() {
        final Window window = new Window();
        window.setWidth(600);
        window.setHeight(400);
        window.setLayout(new FitLayout());
        window.setButtonAlign(Position.CENTER);
        window.setModal(true);
        window.setCloseAction(Window.HIDE);

        FormPanel panel = new FormPanel();

        Button okButton = new Button("OK", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                if (!conditionEditor.isValid()) {
                    MessageBox.showAlert("Expression not valid", "The class expression is not valid.  " +
                    		"Please edit the class expression or cancel the edit.");
                } else {
                    onConditionChanged();
                    window.hide();
                }
            }
        });

        Button cancelButton = new Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                window.hide();
            }
        });

        window.addListener(new PanelListenerAdapter() {
            @Override
            public void onAfterLayout(Container self) {
                //TT: I really did not want to use a timer, but could not find another way of making the focus work
                Timer timer = new Timer(){
                    @Override
                    public void run() {
                        conditionEditor.requestFocus();
                    }
                };
                timer.schedule(100);
            }
        });

        panel.add(conditionEditor, new AnchorLayoutData("100% 100%"));
        panel.addButton(okButton);
        panel.addButton(cancelButton);
        window.add(panel);
        return window;
    }

    protected void onDelete(int row) {
        OntologyServiceManager.getInstance().deleteCondition(getProjectId(),
                getEntity().getName(), conditionItems.get(row), row, "", new DeleteClassConditionAsyncHandler(getEntity()));
    }

    protected void onEdit(int row) {
        conditionEditor.setConditionText(UIUtil.removeHTMLTags(conditionItems.get(row).getBrowserText()));

        editMode = EDIT_CONDITION_MODE;

        conditionEditorWindow.setTitle("Edit condition for " + getEntity().getBrowserText());
        conditionEditorWindow.show();
        conditionEditorWindow.center();
        conditionEditorWindow.focus();
     }

    protected void onAddNewNNSCondition(boolean isNS) {
        conditionEditor.setConditionText("");

        editMode = isNS ? ADD_NS_CONDITION_MODE : ADD_N_CONDITION_MODE;

        conditionEditorWindow.setTitle("Add new " + (isNS ? "Necessary & Sufficient" : "Necessary") +" condition for " + getEntity().getBrowserText());
        conditionEditorWindow.show();
        conditionEditorWindow.center();
        conditionEditorWindow.focus();
    }

    protected void onConditionChanged() {
        if (lastActionRow < 0)  { //something went wrong
            return;
        }
        if (editMode == EDIT_CONDITION_MODE) {
            editCondition(lastActionRow);
        } else if (editMode == ADD_N_CONDITION_MODE) {
            addCondition(lastActionRow, false);
        } else if (editMode == ADD_NS_CONDITION_MODE) {
            addCondition(lastActionRow, true);
        }
    }

    protected void editCondition(int row) {
        if (UIUtil.removeHTMLTags(conditionItems.get(row).getBrowserText()).equals(conditionEditor.getCondition())) {
            return;
        }
        OntologyServiceManager.getInstance().replaceCondition(getProjectId(), getEntity().getName(),
                conditionItems.get(row), row, conditionEditor.getCondition(), null,
                new ReplaceConditionAsyncHandler(conditionItems.get(row)));
    }

    protected void addCondition(int row, boolean isNS) {
        OntologyServiceManager.getInstance().addCondition(getProjectId(), getEntity().getName(), row,
                conditionEditor.getCondition(), isNS, null,
                new AddConditionAsyncHandler());
    }

    public Collection<EntityData> getSelection() {
        return null;
    }

    /*
     * Remote calls
     */

    protected void fillRemoteConditions(List<ConditionItem> conditions) {
        if (conditions == null || conditions.size() == 0) {
            return;
        }
        conditionItems = conditions;
        fillGrid(conditions);
    }

    class GetClassConditionsAsyncHandler extends AbstractAsyncHandler<List<ConditionItem>> {

        private EntityData entity;

        public GetClassConditionsAsyncHandler(EntityData entity) {
            this.entity = entity;
        }

        @Override
        public void handleFailure(Throwable caught) {
            setTitle("*** Error at retrieving asserted Conditions for " + entity.getBrowserText());
        }

        @Override
        public void handleSuccess(List<ConditionItem> conditions) {
            if (!entity.equals(getEntity())) { //these are the conditions for a different class
                return;
            }
            fillRemoteConditions(conditions);
        }
    }

    class DeleteClassConditionAsyncHandler extends AbstractAsyncHandler<List<ConditionItem>> {

        private EntityData entity;

        public DeleteClassConditionAsyncHandler(EntityData entity) {
            this.entity = entity;
        }

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.showErrorMessage("Delete failed", caught);
        }

        @Override
        public void handleSuccess(List<ConditionItem> conditions) {
            clearGrid();
            fillRemoteConditions(conditions);
        }
    }

    class ReplaceConditionAsyncHandler extends AbstractAsyncHandler<List<ConditionItem>> {

        private ConditionItem originalConditionItem;

        public ReplaceConditionAsyncHandler(ConditionItem originalConditionItem) {
            this.originalConditionItem = originalConditionItem;
        }

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.showErrorMessage("Edit failed", caught);
        }

        @Override
        public void handleSuccess(List<ConditionItem> conditions) {
            clearGrid();
            fillRemoteConditions(conditions);
        }
    }

    class AddConditionAsyncHandler extends AbstractAsyncHandler<List<ConditionItem>> {

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.showErrorMessage("Edit failed", caught);
        }

        @Override
        public void handleSuccess(List<ConditionItem> conditions) {
            clearGrid();
            fillRemoteConditions(conditions);
        }
    }


}

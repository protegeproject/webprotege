package edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.ICDServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.client.ui.icd.ICDClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.search.SearchGridPanel;
import edu.stanford.bmir.protege.web.client.ui.search.SearchUtil;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.client.ui.util.ClassSelectionField;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.client.ui.util.field.TextAreaField;

public class CreateClassPanel extends FormPanel implements Selectable {

    private final Project project;
    private ClassSelectionField parentsField;
    private TextField titleField;
    private TextField sortingLabelField;
    private TextAreaField reasonField;
    private AsyncCallback<EntityData> asyncCallback;
    private String topClass;
    private boolean createICDSpecificEntities;
    private SearchGridPanel searchGridPanel;
    private Selectable searchDblClickSelectable;

    public CreateClassPanel(Project project, boolean createICDSpecificEntities) {
        this(project, createICDSpecificEntities, null, null, null);
    }

    public CreateClassPanel(final Project project, final boolean createICDSpecificEntities, final AsyncCallback<EntityData> asyncCallback, final String topClass,
            final Selectable searchDblClickSelectable) {
        this.project = project;
        this.asyncCallback = asyncCallback;
        this.topClass = topClass;
        this.createICDSpecificEntities = createICDSpecificEntities;
        this.searchDblClickSelectable = searchDblClickSelectable;
        buildUI();
    }

    private void buildUI() {
        setHeight(550);
        setPaddings(5, 7, 0, 0);
        setAutoScroll(true);

        Label title = new Label("CREATE NEW CATEGORY");
        title.setStylePrimaryName("hierarchy-title");
        add(title, new AnchorLayoutData("98%"));

        HTML explanationHtml = new HTML("Please enter a <b>title</b> and a sorting label (optional) for the new category.<br />" +
                "Select one or more <b>parents</b> for the category by clicking on the &nbsp <img src=\"../images/add.png\"></img> &nbsp icon in the <i>Parents</i> field.<br />" +
                "Operations are performed only after clicking on the <i>Create</i> button.");
        explanationHtml.setStylePrimaryName("explanation");
        add(explanationHtml);

        titleField = new TextField("Title", "name");
        titleField.setAllowBlank(false);
        titleField.setEmptyText("Enter title of new category");
        titleField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onValid(Field field) {
                onTitleChange(titleField.getValueAsString());
            }
        });
        add(titleField, new AnchorLayoutData("98%"));

        sortingLabelField = new TextField("Sorting label", "code");
        sortingLabelField.setAllowBlank(false);
        add(sortingLabelField, new AnchorLayoutData("98%"));

        //FIXME: ICD specific!!!!
        
        parentsField = new ClassSelectionField(project, "Parent(s)", true, topClass) {
            @Override
            protected edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet createSelectable() {
                ClassTreePortlet treePortlet = new ICDClassTreePortlet(project, true, false, false, false, topClass);
                treePortlet.setDraggable(false);
                treePortlet.setClosable(false);
                treePortlet.setCollapsible(false);
                treePortlet.setHeight(300);
                treePortlet.setWidth(450);

                return treePortlet;
            };
        };
        add(parentsField, new AnchorLayoutData("98%"));

        reasonField = new TextAreaField();
        reasonField.setLabel("Reason for change:");
        ((TextArea)reasonField.getFieldComponent()).setHeight(60);
        add(reasonField, new AnchorLayoutData("98%"));

        HTML explanation2Html = new HTML("<b>Before creating a new category, please search for the term to ensure that you are not creating duplicates.</b><br />" +
        		"After you typed 4 characters in the Title field, automatic searches will show you possible matches.");
        explanation2Html.setStylePrimaryName("explanation");
        add(explanation2Html);

        add(new HTML("<b>Possible duplicate categories:</b>"));

        searchGridPanel = new SearchUtil(project, searchDblClickSelectable).getSearchGridPanel();
        searchGridPanel.setHeight(160);
        add(searchGridPanel, new AnchorLayoutData("98%"));

        Button createButton = new Button("Create");
        createButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                if (UIUtil.confirmOperationAllowed(project)) {
                    onCreate();
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                clear();
                destroy();
                if (asyncCallback != null) {
                    asyncCallback.onSuccess((null));
                }
            }
        });

        addButton(createButton);
        addButton(cancelButton);
    }

    private void onTitleChange(String searchText) {
        if (searchText == null) {
            return;
        }
        searchText = searchText.trim();
        if (searchText.length() > 3) {
            searchGridPanel.setSearchFieldText(searchText);
        } else {
            searchGridPanel.setSearchFieldText("");
            searchGridPanel.emptyContent();
        }
    }

    public void setParentsClses(Collection<EntityData> parents) {
        parentsField.setClsValues(parents);
    }

    public Collection<EntityData> getParentsClses() {
       return parentsField.getClsValues();
    }

    private void onCreate() {
        final String clsName = titleField.getValueAsString();
        final String reasonForChange = reasonField.getValueAsString();

        if (clsName == null || clsName.length() == 0) {
            MessageBox.alert("Empty class name", "Please enter a class name.");
            return;
        }

        if (reasonForChange == null || reasonForChange.length() == 0) {
            MessageBox.alert("No reason for change", "A reason for the change was not provided.<br />" +
                    "Please fill in the <i>Reason for change</i> field.");
            return;
        }

        UIUtil.mask(getEl(), "Class " + clsName + " is being created...", true, 10);
        performCreate();
    }

    protected void performCreate() {
        ICDServiceManager.getInstance().createICDCls(project.getProjectName(), null,
                UIUtil.getStringCollection(parentsField.getClsValues()), titleField.getValueAsString(), sortingLabelField.getValueAsString(),
                createICDSpecificEntities, GlobalSettings.getGlobalSettings().getUserName(), getOperationDescription(),
                "reason for change", new CreateClassHandler()); //TODO: remove the unneeded args
    }

    protected void createNote(final EntityData newClass, String opDesc, String reasonForChange) {
        NotesData noteData = new NotesData();
        noteData.setAuthor(GlobalSettings.getGlobalSettings().getUserName());
        noteData.setSubject("[Reason for change]: " + opDesc);
        noteData.setBody(reasonForChange);
        noteData.setAnnotatedEntity(newClass);
        ChAOServiceManager.getInstance().createNote(project.getProjectName(), noteData, false, new AbstractAsyncHandler<NotesData>() {
            @Override
            public void handleFailure(Throwable caught) {
                GWT.log("Could not create note for " + newClass);
            }
            @Override
            public void handleSuccess(NotesData result) {
                //TODO: maybe update notes count?
            }
        });
    }

    public String getOperationDescription() {
       return "Create class with name: " + titleField.getValueAsString() +
        ", parents: " + UIUtil.prettyPrintList(parentsField.getClsValues());
    }

    public String getReasonForChange() {
       return reasonField.getValueAsString();
    }

    public void setAsyncCallback(AsyncCallback<EntityData> asyncCallback) {
        this.asyncCallback = asyncCallback;
    }

    public String getTopClass() {
        return topClass;
    }

    public void setTopClass(String topClass) {
        this.topClass = topClass;
        parentsField.setTopClass(topClass);
    }

    protected void refreshFromServer() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                project.forceGetEvents();
            }
        };
        timer.schedule(500);
    }

    public void addSelectionListener(SelectionListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Collection<EntityData> getSelection() {
        return parentsField.getClsValues();
    }

    public void notifySelectionListeners(SelectionEvent selectionEvent) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void removeSelectionListener(SelectionListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setSelection(Collection<EntityData> selection) {
        parentsField.setClsValues(selection);
    }

    public void setSearchDblClickSelectable(Selectable selectable) {
        this.searchDblClickSelectable = selectable;
    }

    /*
     * Remote calls
     */

    class CreateClassHandler extends AbstractAsyncHandler<EntityData> {

        @Override
        public void handleFailure(Throwable caught) {
            getEl().unmask();
            MessageBox.alert("Errot at creating new class", "There was an error at creating the new class.<br />" +
            		"Message: " + caught.getMessage());
            GWT.log("There was an error at creating the new class." + caught.getMessage(), caught);
            if (asyncCallback != null) {
                asyncCallback.onFailure(caught);
            }
        }

        @Override
        public void handleSuccess(EntityData entityData) {
            getEl().unmask();
            if (entityData != null) {
                createNote(entityData, getOperationDescription(), getReasonForChange()); //create note
                titleField.reset();
            } else {
                GWT.log("Problem at creating class", null);
                MessageBox.alert("Class creation failed. Please try again later.");
            }
            if (asyncCallback != null) {
                asyncCallback.onSuccess(entityData);
            }
            refreshFromServer();
        }

    }
}

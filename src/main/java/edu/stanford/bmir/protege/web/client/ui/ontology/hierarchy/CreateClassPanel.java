package edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy;

import com.google.gwt.core.client.GWT;
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
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;

import edu.stanford.bmir.protege.web.client.rpc.EmptySuccessWebProtegeCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.search.SearchGridPanel;
import edu.stanford.bmir.protege.web.client.ui.search.SearchUtil;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.client.ui.util.ClassSelectionField;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.client.ui.util.field.TextAreaField;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityAction;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityResult;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Collection;

public class CreateClassPanel extends FormPanel implements Selectable {

    private final ProjectId projectId;
    private ClassSelectionField parentsField;
    private TextField titleField;
    private TextField sortingLabelField;
    private TextAreaField reasonField;
    private AsyncCallback<EntityData> asyncCallback;
    private String topClass;
    private SearchGridPanel searchGridPanel;
    private Selectable searchDblClickSelectable;

    public CreateClassPanel(ProjectId projectId, boolean createICDSpecificEntities) {
        this(projectId, createICDSpecificEntities, null, null, null);
    }

    public CreateClassPanel(final ProjectId projectId, final boolean createICDSpecificEntities, final AsyncCallback<EntityData> asyncCallback, final String topClass,
            final Selectable searchDblClickSelectable) {
        this.projectId = projectId;
        this.asyncCallback = asyncCallback;
        this.topClass = topClass;
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
        
        parentsField = new ClassSelectionField(projectId, "Parent(s)", true, topClass);

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

        searchGridPanel = new SearchUtil(projectId, searchDblClickSelectable).getSearchGridPanel();
        searchGridPanel.setHeight(160);
        add(searchGridPanel, new AnchorLayoutData("98%"));

        Button createButton = new Button("Create");
        createButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                if (UIUtil.confirmOperationAllowed(projectId)) {
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
        throw new RuntimeException("BROKEN");
//        ICDServiceManager.getInstance().createICDCls(project.getDisplayName(), null,
//                UIUtil.getStringCollection(parentsField.getClsValues()), titleField.getValueAsString(), sortingLabelField.getValueAsString(),
//                createICDSpecificEntities, GlobalSettings.get().getUserName(), getOperationDescription(),
//                "reason for change", new CreateClassHandler()); //TODO: remove the unneeded args
    }

    protected void createNote(final EntityData newClass, String opDesc, String reasonForChange) {

        NoteContent.Builder builder = NoteContent.builder();
        builder.setSubject("Reason for change: " + opDesc);
        builder.setBody(reasonForChange);
        final OWLClass cls = DataFactory.getOWLClass(newClass.getName());
        DispatchServiceManager.get().execute(new AddNoteToEntityAction(projectId, cls, builder.build()), new EmptySuccessWebProtegeCallback<AddNoteToEntityResult>());
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
        throw   new RuntimeException("BROKEN");
//        Timer timer = new Timer() {
//            @Override
//            public void run() {
//                project.forceGetEvents();
//            }
//        };
//        timer.schedule(500);
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

    class CreateClassHandler implements AsyncCallback<EntityData> {

        @Override
        public void onFailure(Throwable caught) {
            getEl().unmask();
            MessageBox.alert("Errot at creating new class", "There was an error at creating the new class.<br />" +
            		"Message: " + caught.getMessage());
            GWT.log("There was an error at creating the new class." + caught.getMessage(), caught);
            if (asyncCallback != null) {
                asyncCallback.onFailure(caught);
            }
        }

        @Override
        public void onSuccess(EntityData entityData) {
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

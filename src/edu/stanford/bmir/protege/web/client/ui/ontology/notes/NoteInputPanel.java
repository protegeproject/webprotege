package edu.stanford.bmir.protege.web.client.ui.ontology.notes;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.HtmlEditor;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.VerticalLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class NoteInputPanel extends Panel {

    protected Project project;
    protected boolean showHistory;
    private Component mainComponent;
    private AsyncCallback<NotesData> cb;
    protected EntityData annotatedEntity;

    protected TextField subjectField;
    protected ComboBox typeComboBox;
    protected HtmlEditor htmlEditor;
    protected Button sendButton;
    protected Button cancelButton;

    protected NotesData note = null;

    public NoteInputPanel(final Project project, String message, boolean showHistory,
                          EntityData annotatedEntity, AsyncCallback<NotesData> cb) {
        this(project, message, showHistory, "", "", annotatedEntity, cb);
    }

    public NoteInputPanel(final Project project, String message, boolean showHistory,
                          EntityData annotatedEntity, final Window window) {
        this(project, message, showHistory, "", "", annotatedEntity, window);
    }

    public NoteInputPanel(final Project project, String message, boolean showHistory,
                          EntityData annotatedEntity, final Window window, AsyncCallback<NotesData> cb) {
        this(project, message, showHistory, "", "", annotatedEntity, window, cb);
    }

    public NoteInputPanel(final Project project, String message, boolean showHistory,
                          String subject, String text,
                          EntityData annotatedEntity, final Window window) {
        this(project, message, showHistory, subject, text,
                annotatedEntity, window, new AsyncCallback<NotesData>() {
                    public void onFailure(Throwable caught) {
                        if (caught != null) {
                            MessageBox.alert(caught.getMessage());
                        }
                    }

                    public void onSuccess(NotesData note) {
                        if (note != null &&
                                ((note.getSubject() != null && note.getSubject().length() > 0) ||
                                        (note.getBody() != null && note.getBody().length() > 0))) {
                            ChAOServiceManager.getInstance().createNote(project.getProjectName(), note, false, new AbstractAsyncHandler<NotesData>() {
                                @Override
                                public void handleFailure(Throwable caught) {
                                    MessageBox.alert("Creating note failed: " + caught.getMessage());
                                }

                                @Override
                                public void handleSuccess(NotesData result) {
                                    if (result != null) {
                                        GWT.log("Note created successfully: " + result.toString(), null);
                                    }
                                    else {
                                        GWT.log("WARNING: Creating note returned null!");
                                    }
                                }
                            });
                        }
                    }
                });
    }

    public NoteInputPanel(final Project project, String message, boolean showHistory,
                          String subject, String text,
                          EntityData annotatedEntity, final Window window, final AsyncCallback<NotesData> cb) {
        this(project, message, showHistory, subject, text,
                annotatedEntity, new AsyncCallback<NotesData>() {
                    public void onFailure(Throwable caught) {
                        cb.onFailure(caught);
                        window.close();
                    }

                    public void onSuccess(NotesData result) {
                        cb.onSuccess(result);
                        window.close();
                    }
                });
    }

    public NoteInputPanel(Project project, String message, boolean showHistory,
                          String subject, String text,
                          EntityData annotatedEntity, AsyncCallback<NotesData> cb) {
        this.project = project;
        this.annotatedEntity = annotatedEntity;
        this.showHistory = showHistory;
        this.cb = cb;
        intialize(message, subject, text);
    }

    public NoteInputPanel(Project project, String message, boolean showHistory, NotesData note, EntityData annotatedEntity, AsyncCallback<NotesData> cb) {
        this.project = project;
        this.annotatedEntity = annotatedEntity;
        this.showHistory = showHistory;
        this.cb = cb;
        this.note = note;
        intialize(message, note.getSubject(), note.getBody());
    }

     native private void synchEditor(JavaScriptObject editor)/*-{
         editor.syncValue();
     }-*/;

    public void intialize(String message, String subject, String text) {
        // create the FormPanel and set the label position to top
        FormPanel formPanel = new FormPanel();
        formPanel.setFrame(true);
        formPanel.setPaddings(5, 5, 5, 5);
        formPanel.setWidth(550);
        formPanel.setHeight(showHistory ? 250 : 350);

        subjectField = new TextField("Subject", "subject");
        subjectField.setValue(subject);
        subjectField.setWidth(300);
        formPanel.add(subjectField, new AnchorLayoutData("100%"));

        typeComboBox = getTypeComboBox();
        formPanel.add(typeComboBox, new AnchorLayoutData("100%"));

        htmlEditor = new HtmlEditor("message");
        htmlEditor.setHideLabel(true);
        htmlEditor.setHeight(100);
        if (text != null && text.length() > 0) {
            htmlEditor.setValue(text);
        }
        //htmlEditor.focus();
        htmlEditor.enable();
        htmlEditor.setReadOnly(false);
        formPanel.add(htmlEditor, new AnchorLayoutData("100% -53"));
        mainComponent = htmlEditor;

        sendButton = new Button("Send", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                doSendNote();
            }
        });

        cancelButton = new Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                doCancelButton();
            }
        });

        formPanel.addButton(sendButton);
        formPanel.addButton(cancelButton);


        NotesGrid ng = new NotesGrid(project);
        ng.setTopLevel(false);
        ng.setBorder(true);
        ng.setEntity(annotatedEntity);
        ng.setVisible(showHistory);

        this.setLayout(new VerticalLayout(7));
        if (message != null && message.length() > 0) {
            Label messageLabel = new Label(message);
            messageLabel.setCls("note_message");
            this.add(messageLabel, new AnchorLayoutData("100% 5%"));//do not add this to formPanel 'cause it won't work in FF
        }
        this.setCls(formPanel.getCls());
        this.setLayout(new AnchorLayout());
        this.setWidth(550);
        this.setHeight(showHistory ? 600 : 400);
        this.add(formPanel, new AnchorLayoutData(showHistory ? "100% 60%" : "100% -20"));
        this.add(ng, new AnchorLayoutData("100% 35%"));
    }

    public void setAnnotatedEntity(EntityData annotatedEntity) {
        this.annotatedEntity = annotatedEntity;
    }

    private String getTimeString() {
        return "time";
    }

    private ComboBox getTypeComboBox() {
        final ComboBox cb = new ComboBox();
        final RecordDef recDef = new RecordDef(new FieldDef[]{new StringFieldDef("commentType")});

        //FIXME make the good solution (something like the one commented out below)
        //      work properly and get rid of the hack in UIUtil and Ontology classes.
        List<String[]> values = new ArrayList<String[]>();
        String[] noteTypes = UIUtil.getNoteTypes();
        for (int i = 0; i < noteTypes.length; i++) {
            values.add(new String[]{noteTypes[i]});
        }
        final Store cbstore = new SimpleStore(new String[]{"commentType"}, values.toArray(new String[][]{{}}));
        cb.setStore(cbstore);
        cb.setValueNotFoundText(noteTypes[0]);

        String defaultString = this.note == null ? "Comment" : this.note.getType();
        cb.setValue(defaultString);
//        ChAOServiceManager.getInstance().getAvailableNoteTypes(
//                project.getEscapedProjectName(), new AsyncCallback<Collection<EntityData>>(){
//                    public void onFailure(Throwable caught) {
//                        GWT.log("Error getting the list of available note types", 
//                                new Exception("calling getAvailableNoteTypes failed"));
//                    }
//
//                    public void onSuccess(Collection<EntityData> result) {
//                        if (result != null) {
//                            String[] noteTypes = new String[result.size()];
//                            int i = 0;
//                            for (Iterator<EntityData> it = result.iterator(); it.hasNext();) {
//                                EntityData noteType = it.next();
//                                noteTypes[i++] = noteType.getBrowserText();
//                            }
////                            Store cbstore = new SimpleStore(new String[] { "commentType" }, new String[][]{{}});
////                            cbstore.removeAll();
//                            for (int j = 0; j < noteTypes.length; j++) {
//                                Record record = recDef.createRecord(new Object[] {noteTypes[j]});
//                                cbstore.add(record);
//                            }
//                            cb.setStore(cbstore);
//                            cbstore.reload();
//                            cb.getEl().repaint();
//                        }
//                    }
//                });

        cbstore.load();
        cb.setForceSelection(true);
        cb.setMinChars(1);
        cb.setFieldLabel("Type");
        cb.setDisplayField("commentType");
        cb.setMode(ComboBox.LOCAL);
        cb.setTriggerAction(ComboBox.ALL);
        cb.setEmptyText("Select Type");
        cb.setTypeAhead(true);
        cb.setSelectOnFocus(true);
        cb.setWidth(200);
        cb.setHideTrigger(false);
        return cb;
    }

    public Component getMainComponentForFocus() {
        return mainComponent;
    }

    public AsyncCallback<NotesData> getCallBack() {
        return cb;
    }

    public void setCallback(AsyncCallback<NotesData> cb) {
        this.cb = cb;
    }

    public NotesData getNotesData(String userName) {
        try {
            if (htmlEditor != null){
                synchEditor(htmlEditor.getJsObj());
            }
        } catch (Exception e) {
            GWT.log(e.getMessage());
        }
        String text = htmlEditor.getValueAsString();
        String subject = subjectField.getValueAsString();
        NotesData note = new NotesData(userName,
                text, getTimeString(), typeComboBox.getValueAsString(), subject,
                null, annotatedEntity, null);
        return note;
    }

    public void doCancelButton() {
        cb.onFailure(null);
    }

    public void doSendNote() {
        String userName = GlobalSettings.getGlobalSettings().getUserName();
        if (userName == null) {
            cb.onFailure(new Exception("To post a message you need to be logged in."));
            return;
        }
        NotesData note = getNotesData(userName);
        cb.onSuccess(note);
    }

    public void showButtons(boolean visible) {
        if (sendButton != null) {
            sendButton.setVisible(visible);
        }
        if (cancelButton != null) {
            cancelButton.setVisible(visible);
        }
    }

    public void setSendButtonLabel(String newLabel) {
        if (sendButton != null) {
            sendButton.setText(newLabel);
        }
    }

    public void setCancelButtonLabel(String newLabel) {
        if (cancelButton != null) {
            cancelButton.setText(newLabel);
        }
    }

    public void setSubject(String subject) {
        subjectField.setValue(subject);
    }

    public void setNoteType(String noteType) {
        typeComboBox.setValue(noteType);
    }
}

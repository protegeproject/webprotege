package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal;

import java.util.Date;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.HtmlEditor;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.AsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioportalProposalsManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;


public class NewNotePanel extends FormPanel {

    private Record record;
    private EntityData entityData;
    private String ontologyVersionId; // in case record is null

    private TextField subjectField;
    private HtmlEditor bodyField;

    private Window window;
    private AsyncHandler<Void> noteSentCallback;

    public NewNotePanel() {
        setLabelAlign(Position.TOP);
        setLabelWidth(100);
        setPaddings(10);

        subjectField = new TextField("Subject");

        bodyField = new HtmlEditor("Body");
        bodyField.setHeight(200);
        bodyField.setCls("html-editor");

        add(subjectField, new AnchorLayoutData("100%"));
        add(bodyField, new AnchorLayoutData("100%"));
    }

    public String getSubject() {
        return subjectField.getValueAsString();
    }

    public String getBodyText() {
        return bodyField.getValueAsString();
    }

    public void setBody(String body) {
        bodyField.setValue(body);
    }

    public void setSubject(String subject) {
        subjectField.setValue(subject);
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setEntityData(EntityData entityData) {
        this.entityData = entityData;
    }

    public void setOntologyVersionId(String ontologyVersionId) {
        this.ontologyVersionId = ontologyVersionId;
    }

    public void setNoteSentCallback(AsyncHandler<Void> noteSentCallback) {
        this.noteSentCallback = noteSentCallback;
    }

    public void showPopup(String title) {
        window = new Window();
        window.setSize(600, 470);
        window.setTitle(title);
        window.setLayout(new FitLayout());
        window.setClosable(true);
        window.setPaddings(7);
        window.setCloseAction(Window.CLOSE);
        window.setButtonAlign(Position.CENTER);

        setSubject(record == null ? "" : "Re: " +record.getAsString(BioPortalNoteConstants.SUBJECT));
        setBody(getQuotedReplyText());

        window.add(this);

        window.addButton(new Button("Send", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                onCreateNote();
            }
        }) );

        window.addButton(new Button("Cancel", new ButtonListenerAdapter(){
            @Override
            public void onClick(Button button, EventObject e) {
                window.close();
            }
        }));

        window.show();
    }

    private String getQuotedReplyText() {
        if (record == null) {
            return "";
        }

        String author = BioPortalUsersCache.getBioPortalUserName(record.getAsString(BioPortalNoteConstants.AUTHOR));
        String created = record.getAsString(BioPortalNoteConstants.CREATED);
        if (created == null) { return "";}
        long createdLong = Long.parseLong(created);
        Date createdDate = new Date(createdLong);

        String reply = "<br /><br />On " + createdDate.toString() + ", " + author + " wrote:<br /><br />";
        reply = reply +record.getAsString(BioPortalNoteConstants.BODY);
        return reply;
    }

    protected void onCreateNote() {
        String appliesToId = record == null ? entityData.getName() : record.getAsString(BioPortalNoteConstants.ID);
        String appliesToType = null;
        if (record == null) { // new note
            ValueType valueType = UIUtil.guessValueType(entityData);
            valueType = valueType == null ? ValueType.Instance : valueType;
            appliesToType = BioPortalNoteConstants.VALUE_TYPE_2_ENTITY_TYPE.get(valueType);
        } else { //reply
            appliesToType = BioPortalNoteConstants.ENTITY_TYPE_NOTE;
        }

        String ontologyId = record == null ? ontologyVersionId : record.getAsString(BioPortalNoteConstants.ONTOLOGY_ID);
        boolean isVirtual = record == null ? false : true;

        BioportalProposalsManager.getBioportalProposalsManager().createNote(null, null,
                ontologyId, isVirtual,
                BioPortalNoteConstants.NOTE_TYPE_COMMENT,
                appliesToId, appliesToType,
                subjectField.getValueAsString(), bodyField.getValueAsString(),
                BioPortalUsersCache.getCurrentBpUser(), null, new AbstractAsyncHandler<Void>() {

            @Override
            public void handleFailure(Throwable caught) {
                MessageBox.alert("Error", "There was an error sending this note. Please try again later.");
                if (noteSentCallback != null) {
                    noteSentCallback.handleFailure(caught);
                }
            }

            @Override
            public void handleSuccess(Void result) {
                window.close();
                if (noteSentCallback != null) {
                    noteSentCallback.handleSuccess(result);
                }
            }
        });

    }


}

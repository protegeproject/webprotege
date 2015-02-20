package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractDispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorHandler;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorMode;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteEditorDialogController;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityAction;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityResult;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/04/2013
 */
public class PostNewTopicHandlerImpl implements PostNewTopicHandler {

    private Optional<OWLEntity> entity;

    public PostNewTopicHandlerImpl(Optional<OWLEntity> entity) {
        this.entity = entity;
    }

    @Override
    public void handlePostNewTopic() {
        if(!entity.isPresent()) {
            return;
        }
        NoteEditorDialogController controller = new NoteEditorDialogController(new NoteContentEditorHandler() {
            @Override
            public void handleAccept(Optional<NoteContent> noteContent) {
                if(noteContent.isPresent()) {
                    doPost(noteContent.get());
                }
            }
        });
        controller.setMode(NoteContentEditorMode.NEW_TOPIC);
        WebProtegeDialog.showDialog(controller);
    }



    private void doPost(NoteContent content) {
        ProjectId projectId = Application.get().getActiveProject().get();
        DispatchServiceManager.get().execute(new AddNoteToEntityAction(projectId, entity.get(), content), new AbstractDispatchServiceCallback<AddNoteToEntityResult>() {

            @Override
            public void handleSuccess(AddNoteToEntityResult result) {
                GWT.log("Posted o.k. " + result);
            }
        });
    }





}

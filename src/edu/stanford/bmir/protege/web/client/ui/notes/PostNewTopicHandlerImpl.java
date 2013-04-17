package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorDialog;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorMode;
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
        NoteContentEditorDialog dlg = new NoteContentEditorDialog();
        dlg.setMode(NoteContentEditorMode.NEW_TOPIC);
        dlg.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<Optional<NoteContent>>() {
            @Override
            public void handleHide(Optional<NoteContent> data, WebProtegeDialogCloser closer) {
                if (data.isPresent()) {
                    doPost(data.get());
                }
                closer.hide();
            }
        });
        dlg.setVisible(true);
    }



    private void doPost(NoteContent content) {
        ProjectId projectId = Application.get().getActiveProject().get();
        DispatchServiceManager.get().execute(new AddNoteToEntityAction(projectId, entity.get(), content), new AsyncCallback<AddNoteToEntityResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Post failed", caught);
            }

            @Override
            public void onSuccess(AddNoteToEntityResult result) {
                GWT.log("Posted o.k. " + result);
            }
        });
    }
}

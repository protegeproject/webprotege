package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
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


    private final Optional<OWLEntity> entity;

    private final DispatchServiceManager dispatchServiceManager;

    private final ActiveProjectManager activeProjectManager;

    public PostNewTopicHandlerImpl(Optional<OWLEntity> entity, DispatchServiceManager dispatchServiceManager, ActiveProjectManager activeProjectManager) {
        this.entity = entity;
        this.dispatchServiceManager = dispatchServiceManager;
        this.activeProjectManager = activeProjectManager;
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
        Optional<ProjectId> activeProjectId = activeProjectManager.getActiveProjectId();
        if(!activeProjectId.isPresent()) {
            throw new RuntimeException("Cannot post note.  Active project is not present");
        }
        ProjectId projectId = activeProjectId.get();
        dispatchServiceManager.execute(new AddNoteToEntityAction(projectId, entity.get(), content), new DispatchServiceCallback<AddNoteToEntityResult>() {

            @Override
            public void handleSuccess(AddNoteToEntityResult result) {
            }
        });
    }





}

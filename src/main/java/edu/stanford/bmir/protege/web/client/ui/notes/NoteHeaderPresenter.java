package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.notes.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2013
 */
public class NoteHeaderPresenter implements HasDispose {

    private NoteHeaderView noteHeaderView;

    private Note note;

    private NoteStatus currentNoteStatus;

    private HandlerRegistration registration;

    public NoteHeaderPresenter(NoteHeaderView view) {
        this.noteHeaderView = view;
        noteHeaderView.setResolveNoteHandler(new ResolveNoteHandler() {
            @Override
            public void handleResolvePressed() {
                setNoteResolved();
            }
        });
        registration = EventBusManager.getManager().registerHandlerToProject(Application.get().getActiveProject().get(), NoteStatusChangedEvent.TYPE, new NoteStatusChangedHandler() {
            @Override
            public void handleNoteStatusChanged(NoteStatusChangedEvent event) {
                if (note != null && event.getNoteId().equals(note.getNoteId())) {
                    currentNoteStatus = event.getNoteStatus();
                    updateStatus();
                }
            }
        });
    }

    public void setNote(Note note) {
        this.note = note;
        currentNoteStatus = note.getContent().getNoteStatus().or(NoteStatus.OPEN);
        noteHeaderView.setAuthor(note.getAuthor());
        noteHeaderView.setSubject(note.getContent().getSubject());
        updateStatus();
    }



    private void setNoteResolved() {
        ProjectId projectId = Application.get().getActiveProject().get();
        NoteStatus status = currentNoteStatus == NoteStatus.OPEN ? NoteStatus.RESOLVED : NoteStatus.OPEN;
        DispatchServiceManager.get().execute(new                 SetNoteStatusAction(projectId, note.getNoteId(), status), new DispatchServiceCallback<SetNoteStatusResult>() {
            @Override
            public void handleSuccess(SetNoteStatusResult result) {
                currentNoteStatus = result.getNoteStatus();
            }
        });
    }

    public Widget getWidget() {
        return noteHeaderView.getWidget();
    }


    private void updateStatus() {
        noteHeaderView.setStatus(Optional.of(currentNoteStatus));
        final UserId userId = Application.get().getUserId();
        noteHeaderView.setResolveOptionVisible(!userId.isGuest() && userId.equals(note.getAuthor()));
    }

    @Override
    public void dispose() {
        if (registration != null) {
            registration.removeHandler();
        }
    }
}

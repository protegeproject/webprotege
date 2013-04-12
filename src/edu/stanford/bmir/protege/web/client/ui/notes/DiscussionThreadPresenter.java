package edu.stanford.bmir.protege.web.client.ui.notes;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetDiscussionThreadAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetDiscussionThreadResult;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;
import edu.stanford.bmir.protege.web.shared.notes.Note;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class DiscussionThreadPresenter {

    private DiscussionThreadView view;

    private OWLEntity currentTarget;

    private ProjectId projectId;

    public DiscussionThreadPresenter(ProjectId projectId) {
        view = new DiscussionThreadViewImpl();
        this.projectId = checkNotNull(projectId);
    }

    public void attach() {

    }

    public void detach() {

    }

    public void clearTarget() {
        view.removeAllNotes();
        currentTarget = null;
    }

    public void setTarget(OWLEntity target) {
        currentTarget = target;
        view.removeAllNotes();
        DispatchServiceManager.get().execute(new GetDiscussionThreadAction(projectId, target), new AsyncCallback<GetDiscussionThreadResult>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(GetDiscussionThreadResult result) {
                displayDiscussionThread(result.getThread());
            }
        });


    }

    private void displayDiscussionThread(DiscussionThread thread) {
        for(Note rootNote : thread.getRootNotes()) {
            appendNote(rootNote, 0, thread);
        }
    }


    private void appendNote(Note note, int depth, DiscussionThread discussionThread) {
        NoteContainerPresenter noteContainerPresenter = new NoteContainerPresenter(new NoteContainerViewImpl());
        noteContainerPresenter.setNote(note, discussionThread);
        view.addNote(noteContainerPresenter, depth);
        for(Note childNote : discussionThread.getReplies(note.getNoteId())) {
            appendNote(childNote, depth + 1, discussionThread);
        }
    }

    public Widget getWidget() {
        return view.getWidget();
    }
}

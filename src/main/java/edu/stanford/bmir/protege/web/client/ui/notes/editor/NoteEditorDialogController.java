package edu.stanford.bmir.protege.web.client.ui.notes.editor;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class NoteEditorDialogController extends WebProtegeOKCancelDialogController<Optional<NoteContent>> {

    private NoteContentEditorView view;

    public NoteEditorDialogController(final NoteContentEditorHandler handler) {
        super("Edit Note");
        this.view = new NoteContentEditorViewImpl();
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<Optional<NoteContent>>() {
            @Override
            public void handleHide(Optional<NoteContent> data, WebProtegeDialogCloser closer) {
                if (data.isPresent()) {
                    handler.handleAccept(data);
                }
                closer.hide();
            }
        });
    }

//    private void doPost(NoteContent content) {
//        ProjectId projectId = Application.get().getActiveProject().get();
//        DispatchServiceManager.get().execute(new AddNoteToEntityAction(projectId, entity.get(), content), new AsyncCallback<AddNoteToEntityResult>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                GWT.log("Post failed", caught);
//            }
//
//            @Override
//            public void handleSuccess(AddNoteToEntityResult result) {
//                GWT.log("Posted o.k. " + result);
//            }
//        });
//    }
//
//    private void doReply(NoteContent content) {
//        ProjectId projectId = Application.get().getActiveProject().get();
//        DispatchServiceManager.get().execute(new AddReplyToNoteAction(projectId, noteId, content), new AsyncCallback<AddReplyToNoteResult>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                GWT.log("Reply failed", caught);
//            }
//
//            @Override
//            public void handleSuccess(AddReplyToNoteResult result) {
//                GWT.log("Replied o.k. " + result);
//            }
//        });
//    }

    public void setMode(NoteContentEditorMode mode) {
        view.setMode(mode);
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    @Override
    public Optional<NoteContent> getData() {
        return view.getValue();
    }

    @Override
    public Widget getWidget() {
        return view.asWidget();
    }
}

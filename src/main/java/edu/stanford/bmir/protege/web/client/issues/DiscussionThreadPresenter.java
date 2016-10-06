package edu.stanford.bmir.protege.web.client.issues;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorHandler;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorMode;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteEditorDialogController;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class DiscussionThreadPresenter implements HasDispose {

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final DiscussionThreadListView view;

    @Nonnull
    private final ProjectId projectId;

//    @Nonnull
//    private final Provider<DiscussionThreadView2> discussionThreadViewProvider;

//    @Nonnull
//    private final Provider<CommentView> commentViewProvider;

    private HandlerRegistration handlerRegistration = () -> {};

    @Inject
    public DiscussionThreadPresenter(
            @Nonnull EventBus eventBus,
            @Nonnull DispatchServiceManager dispatchServiceManager,
            @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
            @Nonnull DiscussionThreadListView view,
            @Nonnull ProjectId projectId
//            @Nonnull Provider<DiscussionThreadView2> discussionThreadViewProvider,
//            @Nonnull Provider<CommentView> commentViewProvider
    ) {
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
        this.permissionChecker = permissionChecker;
        this.view = view;
        this.projectId = projectId;
//        this.discussionThreadViewProvider = discussionThreadViewProvider;
//        this.commentViewProvider = commentViewProvider;
    }

    public void start() {
        handlerRegistration = eventBus.addHandler(PermissionsChangedEvent.TYPE, event -> {
            updateEnabled();
        });
        updateEnabled();
    }

    private void updateEnabled() {
        view.setEnabled(false);
        permissionChecker.hasCommentPermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean b) {
                view.setEnabled(b);
            }
        });
    }

    public void setEntity(@Nonnull OWLEntity entity) {
        dispatchServiceManager.execute(new GetEntityDiscussionThreadsAction(projectId, entity),
                                       new DispatchServiceCallback<GetEntityDiscussionThreadsResult>() {
                                           @Override
                                           public void handleSuccess(GetEntityDiscussionThreadsResult result) {
                                               displayThreads(result.getThreads());
                                           }
                                       });
    }

    private void displayThreads(List<EntityDiscussionThread> threads) {
        view.clear();
        GWT.log("Displaying threads");
        for (EntityDiscussionThread thread : threads) {
            try {
                DiscussionThreadView threadView = new DiscussionThreadViewImpl();
                for (Comment comment : thread.getComments()) {
                    CommentView commentView = null;
                    try {
                        commentView = new CommentViewImpl();
                        commentView.setCreatedBy(comment.getCreatedBy());
                        commentView.setCreatedAt(comment.getCreatedAt());
                        commentView.setUpdatedAt(comment.getUpdatedAt());
                        commentView.setBody(comment.getBody());
                        commentView.setReplyToCommentHandler(() -> handleReplyToComment(thread.getId()));
                        threadView.addCommentView(commentView);
                    } catch (Exception e) {
                        GWT.log("An error occurred creating the comment view: " + e.getMessage());
                    }
                    //                // TODO: Enabled

                }
                view.addDiscussionThreadView(threadView);
            } catch (Exception e) {
                GWT.log("An error occurred: " + e.getMessage());
            }
        }
    }

    public void addThread() {
        NoteEditorDialogController controller = new NoteEditorDialogController(new NoteContentEditorHandler() {
            @Override
            public void handleAccept(Optional<NoteContent> noteContent) {

            }
        });
        controller.setMode(NoteContentEditorMode.REPLY);

    }

    private void handleReplyToComment(ThreadId threadId) {

    }



    @Override
    public void dispose() {
        handlerRegistration.removeHandler();
    }

    public void clear() {
        view.clear();
    }

    @Nonnull
    public DiscussionThreadListView getView() {
        return view;
    }
}

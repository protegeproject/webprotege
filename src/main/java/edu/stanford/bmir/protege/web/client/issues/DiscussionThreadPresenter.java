package edu.stanford.bmir.protege.web.client.issues;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorMode;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteEditorDialogController;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class DiscussionThreadPresenter implements HasDispose {

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final DiscussionThreadListView view;

    @Nonnull
    private final ProjectId projectId;

//    @Nonnull
//    private final Provider<DiscussionThreadView2> discussionThreadViewProvider;

//    @Nonnull
//    private final Provider<CommentView> commentViewProvider;

    private HandlerRegistration handlerRegistration = () -> {
    };

    private java.util.Optional<OWLEntity> entity = java.util.Optional.empty();

    private Map<ThreadId, DiscussionThreadView> discussionThreadViewMap = new HashMap<>();

    private Multimap<ThreadId, Comment> displayedComments = HashMultimap.create();

    @Inject
    public DiscussionThreadPresenter(
            @Nonnull EventBus eventBus,
            @Nonnull DispatchServiceManager dispatch,
            @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
            @Nonnull LoggedInUserProvider loggedInUserProvider, @Nonnull DiscussionThreadListView view,
            @Nonnull ProjectId projectId
//            @Nonnull Provider<DiscussionThreadView2> discussionThreadViewProvider,
//            @Nonnull Provider<CommentView> commentViewProvider
    ) {
        this.eventBus = eventBus;
        this.dispatch = dispatch;
        this.permissionChecker = permissionChecker;
        this.loggedInUserProvider = loggedInUserProvider;
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
        this.entity = java.util.Optional.of(entity);
        dispatch.execute(new GetEntityDiscussionThreadsAction(projectId, entity),
                         new DispatchServiceCallback<GetEntityDiscussionThreadsResult>() {
                                           @Override
                                           public void handleSuccess(GetEntityDiscussionThreadsResult result) {
                                               displayThreads(result.getThreads());
                                           }
                                       });
    }

    private void displayThreads(List<EntityDiscussionThread> threads) {
        view.clear();
        discussionThreadViewMap.clear();
        displayedComments.clear();
        GWT.log("Displaying " + threads.size() + " threads");
        for (EntityDiscussionThread thread : threads) {
            try {
                DiscussionThreadView threadView = new DiscussionThreadViewImpl();
                discussionThreadViewMap.put(thread.getId(), threadView);
                for (Comment comment : thread.getComments()) {
                    CommentView commentView = createCommentView(thread.getId(), comment);
                    threadView.addCommentView(commentView);
                    displayedComments.put(thread.getId(), comment);
                }
                view.addDiscussionThreadView(threadView);
            } catch (Exception e) {
                GWT.log("An error occurred: " + e.getMessage());
            }
        }
    }

    private CommentView createCommentView(ThreadId threadId, Comment comment) {
        final CommentView commentView = new CommentViewImpl();
        commentView.setCreatedBy(comment.getCreatedBy());
        commentView.setCreatedAt(comment.getCreatedAt());
        commentView.setUpdatedAt(comment.getUpdatedAt());
        commentView.setBody(comment.getBody());
        commentView.setReplyToCommentHandler(() -> handleReplyToComment(threadId));
        final boolean userIsCommentCreator = isLoggedInUserCommentCreator(comment);
        commentView.setDeleteButtonVisible(userIsCommentCreator);
        commentView.setEditButtonVisible(userIsCommentCreator);
        commentView.setReplyButtonVisible(false);
        permissionChecker.hasCommentPermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean canComment) {
                commentView.setReplyButtonVisible(canComment);
            }
        });
        return commentView;
    }

    private boolean isLoggedInUserCommentCreator(Comment comment) {
        return comment.getCreatedBy().equals(loggedInUserProvider.getCurrentUserId());
    }

    public void createThread() {
        entity.ifPresent(e -> {

            NoteEditorDialogController ctrl = new NoteEditorDialogController(noteContent -> {
                if (noteContent.get().getBody().isPresent()) {
                    CreateEntityDiscussionThreadAction action = new CreateEntityDiscussionThreadAction(projectId,
                                                                                                       e,
                                                                                                       noteContent.get()
                                                                                                                  .getBody()
                                                                                                                  .get());
                    dispatch.execute(action,
                                     new DispatchServiceCallback<CreateEntityDiscussionThreadResult>() {
                                                       @Override
                                                       public void handleSuccess(
                                                               CreateEntityDiscussionThreadResult result) {
                                                           displayThreads(result.getThreads());
                                                       }
                                                   });
                }
            });
            ctrl.setMode(NoteContentEditorMode.REPLY);
            WebProtegeDialog.showDialog(ctrl);

        });
    }

    private void handleReplyToComment(ThreadId threadId) {
        String body = "The new reply";
        dispatch.execute(new AddEntityCommentAction(projectId, threadId, body),
                         new DispatchServiceCallback<AddEntityCommentResult>() {
                                           @Override
                                           public void handleSuccess(AddEntityCommentResult result) {
                                               handleCommentAdded(threadId, result.getComment());
                                           }
                                       });
    }

    private void handleCommentAdded(ThreadId threadId, Comment comment) {
        if (displayedComments.get(threadId).contains(comment)) {
            return;
        }
        DiscussionThreadView view = discussionThreadViewMap.get(threadId);
        if(view != null) {
            CommentView commentView = createCommentView(threadId, comment);
            view.addCommentView(commentView);
        }
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

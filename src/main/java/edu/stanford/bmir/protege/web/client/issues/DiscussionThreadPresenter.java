package edu.stanford.bmir.protege.web.client.issues;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox.showYesNoConfirmBox;
import static edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentAction.addEntityComment;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
public class DiscussionThreadPresenter implements HasDispose {

    @Nonnull
    private final DiscussionThreadView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CommentViewFactory commentViewFactory;

    @Nonnull
    private final Provider<CommentEditorDialog> commentEditorDialogProvider;

    private final Map<Comment, CommentView> displayedComments = new HashMap<>();

    private HandlerRegistration permissionsChangedHandlerRegistration = () -> {};



    @Inject
    public DiscussionThreadPresenter(@Nonnull DiscussionThreadView view,
                                     @Nonnull Messages messages,
                                     @Nonnull ProjectId projectId,
                                     @Nonnull DispatchServiceManager dispatch,
                                     @Nonnull EventBus eventBus,
                                     @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                     @Nonnull LoggedInUserProvider loggedInUserProvider,
                                     @Nonnull CommentViewFactory commentViewFactory,
                                     @Nonnull Provider<CommentEditorDialog> commentEditorDialogProvider) {
        this.view = checkNotNull(view);
        this.messages = checkNotNull(messages);
        this.projectId = checkNotNull(projectId);
        this.dispatch = checkNotNull(dispatch);
        this.eventBus = eventBus;
        this.permissionChecker = permissionChecker;
        this.loggedInUserProvider = loggedInUserProvider;
        this.commentViewFactory = checkNotNull(commentViewFactory);
        this.commentEditorDialogProvider = checkNotNull(commentEditorDialogProvider);
    }

    @Nonnull
    public DiscussionThreadView getView() {
        return view;
    }

    public void start() {
        permissionsChangedHandlerRegistration =
        eventBus.addHandlerToSource(ON_PERMISSIONS_CHANGED,
                                    projectId,
                                    event -> updateDisplayedViews());
        // TODO: Respond to CommentAdded, CommentEdited and CommentDeletedEvents
    }

    private void updateDisplayedViews() {
        displayedComments.forEach((comment, view) -> updateCommentView(comment, view));
    }

    private void updateCommentView(Comment comment, CommentView commentView) {
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
    }

    private boolean isLoggedInUserCommentCreator(Comment comment) {
        return comment.getCreatedBy().equals(loggedInUserProvider.getCurrentUserId());
    }


    @Override
    public void dispose() {
        permissionsChangedHandlerRegistration.removeHandler();
    }

    public void setDiscussionThread(@Nonnull EntityDiscussionThread thread) {
        view.clear();
        displayedComments.clear();
        for (Comment comment : thread.getComments()) {
            CommentView commentView = createCommentView(thread.getId(), comment);
            updateCommentView(comment, commentView);
            view.addCommentView(commentView);
            displayedComments.put(comment, commentView);
        }
    }

    private CommentView createCommentView(ThreadId threadId, Comment comment) {
        return commentViewFactory.createAndInitView(
                comment,
                () -> handleReplyToComment(threadId),
                () -> handleEditComment(threadId, comment),
                () -> handleDeleteComment(threadId, comment)
        );
    }

    private void handleReplyToComment(ThreadId threadId) {
        CommentEditorDialog dlg = commentEditorDialogProvider.get();
        dlg.show((body) -> dispatch.execute(
                addEntityComment(projectId, threadId, body),
                result -> handleCommentAdded(threadId, result.getComment()))
        );
    }

    private void handleEditComment(ThreadId threadId, Comment comment) {
        CommentEditorDialog dlg = commentEditorDialogProvider.get();
        dlg.setCommentBody(comment.getBody());
        dlg.show((body) -> {
            dispatch.execute(new EditCommentAction(projectId, threadId, comment.getId(), body),
                             result -> updateComment(result.getEditedComment()));
        });
    }

    private void updateComment(Comment comment) {
        CommentView view = displayedComments.get(comment);
        if (view != null) {
            view.setBody(comment.getBody());
        }
    }

    private void handleDeleteComment(ThreadId threadId, Comment comment) {
        showYesNoConfirmBox(messages.deleteCommentConfirmationBoxTitle(),
                            messages.deleteCommentConfirmationBoxText(),
                            () -> {
                                // TODO: Delete comment
                            });
    }

    private void handleCommentAdded(ThreadId threadId, Comment comment) {
        if (displayedComments.containsKey(comment.getId())) {
            return;
        }
        CommentView commentView = createCommentView(threadId, comment);
        view.addCommentView(commentView);
    }

}

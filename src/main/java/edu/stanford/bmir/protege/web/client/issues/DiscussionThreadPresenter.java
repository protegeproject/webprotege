package edu.stanford.bmir.protege.web.client.issues;

import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.HandlerRegistrationManager;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox.showYesNoConfirmBox;
import static edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentAction.addEntityComment;
import static edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent.ON_COMMENT_POSTED;
import static edu.stanford.bmir.protege.web.shared.issues.CommentUpdatedEvent.ON_COMMENT_UPDATED;
import static edu.stanford.bmir.protege.web.shared.issues.SetDiscussionThreadStatusAction.setDiscussionThreadStatus;

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
    private final HandlerRegistrationManager eventBus;

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

    private final Map<CommentId, CommentView> commentViewMap = new HashMap<>();

    private Optional<ThreadId> currentThreadId = Optional.empty();


    @Inject
    public DiscussionThreadPresenter(@Nonnull DiscussionThreadView view,
                                     @Nonnull Messages messages,
                                     @Nonnull ProjectId projectId,
                                     @Nonnull DispatchServiceManager dispatch,
                                     @Nonnull HandlerRegistrationManager eventBus,
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
        eventBus.registerHandlerToProject(projectId,
                                          ON_PERMISSIONS_CHANGED,
                                          event -> updateDisplayedViews());
        eventBus.registerHandlerToProject(projectId,
                                          ON_COMMENT_UPDATED,
                                          event -> updateComment(event.getComment()));
        eventBus.registerHandlerToProject(projectId,
                                          ON_COMMENT_POSTED,
                                          event -> handleCommentAdded(event.getThreadId(), event.getComment()));
    }

    private void updateDisplayedViews() {
        commentViewMap.forEach((commentId, view) -> updateCommentView(view));
    }

    private void updateCommentView(CommentView commentView) {
//        commentView.setDeleteButtonVisible(userIsCommentCreator);
        final boolean userIsCommentCreator = commentView.getCreatedBy().equals(Optional.of(loggedInUserProvider.getCurrentUserId()));
        commentView.setEditButtonVisible(userIsCommentCreator);
        commentView.setReplyButtonVisible(false);
        permissionChecker.hasCommentPermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean canComment) {
                commentView.setReplyButtonVisible(canComment);
            }
        });
    }


    @Override
    public void dispose() {
        eventBus.removeHandlers();
    }

    public void setDiscussionThread(@Nonnull EntityDiscussionThread thread) {
        currentThreadId = Optional.of(checkNotNull(thread).getId());
        view.clear();
        commentViewMap.clear();
        for (Comment comment : thread.getComments()) {
            view.setStatus(thread.getStatus());
            view.setStatusChangedHandler(() -> handleToggleStatus(thread.getId()));
            addCommentView(thread.getId(), comment);
        }
    }

    private void addCommentView(@Nonnull ThreadId threadId, Comment comment) {
        CommentView commentView = createCommentView(threadId, comment);
        updateCommentView(commentView);
        view.addCommentView(commentView);
        commentViewMap.put(comment.getId(), commentView);
    }

    private CommentView createCommentView(ThreadId threadId, Comment comment) {
        return commentViewFactory.createAndInitView(
                comment,
                () -> handleReplyToComment(threadId),
                () -> handleEditComment(threadId, comment),
                () -> handleDeleteComment(threadId, comment)
        );
    }

    private void handleToggleStatus(ThreadId threadId) {
        Status nextStatus = view.getStatus() == Status.OPEN ? Status.CLOSED : Status.OPEN;
        dispatch.execute(
                setDiscussionThreadStatus(projectId, threadId, nextStatus),
                (result) -> view.setStatus(result.getResult())
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
        dlg.show((body) -> dispatch.execute(new EditCommentAction(projectId, threadId, comment.getId(), body),
                                            result -> result.getEditedComment().ifPresent(c -> updateComment(c))));
    }

    private void updateComment(Comment comment) {
        CommentView view = commentViewMap.get(comment.getId());
        if (view != null) {
            view.setUpdatedAt(comment.getUpdatedAt());
            view.setBody(comment.getBody());
        }
    }

    private void handleDeleteComment(ThreadId threadId, Comment comment) {
        showYesNoConfirmBox(messages.deleteCommentConfirmationBoxTitle(),
                            messages.deleteCommentConfirmationBoxText(),
                            () -> {
                                dispatch.execute(new DeleteEntityCommentAction(comment.getId()),
                                                 result -> {
                                                 });
                            });
    }

    private void handleCommentAdded(ThreadId threadId, Comment comment) {
        if(!threadId.equals(currentThreadId.orElse(null))) {
            return;
        }
        if (commentViewMap.containsKey(comment.getId())) {
            return;
        }
        addCommentView(threadId, comment);
    }

}

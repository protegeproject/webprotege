package edu.stanford.bmir.protege.web.client.issues;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.HasPortletActions;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.HandlerRegistrationManager;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadAction.createEntityDiscussionThread;
import static edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadCreatedEvent.ON_DISCUSSION_THREAD_CREATED;
import static edu.stanford.bmir.protege.web.shared.issues.GetEntityDiscussionThreadsAction.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class DiscussionThreadListPresenter implements HasDispose {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final DiscussionThreadListView view;

    @Nonnull
    private final PortletAction addCommentAction;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Provider<CommentEditorDialog> commentEditorDialogProvider;

    private final Provider<DiscussionThreadPresenter> discussionThreadPresenterProvider;

    private final Map<ThreadId, DiscussionThreadPresenter> threadPresenters = new HashMap<>();

    private final HandlerRegistrationManager eventBus;

    private Optional<OWLEntity> entity = Optional.empty();


    @Inject
    public DiscussionThreadListPresenter(
            @Nonnull HandlerRegistrationManager eventBus,
            @Nonnull DispatchServiceManager dispatch,
            @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
            @Nonnull DiscussionThreadListView view,
            @Nonnull ProjectId projectId,
            @Nonnull Provider<CommentEditorDialog> commentEditorDialogProvider,
            @Nonnull Messages messages,
            @Nonnull Provider<DiscussionThreadPresenter> discussionThreadPresenterProvider) {
        this.eventBus = eventBus;
        this.dispatch = dispatch;
        this.permissionChecker = permissionChecker;
        this.view = view;
        this.projectId = projectId;
        this.commentEditorDialogProvider = commentEditorDialogProvider;
        this.addCommentAction = new PortletAction(messages.startNewCommentThread(),
                                                  (action, event) -> handleCreateThread());
        this.discussionThreadPresenterProvider = discussionThreadPresenterProvider;
    }

    public void installActions(HasPortletActions hasPortletActions) {
        hasPortletActions.addPortletAction(addCommentAction);
    }

    public void start() {
        eventBus.registerHandlerToProject(projectId, ON_PERMISSIONS_CHANGED, event -> updateEnabled());
        eventBus.registerHandlerToProject(projectId, ON_DISCUSSION_THREAD_CREATED, event -> addThread(event.getThread()));
        updateEnabled();
    }

    public void setEntity(@Nonnull OWLEntity entity) {
        this.entity = Optional.of(entity);
        dispatch.execute(
                getDiscussionThreads(projectId, entity),
                result -> displayThreads(result.getThreads())
        );
    }

    public void clear() {
        view.clear();
    }

    @Nonnull
    public DiscussionThreadListView getView() {
        return view;
    }

    private void updateEnabled() {
        view.setEnabled(false);
        addCommentAction.setEnabled(false);
        permissionChecker.hasCommentPermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean b) {
                view.setEnabled(b);
                addCommentAction.setEnabled(b);
            }
        });
    }


    private void displayThreads(List<EntityDiscussionThread> threads) {
        view.clear();
        stopThreadPresenters();
        for (EntityDiscussionThread thread : threads) {
            addThread(thread);
        }
    }

    private void addThread(EntityDiscussionThread thread) {
        if(threadPresenters.containsKey(thread.getId())) {
            return;
        }
        DiscussionThreadPresenter presenter = discussionThreadPresenterProvider.get();
        threadPresenters.put(thread.getId(), presenter);
        presenter.start();
        presenter.setDiscussionThread(thread);
        DiscussionThreadView threadView = presenter.getView();
        view.addDiscussionThreadView(threadView);
    }

    public void handleCreateThread() {
        entity.ifPresent(targetEntity -> {
            CommentEditorDialog dlg = commentEditorDialogProvider.get();
            dlg.show((body) -> dispatch.execute(
                    createEntityDiscussionThread(projectId, targetEntity, body),
                    result -> displayThreads(result.getThreads())
            ));
        });
    }

    private void stopThreadPresenters() {
        threadPresenters.forEach((t,p) -> p.dispose());
        threadPresenters.clear();
    }

    @Override
    public void dispose() {
        eventBus.removeHandlers();
        stopThreadPresenters();
    }

}

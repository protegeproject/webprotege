package edu.stanford.bmir.protege.web.client.issues;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadCreatedEvent;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import edu.stanford.bmir.protege.web.shared.issues.Status;
import edu.stanford.bmir.protege.web.shared.issues.ThreadId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_OBJECT_COMMENT;
import static edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadAction.createEntityDiscussionThread;
import static edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadCreatedEvent.ON_DISCUSSION_THREAD_CREATED;
import static edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent.ON_STATUS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.issues.GetEntityDiscussionThreadsAction.getDiscussionThreads;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 *
 * A presenter that presents discussion threads for a given displayedEntity.
 */
public class DiscussionThreadListPresenter implements HasDispose {

    @Nonnull
    private final DiscussionThreadListView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Provider<CommentEditorDialog> commentEditorDialogProvider;

    @Nonnull
    private final Provider<DiscussionThreadPresenter> discussionThreadPresenterProvider;

    @Nonnull
    private final Map<ThreadId, DiscussionThreadPresenter> threadPresenters = new HashMap<>();

    private boolean displayResolvedThreads = false;

    private Optional<OWLEntity> displayedEntity = Optional.empty();

    private final List<EntityDiscussionThread> displayedThreads = new ArrayList<>();

    @Nonnull
    private HasBusy hasBusy = busy -> {};

    @Inject
    public DiscussionThreadListPresenter(
            @Nonnull DiscussionThreadListView view,
            @Nonnull DispatchServiceManager dispatch,
            @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
            @Nonnull ProjectId projectId,
            @Nonnull Provider<CommentEditorDialog> commentEditorDialogProvider,
            @Nonnull Provider<DiscussionThreadPresenter> discussionThreadPresenterProvider) {
        this.view = view;
        this.dispatch = dispatch;
        this.permissionChecker = permissionChecker;
        this.projectId = projectId;
        this.commentEditorDialogProvider = commentEditorDialogProvider;
        this.discussionThreadPresenterProvider = discussionThreadPresenterProvider;
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    public void start(WebProtegeEventBus eventBus) {
        eventBus.addProjectEventHandler(projectId, ON_PERMISSIONS_CHANGED, event -> updateEnabled());
        eventBus.addProjectEventHandler(projectId, ON_DISCUSSION_THREAD_CREATED, this::handleDiscussionThreadCreated);
        eventBus.addProjectEventHandler(projectId, ON_STATUS_CHANGED, event -> handleThreadStatusChanged(event.getThreadId(), event.getStatus()));
        updateEnabled();
    }

    private void handleDiscussionThreadCreated(DiscussionThreadCreatedEvent event) {
        if (displayedEntity.equals(Optional.of(event.getThread().getEntity()))) {
            addThread(event.getThread());
        }
    }

    /**
     * Gets the view that displays the discussion threads.
     * @return The view.
     */
    @Nonnull
    public IsWidget getView() {
        return view;
    }

    /**
     * Sets the {@link OWLEntity} that discussion threads should be displayed for.  If the displayedEntity
     * is different to the current displayedEntity then the view will be reloaded.
     * @param entity The displayedEntity.
     */
    public void setEntity(@Nonnull OWLEntity entity) {
        if (!this.displayedEntity.equals(Optional.of(entity))) {
            this.displayedEntity = Optional.of(entity);
            reload();
        }
    }

    /**
     * Clears the view so that it is empty.
     */
    public void clear() {
        view.clear();
    }

    /**
     * Force reload the view content.  This will get the latest discussion threads for the current
     * displayedEntity.
     */
    public void reload() {
        displayedEntity.ifPresent(e -> {
            dispatch.execute(
                    getDiscussionThreads(projectId, e),
                    hasBusy,
                    result -> displayThreads(result.getThreads())
            );
        });
    }

    /**
     * Specifies whether threads that have been resolved should be displayed.
     * @param displayResolvedThreads If true then resolved threads will be displayed.  If
     *                               false then resolved threads will be hidden.
     */
    public void setDisplayResolvedThreads(boolean displayResolvedThreads) {
        if (this.displayResolvedThreads != displayResolvedThreads) {
            this.displayResolvedThreads = displayResolvedThreads;
            refresh();
        }
    }

    /**
     * Determines whether resolved threads will be displayed.
     * @return A boolean indicating whether resolved threads will be displayed.  By default this is
     * false.
     */
    public boolean isDisplayResolvedThreads() {
        return displayResolvedThreads;
    }

    /**
     * Prompts the user to create a new comment that will be used to start a new discussion thread.
     */
    public void createThread() {
        displayedEntity.ifPresent(targetEntity -> {
            CommentEditorDialog dlg = commentEditorDialogProvider.get();
            dlg.show((body) -> dispatch.execute(
                    createEntityDiscussionThread(projectId, targetEntity, body),
                    result -> displayThreads(result.getThreads())
            ));
        });
    }

    private void handleThreadStatusChanged(ThreadId threadId, Status status) {
        if(isDisplayResolvedThreads()) {
            return;
        }
        DiscussionThreadPresenter threadPresenter = threadPresenters.get(threadId);
        if(threadPresenter != null && status == Status.CLOSED) {
            DiscussionThreadView threadView = threadPresenter.getView();
            view.hideDiscussionThreadView(threadView);
            threadPresenters.remove(threadId);
        }
    }


    private void updateEnabled() {
        view.setEnabled(false);
        permissionChecker.hasPermission(CREATE_OBJECT_COMMENT, view::setEnabled);
    }

    /**
     * Clears the view and refills it based on current client data
     */
    private void refresh() {
        redisplayThreads();
    }

    private void displayThreads(List<EntityDiscussionThread> threads) {
        this.displayedThreads.clear();
        this.displayedThreads.addAll(threads);
        redisplayThreads();
    }

    private void redisplayThreads() {
        view.clear();
        stopThreadPresenters();
        for (EntityDiscussionThread thread : displayedThreads) {
            addThread(thread);
        }
    }

    private void addThread(EntityDiscussionThread thread) {
        if(threadPresenters.containsKey(thread.getId())) {
            return;
        }
        if(thread.getStatus() == Status.CLOSED && !isDisplayResolvedThreads()) {
            return;
        }
        DiscussionThreadPresenter presenter = discussionThreadPresenterProvider.get();
        threadPresenters.put(thread.getId(), presenter);
        presenter.start();
        presenter.setDiscussionThread(thread);
        DiscussionThreadView threadView = presenter.getView();
        view.addDiscussionThreadView(threadView);
    }

    private void stopThreadPresenters() {
        threadPresenters.forEach((t,p) -> p.dispose());
        threadPresenters.clear();
    }

    @Override
    public void dispose() {
        stopThreadPresenters();
    }
}

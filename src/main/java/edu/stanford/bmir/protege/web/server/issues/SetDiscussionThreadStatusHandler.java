package edu.stanford.bmir.protege.web.server.issues;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.SET_OBJECT_COMMENT_STATUS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class SetDiscussionThreadStatusHandler extends AbstractHasProjectActionHandler<SetDiscussionThreadStatusAction, SetDiscussionThreadStatusResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public SetDiscussionThreadStatusHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull EntityDiscussionThreadRepository repository,
                                            @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                            @Nonnull ProjectId projectId) {
        super(accessManager);
        this.repository = repository;
        this.eventManager = eventManager;
        this.projectId = projectId;
    }

    @Override
    public Class<SetDiscussionThreadStatusAction> getActionClass() {
        return SetDiscussionThreadStatusAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return SET_OBJECT_COMMENT_STATUS;
    }

    @Override
    public SetDiscussionThreadStatusResult execute(SetDiscussionThreadStatusAction action,
                                                      ExecutionContext executionContext) {
        EventTag fromTag = eventManager.getCurrentTag();
        ThreadId threadId = action.getThreadId();
        Status status = action.getStatus();
        Optional<EntityDiscussionThread> thread = repository.setThreadStatus(threadId, status);
        int openComments = thread.map(t -> repository.getOpenCommentsCount(projectId, t.getEntity())).orElse(-1);
        eventManager.postEvent(new DiscussionThreadStatusChangedEvent(projectId,
                                                                                   threadId,
                                                                                   thread.map(EntityDiscussionThread::getEntity),
                                                                                   openComments,
                                                                                   status));
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(fromTag);
        return new SetDiscussionThreadStatusResult(threadId, status, eventList);
    }
}

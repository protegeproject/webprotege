package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.tag.UpdateEntityTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.UpdateEntityTagsResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Mar 2018
 */
public class UpdateEntityTagsActionHandler extends AbstractProjectActionHandler<UpdateEntityTagsAction, UpdateEntityTagsResult> {

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventEventManager;

    @Nonnull
    private final TagsManager tagsManager;

    @Inject
    public UpdateEntityTagsActionHandler(@Nonnull AccessManager accessManager,
                                         @Nonnull EventManager<ProjectEvent<?>> eventEventManager, @Nonnull TagsManager tagsManager) {
        super(accessManager);
        this.eventEventManager = checkNotNull(eventEventManager);
        this.tagsManager = checkNotNull(tagsManager);
    }

    @Nonnull
    @Override
    public Class<UpdateEntityTagsAction> getActionClass() {
        return UpdateEntityTagsAction.class;
    }

    @Nonnull
    @Override
    public UpdateEntityTagsResult execute(@Nonnull UpdateEntityTagsAction action,
                                          @Nonnull ExecutionContext executionContext) {

        EventTag startTag = eventEventManager.getCurrentTag();
        tagsManager.updateTags(action.getEntity(),
                               action.getFromTagIds(),
                               action.getToTagIds());
        EventList<ProjectEvent<?>> events = eventEventManager.getEventsFromTag(startTag);
        return new UpdateEntityTagsResult(events);
    }
}

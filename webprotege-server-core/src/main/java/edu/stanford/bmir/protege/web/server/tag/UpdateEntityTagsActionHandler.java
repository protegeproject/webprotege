package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
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
    private final EntityTagsManager entityTagsManager;

    @Inject
    public UpdateEntityTagsActionHandler(@Nonnull AccessManager accessManager,
                                         @Nonnull EntityTagsManager entityTagsManager) {
        super(accessManager);
        this.entityTagsManager = checkNotNull(entityTagsManager);
    }

    @Nonnull
    @Override
    public Class<UpdateEntityTagsAction> getActionClass() {
        return UpdateEntityTagsAction.class;
    }

    @Nonnull
    @Override
    public UpdateEntityTagsResult execute(@Nonnull UpdateEntityTagsAction action, @Nonnull ExecutionContext executionContext) {
        entityTagsManager.updateTags(action.getEntity(),
                                     action.getFromTagIds(),
                                     action.getToTagIds());
        return new UpdateEntityTagsResult();
    }
}

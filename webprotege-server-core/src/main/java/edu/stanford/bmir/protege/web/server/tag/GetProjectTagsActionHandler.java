package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_PROJECT_TAGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2018
 */
public class GetProjectTagsActionHandler extends AbstractProjectActionHandler<GetProjectTagsAction, GetProjectTagsResult> {

    @Nonnull
    private final EntityTagsManager tagsManager;

    @Inject
    public GetProjectTagsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull EntityTagsManager tagsManager) {
        super(accessManager);
        this.tagsManager = checkNotNull(tagsManager);
    }

    @Nonnull
    @Override
    public Class<GetProjectTagsAction> getActionClass() {
        return GetProjectTagsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_PROJECT_TAGS;
    }

    @Nonnull
    @Override
    public GetProjectTagsResult execute(@Nonnull GetProjectTagsAction action, @Nonnull ExecutionContext executionContext) {
        return new GetProjectTagsResult(tagsManager.getProjectTags());
    }
}

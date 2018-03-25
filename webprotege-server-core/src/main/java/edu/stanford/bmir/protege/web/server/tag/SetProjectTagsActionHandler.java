package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.tag.SetProjectTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.SetProjectTagsResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_PROJECT_TAGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
public class SetProjectTagsActionHandler extends AbstractProjectActionHandler<SetProjectTagsAction, SetProjectTagsResult> {

    @Nonnull
    private final TagsManager tagsManager;

    @Inject
    public SetProjectTagsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull TagsManager tagsManager) {
        super(accessManager);
        this.tagsManager = checkNotNull(tagsManager);
    }

    @Nonnull
    @Override
    public Class<SetProjectTagsAction> getActionClass() {
        return SetProjectTagsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_PROJECT_TAGS;
    }

    @Nonnull
    @Override
    public SetProjectTagsResult execute(@Nonnull SetProjectTagsAction action, @Nonnull ExecutionContext executionContext) {
        tagsManager.setProjectTags(action.getTagData());
        return new SetProjectTagsResult();
    }
}

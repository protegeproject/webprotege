package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsResult;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2018
 */
public class GetProjectTagsActionHandler extends AbstractProjectActionHandler<GetProjectTagsAction, GetProjectTagsResult> {

    @Nonnull
    private final TagsManager tagsManager;

    @Inject
    public GetProjectTagsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull TagsManager tagsManager) {
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
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetProjectTagsAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetProjectTagsResult execute(@Nonnull GetProjectTagsAction action, @Nonnull ExecutionContext executionContext) {
        Collection<Tag> projectTags = tagsManager.getProjectTags();
        Map<TagId, Integer> usage = new HashMap<>();
        // Disabled for now, because this is really expensive for large ontologies
//        projectTags.forEach(tag -> usage.put(tag.getTagId(),
//                                             tagsManager.getTaggedEntities(tag.getTagId()).size()));
        projectTags.forEach(tag -> usage.put(tag.getTagId(), 0));
        return new GetProjectTagsResult(projectTags, usage);
    }
}

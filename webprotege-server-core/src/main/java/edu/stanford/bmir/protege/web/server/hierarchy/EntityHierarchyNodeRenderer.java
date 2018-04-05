package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.tag.TagsManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 29 Nov 2017
 */
public class EntityHierarchyNodeRenderer {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RenderingManager renderingManager
            ;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private final WatchManager watchManager;

    @Nonnull
    private final EntityDiscussionThreadRepository discussionThreadRepository;

    @Nonnull
    private final TagsManager tagsManager;

    @Inject
    public EntityHierarchyNodeRenderer(@Nonnull ProjectId projectId,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                                       @Nonnull WatchManager watchManager,
                                       @Nonnull EntityDiscussionThreadRepository discussionThreadRepository,
                                       @Nonnull TagsManager tagsManager) {
        this.projectId = checkNotNull(projectId);
        this.renderingManager = checkNotNull(renderingManager);
        this.deprecatedEntityChecker = checkNotNull(deprecatedEntityChecker);
        this.watchManager = checkNotNull(watchManager);
        this.discussionThreadRepository = checkNotNull(discussionThreadRepository);
        this.tagsManager = checkNotNull(tagsManager);
    }

    /**
     * Renders the hierarchy node for the specified entity.
     * @param entity The entity to be rendered.
     * @return The hierarchy node for the specified entity.
     */
    @Nonnull
    public EntityHierarchyNode render(@Nonnull OWLEntity entity) {
        return new EntityHierarchyNode(
                entity,
                renderingManager.getShortForm(entity),
                deprecatedEntityChecker.isDeprecated(entity),
                watchManager.getDirectWatches(entity),
                discussionThreadRepository.getOpenCommentsCount(projectId, entity),
                tagsManager.getTags(entity));
    }
}

package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 29 Nov 2017
 */
public class OWLEntityRenderingGenerator {

    @Nonnull
    private RenderingManager renderingManager;

    @Nonnull
    private DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private WatchManager watchManager;

    @Nonnull
    private EntityDiscussionThreadRepository discussionThreadRepository;

    @Inject
    public OWLEntityRenderingGenerator(@Nonnull RenderingManager renderingManager, @Nonnull DeprecatedEntityChecker deprecatedEntityChecker, @Nonnull WatchManager watchManager, @Nonnull EntityDiscussionThreadRepository discussionThreadRepository) {
        this.renderingManager = renderingManager;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.watchManager = watchManager;
        this.discussionThreadRepository = discussionThreadRepository;
    }

    public EntityHierarchyNode render(@Nonnull OWLEntity entity,
                                      @Nonnull ProjectId projectId,
                                      @Nonnull UserId userId) {

        return new EntityHierarchyNode(
                entity,
                renderingManager.getRendering(entity).getBrowserText(),
                deprecatedEntityChecker.isDeprecated(entity),
                watchManager.getDirectWatches(entity, userId),
                discussionThreadRepository.getOpenCommentsCount(projectId, entity));
    }

    public EntityHierarchyNode render(@Nonnull OWLEntity entity,
                                      @Nonnull ProjectId projectId) {
        return this.render(entity, projectId, UserId.getGuest());
    }
}

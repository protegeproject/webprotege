package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.server.tag.EntityTagsManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
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
    private final BrowserTextProvider browserTextProvider;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private final WatchManager watchManager;

    @Nonnull
    private final EntityDiscussionThreadRepository discussionThreadRepository;

    @Nonnull
    private final EntityTagsManager entityTagsManager;

    @Inject
    public EntityHierarchyNodeRenderer(@Nonnull ProjectId projectId,
                                       @Nonnull BrowserTextProvider browserTextProvider,
                                       @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                                       @Nonnull WatchManager watchManager,
                                       @Nonnull EntityDiscussionThreadRepository discussionThreadRepository, @Nonnull EntityTagsManager entityTagsManager) {
        this.projectId = checkNotNull(projectId);
        this.browserTextProvider = checkNotNull(browserTextProvider);
        this.deprecatedEntityChecker = checkNotNull(deprecatedEntityChecker);
        this.watchManager = checkNotNull(watchManager);
        this.discussionThreadRepository = checkNotNull(discussionThreadRepository);
        this.entityTagsManager = checkNotNull(entityTagsManager);
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
                browserTextProvider.getOWLEntityBrowserText(entity).orElse(""),
                deprecatedEntityChecker.isDeprecated(entity),
                watchManager.getDirectWatches(entity),
                discussionThreadRepository.getOpenCommentsCount(projectId, entity),
                entityTagsManager.getTags(projectId, entity));
    }
}

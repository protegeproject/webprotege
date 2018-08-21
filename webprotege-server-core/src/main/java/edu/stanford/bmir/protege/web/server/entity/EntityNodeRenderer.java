package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.tag.TagsManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 29 Nov 2017
 */
public class EntityNodeRenderer {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private final WatchManager watchManager;

    @Nonnull
    private final EntityDiscussionThreadRepository discussionThreadRepository;

    @Nonnull
    private final TagsManager tagsManager;

    @Nonnull
    private final LanguageManager languageManager;

    @Inject
    public EntityNodeRenderer(@Nonnull ProjectId projectId,
                              @Nonnull DictionaryManager dictionaryManager,
                              @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                              @Nonnull WatchManager watchManager,
                              @Nonnull EntityDiscussionThreadRepository discussionThreadRepository,
                              @Nonnull TagsManager tagsManager, @Nonnull LanguageManager languageManager) {
        this.projectId = checkNotNull(projectId);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.deprecatedEntityChecker = checkNotNull(deprecatedEntityChecker);
        this.watchManager = checkNotNull(watchManager);
        this.discussionThreadRepository = checkNotNull(discussionThreadRepository);
        this.tagsManager = checkNotNull(tagsManager);
        this.languageManager = checkNotNull(languageManager);
    }

    /**
     * Renders the node for the specified entity.
     * @param entity The entity to be rendered.
     * @return The node for the specified entity.
     */
    @Nonnull
    public EntityNode render(@Nonnull OWLEntity entity) {
        return EntityNode.get(
                entity,
                dictionaryManager.getShortForm(entity, languageManager.getLanguages()),
                dictionaryManager.getShortForms(entity),
                deprecatedEntityChecker.isDeprecated(entity),
                watchManager.getDirectWatches(entity),
                discussionThreadRepository.getOpenCommentsCount(projectId, entity),
                tagsManager.getTags(entity));
    }
}

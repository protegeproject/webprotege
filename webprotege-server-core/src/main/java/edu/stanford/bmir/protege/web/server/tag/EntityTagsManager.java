package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2018
 */
public class EntityTagsManager {

    @Nonnull
    private final EntityTagsRepository repository;

    @Nonnull
    private final TagRepository tagRepository;

    @Inject
    public EntityTagsManager(@Nonnull EntityTagsRepository repository,
                             @Nonnull TagRepository tagRepository) {
        this.repository = checkNotNull(repository);
        this.tagRepository = checkNotNull(tagRepository);
    }

    /**
     * Gets the tags for the specified entity in the specified project.
     * @param projectId The project.
     * @param entity The entity.
     * @return The tags that tag the specified entity in the specified project.
     */
    @Nonnull
    public Collection<Tag> getTags(@Nonnull ProjectId projectId,
                                   @Nonnull OWLEntity entity) {
        checkNotNull(projectId);
        checkNotNull(entity);
        Map<TagId, Tag> tagsById = tagRepository.findTagsByProjectId(projectId).stream()
                                                .collect(toMap(Tag::getTagId, tag -> tag));
        return repository.findByEntity(projectId, entity).stream()
                         .flatMap(entityTag -> entityTag.getTags().stream())
                         .map(tagsById::get)
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());
    }

    /**
     * Gets the tags that are available for a given project.
     * @param projectId The project Id.
     */
    @Nonnull
    public Collection<Tag> getProjectTags(@Nonnull ProjectId projectId) {
        return tagRepository.findTagsByProjectId(projectId);
    }
}

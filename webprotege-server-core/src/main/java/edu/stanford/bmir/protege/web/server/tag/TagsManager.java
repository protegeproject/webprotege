package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.events.HasPostEvents;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.EntityTagsChangedEvent;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagData;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.tag.TagId.createTagId;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2018
 */
@ProjectSingleton
public class TagsManager {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityTagsRepository repository;

    @Nonnull
    private final TagRepository tagRepository;

    @Nonnull
    private final HasPostEvents<ProjectEvent<?>> eventBus;


    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Inject
    public TagsManager(@Nonnull ProjectId projectId,
                       @Nonnull EntityTagsRepository repository,
                       @Nonnull TagRepository tagRepository, @Nonnull HasPostEvents<ProjectEvent<?>> eventBus) {
        this.projectId = checkNotNull(projectId);
        this.repository = checkNotNull(repository);
        this.tagRepository = checkNotNull(tagRepository);
        this.eventBus = checkNotNull(eventBus);
    }

    /**
     * Gets the tags for the specified entity.
     *
     * @param entity The entity.
     * @return The tags that tag the specified entity.
     */
    @Nonnull
    public Collection<Tag> getTags(@Nonnull OWLEntity entity) {
        checkNotNull(projectId);
        checkNotNull(entity);
        try {
            readLock.lock();
            Map<TagId, Tag> tagsById = tagRepository.findTagsByProjectId(projectId).stream()
                                                    .collect(toMap(Tag::getTagId, tag -> tag));
            Optional<EntityTags> entityTags = repository.findByEntity(projectId, entity);
            if (!entityTags.isPresent()) {
                return Collections.emptySet();
            }
            return entityTags.get().getTags().stream()
                             .map(tagsById::get)
                             .filter(Objects::nonNull)
                             .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Gets the tags that are available for the project that this manager is
     * associated with.
     */
    @Nonnull
    public Collection<Tag> getProjectTags() {
        try {
            readLock.lock();
            return tagRepository.findTagsByProjectId(projectId);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Updates the entity tags for a given entity.  A diff will be performed to compute the changes required.
     *
     * @param entity     The entity.
     * @param fromTagIds The set of tags to update from.
     * @param toTagIds   The set of tags to update to.
     */
    public void updateTags(@Nonnull OWLEntity entity,
                              @Nonnull Set<TagId> fromTagIds,
                              @Nonnull Set<TagId> toTagIds) {
        try {
            writeLock.lock();
            Optional<EntityTags> existingTags = repository.findByEntity(projectId,
                                                                        entity);
            Set<TagId> nextTagIds = new HashSet<>();
            existingTags.ifPresent(entityTags -> nextTagIds.addAll(entityTags.getTags()));
            fromTagIds.stream()
                      .filter(tagId -> !toTagIds.contains(tagId))
                      .forEach(nextTagIds::remove);
            toTagIds.stream()
                    .filter(tagId -> !fromTagIds.contains(tagId))
                    .forEach(nextTagIds::add);
            EntityTags nextEntityTags = new EntityTags(projectId,
                                                       entity,
                                                       new ArrayList<>(nextTagIds));
            repository.save(nextEntityTags);
            boolean changed = !existingTags.equals(Optional.of(nextEntityTags));
            if(changed) {
                eventBus.postEvent(new EntityTagsChangedEvent(projectId,
                                                              entity,
                                                              getTags(entity)));
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void setProjectTags(@Nonnull List<TagData> projectTags) {
        try {
            checkNotNull(projectTags);
            writeLock.lock();
            List<Tag> tags = projectTags.stream()
                                           .map(tagData -> new Tag(
                                                   tagData.getTagId()
                                                          .orElse(createTagId()),
                                                   projectId,
                                                   tagData.getLabel(),
                                                   tagData.getDescription(),
                                                   tagData.getColor(),
                                                   tagData.getBackgroundColor()
                                           ))
                                           .collect(toList());
            tagRepository.saveTags(tags);
        } finally {
            writeLock.unlock();
        }

    }
}

package edu.stanford.bmir.protege.web.server.tag;

import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.events.HasPostEvents;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.tag.TagId.createTagId;
import static java.util.stream.Collectors.*;

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
    private final EntityTagsRepository entityTagsRepository;

    @Nonnull
    private final CriteriaBasedTagsManager criteriaBasedTagsManager;

    @Nonnull
    private final TagRepository tagRepository;

    @Nonnull
    private final HasPostEvents<ProjectEvent<?>> eventBus;


    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Nullable
    private Map<TagId, Tag> projectTags;

    @Inject
    public TagsManager(@Nonnull ProjectId projectId,
                       @Nonnull EntityTagsRepository entityTagsRepository,
                       @Nonnull CriteriaBasedTagsManager criteriaBasedTagsManager,
                       @Nonnull TagRepository tagRepository,
                       @Nonnull HasPostEvents<ProjectEvent<?>> eventBus) {
        this.projectId = checkNotNull(projectId);
        this.entityTagsRepository = checkNotNull(entityTagsRepository);
        this.criteriaBasedTagsManager = checkNotNull(criteriaBasedTagsManager);
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
            Map<TagId, Tag> tagsById = getProjectTagsByTagId();
            Optional<EntityTags> entityTags = entityTagsRepository.findByEntity(entity);
            Stream<TagId> explicitTags = entityTags.map(tags -> tags.getTags().stream())
                                                   .orElse(Stream.empty());

            Stream<TagId> criteriaBasedTags = criteriaBasedTagsManager.getTagsForEntity(entity);
            return Streams.concat(explicitTags, criteriaBasedTags)
                          .distinct()
                          .map(tagsById::get)
                          .filter(Objects::nonNull)
                          .collect(toList());
        } finally {
            readLock.unlock();
        }
    }

    @Nonnull
    private Map<TagId, Tag> getProjectTagsByTagId() {
        try {
            readLock.lock();
            if (projectTags == null) {
                projectTags = tagRepository.findTags().stream()
                                           .collect(toMap(Tag::getTagId, tag -> tag));
            }
            return projectTags;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Gets the entities that have been tagged with the specified tag.
     *
     * @param tagId The tag id.
     */
    @Nonnull
    public Collection<OWLEntity> getTaggedEntities(@Nonnull TagId tagId) {
        try {
            readLock.lock();

            Stream<OWLEntity> explicitTags = entityTagsRepository.findByTagId(tagId)
                                                                 .stream()
                                                                 .map(EntityTags::getEntity);
            Stream<OWLEntity> criteriaBasedTags = criteriaBasedTagsManager.getTaggedEntities(tagId);

            return Streams.concat(explicitTags, criteriaBasedTags)
                          .collect(toSet());
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
            return new ArrayList<>(getProjectTagsByTagId().values());
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Sets the tags for the project that this manager is associated with.
     */
    public void setProjectTags(@Nonnull Collection<TagData> newProjectTags) {
        checkNotNull(newProjectTags);
        Collection<Tag> currentProjectTags = getProjectTags();
        Set<OWLEntity> modifiedEntityTags = new HashSet<>();
        try {
            writeLock.lock();
            projectTags = null;
            Set<TagId> tagIds = newProjectTags.stream()
                                              .map(TagData::getTagId)
                                              .filter(Optional::isPresent)
                                              .map(Optional::get)
                                              .collect(toSet());
            // Remove current tags
            currentProjectTags.stream()
                              .map(Tag::getTagId)
                              .peek(tagId -> {
                                  if (!tagIds.contains(tagId)) {
                                      // Record modified entity tags
                                      entityTagsRepository.findByTagId(tagId).stream()
                                                          .map(EntityTags::getEntity)
                                                          .forEach(modifiedEntityTags::add);
                                      // Remove tag from entity
                                      entityTagsRepository.removeTag(tagId);
                                  }
                              })
                              .forEach(tagRepository::deleteTag);
            // Set fresh tags
            List<Tag> tags = newProjectTags.stream()
                                           .map(tagData -> Tag.get(
                                                   tagData.getTagId()
                                                          .orElse(createTagId()),
                                                   projectId,
                                                   tagData.getLabel(),
                                                   tagData.getDescription(),
                                                   tagData.getColor(),
                                                   tagData.getBackgroundColor(),
                                                   tagData.getCriteria()
                                           ))
                                           .peek(tag -> {
                                               // Find the modified entities for this tag
                                               entityTagsRepository.findByTagId(tag.getTagId()).stream()
                                                                   .map(EntityTags::getEntity)
                                                                   .forEach(modifiedEntityTags::add);
                                           })
                                           .collect(toList());
            tagRepository.saveTags(tags);
        } finally {
            writeLock.unlock();
        }
        Set<Tag> oldProjectTags = new HashSet<>(currentProjectTags);
        Set<Tag> projectTags = new HashSet<>(getProjectTags());
        if (!oldProjectTags.equals(projectTags)) {
            List<ProjectEvent<?>> events = new ArrayList<>();
            for (OWLEntity entity : modifiedEntityTags) {
                EntityTagsChangedEvent event = new EntityTagsChangedEvent(projectId, entity, getTags(entity));
                events.add(event);
            }
            events.add(new ProjectTagsChangedEvent(projectId, projectTags));
            eventBus.postEvents(events);
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
            Optional<EntityTags> existingTags = entityTagsRepository.findByEntity(
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
            entityTagsRepository.save(nextEntityTags);
            boolean changed = !existingTags.equals(Optional.of(nextEntityTags));
            if (changed) {
                eventBus.postEvent(new EntityTagsChangedEvent(projectId,
                                                              entity,
                                                              getTags(entity)));
            }
        } finally {
            writeLock.unlock();
        }
    }
}

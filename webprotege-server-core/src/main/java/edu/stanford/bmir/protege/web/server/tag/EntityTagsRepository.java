package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2018
 */
public interface EntityTagsRepository {

    void save(EntityTags tag);

    void addTag(ProjectId projectId, OWLEntity entity, TagId tagId);

    void removeTag(ProjectId projectId, OWLEntity entity, TagId tagId);

    void removeTag(ProjectId projectId, TagId tagId);

    Map<OWLEntity, EntityTags> findByProject(ProjectId projectId);

    Optional<EntityTags> findByEntity(ProjectId projectId, OWLEntity entity);

    Collection<EntityTags> findByTagId(TagId tagId);
}

package edu.stanford.bmir.protege.web.shared.entity;

import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Aug 2018
 */
public interface EntityNodeIndex {

    Optional<EntityNode> getNode(@Nonnull OWLEntity entity);

    void updateNode(@Nonnull EntityNode entityNode);
}

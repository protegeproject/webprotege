package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-27
 */
public interface BuiltInEntitiesIndex {

    @Nonnull
    Stream<OWLEntity> getBuiltInEntities();
}

package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-05
 */
public interface ProjectAxiomsSignatureIndex {

    @Nonnull
    <E extends OWLEntity> Stream<E> getProjectAxiomsSignature(@Nonnull EntityType<E> entityType);

}

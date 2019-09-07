package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.auto.value.AutoValue;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-07
 */
@AutoValue
public abstract class Key<T> {

    public static <T> Key<T> get(@Nonnull OWLOntologyID ontologyId,
                                 @Nonnull T value) {
        return new AutoValue_Key<>(ontologyId, value);
    }

    @Nonnull
    public abstract OWLOntologyID getOntologyId();

    @Nonnull
    public abstract T getValue();

}

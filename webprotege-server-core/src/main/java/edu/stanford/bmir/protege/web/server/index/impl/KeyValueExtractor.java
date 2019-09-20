package edu.stanford.bmir.protege.web.server.index.impl;


import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-07
 */
@FunctionalInterface
public interface KeyValueExtractor<V, A extends OWLAxiom> {

    @Nullable
    V extractValue(@Nonnull A axiom);
}

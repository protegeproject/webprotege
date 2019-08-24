package edu.stanford.bmir.protege.web.server.project;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 *
 * A lightweight ontology object that contains an id, annotations and axiom.
 */
@AutoValue
public abstract class Ontology {

    public static Ontology get(@Nonnull OWLOntologyID ontologyId,
                               @Nonnull Set<OWLAnnotation> ontologyAnnotations,
                               @Nonnull Set<OWLAxiom> ontologyAxioms) {
        return new AutoValue_Ontology(ontologyId, ImmutableSet.copyOf(ontologyAnnotations), ImmutableSet.copyOf(ontologyAxioms));
    }

    @Nonnull
    public abstract OWLOntologyID getOntologyId();

    @Nonnull
    public abstract ImmutableSet<OWLAnnotation> getAnnotations();

    @Nonnull
    public abstract ImmutableSet<OWLAxiom> getAxioms();
}

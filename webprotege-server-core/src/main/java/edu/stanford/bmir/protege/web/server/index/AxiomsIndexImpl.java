package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public class AxiomsIndexImpl implements AxiomsIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public AxiomsIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = ontologyIndex;
    }

    @Override
    public boolean containsAxiom(@Nonnull OWLAxiom axiom,
                                 @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(axiom);
        checkNotNull(ontologyId);
        return ontologyIndex.getOntology(ontologyId)
                .map(ont -> ont.containsAxiom(axiom))
                .orElse(false);
    }

    @Override
    public boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom,
                                                  @Nonnull OWLOntologyID ontologyId) {
        return containsAxiom(axiom, ontologyId)
                ||
                ontologyIndex.getOntology(ontologyId)
                .map(ont -> ont.containsAxiomIgnoreAnnotations(axiom))
                .orElse(false);
    }
}

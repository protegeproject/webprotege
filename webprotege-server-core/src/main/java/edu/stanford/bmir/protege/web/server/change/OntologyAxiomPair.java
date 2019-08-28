package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.value.AutoValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Oct 2018
 */
@AutoValue
public abstract class OntologyAxiomPair {


    public static OntologyAxiomPair get(@Nonnull OWLOntologyID ontology,
                                        @Nonnull OWLAxiom axiom) {
        return new AutoValue_OntologyAxiomPair(ontology, axiom);
    }

    @Nonnull
    public abstract OWLOntologyID getOntology();

    @Nonnull
    public abstract OWLAxiom getAxiom();
}

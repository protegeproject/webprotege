package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public class SubDataPropertyAxiomsBySubPropertyIndexImpl implements SubDataPropertyAxiomsBySubPropertyIndex {

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public SubDataPropertyAxiomsBySubPropertyIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = ontologyIndex;
    }

    @Nonnull
    @Override
    public Stream<OWLSubDataPropertyOfAxiom> getSubPropertyOfAxioms(@Nonnull OWLDataProperty dataProperty,
                                                                    @Nonnull OWLOntologyID ontologyID) {
        checkNotNull(ontologyID);
        checkNotNull(dataProperty);
        return ontologyIndex.getOntology(ontologyID)
                .stream()
                .flatMap(ont -> ont.getDataSubPropertyAxiomsForSubProperty(dataProperty).stream());
    }
}

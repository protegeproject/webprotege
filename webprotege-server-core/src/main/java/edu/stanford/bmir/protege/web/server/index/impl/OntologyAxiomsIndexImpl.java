package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsIndex;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
public class OntologyAxiomsIndexImpl implements OntologyAxiomsIndex {

    @Nonnull
    private final AxiomsByTypeIndexImpl axiomsByTypeIndex;

    @Inject
    public OntologyAxiomsIndexImpl(@Nonnull AxiomsByTypeIndexImpl axiomsByTypeIndex) {
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLAxiom> getAxioms(@Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        var streamsBuilder = Stream.<Stream<? extends OWLAxiom>>builder();
        AxiomType.AXIOM_TYPES.forEach(axiomType -> {
            streamsBuilder.add(axiomsByTypeIndex.getAxiomsByType(axiomType, ontologyId));
        });
        return streamsBuilder.build().flatMap(s -> s);
    }

    @Override
    public boolean containsAxiom(@Nonnull OWLAxiom axiom,
                                 @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(axiom);
        checkNotNull(ontologyId);
        return axiomsByTypeIndex.containsAxiom(axiom, ontologyId);
    }

    @Override
    public boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom,
                                                  @Nonnull OWLOntologyID ontologyId) {
        return containsAxiom(axiom, ontologyId);
    }
}

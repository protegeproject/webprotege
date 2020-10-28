package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.Index;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@ProjectSingleton
public class OntologyAxiomsIndexImpl implements OntologyAxiomsIndex, DependentIndex {

    @Nonnull
    private final AxiomsByTypeIndexImpl axiomsByTypeIndex;

    @Inject
    public OntologyAxiomsIndexImpl(@Nonnull AxiomsByTypeIndexImpl axiomsByTypeIndex) {
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(axiomsByTypeIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLAxiom> getAxioms(@Nonnull OntologyDocumentId ontologyDocumentId) {
        checkNotNull(ontologyDocumentId);
        var streamsBuilder = Stream.<Stream<? extends OWLAxiom>>builder();
        AxiomType.AXIOM_TYPES.forEach(axiomType -> {
            streamsBuilder.add(axiomsByTypeIndex.getAxiomsByType(axiomType, ontologyDocumentId));
        });
        return streamsBuilder.build().flatMap(s -> s);
    }

    @Override
    public boolean containsAxiom(@Nonnull OWLAxiom axiom,
                                 @Nonnull OntologyDocumentId ontologyDocumentId) {
        checkNotNull(axiom);
        checkNotNull(ontologyDocumentId);
        return axiomsByTypeIndex.containsAxiom(axiom, ontologyDocumentId);
    }

    @Override
    public boolean containsAxiomIgnoreAnnotations(@Nonnull OWLAxiom axiom,
                                                  @Nonnull OntologyDocumentId ontologyDocumentId) {
        return containsAxiom(axiom, ontologyDocumentId);
    }
}

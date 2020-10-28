package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.DifferentIndividualsAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
@ProjectSingleton
public class DifferentIndividualsAxiomsIndexImpl implements DifferentIndividualsAxiomsIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLDifferentIndividualsAxiom> index;

    @Inject
    public DifferentIndividualsAxiomsIndexImpl() {
        this.index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(
                OWLDifferentIndividualsAxiom.class,
                OWLDifferentIndividualsAxiom::getIndividuals);
    }

    @Nonnull
    @Override
    public Stream<OWLDifferentIndividualsAxiom> getDifferentIndividualsAxioms(@Nonnull OWLIndividual individual,
                                                                              @Nonnull OntologyDocumentId ontologyDocumentId) {
        checkNotNull(ontologyDocumentId);
        checkNotNull(individual);
        return index.getAxioms(individual, ontologyDocumentId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}

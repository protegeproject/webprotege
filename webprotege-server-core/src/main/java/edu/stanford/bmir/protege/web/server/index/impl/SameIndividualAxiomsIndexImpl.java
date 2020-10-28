package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.SameIndividualAxiomsIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
@ProjectSingleton
public class SameIndividualAxiomsIndexImpl implements SameIndividualAxiomsIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLSameIndividualAxiom> index;

    @Inject
    public SameIndividualAxiomsIndexImpl() {
        index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(OWLSameIndividualAxiom.class,
                                                                   OWLSameIndividualAxiom::getIndividuals);
    }

    @Nonnull
    @Override
    public Stream<OWLSameIndividualAxiom> getSameIndividualAxioms(@Nonnull OWLIndividual individual,
                                                                  @Nonnull OntologyDocumentId ontologyDocumentId) {
        checkNotNull(individual);
        checkNotNull(ontologyDocumentId);
        return index.getAxioms(individual, ontologyDocumentId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}

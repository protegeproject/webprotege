package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.DataPropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
@ProjectSingleton
public class DataPropertyAssertionAxiomsBySubjectIndexImpl implements DataPropertyAssertionAxiomsBySubjectIndex,
        UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLDataPropertyAssertionAxiom> index;

    @Inject
    public DataPropertyAssertionAxiomsBySubjectIndexImpl() {
        index = AxiomMultimapIndex.create(OWLDataPropertyAssertionAxiom.class,
                                          OWLDataPropertyAssertionAxiom::getSubject);
    }

    @Nonnull
    @Override
    public Stream<OWLDataPropertyAssertionAxiom> getDataPropertyAssertions(@Nonnull OWLIndividual individual,
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

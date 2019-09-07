package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.DataPropertyAssertionAxiomsBySubjectIndex;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
public class DataPropertyAssertionAxiomsBySubjectIndexImpl implements DataPropertyAssertionAxiomsBySubjectIndex,
RequiresOntologyChangeNotification {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLDataPropertyAssertionAxiom> index;

    @Inject
    public DataPropertyAssertionAxiomsBySubjectIndexImpl() {
        index = new AxiomMultimapIndex<>(OWLDataPropertyAssertionAxiom.class,
                                         OWLDataPropertyAssertionAxiom::getSubject,
                                         MultimapBuilder.hashKeys().arrayListValues().build());
    }

    @Nonnull
    @Override
    public Stream<OWLDataPropertyAssertionAxiom> getDataPropertyAssertions(@Nonnull OWLIndividual individual,
                                                                           @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(individual);
        checkNotNull(ontologyId);
        return index.getAxioms(individual, ontologyId);
    }

    @Override
    public void handleOntologyChanges(@Nonnull List<OntologyChange> changes) {
        index.handleOntologyChanges(changes);
    }
}

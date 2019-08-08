package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@ProjectSingleton
public class AxiomsByTypeIndexImpl implements AxiomsByTypeIndex {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Nonnull
    private final OntologyIndex ontologyIndex;

    @Inject
    public AxiomsByTypeIndexImpl(@Nonnull OntologyIndex ontologyIndex) {
        this.ontologyIndex = checkNotNull(ontologyIndex);
    }

    @Override
    public <T extends OWLAxiom> Stream<T> getAxiomsByType(AxiomType<T> axiomType,
                                                          OWLOntologyID ontologyId) {
        checkNotNull(axiomType);
        checkNotNull(ontologyId);
        try {
            writeLock.lock();
            return ontologyIndex.getOntology(ontologyId)
                    .stream()
                    .flatMap(ont -> ont.getAxioms(axiomType).stream());
        } finally {
            writeLock.unlock();
        }
    }
}

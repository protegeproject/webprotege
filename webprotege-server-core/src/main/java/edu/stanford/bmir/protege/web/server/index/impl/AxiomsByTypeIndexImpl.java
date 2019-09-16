package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import edu.stanford.bmir.protege.web.server.change.AxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
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
public class AxiomsByTypeIndexImpl implements AxiomsByTypeIndex, UpdatableIndex {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock writeLock = readWriteLock.writeLock();

    /**
     * A list of ontology to axiom maps.  Each item in the list corresponds to a particular
     * axiom type – the index is equal to the axiom type index.
     */
    @Nonnull
    private final ImmutableList<SetMultimap<OWLOntologyID, OWLAxiom>> axiomTypeList;

    private AxiomChangeHandler axiomChangeHandler = new AxiomChangeHandler();

    @Inject
    public AxiomsByTypeIndexImpl() {
        var listBuilder = ImmutableList.<SetMultimap<OWLOntologyID, OWLAxiom>>builder();
        AxiomType.AXIOM_TYPES.forEach(
                axiomType -> {
                    var axiomsByOntologyMap = MultimapBuilder.hashKeys(1)
                                                             .hashSetValues()
                            .<OWLOntologyID, OWLAxiom>build();
                    listBuilder.add(axiomsByOntologyMap);
                }
        );
        axiomTypeList = listBuilder.build();
        axiomChangeHandler.setAxiomChangeConsumer(this::handleAxiomChange);
    }

    private void handleAxiomChange(AxiomChange change) {
        var ontId = change.getOntologyId();
        var axiom = change.getAxiom();
        var axiomType = axiom.getAxiomType();
        var ont2Axioms = getAxiomsByOntologyMap(axiomType);
        if(change.isAddAxiom()) {
            ont2Axioms.put(ontId, axiom);
        }
        else {
            ont2Axioms.remove(ontId, axiom);
        }
    }

    private SetMultimap<OWLOntologyID, OWLAxiom> getAxiomsByOntologyMap(AxiomType<? extends OWLAxiom> axiomType) {
        return axiomTypeList.get(axiomType.getIndex());
    }

    @Override
    public synchronized void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        try {
            writeLock.lock();
            axiomChangeHandler.handleOntologyChanges(changes);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean containsAxiom(@Nonnull OWLAxiom axiom,
                                 @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(axiom);
        checkNotNull(ontologyId);
        try {
            readWriteLock.readLock()
                         .lock();
            var ont2Axioms = getAxiomsByOntologyMap(axiom.getAxiomType());
            return ont2Axioms.containsEntry(ontologyId, axiom);
        } finally {
            readWriteLock.readLock()
                         .unlock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends OWLAxiom> Stream<T> getAxiomsByType(AxiomType<T> axiomType,
                                                          OWLOntologyID ontologyId) {
        checkNotNull(axiomType);
        checkNotNull(ontologyId);
        try {
            readWriteLock.readLock()
                         .lock();
            var ont2Axioms = getAxiomsByOntologyMap(axiomType);
            var axioms = (Collection<T>) ont2Axioms.get(ontologyId);
            return ImmutableList.copyOf(axioms).stream();
        } finally {
            readWriteLock.readLock()
                         .unlock();
        }
    }
}

package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.auto.value.AutoValue;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.change.AxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.List;
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
public class AxiomsByTypeIndexImpl implements AxiomsByTypeIndex, RequiresOntologyChangeNotification {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private Multimap<Key, OWLAxiom> index;

    private AxiomChangeHandler axiomChangeHandler = new AxiomChangeHandler();

    @Inject
    public AxiomsByTypeIndexImpl() {
        index = HashMultimap.create();
        axiomChangeHandler.setAxiomChangeConsumer(this::handleAxiomChange);
    }

    private void handleAxiomChange(AxiomChange change) {
        var ontId = change.getOntologyId();
        var axiom = change.getAxiom();
        var axiomType = axiom.getAxiomType();
        var key = Key.get(ontId, axiomType);
        try {
            writeLock.lock();
            if(change.isAddAxiom()) {
                index.put(key, axiom);
            }
            else {
                index.remove(key, axiom);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends OWLAxiom> Stream<T> getAxiomsByType(AxiomType<T> axiomType,
                                                          OWLOntologyID ontologyId) {
        checkNotNull(axiomType);
        checkNotNull(ontologyId);
        var key = Key.get(ontologyId, axiomType);
        try {
            readWriteLock.readLock()
                         .lock();
            return ImmutableList.copyOf((Collection<T>) index.get(key))
                                .stream();
        } finally {
            readWriteLock.readLock()
                         .unlock();
        }
    }

    public boolean containsAxiom(@Nonnull OWLAxiom axiom,
                                 @Nonnull OWLOntologyID ontologyId) {
        try {
            readWriteLock.readLock().lock();
            var key = Key.get(ontologyId, axiom.getAxiomType());
            return index.containsEntry(key, axiom);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void applyChanges(@Nonnull List<OntologyChange> changes) {
        axiomChangeHandler.handleOntologyChanges(changes);
    }

    @AutoValue
    protected abstract static class Key {

        public static Key get(@Nonnull OWLOntologyID ontologyID,
                              @Nonnull AxiomType axiomType) {
            return new AutoValue_AxiomsByTypeIndexImpl_Key(ontologyID, axiomType);
        }

        public abstract OWLOntologyID getOntologyId();

        public abstract AxiomType getAxiomType();
    }
}

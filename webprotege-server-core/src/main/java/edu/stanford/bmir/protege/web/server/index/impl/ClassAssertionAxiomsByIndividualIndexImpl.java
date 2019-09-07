package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.AxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByIndividualIndex;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public class ClassAssertionAxiomsByIndividualIndexImpl implements ClassAssertionAxiomsByIndividualIndex, RequiresOntologyChangeNotification {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final AxiomChangeHandler axiomChangeHandler = new AxiomChangeHandler();

    private final Multimap<Key, OWLClassAssertionAxiom> index = MultimapBuilder.hashKeys()
                                                                               .arrayListValues()
                                                                               .build();

    @Inject
    public ClassAssertionAxiomsByIndividualIndexImpl() {
        axiomChangeHandler.setAxiomChangeConsumer(this::handleOntologyChange);
    }

    @Override
    public Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLIndividual individual,
                                                                  @Nonnull OWLOntologyID ontologyID) {
        checkNotNull(individual);
        checkNotNull(ontologyID);
        var key = Key.get(ontologyID, individual);
        try {
            readLock.lock();
            return ImmutableList.copyOf(index.get(key)).stream();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void handleOntologyChanges(@Nonnull List<OntologyChange> changes) {
        try {
            writeLock.lock();
            axiomChangeHandler.handleOntologyChanges(changes);
        } finally {
            writeLock.unlock();
        }
    }

    private void handleOntologyChange(@Nonnull AxiomChange change) {
        var axiom = change.getAxiom();
        if(!(axiom instanceof OWLClassAssertionAxiom)) {
            return;
        }
        var classAssertionAxiom = (OWLClassAssertionAxiom) axiom;
        var key = Key.get(change.getOntologyId(),
                          classAssertionAxiom.getIndividual());
        if(change.isAddAxiom()) {
            index.put(key, classAssertionAxiom);
        }
        else {
            index.remove(key, classAssertionAxiom);
        }
    }


    @AutoValue
    public static abstract class Key {

        public static Key get(@Nonnull OWLOntologyID ontologyId,
                              @Nonnull OWLIndividual individual) {
            return new AutoValue_ClassAssertionAxiomsByIndividualIndexImpl_Key(ontologyId,
                                                                               individual);
        }

        public abstract OWLOntologyID getOntologyId();

        public abstract OWLIndividual getIndividual();
    }
}

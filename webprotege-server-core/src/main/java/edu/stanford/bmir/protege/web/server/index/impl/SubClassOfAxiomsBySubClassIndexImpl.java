package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.AxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.SubClassOfAxiomsBySubClassIndex;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

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
 * 2019-08-09
 */
public class SubClassOfAxiomsBySubClassIndexImpl implements SubClassOfAxiomsBySubClassIndex, RequiresOntologyChangeNotification {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final AxiomChangeHandler axiomChangeHandler = new AxiomChangeHandler();

    @Nonnull
    private final Multimap<Key, OWLSubClassOfAxiom> index = MultimapBuilder.hashKeys()
                                                  .arrayListValues(1)
                                                  .build();

    @Inject
    public SubClassOfAxiomsBySubClassIndexImpl() {
        axiomChangeHandler.setAxiomChangeConsumer(this::handleOntologyChange);
    }

    @Override
    public Stream<OWLSubClassOfAxiom> getSubClassOfAxiomsForSubClass(@Nonnull OWLClass subClass,
                                                                     @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(subClass);
        checkNotNull(ontologyId);
        var key = Key.get(ontologyId, subClass);
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
            Stats.dump("SubClassOfAxiomsBySubClassIndex", index, System.out);
        } finally {
            writeLock.unlock();
        }
    }

    private void handleOntologyChange(@Nonnull AxiomChange change) {
        var axiom = change.getAxiom();
        if(!(axiom instanceof OWLSubClassOfAxiom)) {
            return;
        }
        var subClassOfAxiom = (OWLSubClassOfAxiom) axiom;
        var subClassExpr = subClassOfAxiom.getSubClass();
        if(subClassExpr.isAnonymous()) {
            return;
        }
        var subCls = subClassExpr.asOWLClass();
        var key = Key.get(change.getOntologyId(), subCls);
        if(change.isAddAxiom()) {
            index.put(key, subClassOfAxiom);
        }
        else {
            index.remove(key, subClassOfAxiom);
        }
    }

    @AutoValue
    public static abstract class Key {

        public static Key get(@Nonnull OWLOntologyID ontologyId,
                              @Nonnull OWLClass cls) {
            return new AutoValue_SubClassOfAxiomsBySubClassIndexImpl_Key(ontologyId, cls);
        }

        public abstract OWLOntologyID getOntologyId();

        public abstract OWLClass getCls();
    }
}

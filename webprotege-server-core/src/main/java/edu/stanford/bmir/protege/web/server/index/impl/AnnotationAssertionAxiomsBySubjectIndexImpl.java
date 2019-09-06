package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.auto.value.AutoValue;
import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
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
 * 2019-08-09
 */
public class AnnotationAssertionAxiomsBySubjectIndexImpl implements AnnotationAssertionAxiomsBySubjectIndex, RequiresOntologyChangeNotification {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final Multimap<Key, OWLAnnotationAssertionAxiom> index = ArrayListMultimap.create(1000, 4);

    private final AxiomChangeHandler changeHandler = new AxiomChangeHandler();

    @Inject
    public AnnotationAssertionAxiomsBySubjectIndexImpl() {
        changeHandler.setAxiomChangeConsumer(this::handleChange);
    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAxiomsForSubject(@Nonnull OWLAnnotationSubject subject,
                                                                   @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(subject);
        checkNotNull(ontologyId);
        try {
            readLock.lock();
            var key = Key.get(ontologyId, subject);
            return ImmutableList.copyOf(index.get(key)).stream();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void handleOntologyChanges(@Nonnull List<OntologyChange> changes) {
        try {
            writeLock.lock();
            changeHandler.handleOntologyChanges(changes);
        } finally {
            writeLock.unlock();
        }
    }

    private void handleChange(@Nonnull AxiomChange axiomChange) {
        var axiom = axiomChange.getAxiom();
        if(!(axiom instanceof OWLAnnotationAssertionAxiom)) {
            return;
        }
        var annotationAssertionAxiom = (OWLAnnotationAssertionAxiom) axiom;
        var key = Key.get(axiomChange.getOntologyId(),
                          annotationAssertionAxiom.getSubject());
        if(axiomChange.isAddAxiom()) {
            index.put(key, annotationAssertionAxiom);
        }
        else {
            index.remove(key, annotationAssertionAxiom);
        }
    }

    @AutoValue
    public static abstract class Key {

        public static Key get(@Nonnull OWLOntologyID ontologyId,
                              @Nonnull OWLAnnotationSubject subject) {
            return new AutoValue_AnnotationAssertionAxiomsBySubjectIndexImpl_Key(ontologyId, subject);
        }

        public abstract OWLOntologyID getOntologyId();

        public abstract OWLAnnotationSubject getAnnotationSubject();

    }
}

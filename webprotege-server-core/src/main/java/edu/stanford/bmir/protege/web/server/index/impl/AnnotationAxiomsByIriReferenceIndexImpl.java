package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAxiomsByIriReferenceIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

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
 * 2019-08-07
 */
@ProjectSingleton
public class AnnotationAxiomsByIriReferenceIndexImpl implements AnnotationAxiomsByIriReferenceIndex, RequiresOntologyChangeNotification {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final Multimap<Key, OWLAnnotationAxiom> cache = ArrayListMultimap.create();

    @Inject
    public AnnotationAxiomsByIriReferenceIndexImpl() {
    }

    public void applyChanges(@Nonnull List<OntologyChange> changes) {
        try {
            writeLock.lock();
            changes.stream()
                   .filter(OntologyChange::isAxiomChange)
                   .filter(chg -> chg.getAxiomOrThrow() instanceof OWLAnnotationAxiom)
                   .forEach(chg -> {
                       var ontologyId = chg.getOntologyId();
                       if(chg.isAddAxiom()) {
                           handleAddAxiomChange(chg.getAxiomOrThrow(), ontologyId);
                       }
                       else {
                           handleRemoveAxiomChange(chg.getAxiomOrThrow(), ontologyId);
                       }
                   });
        } finally {
            writeLock.unlock();
        }
    }

    private void addAnnotationAssertionAxiom(OWLAnnotationAssertionAxiom ax, OWLOntologyID ontologyId) {
        if(ax.getSubject() instanceof IRI) {
            var key = Key.get(ontologyId, (IRI) ax.getSubject());
            addToIndex(key, ax);
        }
        if(ax.getValue() instanceof IRI) {
            var key = Key.get(ontologyId, (IRI) ax.getValue());
            addToIndex(key, ax);
        }
    }

    private void addAnnotationPropertyDomainAxiom(OWLAnnotationPropertyDomainAxiom ax, OWLOntologyID ontologyId) {
        var key = Key.get(ontologyId, ax.getDomain());
        addToIndex(key, ax);
    }

    private void remove(OWLAnnotationAssertionAxiom ax, OWLOntologyID ontologyId) {
        if(ax.getSubject() instanceof IRI) {
            var key = Key.get(ontologyId, (IRI) ax.getSubject());
            removeFromIndex(key, ax);
        }
        if(ax.getValue() instanceof IRI) {
            var key = Key.get(ontologyId, (IRI) ax.getValue());
            removeFromIndex(key, ax);
        }
    }

    private void remove(OWLAnnotationPropertyDomainAxiom ax, OWLOntologyID ontologyId) {
        var key = Key.get(ontologyId, ax.getDomain());
        removeFromIndex(key, ax);
    }

    private void remove(OWLAnnotationPropertyRangeAxiom ax, OWLOntologyID ontologyId) {
        var key = Key.get(ontologyId, ax.getRange());
        removeFromIndex(key, ax);
    }

    private void addAnnotationPropertyRangeAxiom(OWLAnnotationPropertyRangeAxiom ax, OWLOntologyID ontologyId) {
        var key = Key.get(ontologyId, ax.getRange());
        addToIndex(key, ax);
    }

    private void addToIndex(Key key, OWLAnnotationAxiom ax) {
        cache.put(key, ax);
        indexAgainstAnnotations(key.getOntologyId(), ax, ax);
    }

    private void removeFromIndex(Key key, OWLAnnotationAxiom ax) {
        cache.remove(key, ax);
        clearIndexAgainstAnnotations(key.getOntologyId(), ax, ax);
    }


    private void indexAgainstAnnotations(OWLOntologyID ontologyId,
                                         OWLAnnotationAxiom rootAxiom,
                                         HasAnnotations hasAnnotations) {
        for(OWLAnnotation annotation : hasAnnotations.getAnnotations()) {
            if(annotation.getValue() instanceof IRI) {
                var iri = (IRI) annotation.getValue();
                cache.put(Key.get(ontologyId, iri), rootAxiom);
            }
            indexAgainstAnnotations(ontologyId, rootAxiom, annotation);
        }
    }

    private void clearIndexAgainstAnnotations(OWLOntologyID ontologyId,
                                              OWLAnnotationAxiom rootAxiom,
                                              HasAnnotations hasAnnotations) {
        for(OWLAnnotation annotation : hasAnnotations.getAnnotations()) {
            if(annotation.getValue() instanceof IRI) {
                IRI iri = (IRI) annotation.getValue();
                cache.remove(Key.get(ontologyId, iri), rootAxiom);
            }
            clearIndexAgainstAnnotations(ontologyId, rootAxiom, annotation);
        }
    }


    @Override
    public Stream<OWLAnnotationAxiom> getReferencingAxioms(@Nonnull IRI iri,
                                                           @Nonnull OWLOntologyID ontologyID) {
        var key = Key.get(ontologyID, iri);
        try {
            readLock.lock();
            return ImmutableList.copyOf(cache.get(key)).stream();
        } finally {
            readLock.unlock();
        }
    }


    public void handleAddAxiomChange(OWLAxiom axiom, OWLOntologyID ontologyID) throws RuntimeException {
        axiom.accept(new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(OWLAnnotationAssertionAxiom axiom) {
                addAnnotationAssertionAxiom(axiom, ontologyID);
            }

            @Override
            public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
                addAnnotationPropertyDomainAxiom(axiom, ontologyID);
            }

            @Override
            public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
                addAnnotationPropertyRangeAxiom(axiom, ontologyID);
            }
        });
    }


    private void handleRemoveAxiomChange(OWLAxiom axiom, OWLOntologyID ontologyID) {
        axiom.accept(new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(OWLAnnotationAssertionAxiom axiom) {
                remove(axiom, ontologyID);
            }

            @Override
            public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
                remove(axiom, ontologyID);
            }

            @Override
            public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
                remove(axiom, ontologyID);
            }
        });
    }


    @AutoValue
    public static abstract class Key {

        public static Key get(@Nonnull OWLOntologyID ontologyId,
                              @Nonnull IRI iri) {
            return new AutoValue_AnnotationAxiomsByIriReferenceIndexImpl_Key(ontologyId, iri);
        }

        public abstract OWLOntologyID getOntologyId();
        public abstract IRI getIri();
    }
}

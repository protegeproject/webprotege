package edu.stanford.bmir.protege.web.server.index;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final Map<OWLOntologyID, Multimap<IRI, OWLAnnotationAxiom>> cache = new HashMap<>();

    private boolean loaded = false;

    @Inject
    public AnnotationAxiomsByIriReferenceIndexImpl() {
    }


    /**
     * Loads the specified ontologies into this index.
     *
     * @param ontologies        A stream of the ontologies to index.
     * @param axiomsByTypeIndex A helper index for retrieving axioms by type
     */
    public void load(@Nonnull Stream<OWLOntologyID> ontologies,
                     @Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        checkNotNull(ontologies);
        checkNotNull(axiomsByTypeIndex);
        try {
            writeLock.lock();
            if(loaded) {
                return;
            }
            loaded = true;
            ontologies.forEach(ontologyId -> load(ontologyId, axiomsByTypeIndex));

        } finally {
            writeLock.unlock();
        }
    }

    private void load(@Nonnull OWLOntologyID ontologyId, @Nonnull AxiomsByTypeIndex axiomsByTypeIndex) {
        var index = getIndexForOntology(ontologyId);
        axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_ASSERTION, ontologyId)
                         .forEach(ax -> add(ax, index));
        axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_PROPERTY_DOMAIN, ontologyId)
                         .forEach(ax -> add(ax, index));
        axiomsByTypeIndex.getAxiomsByType(AxiomType.ANNOTATION_PROPERTY_RANGE, ontologyId)
                         .forEach(ax -> add(ax, index));
    }

    public void handleOntologyChanges(@Nonnull List<OntologyChange> changes) {
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

    private Multimap<IRI, OWLAnnotationAxiom> getIndexForOntology(@Nonnull OWLOntologyID ontologyId) {
        var index = cache.get(ontologyId);
        if(index == null) {
            index = HashMultimap.create();
            cache.put(ontologyId, index);
        }
        return index;
    }

    private void add(OWLAnnotationAssertionAxiom ax, Multimap<IRI, OWLAnnotationAxiom> index) {
        if(ax.getSubject() instanceof IRI) {
            index.put((IRI) ax.getSubject(), ax);
        }
        if(ax.getValue() instanceof IRI) {
            index.put((IRI) ax.getValue(), ax);
        }
        indexAgainstAnnotations(ax, ax, index);
    }

    private void remove(OWLAnnotationAssertionAxiom ax, Multimap<IRI, OWLAnnotationAxiom> index) {
        if(ax.getSubject() instanceof IRI) {
            index.remove(ax.getSubject(), ax);
        }
        if(ax.getValue() instanceof IRI) {
            index.remove(ax.getValue(), ax);
        }
        clearIndexAgainstAnnotations(ax, ax, index);
    }

    private void add(OWLAnnotationPropertyDomainAxiom ax, Multimap<IRI, OWLAnnotationAxiom> index) {
        index.put(ax.getDomain(), ax);
        indexAgainstAnnotations(ax, ax, index);
    }

    private void remove(OWLAnnotationPropertyDomainAxiom ax, Multimap<IRI, OWLAnnotationAxiom> index) {
        index.remove(ax.getDomain(), ax);
        clearIndexAgainstAnnotations(ax, ax, index);
    }


    private void add(OWLAnnotationPropertyRangeAxiom ax, Multimap<IRI, OWLAnnotationAxiom> index) {
        index.put(ax.getRange(), ax);
        indexAgainstAnnotations(ax, ax, index);
    }

    private void remove(OWLAnnotationPropertyRangeAxiom ax, Multimap<IRI, OWLAnnotationAxiom> index) {
        index.remove(ax.getRange(), ax);
        clearIndexAgainstAnnotations(ax, ax, index);
    }

    private void indexAgainstAnnotations(OWLAnnotationAxiom rootAxiom,
                                         HasAnnotations hasAnnotations,
                                         Multimap<IRI, OWLAnnotationAxiom> index) {
        for(OWLAnnotation annotation : hasAnnotations.getAnnotations()) {
            if(annotation.getValue() instanceof IRI) {
                index.put((IRI) annotation.getValue(), rootAxiom);
            }
            indexAgainstAnnotations(rootAxiom, annotation, index);
        }
    }

    private void clearIndexAgainstAnnotations(OWLAnnotationAxiom rootAxiom,
                                              HasAnnotations hasAnnotations,
                                              Multimap<IRI, OWLAnnotationAxiom> index) {
        for(OWLAnnotation annotation : hasAnnotations.getAnnotations()) {
            if(annotation.getValue() instanceof IRI) {
                index.remove(annotation.getValue(), rootAxiom);
            }
            clearIndexAgainstAnnotations(rootAxiom, annotation, index);
        }
    }


    @Override
    public Stream<OWLAnnotationAxiom> getReferencingAxioms(@Nonnull IRI iri,
                                                           @Nonnull OWLOntologyID ontologyID) {
        try {
            readLock.lock();
            Multimap<IRI, OWLAnnotationAxiom> index = cache.get(ontologyID);
            if(index == null) {
                return Stream.empty();
            }
            else {
                var axioms = index.get(iri);
                return ImmutableList.copyOf(axioms)
                                    .stream();
            }
        } finally {
            readLock.unlock();
        }
    }


    public void handleAddAxiomChange(OWLAxiom axiom, OWLOntologyID ontologyID) throws RuntimeException {
        axiom.accept(new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(OWLAnnotationAssertionAxiom axiom) {
                add(axiom, getIndexForOntology(ontologyID));
            }

            @Override
            public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
                add(axiom, getIndexForOntology(ontologyID));
            }

            @Override
            public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
                add(axiom, getIndexForOntology(ontologyID));
            }
        });
    }


    private void handleRemoveAxiomChange(OWLAxiom axiom, OWLOntologyID ontologyID) {
        axiom.accept(new OWLAxiomVisitorAdapter() {
            @Override
            public void visit(OWLAnnotationAssertionAxiom axiom) {
                remove(axiom, getIndexForOntology(ontologyID));
            }

            @Override
            public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
                remove(axiom, getIndexForOntology(ontologyID));
            }

            @Override
            public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
                remove(axiom, getIndexForOntology(ontologyID));
            }
        });
    }
}

package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/05/2012
 * <p>
 * This class is thread safe.
 * </p>
 */
public class WebProtegeBidirectionalShortFormProvider implements BidirectionalShortFormProvider {

    private OWLOntology rootOntology;

    private BidirectionalShortFormProviderAdapterEx delegate;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Inject
    public WebProtegeBidirectionalShortFormProvider(@RootOntology OWLOntology rootOntology, ShortFormProvider shortFormProvider) {
        this.rootOntology = rootOntology;
        final Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
        delegate = new BidirectionalShortFormProviderAdapterEx(importsClosure, shortFormProvider);
        setupBuiltinObjectRenderings(rootOntology);

        OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
        manager.addOntologyChangeListener(new OWLOntologyChangeListener() {
            public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
                updateRenderings(changes);
            }
        });
    }

    private void setupBuiltinObjectRenderings(OWLOntology rootOntology) {
        OWLDataFactory df = rootOntology.getOWLOntologyManager().getOWLDataFactory();
        for(IRI iri : OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS) {
            delegate.add(df.getOWLAnnotationProperty(iri));
        }

        delegate.add(df.getOWLThing());
        delegate.add(df.getOWLNothing());

        delegate.add(df.getOWLTopObjectProperty());
        delegate.add(df.getOWLBottomObjectProperty());
        delegate.add(df.getOWLBottomDataProperty());
        delegate.add(df.getOWLBottomDataProperty());

        for(OWL2Datatype datatype : OWL2Datatype.values()) {
            delegate.add(df.getOWLDatatype(datatype.getIRI()));
        }

        for(DublinCoreVocabulary vocabulary : DublinCoreVocabulary.values()) {
            delegate.add(df.getOWLAnnotationProperty(vocabulary.getIRI()));
        }

        for(OWLAnnotationProperty annotationProperty : SKOSVocabulary.getAnnotationProperties(df)) {
            delegate.add(annotationProperty);
        }
    }

    public Set<OWLEntity> getEntities(String shortForm) {
        try {
            readWriteLock.readLock().lock();
            return delegate.getEntities(shortForm);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    public OWLEntity getEntity(String shortForm) {
        try {
            readWriteLock.readLock().lock();
            return delegate.getEntity(shortForm);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    public Set<String> getShortForms() {
        try {
            readWriteLock.readLock().lock();
            return delegate.getShortForms();
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    public String getShortForm(OWLEntity entity) {
        try {
            readWriteLock.readLock().lock();
            return delegate.getShortForm(entity);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void dispose() {
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /// Change handling

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void updateRenderings(List<? extends OWLOntologyChange> changes) {

        readWriteLock.writeLock().lock();
        try {
            for (OWLOntologyChange chg : changes) {

                final Set<OWLEntity> entities = chg.getSignature();
                Set<OWLEntity> processed = new HashSet<OWLEntity>(entities.size());
                if(chg.isAxiomChange()) {
                    chg.getAxiom().accept(new OWLAxiomVisitorAdapter() {
                        @Override
                        public void visit(OWLAnnotationAssertionAxiom axiom) {
                            if(axiom.getSubject() instanceof IRI) {
                                entities.addAll(rootOntology.getEntitiesInSignature((IRI) axiom.getSubject(), true));
                            }
                        }
                    });
                }
                for (OWLEntity entity : entities) {
                    if (!processed.contains(entity)) {
                        processed.add(entity);
                        delegate.remove(entity);
                        if (rootOntology.containsEntityInSignature(entity, true)) {
                            delegate.add(entity);
                        }
                    }
                }

            }
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }




}

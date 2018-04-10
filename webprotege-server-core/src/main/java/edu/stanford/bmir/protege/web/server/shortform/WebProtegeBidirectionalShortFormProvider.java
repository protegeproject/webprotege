package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.apache.commons.lang.time.StopWatch;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/05/2012
 * <p>
 * This class is thread safe.
 * </p>
 */
@Deprecated
@ProjectSingleton
public class WebProtegeBidirectionalShortFormProvider implements BidirectionalShortFormProvider {

    private static final Logger logger = LoggerFactory.getLogger(WebProtegeBidirectionalShortFormProvider.class);

    private final ProjectId projectId;

    private final OWLOntology rootOntology;

    private final BidirectionalShortFormProviderAdapterEx delegate;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Inject
    public WebProtegeBidirectionalShortFormProvider(ProjectId projectId, @RootOntology OWLOntology rootOntology, ShortFormProvider shortFormProvider) {
        this.projectId = projectId;
        this.rootOntology = rootOntology;
        final Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
        logger.info("{} Building short form provider", projectId);
        int priority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        Stopwatch stopwatch = Stopwatch.createStarted();
        delegate = new BidirectionalShortFormProviderAdapterEx(importsClosure, shortFormProvider);
        setupBuiltinObjectRenderings(rootOntology);
        Thread.currentThread().setPriority(priority);
        logger.info("{} Built short form provider in {} ms", projectId, stopwatch.elapsed(MILLISECONDS));
        OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
        manager.addOntologyChangeListener(new OWLOntologyChangeListener() {
            public void ontologiesChanged(@Nonnull List<? extends OWLOntologyChange> changes) throws OWLException {
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

    @Nonnull
    public Set<OWLEntity> getEntities(@Nonnull String shortForm) {
        try {
            readWriteLock.readLock().lock();
            return delegate.getEntities(shortForm);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    public OWLEntity getEntity(@Nonnull String shortForm) {
        try {
            readWriteLock.readLock().lock();
            return delegate.getEntity(shortForm);
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Nonnull
    public Set<String> getShortForms() {
        try {
            readWriteLock.readLock().lock();
            return delegate.getShortForms();
        }
        finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Nonnull
    public String getShortForm(@Nonnull OWLEntity entity) {
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
                Set<OWLEntity> processed = new HashSet<>(entities.size());
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

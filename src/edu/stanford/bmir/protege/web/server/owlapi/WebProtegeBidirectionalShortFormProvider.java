package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

import java.util.*;
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

    private OWLAPIProject project;

    private BidirectionalShortFormProviderAdapter delegate;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public WebProtegeBidirectionalShortFormProvider(OWLAPIProject project) {
        this.project = project;
        final Set<OWLOntology> importsClosure = project.getRootOntology().getImportsClosure();
        delegate = new BidirectionalShortFormProviderAdapter(importsClosure, new WebProtegeShortFormProvider(project));

        OWLOntologyManager manager = project.getRootOntology().getOWLOntologyManager();
        manager.addOntologyChangeListener(new OWLOntologyChangeListener() {
            public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
                updateRenderings(changes);
            }
        });
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
                                entities.addAll(project.getRootOntology().getEntitiesInSignature((IRI) axiom.getSubject(), true));
                            }
                        }
                    });
                }
                for (OWLEntity entity : entities) {
                    if (!processed.contains(entity)) {
                        processed.add(entity);
                        if (project.getRootOntology().containsEntityInSignature(entity, true)) {
                            delegate.add(entity);
                        }
                        else {
                            delegate.remove(entity);
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

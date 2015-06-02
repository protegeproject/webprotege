package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class OWLAPIProjectOWLOntologyManager implements OWLOntologyManager {

    final private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    final private Lock readLock = readWriteLock.readLock();

    final private Lock writeLock = readWriteLock.writeLock();

    private OWLOntologyManager delegate;

    private boolean sealed = false;

    public OWLAPIProjectOWLOntologyManager() {
    }

    public void setDelegate(OWLOntologyManager delegate) {
        this.delegate = delegate;
    }

    // Package private
    OWLOntologyManager getDelegate() {
        return delegate;
    }

    public void sealDelegate() {
        sealed = true;
    }

    public OWLDataFactory getOWLDataFactory() {
        return delegate.getOWLDataFactory();
    }

    public Set<OWLOntology> getOntologies() {
        return delegate.getOntologies();
    }

    public Set<OWLOntology> getOntologies(OWLAxiom axiom) {
        return delegate.getOntologies(axiom);
    }

    public Set<OWLOntology> getVersions(IRI ontology) {
        return delegate.getVersions(ontology);
    }

    public boolean contains(IRI ontologyIRI) {
        return delegate.contains(ontologyIRI);
    }

    @Override
    public boolean containsVersion(IRI ontologyVersionIRI) {
        return delegate.containsVersion(ontologyVersionIRI);
    }

    @Override
    public Set<OWLOntologyID> getOntologyIDsByVersion(IRI ontologyVersionIRI) {
        return delegate.getOntologyIDsByVersion(ontologyVersionIRI);
    }

    @Override
    public void removeOntology(OWLOntologyID ontologyID) {
        delegate.removeOntology(ontologyID);
    }

    public boolean contains(OWLOntologyID id) {
        return delegate.contains(id);
    }

    public OWLOntology getOntology(IRI ontologyIRI) {
        return delegate.getOntology(ontologyIRI);
    }

    public OWLOntology getOntology(OWLOntologyID ontologyID) {
        return delegate.getOntology(ontologyID);
    }

    public OWLOntology getImportedOntology(OWLImportsDeclaration declaration) {
        return delegate.getImportedOntology(declaration);
    }

    public Set<OWLOntology> getDirectImports(OWLOntology ontology) {
        return delegate.getDirectImports(ontology);
    }

    public Set<OWLOntology> getImports(OWLOntology ontology) {
        return delegate.getImports(ontology);
    }

    public Set<OWLOntology> getImportsClosure(OWLOntology ontology) {
        return delegate.getImportsClosure(ontology);
    }

    public List<OWLOntology> getSortedImportsClosure(OWLOntology ontology) {
        return delegate.getSortedImportsClosure(ontology);
    }

    public List<OWLOntologyChange> applyChanges(List<? extends OWLOntologyChange> changes) throws OWLOntologyRenameException {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.applyChanges(changes);
    }

    public List<OWLOntologyChange> addAxioms(OWLOntology ont, Set<? extends OWLAxiom> axioms) {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.addAxioms(ont, axioms);
    }

    public List<OWLOntologyChange> addAxiom(OWLOntology ont, OWLAxiom axiom) {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.addAxiom(ont, axiom);
    }

    public List<OWLOntologyChange> removeAxiom(OWLOntology ont, OWLAxiom axiom) {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.removeAxiom(ont, axiom);
    }

    public List<OWLOntologyChange> removeAxioms(OWLOntology ont, Set<? extends OWLAxiom> axioms) {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.removeAxioms(ont, axioms);
    }

    public List<OWLOntologyChange> applyChange(OWLOntologyChange change) throws OWLOntologyRenameException {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.applyChange(change);
    }

    public OWLOntology createOntology() throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology();
    }

    public OWLOntology createOntology(Set<OWLAxiom> axioms) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(axioms);
    }

    public OWLOntology createOntology(Set<OWLAxiom> axioms, IRI ontologyIRI) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(axioms, ontologyIRI);
    }

    public OWLOntology createOntology(IRI ontologyIRI) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(ontologyIRI);
    }

    public OWLOntology createOntology(OWLOntologyID ontologyID) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(ontologyID);
    }

    public OWLOntology createOntology(IRI ontologyIRI, Set<OWLOntology> ontologies, boolean copyLogicalAxiomsOnly) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(ontologyIRI, ontologies, copyLogicalAxiomsOnly);
    }

    public OWLOntology createOntology(IRI ontologyIRI, Set<OWLOntology> ontologies) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(ontologyIRI, ontologies);
    }

    public OWLOntology loadOntology(IRI ontologyIRI) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntology(ontologyIRI);
    }

    public OWLOntology loadOntologyFromOntologyDocument(IRI documentIRI) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(documentIRI);
    }

    public OWLOntology loadOntologyFromOntologyDocument(File file) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(file);
    }

    public OWLOntology loadOntologyFromOntologyDocument(InputStream inputStream) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(inputStream);
    }

    public OWLOntology loadOntologyFromOntologyDocument(OWLOntologyDocumentSource documentSource) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(documentSource);
    }

    public OWLOntology loadOntologyFromOntologyDocument(OWLOntologyDocumentSource documentSource, OWLOntologyLoaderConfiguration config) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(documentSource, config);
    }

    public void removeOntology(OWLOntology ontology) {
        if (sealed) {
            throw new OntologyRemovalNotAllowedException();
        }
        delegate.removeOntology(ontology);
    }

    public IRI getOntologyDocumentIRI(OWLOntology ontology) {
        return delegate.getOntologyDocumentIRI(ontology);
    }

    public void setOntologyDocumentIRI(OWLOntology ontology, IRI documentIRI) throws UnknownOWLOntologyException {
        if (sealed) {
            throw new DocumentIRIChangeNotAllowedException();
        }
        delegate.setOntologyDocumentIRI(ontology, documentIRI);
    }

    public OWLOntologyFormat getOntologyFormat(OWLOntology ontology) throws UnknownOWLOntologyException {
        return delegate.getOntologyFormat(ontology);
    }

    public void setOntologyFormat(OWLOntology ontology, OWLOntologyFormat ontologyFormat) {
        if (sealed) {
            throw new OntologyFormatChangeNotAllowedException();
        }
        delegate.setOntologyFormat(ontology, ontologyFormat);
    }

    public void saveOntology(OWLOntology ontology) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology);
    }

    public void saveOntology(OWLOntology ontology, IRI documentIRI) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, documentIRI);
    }

    public void saveOntology(OWLOntology ontology, OutputStream outputStream) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, outputStream);
    }

    public void saveOntology(OWLOntology ontology, OWLOntologyFormat ontologyFormat) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, ontologyFormat);
    }

    public void saveOntology(OWLOntology ontology, OWLOntologyFormat ontologyFormat, IRI documentIRI) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, ontologyFormat, documentIRI);
    }

    public void saveOntology(OWLOntology ontology, OWLOntologyFormat ontologyFormat, OutputStream outputStream) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, ontologyFormat, outputStream);
    }

    public void saveOntology(OWLOntology ontology, OWLOntologyDocumentTarget documentTarget) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, documentTarget);
    }

    public void saveOntology(OWLOntology ontology, OWLOntologyFormat ontologyFormat, OWLOntologyDocumentTarget documentTarget) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, ontologyFormat, documentTarget);
    }

    public void addIRIMapper(OWLOntologyIRIMapper mapper) {
        delegate.addIRIMapper(mapper);
    }

    public void removeIRIMapper(OWLOntologyIRIMapper mapper) {
        delegate.removeIRIMapper(mapper);
    }

    public void clearIRIMappers() {
        delegate.clearIRIMappers();
    }

    public void addOntologyFactory(OWLOntologyFactory factory) {
        delegate.addOntologyFactory(factory);
    }

    public void removeOntologyFactory(OWLOntologyFactory factory) {
        delegate.removeOntologyFactory(factory);
    }

    public Collection<OWLOntologyFactory> getOntologyFactories() {
        return delegate.getOntologyFactories();
    }

    public void addOntologyStorer(OWLOntologyStorer storer) {
        delegate.addOntologyStorer(storer);
    }

    public void removeOntologyStorer(OWLOntologyStorer storer) {
        delegate.removeOntologyStorer(storer);
    }

    public void addOntologyChangeListener(OWLOntologyChangeListener listener) {
        delegate.addOntologyChangeListener(listener);
    }

    public void addOntologyChangeListener(OWLOntologyChangeListener listener, OWLOntologyChangeBroadcastStrategy strategy) {
        delegate.addOntologyChangeListener(listener, strategy);
    }

    public void addImpendingOntologyChangeListener(ImpendingOWLOntologyChangeListener listener) {
        delegate.addImpendingOntologyChangeListener(listener);
    }

    public void removeImpendingOntologyChangeListener(ImpendingOWLOntologyChangeListener listener) {
        delegate.removeImpendingOntologyChangeListener(listener);
    }

    public void addOntologyChangesVetoedListener(OWLOntologyChangesVetoedListener listener) {
        delegate.addOntologyChangesVetoedListener(listener);
    }

    public void removeOntologyChangesVetoedListener(OWLOntologyChangesVetoedListener listener) {
        delegate.removeOntologyChangesVetoedListener(listener);
    }

    public void setDefaultChangeBroadcastStrategy(OWLOntologyChangeBroadcastStrategy strategy) {
        if (sealed) {
            throw new OperationNotPermittedException();
        }
        delegate.setDefaultChangeBroadcastStrategy(strategy);
    }

    public void removeOntologyChangeListener(OWLOntologyChangeListener listener) {
        delegate.removeOntologyChangeListener(listener);
    }

    @Deprecated
    public void makeLoadImportRequest(OWLImportsDeclaration declaration) throws UnloadableImportException {
        if (sealed) {
            throw new ImportLoadingNotAllowedException();
        }
        delegate.makeLoadImportRequest(declaration);
    }

    public void makeLoadImportRequest(OWLImportsDeclaration declaration, OWLOntologyLoaderConfiguration configuration) throws UnloadableImportException {
        if (sealed) {
            throw new ImportLoadingNotAllowedException();
        }
        delegate.makeLoadImportRequest(declaration, configuration);
    }

    @Deprecated
    public void setSilentMissingImportsHandling(boolean b) {
        delegate.setSilentMissingImportsHandling(b);
    }

    @Deprecated
    public boolean isSilentMissingImportsHandling() {
        return delegate.isSilentMissingImportsHandling();
    }

    public void addMissingImportListener(MissingImportListener listener) {
        delegate.addMissingImportListener(listener);
    }

    public void removeMissingImportListener(MissingImportListener listener) {
        delegate.removeMissingImportListener(listener);
    }

    public void addOntologyLoaderListener(OWLOntologyLoaderListener listener) {
        delegate.addOntologyLoaderListener(listener);
    }

    public void removeOntologyLoaderListener(OWLOntologyLoaderListener listener) {
        delegate.removeOntologyLoaderListener(listener);
    }

    public void addOntologyChangeProgessListener(OWLOntologyChangeProgressListener listener) {
        delegate.addOntologyChangeProgessListener(listener);
    }

    public void removeOntologyChangeProgessListener(OWLOntologyChangeProgressListener listener) {
        delegate.removeOntologyChangeProgessListener(listener);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class OntologyCreationNotAllowedException extends OWLOntologyCreationException {

    }

    private static class ImportLoadingNotAllowedException extends RuntimeException {

    }

    private static class OntologyRemovalNotAllowedException extends RuntimeException {

    }


    private static class DocumentIRIChangeNotAllowedException extends RuntimeException {

    }

    private static class OntologyFormatChangeNotAllowedException extends RuntimeException {

    }

    private static class OperationNotPermittedException extends RuntimeException {

    }

    private static class DirectChangeApplicationNotAllowedException extends RuntimeException {

    }
}

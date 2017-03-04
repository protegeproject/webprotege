package edu.stanford.bmir.protege.web.server.project;

import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.OWLParserFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.model.parameters.OntologyCopy;
import org.semanticweb.owlapi.util.PriorityCollection;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
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
        for(OWLOntology ontology : delegate.getOntologies()) {
            ontology.setOWLOntologyManager(this);
        }
        sealed = true;
    }

    @Override
    public OWLDataFactory getOWLDataFactory() {
        return delegate.getOWLDataFactory();
    }

    @Override
    public Set<OWLOntology> getOntologies() {
        return delegate.getOntologies();
    }

    @Override
    public Set<OWLOntology> getOntologies(OWLAxiom axiom) {
        return delegate.getOntologies(axiom);
    }

    @Override
    public Set<OWLOntology> getVersions(IRI ontology) {
        return delegate.getVersions(ontology);
    }

    @Override
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

    @Override
    public boolean contains(OWLOntologyID id) {
        return delegate.contains(id);
    }

    @Override
    public OWLOntology getOntology(IRI ontologyIRI) {
        return delegate.getOntology(ontologyIRI);
    }

    @Override
    public OWLOntology getOntology(OWLOntologyID ontologyID) {
        return delegate.getOntology(ontologyID);
    }

    @Override
    public OWLOntology getImportedOntology(OWLImportsDeclaration declaration) {
        return delegate.getImportedOntology(declaration);
    }

    @Override
    public Set<OWLOntology> getDirectImports(OWLOntology ontology) {
        return delegate.getDirectImports(ontology);
    }

    @Override
    public Set<OWLOntology> getImports(OWLOntology ontology) {
        return delegate.getImports(ontology);
    }

    @Override
    public Set<OWLOntology> getImportsClosure(OWLOntology ontology) {
        return delegate.getImportsClosure(ontology);
    }



    @Override
    public List<OWLOntology> getSortedImportsClosure(OWLOntology ontology) {
        return delegate.getSortedImportsClosure(ontology);
    }

    @Override
    public ChangeApplied applyChanges(List<? extends OWLOntologyChange> changes) throws OWLOntologyRenameException {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.applyChanges(changes);
    }

    @Override
    public ChangeApplied addAxioms(OWLOntology ont, Set<? extends OWLAxiom> axioms) {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.addAxioms(ont, axioms);
    }

    @Override
    public ChangeApplied addAxiom(OWLOntology ont, OWLAxiom axiom) {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.addAxiom(ont, axiom);
    }

    @Override
    public ChangeApplied removeAxiom(OWLOntology ont, OWLAxiom axiom) {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.removeAxiom(ont, axiom);
    }

    @Override
    public ChangeApplied removeAxioms(OWLOntology ont, Set<? extends OWLAxiom> axioms) {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.removeAxioms(ont, axioms);
    }

    @Override
    public ChangeApplied applyChange(OWLOntologyChange change) throws OWLOntologyRenameException {
        if (sealed) {
            throw new DirectChangeApplicationNotAllowedException();
        }
        return delegate.applyChange(change);
    }

    @Override
    public OWLOntology createOntology() throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology();
    }

    @Override
    public OWLOntology createOntology(Set<OWLAxiom> axioms) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(axioms);
    }

    @Override
    public OWLOntology createOntology(Set<OWLAxiom> axioms, IRI ontologyIRI) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(axioms, ontologyIRI);
    }

    @Override
    public OWLOntology createOntology(IRI ontologyIRI) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(ontologyIRI);
    }

    @Override
    public OWLOntology createOntology(OWLOntologyID ontologyID) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(ontologyID);
    }

    @Override
    public OWLOntology createOntology(IRI ontologyIRI, Set<OWLOntology> ontologies, boolean copyLogicalAxiomsOnly) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(ontologyIRI, ontologies, copyLogicalAxiomsOnly);
    }

    @Override
    public OWLOntology createOntology(IRI ontologyIRI, Set<OWLOntology> ontologies) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.createOntology(ontologyIRI, ontologies);
    }

    @Override
    public OWLOntology loadOntology(IRI ontologyIRI) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntology(ontologyIRI);
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(IRI documentIRI) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(documentIRI);
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(File file) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(file);
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(InputStream inputStream) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(inputStream);
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(OWLOntologyDocumentSource documentSource) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(documentSource);
    }

    @Override
    public OWLOntology loadOntologyFromOntologyDocument(OWLOntologyDocumentSource documentSource, OWLOntologyLoaderConfiguration config) throws OWLOntologyCreationException {
        if (sealed) {
            throw new OntologyCreationNotAllowedException();
        }
        return delegate.loadOntologyFromOntologyDocument(documentSource, config);
    }

    @Override
    public void removeOntology(OWLOntology ontology) {
        if (sealed) {
            throw new OntologyRemovalNotAllowedException();
        }
        delegate.removeOntology(ontology);
    }

    @Override
    public IRI getOntologyDocumentIRI(OWLOntology ontology) {
        return delegate.getOntologyDocumentIRI(ontology);
    }

    @Override
    public void setOntologyDocumentIRI(OWLOntology ontology, IRI documentIRI) throws UnknownOWLOntologyException {
        if (sealed) {
            throw new DocumentIRIChangeNotAllowedException();
        }
        delegate.setOntologyDocumentIRI(ontology, documentIRI);
    }

    @Override
    public OWLDocumentFormat getOntologyFormat(OWLOntology ontology) throws UnknownOWLOntologyException {
        return delegate.getOntologyFormat(ontology);
    }

    @Override
    public void setOntologyFormat(OWLOntology ontology, OWLDocumentFormat ontologyFormat) {
        if (sealed) {
            throw new OntologyFormatChangeNotAllowedException();
        }
        delegate.setOntologyFormat(ontology, ontologyFormat);
    }

    @Override
    public void saveOntology(OWLOntology ontology) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology);
    }

    @Override
    public void saveOntology(OWLOntology ontology, IRI documentIRI) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, documentIRI);
    }

    @Override
    public void saveOntology(OWLOntology ontology, OutputStream outputStream) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, outputStream);
    }

    @Override
    public void saveOntology(OWLOntology ontology, OWLDocumentFormat ontologyFormat) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, ontologyFormat);
    }

    @Override
    public void saveOntology(OWLOntology ontology, OWLDocumentFormat ontologyFormat, IRI documentIRI) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, ontologyFormat, documentIRI);
    }

    @Override
    public void saveOntology(OWLOntology ontology, OWLDocumentFormat ontologyFormat, OutputStream outputStream) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, ontologyFormat, outputStream);
    }

    @Override
    public void saveOntology(OWLOntology ontology, OWLOntologyDocumentTarget documentTarget) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, documentTarget);
    }

    @Override
    public void saveOntology(OWLOntology ontology, OWLDocumentFormat ontologyFormat, OWLOntologyDocumentTarget documentTarget) throws OWLOntologyStorageException {
        delegate.saveOntology(ontology, ontologyFormat, documentTarget);
    }

    @Override
    public void addIRIMapper(OWLOntologyIRIMapper mapper) {
        delegate.addIRIMapper(mapper);
    }

    @Override
    public void removeIRIMapper(OWLOntologyIRIMapper mapper) {
        delegate.removeIRIMapper(mapper);
    }

    @Override
    public void clearIRIMappers() {
        delegate.clearIRIMappers();
    }

    public void addOntologyFactory(OWLOntologyFactory factory) {
        delegate.getOntologyFactories().add(factory);
    }

    public void removeOntologyFactory(OWLOntologyFactory factory) {
        delegate.getOntologyFactories().remove(factory);
    }

    @Override
    public PriorityCollection<OWLOntologyFactory> getOntologyFactories() {
        return delegate.getOntologyFactories();
    }

    @Override
    public void addOntologyChangeListener(OWLOntologyChangeListener listener) {
        delegate.addOntologyChangeListener(listener);
    }

    @Override
    public void addOntologyChangeListener(OWLOntologyChangeListener listener, OWLOntologyChangeBroadcastStrategy strategy) {
        delegate.addOntologyChangeListener(listener, strategy);
    }

    @Override
    public void addImpendingOntologyChangeListener(ImpendingOWLOntologyChangeListener listener) {
        delegate.addImpendingOntologyChangeListener(listener);
    }

    @Override
    public void removeImpendingOntologyChangeListener(ImpendingOWLOntologyChangeListener listener) {
        delegate.removeImpendingOntologyChangeListener(listener);
    }

    @Override
    public void addOntologyChangesVetoedListener(OWLOntologyChangesVetoedListener listener) {
        delegate.addOntologyChangesVetoedListener(listener);
    }

    @Override
    public void removeOntologyChangesVetoedListener(OWLOntologyChangesVetoedListener listener) {
        delegate.removeOntologyChangesVetoedListener(listener);
    }

    @Override
    public void setDefaultChangeBroadcastStrategy(OWLOntologyChangeBroadcastStrategy strategy) {
        if (sealed) {
            throw new OperationNotPermittedException();
        }
        delegate.setDefaultChangeBroadcastStrategy(strategy);
    }

    @Override
    public void removeOntologyChangeListener(OWLOntologyChangeListener listener) {
        delegate.removeOntologyChangeListener(listener);
    }

    @Deprecated
    @Override
    public void makeLoadImportRequest(OWLImportsDeclaration declaration) throws UnloadableImportException {
        if (sealed) {
            throw new ImportLoadingNotAllowedException();
        }
        delegate.makeLoadImportRequest(declaration);
    }

    @Override
    public void makeLoadImportRequest(OWLImportsDeclaration declaration, OWLOntologyLoaderConfiguration configuration) throws UnloadableImportException {
        if (sealed) {
            throw new ImportLoadingNotAllowedException();
        }
        delegate.makeLoadImportRequest(declaration, configuration);
    }

    @Override
    public void addMissingImportListener(MissingImportListener listener) {
        delegate.addMissingImportListener(listener);
    }

    @Override
    public void removeMissingImportListener(MissingImportListener listener) {
        delegate.removeMissingImportListener(listener);
    }

    @Override
    public void addOntologyLoaderListener(OWLOntologyLoaderListener listener) {
        delegate.addOntologyLoaderListener(listener);
    }

    @Override
    public void removeOntologyLoaderListener(OWLOntologyLoaderListener listener) {
        delegate.removeOntologyLoaderListener(listener);
    }

    @Override
    public void addOntologyChangeProgessListener(OWLOntologyChangeProgressListener listener) {
        delegate.addOntologyChangeProgessListener(listener);
    }

    @Override
    public void removeOntologyChangeProgessListener(OWLOntologyChangeProgressListener listener) {
        delegate.removeOntologyChangeProgessListener(listener);
    }

    /**
     * @param ontology ontology to check
     * @return true if the ontology is contained
     */
    @Override
    public boolean contains(OWLOntology ontology) {
        return delegate.contains(ontology);
    }

    /**
     * Copy an ontology from another manager to this one. The returned
     * OWLOntology will return this manager when getOWLOntologyManager() is
     * invoked. The copy mode is defined by the OntologyCopy parameter: SHALLOW
     * for simply creating a new ontology containing the same axioms and same
     * id, DEEP for copying actoss format and document IRI, MOVE to remove the
     * ontology from its previous manager.
     *
     * @param toCopy   ontology to copy
     * @param settings settings for the copy
     * @return copied ontology. This is the same object as toCopy only for MOVE
     * copies
     * @throws org.semanticweb.owlapi.model.OWLOntologyCreationException if this manager cannot add the new ontology
     */
    @Nonnull
    @Override
    public OWLOntology copyOntology(OWLOntology toCopy, OntologyCopy settings) throws OWLOntologyCreationException {
        if(sealed) {
            throw new OntologyCopyingNotAllowedException();
        }
        return null;
    }

    /**
     * Add astorer to the manager
     *
     * @param storer the storer to add
     * @deprecated use getOntologyStorers().add() instead
     */
    @Override
    public void addOntologyStorer(OWLStorerFactory storer) {
        delegate.getOntologyStorers().add(storer);
    }

    /**
     * Remove a storer from the manager
     *
     * @param storer the storer to remove
     * @deprecated use getOntologyStorers().remove() instead
     */
    @Override
    public void removeOntologyStorer(OWLStorerFactory storer) {
        delegate.getOntologyStorers().remove(storer);
    }

    /**
     * Clear the manager storers
     *
     * @deprecated use getOntologyStorers().clear() instead
     */
    @Override
    public void clearOntologyStorers() {
        delegate.getOntologyStorers().clear();
    }

    /**
     * Set the collection of IRI mappers. It is used by Guice injection, but can
     * be used manually as well to replace the existing mappers with new ones.
     * The mappers are used to obtain ontology document IRIs for ontology IRIs.
     * If their type is annotated with a HasPriority type, this will be used to
     * decide the order they are used. Otherwise, the order in which the
     * collection is iterated will determine the order in which the mappers are
     * used.
     *
     * @param mappers the mappers to be injected
     */
    @Override
    public void setIRIMappers(Set<OWLOntologyIRIMapper> mappers) {
        delegate.setIRIMappers(mappers);
    }

    /**
     * @return the collection of IRI mappers. This allows for iteration and
     * modification of the list.
     */
    @Nonnull
    @Override
    public PriorityCollection<OWLOntologyIRIMapper> getIRIMappers() {
        return delegate.getIRIMappers();
    }

    /**
     * Set the collection of parsers. It is used by Guice injection, but can be
     * used manually as well to replace the existing parsers with new ones. If
     * their type is annotated with a HasPriority type, this will be used to
     * decide the order they are used. Otherwise, the order in which the
     * collection is iterated will determine the order in which the parsers are
     * used.
     *
     * @param parsers the factories to be injected
     */
    @Override
    public void setOntologyParsers(Set<OWLParserFactory> parsers) {
        delegate.setOntologyParsers(parsers);
    }

    /**
     * @return the collection of parsers. This allows for iteration and
     * modification of the list.
     */
    @Nonnull
    @Override
    public PriorityCollection<OWLParserFactory> getOntologyParsers() {
        return delegate.getOntologyParsers();
    }

    /**
     * Set the collection of ontology factories. It is used by Guice injection,
     * but can be used manually as well to replace the existing factories with
     * new ones. If their type is annotated with a HasPriority type, this will
     * be used to decide the order they are used. Otherwise, the order in which
     * the collection is iterated will determine the order in which the parsers
     * are used.
     *
     * @param factories the factories to be injected
     */
    @Override
    public void setOntologyFactories(Set<OWLOntologyFactory> factories) {
        delegate.setOntologyFactories(factories);
    }

    /**
     * Set the list of ontology storers. If their type is annotated with a
     * HasPriority type, this will be used to decide the order they are used.
     * Otherwise, the order in which the collection is iterated will determine
     * the order in which the storers are used.
     *
     * @param storers The storers to be used
     */
    @Override
    public void setOntologyStorers(Set<OWLStorerFactory> storers) {
        delegate.setOntologyStorers(storers);
    }

    /**
     * @return the collection of storers. This allows for iteration and
     * modification of the list.
     */
    @Nonnull
    @Override
    public PriorityCollection<OWLStorerFactory> getOntologyStorers() {
        return delegate.getOntologyStorers();
    }

    /**
     * Sets the configuration.
     *
     * @param config configuration to be used
     */
    @Override
    public void setOntologyLoaderConfiguration(OWLOntologyLoaderConfiguration config) {
        delegate.setOntologyLoaderConfiguration(config);
    }

    /**
     * @return the configuration for this object. This is a read only accessor,
     * since the configuration is an immutable object. To change the
     * configuration, use the setter in this interface to set a modified
     * configuration.
     */
    @Nonnull
    @Override
    public OWLOntologyLoaderConfiguration getOntologyLoaderConfiguration() {
        return delegate.getOntologyLoaderConfiguration();
    }

    /**
     * Sets the configuration provider.
     *
     * @param provider the provider to be used
     */
    @Override
    public void setOntologyLoaderConfigurationProvider(Provider<OWLOntologyLoaderConfiguration> provider) {
        delegate.setOntologyLoaderConfigurationProvider(provider);
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

    private static class OntologyCopyingNotAllowedException extends RuntimeException {

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

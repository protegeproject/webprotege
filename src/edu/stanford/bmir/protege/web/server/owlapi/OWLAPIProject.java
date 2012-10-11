package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricsManager;
import edu.stanford.bmir.protege.web.server.owlapi.notes.OWLAPINotesManagerNotesAPIImpl;
import edu.stanford.bmir.protege.web.server.owlapi.notes.OWLAPINotesManager;
import edu.stanford.smi.protege.util.Log;
import org.coode.owlapi.functionalrenderer.OWLFunctionalSyntaxOntologyStorer;
import org.coode.owlapi.owlxml.renderer.OWLXMLOntologyStorer;
import org.coode.owlapi.rdf.rdfxml.RDFXMLOntologyStorer;
import org.protege.editor.owl.model.hierarchy.AssertedClassHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLAnnotationPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLDataPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLObjectPropertyHierarchyProvider;
import org.protege.owlapi.model.ProtegeOWLOntologyManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.binaryowl.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.binaryowl.BinaryOWLParseException;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.NonMappingOntologyIRIMapper;
import uk.ac.manchester.cs.owl.owlapi.EmptyInMemOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.ParsableOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOntologyStorer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/03/2012
 */
public class OWLAPIProject {

    private OWLAPIProjectDocumentStore documentStore;

    final private OWLAPIProjectOWLOntologyManager manager;

    private RenderingManager renderingManager;

    private OWLOntology ontology;

    private OWLAPIProjectAttributes projectAttributes;

    private OWLAPIProjectConfiguration projectConfiguration;

    private OWLAPIEntityEditorKit entityEditorKit;

    private AssertedClassHierarchyProvider classHierarchyProvider = new AssertedClassHierarchyProvider(OWLManager.createOWLOntologyManager());

    private OWLObjectPropertyHierarchyProvider objectPropertyHierarchyProvider;

    private OWLDataPropertyHierarchyProvider dataPropertyHierarchyProvider;

    private OWLAnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider;

    private OWLAPISearchManager searchManager;

    private OWLAPINotesManager notesManager;

    private OWLAPIChangeManager changeManager;

    final private OWLOntologyManager delegateManager;

    private OWLAPIProjectMetricsManager metricsManager;

    
    
    private final ReadWriteLock projectChangeLock = new ReentrantReadWriteLock();
    
    private final Lock projectChangeReadLock = projectChangeLock.readLock();
    
    private final Lock projectChangeWriteLock = projectChangeLock.writeLock();

    private final ExecutorService projectAttributesSaver = Executors.newSingleThreadExecutor();;


    public static synchronized OWLAPIProject getProject(OWLAPIProjectDocumentStore documentStore) throws IOException, OWLParserException {
        return new OWLAPIProject(documentStore);
    }


    /**
     * Constructs and OWLAPIProject over the sources specified by the {@link OWLAPIProjectDocumentStore}.
     * @param documentStore The document store.
     * @throws IOException If there was a problem reading sources.
     * @throws OWLParserException If there was a problem parsing sources.
     */
    private OWLAPIProject(OWLAPIProjectDocumentStore documentStore) throws IOException, OWLParserException {
        this.documentStore = documentStore;
        final boolean useCachingInDataFactory = false;
        final boolean useCompressionInDataFactory = false;
        OWLDataFactory df = new OWLDataFactoryImpl(useCachingInDataFactory, useCompressionInDataFactory);

        // The delegate - we use the concurrent ontology manager
        delegateManager = new ProtegeOWLOntologyManager(df);
        // We only support the binary format for speed
        delegateManager.addOntologyStorer(new BinaryOWLOntologyDocumentStorer());
        delegateManager.addOntologyStorer(new RDFXMLOntologyStorer());
        delegateManager.addOntologyStorer(new OWLXMLOntologyStorer());
        delegateManager.addOntologyStorer(new OWLFunctionalSyntaxOntologyStorer());
        delegateManager.addOntologyStorer(new ManchesterOWLSyntaxOntologyStorer());
        // Pass through mapping
        delegateManager.addIRIMapper(new NonMappingOntologyIRIMapper());

        // The wrapper manager
        manager = new OWLAPIProjectOWLOntologyManager();

        // We have to do some twiddling of the factories so that the delegate manager creates ontologies which point
        // to the non-delegate manager.
        EmptyInMemOWLOntologyFactory imMemFactory = new EmptyInMemOWLOntologyFactory();
        delegateManager.addOntologyFactory(imMemFactory);
        imMemFactory.setOWLOntologyManager(manager);

        ParsableOWLOntologyFactory parsingFactory = new ParsableOWLOntologyFactory();
        delegateManager.addOntologyFactory(parsingFactory);
        parsingFactory.setOWLOntologyManager(manager);

        manager.setDelegate(delegateManager);

        loadProject();
        initialiseProjectMachinery();

    }
    




    /**
     * Loads the specified project.  This method is only called from the constructor of this class.
     */
    private void loadProject() throws IOException, BinaryOWLParseException {
        try {
            if(!documentStore.exists()) {
                throw new ProjectDocumentNotFoundException(documentStore.getProjectId());
            }
            ontology = documentStore.loadRootOntologyIntoManager(delegateManager);
            delegateManager.addOntologyChangeListener(new OWLOntologyChangeListener() {
                public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
                    handleOntologiesChanged(changes);
                }
            });
            manager.sealDelegate();
            projectAttributes = documentStore.getProjectAttributes();
            projectAttributes.addListener(new OWLAPIProjectAttributesListener() {
                public void attributeRemoved(OWLAPIProjectAttributes attributes, String attributeName) {
                    saveProjectAttributes();
                }

                public void attributeChanged(OWLAPIProjectAttributes attributes, String attributeName) {
                    saveProjectAttributes();
                }

                public void attributesRemoved(OWLAPIProjectAttributes attributes) {
                    saveProjectAttributes();
                }
            });
            projectConfiguration = new OWLAPIProjectConfiguration(projectAttributes);


        }
        catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Failed to load project: " + e.getMessage(), e);
        }
    }
    
    private void saveProjectAttributes() {
        projectAttributesSaver.submit(new Runnable() {
            public void run() {
                try {
                    documentStore.saveProjectAttributes(projectAttributes);
                }
                catch (IOException e) {
                    Log.getLogger().severe("Could not save project attributes. Reason: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Call from constructor only.
     */
    private void initialiseProjectMachinery() {
        renderingManager = new RenderingManager(this);

        searchManager = new OWLAPISearchManager(this);

        changeManager = new OWLAPIChangeManager(this);

        notesManager = new OWLAPINotesManagerNotesAPIImpl(this);


        // MH: All of this is highly dodgy and not at all thread safe.  It is therefore BROKEN!  Needs fixing.

        classHierarchyProvider = new AssertedClassHierarchyProvider(manager);
        classHierarchyProvider.setOntologies(manager.getOntologies());

        objectPropertyHierarchyProvider = new OWLObjectPropertyHierarchyProvider(manager);
        objectPropertyHierarchyProvider.setOntologies(manager.getOntologies());

        dataPropertyHierarchyProvider = new OWLDataPropertyHierarchyProvider(manager);
        dataPropertyHierarchyProvider.setOntologies(manager.getOntologies());

        annotationPropertyHierarchyProvider = new OWLAnnotationPropertyHierarchyProvider(manager);
        annotationPropertyHierarchyProvider.setOntologies(manager.getOntologies());

        entityEditorKit = projectConfiguration.getOWLEntityEditorKitFactory().createEntityEditorKit(this);

        metricsManager = new OWLAPIProjectMetricsManager(this);

    }


    public ProjectId getProjectId() {
        return documentStore.getProjectId();
    }

//    public OWLAPIProjectConfiguration getProjectConfiguration() {
//        return projectConfiguration;
//    }

    private void handleOntologiesChanged(List<? extends OWLOntologyChange> changes) {
        documentStore.saveOntologyChanges(Collections.unmodifiableList(changes));
    }



    public OWLAPIChangeManager getChangeManager() {
        return changeManager;
    }

    public AssertedClassHierarchyProvider getClassHierarchyProvider() {
        return classHierarchyProvider;
    }

    public OWLObjectPropertyHierarchyProvider getObjectPropertyHierarchyProvider() {
        return objectPropertyHierarchyProvider;
    }

    public OWLDataPropertyHierarchyProvider getDataPropertyHierarchyProvider() {
        return dataPropertyHierarchyProvider;
    }

    public OWLAnnotationPropertyHierarchyProvider getAnnotationPropertyHierarchyProvider() {
        return annotationPropertyHierarchyProvider;
    }

    public OWLAPIProjectMetricsManager getMetricsManager() {
        return metricsManager;
    }

    public OWLDataFactory getDataFactory() {
        return delegateManager.getOWLDataFactory();
    }

    public OWLAPISearchManager getSearchManager() {
        return searchManager;
    }

    public OWLAPIEntityEditorKit getOWLEntityEditorKit() {
        return entityEditorKit;
    }

    public OWLOntology getRootOntology() {
        return ontology;
    }

    public RenderingManager getRenderingManager() {
        return renderingManager;
    }

    public OWLAPINotesManager getNotesManager() {
        return notesManager;
    }

    public RevisionNumber getRevisionNumber() {
        try {
            projectChangeReadLock.lock();
            return changeManager.getCurrentRevision();
        }
        finally {
            projectChangeReadLock.unlock();
        }
    }

    public void applyChanges(UserId userId, List<OWLOntologyChange> changes, String changeDescription) {
        try {
            projectChangeWriteLock.lock();
            List<OWLOntologyChange> appliedChanges = delegateManager.applyChanges(changes);
            if (!appliedChanges.isEmpty()) {
                changeManager.logChanges(userId, appliedChanges, changeDescription);
                OWLAPIProjectMetadataManager metadataManager = OWLAPIProjectMetadataManager.getManager();
                metadataManager.setLastModifiedTime(getProjectId(), System.currentTimeMillis());
                metadataManager.setLastModifiedBy(getProjectId(), userId);

//                projectAttributes.setLongAttribute("lastModified", System.currentTimeMillis());
//                projectAttributes.setStringAttribute("lastModifiedBy", userId.getUserName());
            }
        }
        finally {
            projectChangeWriteLock.unlock();
        }
    }

}

package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricsManager;
import edu.stanford.bmir.protege.web.server.owlapi.notes.OWLAPINotesManagerNotesAPIImpl;
import edu.stanford.bmir.protege.web.server.owlapi.notes.OWLAPINotesManager;
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

    private ProjectId projectId;

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


    private OWLAPIProject() {
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

    }
    
    public OWLAPIProject(ProjectId projectId) throws ProjectDocumentNotFoundException, IOException, OWLOntologyCreationException, OWLParserException {
        this();
        OWLAPIProjectDocumentStoreImpl documentStore = OWLAPIProjectDocumentStoreImpl.getProjectDocumentStore(projectId);
        if(!documentStore.exists()) {
            throw new ProjectDocumentNotFoundException(projectId);
        }
        this.projectId = projectId;
        loadProject(projectId);
        initialiseProjectMachinery();

    }


    

    public OWLAPIProject(NewProjectSettings newProjectSettings) throws IOException, OWLParserException {
        this();
        OWLAPIProjectDocumentStoreImpl documentStore = OWLAPIProjectDocumentStoreImpl.createNewProject(newProjectSettings);
        this.projectId = new ProjectId(newProjectSettings.getProjectName());
        loadProject(projectId);
        initialiseProjectMachinery();
    }



    /**
     * Loads the specified project.  This method is only called from the constructor of this class.
     * @param projectId The project to be loaded.
     */
    private void loadProject(ProjectId projectId) throws IOException, BinaryOWLParseException {
        try {
            OWLAPIProjectDocumentStoreImpl documentStore = OWLAPIProjectDocumentStoreImpl.getProjectDocumentStore(projectId);
            if(!documentStore.exists()) {
                throw new ProjectDocumentNotFoundException(projectId);
            }
            ontology = documentStore.loadRootOntologyIntoManager(delegateManager);
            delegateManager.addOntologyChangeListener(new OWLOntologyChangeListener() {
                public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
                    handleOntologiesChanged(changes);
                }
            });
            manager.sealDelegate();
            projectAttributes = documentStore.getProjectAttributes();
            projectConfiguration = new OWLAPIProjectConfiguration(projectAttributes);


        }
        catch (OWLOntologyCreationException e) {
            throw new RuntimeException("Failed to load project: " + e.getMessage(), e);
        }
    }

    /**
     * Call from constructor only.
     */
    private void initialiseProjectMachinery() {
        renderingManager = new RenderingManager(this);

        searchManager = new OWLAPISearchManager(this);

        changeManager = new OWLAPIChangeManager(this);

        notesManager = new OWLAPINotesManagerNotesAPIImpl(this);


//        entityCreatorFactory = new OBOEntityCreatorFactory(this, 7);



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
        return projectId;
    }

    public OWLAPIProjectConfiguration getProjectConfiguration() {
        return projectConfiguration;
    }

    private void handleOntologiesChanged(List<? extends OWLOntologyChange> changes) {
        OWLAPIProjectDocumentStoreImpl documentStore = OWLAPIProjectDocumentStoreImpl.getProjectDocumentStore(projectId);
        documentStore.saveOntologyChanges(Collections.unmodifiableList(changes));
        
//
//        // Could check here to see if changes were the last ones that were logged.
//        Collection<OWLOntology> ontologies = new HashSet<OWLOntology>();
//        for (OWLOntologyChange change : changes) {
//            ontologies.add(change.getOntology());
//        }
//
//        for (OWLOntology ontology : ontologies) {
//            try {
//                manager.saveOntology(ontology);
//            }
//            catch (OWLOntologyStorageException e) {
//                e.printStackTrace();
//            }
//        }
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

    public long getRevision() {
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
            }
        }
        finally {
            projectChangeWriteLock.unlock();
        }
    }

}

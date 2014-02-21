package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.OntologyChangeSubjectProvider;
import edu.stanford.bmir.protege.web.server.crud.BrowserTextChangedEventComputer;
import edu.stanford.bmir.protege.web.shared.*;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudContext;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.crud.ProjectEntityCrudKitHandlerCache;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettings;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepositoryManager;
import edu.stanford.bmir.protege.web.server.events.EventLifeTime;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.events.HighLevelEventGenerator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.notes.OWLAPINotesManager;
import edu.stanford.bmir.protege.web.server.notes.OWLAPINotesManagerNotesAPIImpl;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricsManager;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManagerImpl;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.coode.owlapi.functionalrenderer.OWLFunctionalSyntaxOntologyStorer;
import org.coode.owlapi.owlxml.renderer.OWLXMLOntologyStorer;
import org.coode.owlapi.rdf.rdfxml.RDFXMLOntologyStorer;
import org.protege.editor.owl.model.hierarchy.OWLAnnotationPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLDataPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLObjectPropertyHierarchyProvider;
import org.protege.owlapi.model.ProtegeOWLOntologyManager;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.NonMappingOntologyIRIMapper;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapterEx;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.EmptyInMemOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.ParsableOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOntologyStorer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/03/2012
 */
public class OWLAPIProject implements HasDispose, HasDataFactory, HasContainsEntityInSignature, HasGetEntitiesWithIRI {




    public static final EventLifeTime PROJECT_EVENT_LIFE_TIME = EventLifeTime.get(60, TimeUnit.SECONDS);

    private static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(OWLAPIProject.class);

    private OWLAPIProjectDocumentStore documentStore;

    final private OWLAPIProjectOWLOntologyManager manager;

    final private ProjectAccessManager projectAccessManager;

    private RenderingManager renderingManager;

    private final EventManager<ProjectEvent<?>> projectEventManager;

    private OWLOntology ontology;

    private AssertedClassHierarchyProvider classHierarchyProvider = new AssertedClassHierarchyProvider(WebProtegeOWLManager.createOWLOntologyManager());

    private OWLObjectPropertyHierarchyProvider objectPropertyHierarchyProvider;

    private OWLDataPropertyHierarchyProvider dataPropertyHierarchyProvider;

    private OWLAnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider;

    private OWLAPISearchManager searchManager;

    private OWLAPINotesManager notesManager;

    private OWLAPIChangeManager changeManager;

    final private OWLOntologyManager delegateManager;

    private OWLAPIProjectMetricsManager metricsManager;

    // TODO Dependency injection
    private final WatchManager watchManager;


    private final ReadWriteLock projectChangeLock = new ReentrantReadWriteLock();

    private final Lock projectChangeReadLock = projectChangeLock.readLock();

    private final Lock projectChangeWriteLock = projectChangeLock.writeLock();

    private final Lock changeProcesssingLock = new ReentrantLock();

    private final ProjectEntityCrudKitHandlerCache entityCrudKitHandlerCache;

    private String defaultLanguage = "en";

    public static OWLAPIProject getProject(OWLAPIProjectDocumentStore documentStore) throws IOException, OWLParserException {
        return new OWLAPIProject(documentStore);
    }


    /**
     * Constructs and OWLAPIProject over the sources specified by the {@link OWLAPIProjectDocumentStore}.
     * @param documentStore The document store.
     * @throws IOException        If there was a problem reading sources.
     * @throws OWLParserException If there was a problem parsing sources.
     */
    private OWLAPIProject(OWLAPIProjectDocumentStore documentStore) throws IOException, OWLParserException {
        this.documentStore = documentStore;
        this.projectEventManager = EventManager.create(PROJECT_EVENT_LIFE_TIME);
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

        this.projectAccessManager = new ProjectAccessManager(getProjectId(), projectEventManager);
        this.watchManager = new WatchManagerImpl(this);

        entityCrudKitHandlerCache = new ProjectEntityCrudKitHandlerCache(getProjectId());
        loadProject();
        initialiseProjectMachinery();

    }


    /**
     * Loads the specified project.  This method is only called from the constructor of this class.
     */
    private void loadProject() throws IOException, BinaryOWLParseException {
        try {
            if (!documentStore.exists()) {
                throw new ProjectDocumentNotFoundException(documentStore.getProjectId());
            }
            ontology = documentStore.loadRootOntologyIntoManager(delegateManager);
            delegateManager.addOntologyChangeListener(new OWLOntologyChangeListener() {
                public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
                    handleOntologiesChanged(changes);
                }
            });
            manager.sealDelegate();
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


        // MH: All of this is highly dodgy and not at all thread safe.  It is therefore BROKEN!  Needs fixing.

        classHierarchyProvider = new AssertedClassHierarchyProvider(manager);
        classHierarchyProvider.setOntologies(manager.getOntologies());

        objectPropertyHierarchyProvider = new OWLObjectPropertyHierarchyProvider(manager);
        objectPropertyHierarchyProvider.setOntologies(manager.getOntologies());

        dataPropertyHierarchyProvider = new OWLDataPropertyHierarchyProvider(manager);
        dataPropertyHierarchyProvider.setOntologies(manager.getOntologies());

        annotationPropertyHierarchyProvider = new OWLAnnotationPropertyHierarchyProvider(manager);
        annotationPropertyHierarchyProvider.setOntologies(manager.getOntologies());

        metricsManager = new OWLAPIProjectMetricsManager(this);

    }


    public ProjectId getProjectId() {
        return documentStore.getProjectId();
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Determines if the specified entity is deprecated in this project.
     * @param entity The entity to test.
     * @return {@code true} if the entity is deprecated in this project, otherwise {@code false}.
     */
    public boolean isDeprecated(OWLEntity entity) {
        if (!getRootOntology().containsAnnotationPropertyInSignature(OWLRDFVocabulary.OWL_DEPRECATED.getIRI(), true)) {
            return false;
        }
        // TODO: Cache
        for (OWLOntology ont : getRootOntology().getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(entity.getIRI())) {
                if (ax.isDeprecatedIRIAssertion()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void handleOntologiesChanged(List<? extends OWLOntologyChange> changes) {
        documentStore.saveOntologyChanges(Collections.unmodifiableList(changes));
    }


    public EventManager<ProjectEvent<?>> getEventManager() {
        return projectEventManager;
    }

    public ProjectAccessManager getProjectAccessManager() {
        return projectAccessManager;
    }

    public WatchManager getWatchManager() {
        return watchManager;
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

    private <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(UserId userId, String shortName, EntityType<E> entityType) {
        // TODO: SWAP
        Optional<E> entity = getEntityOfTypeIfPresent(entityType, shortName);
        if (!entity.isPresent()) {
            OntologyChangeList.Builder<E> builder = OntologyChangeList.builder();
            E ent = getEntityCrudKitHandler().create(entityType, EntityShortForm.get(shortName), new EntityCrudContext(getRootOntology(), getDataFactory()), builder);
            return new OWLEntityCreator<E>(ent, builder.build().getChanges());
        }
        else {
            return new OWLEntityCreator<E>(entity.get(), Collections.<OWLOntologyChange>emptyList());
        }
    }

    public void setEntityCrudKitSettings(EntityCrudKitSettings<?> entityCrudKitSettings) {
        ProjectEntityCrudKitSettings projectSettings = new ProjectEntityCrudKitSettings(getProjectId(), entityCrudKitSettings);
        ProjectEntityCrudKitSettingsRepositoryManager.getRepository().save(projectSettings);
    }


    public EntityCrudKitHandler<?> getEntityCrudKitHandler() {
        return entityCrudKitHandlerCache.getHandler();
    }

    @SuppressWarnings("unchecked")
    private <E extends OWLEntity> Optional<E> getEntityOfTypeIfPresent(EntityType<E> entityType, String shortName) {
        for (OWLEntity entity : renderingManager.getEntities(shortName)) {
            if (entity.isType(entityType)) {
                return Optional.of((E) entity);
            }
        }
        return Optional.absent();
    }


    /**
     * Applies a list of changes to ontologies in this project.
     * @param userId The userId of the user applying the changes. Not {@code null}.
     * @param changes The list of changes to be applied.  Not {@code null}.
     * @param changeDescription A description of the changes. Not {@code null}.
     * @return A {@link ChangeApplicationResult} that describes the changes which took place an any renaminings.
     * @throws NullPointerException if any parameters are {@code null}.
     * @deprecated Use {@link #applyChanges(edu.stanford.bmir.protege.web.shared.user.UserId,
     *             edu.stanford.bmir.protege.web.server.change.ChangeListGenerator, ChangeDescriptionGenerator)}
     */
    public ChangeApplicationResult<?> applyChanges(UserId userId, List<OWLOntologyChange> changes, String changeDescription) {
        return applyChanges(userId, FixedChangeListGenerator.get(changes), FixedMessageChangeDescriptionGenerator.get(changeDescription));
    }

    /**
     * Applies ontology changes to the ontologies contained within a project.
     * @param userId The userId of the user applying the changes.  Not {@code null}.
     * @param changeListGenerator A generator which creates a list of changes (based on the state of the project at
     * the time of change application).  The idea behind passing in a change generator is that the list of changes to
     * be applied can be created based on the state of the project immediately before they are applied.  This is
     * necessary where the changes depend on the structure/state of the ontology.  This method guarantees that no third
     * party
     * ontology changes will take place between the {@link ChangeListGenerator#generateChanges(OWLAPIProject,
     * edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext)}
     * method being called and the changes being applied.
     * @param changeDescriptionGenerator A generator that describes the changes that took place.
     * @return A {@link ChangeApplicationResult} that describes the changes which took place an any renaminings.
     * @throws NullPointerException      if any parameters are {@code null}.
     * @throws PermissionDeniedException if the user identified by {@code userId} does not have permssion to write to
     *                                   ontologies in this project.
     */
    public <R> ChangeApplicationResult<R> applyChanges(final UserId userId, final ChangeListGenerator<R> changeListGenerator, final ChangeDescriptionGenerator<R> changeDescriptionGenerator) throws PermissionDeniedException {
        checkNotNull(userId);
        checkNotNull(changeListGenerator);
        checkNotNull(changeDescriptionGenerator);

        if (!getPermissionsManager().hasWriteAccess(userId)) {
            throw new PermissionDeniedException("You do not have permission to write to ontologies in this project");
        }

        final Set<OWLEntity> changeSignature = new HashSet<OWLEntity>();
        final List<OWLOntologyChange> appliedChanges;
        final ChangeApplicationResult<R> finalResult;


        // The following must take into consideration fresh entity IRIs.  Entity IRIs are minted on the server, so
        // ontology changes may contain fresh entity IRIs as place holders. We need to make sure these get replaced
        // with true entity IRIs
        try {
            // Compute the changes that need to take place.  We don't allow any other writes here because the
            // generation of the changes may depend upon the state of the project
            changeProcesssingLock.lock();

            final ChangeGenerationContext context = new ChangeGenerationContext(userId);
            OntologyChangeList<R> gen = changeListGenerator.generateChanges(this, context);

            // We have our changes

            List<OWLOntologyChange> changes = gen.getChanges();

            // We coin fresh entities for places where tmp: is the scheme - the name for the entity comes from
            // the fragment
            final Map<IRI, IRI> iriRenameMap = new HashMap<IRI, IRI>();

            Set<OWLOntologyChange> changesToRename = new HashSet<OWLOntologyChange>();
            List<OWLOntologyChange> freshEntityChanges = new ArrayList<OWLOntologyChange>();
            for (OWLOntologyChange change : changes) {
                for (OWLEntity entity : change.getSignature()) {
                    if (DataFactory.isFreshEntity(entity)) {
                        changesToRename.add(change);
                        IRI currentIRI = entity.getIRI();
                        if (!iriRenameMap.containsKey(currentIRI)) {
                            String shortName = DataFactory.getFreshEntityShortName(entity);
                            OWLEntityCreator<? extends OWLEntity> creator = getEntityCreator(userId, shortName, (EntityType<? extends OWLEntity>) entity.getEntityType());
                            freshEntityChanges.addAll(creator.getChanges());
                            IRI replacementIRI = creator.getEntity().getIRI();
                            iriRenameMap.put(currentIRI, replacementIRI);
                        }
                    }
                }
            }


            List<OWLOntologyChange> allChangesIncludingRenames = new ArrayList<OWLOntologyChange>();
            final OWLObjectDuplicator duplicator = new OWLObjectDuplicator(manager.getOWLDataFactory(), iriRenameMap);
            for (OWLOntologyChange change : changes) {
                if (changesToRename.contains(change)) {
                    OWLOntologyChange replacementChange = getRenamedChange(change, duplicator);
                    allChangesIncludingRenames.add(replacementChange);
                }
                else {
                    allChangesIncludingRenames.add(change);
                }
            }

            allChangesIncludingRenames.addAll(freshEntityChanges);

            List<OWLOntologyChange> minimisedChanges = getMinimisedChanges(allChangesIncludingRenames);

            for (OWLOntologyChange change : minimisedChanges) {
                changeSignature.addAll(change.getSignature());
            }

            List<HierarchyChangeComputer<?>> computers = createHierarchyChangeComputers();
            for(HierarchyChangeComputer<?> computer : computers) {
                computer.prepareForChanges(minimisedChanges);
            }
            BrowserTextChangedEventComputer shortFormChangeComputer = new BrowserTextChangedEventComputer(
                    new OntologyChangeSubjectProvider(this),
                    renderingManager.getShortFormProvider(),
                    this
            );
            shortFormChangeComputer.prepareForChanges(minimisedChanges);



            // Now we do the actual changing, so we lock the project here.  No writes or reads can take place whilst
            // we apply the changes
            RevisionNumber revisionNumber;
            try {
                projectChangeWriteLock.lock();
                appliedChanges = delegateManager.applyChanges(minimisedChanges);
                final RenameMap renameMap = new RenameMap(iriRenameMap);
                Optional<R> renamedResult = getRenamedResult(changeListGenerator, gen.getResult(), renameMap);
                finalResult = new ChangeApplicationResult<R>(renamedResult, appliedChanges, renameMap);
                if (!appliedChanges.isEmpty()) {
                    logAppliedChanges(userId, finalResult, changeDescriptionGenerator);
                }
                revisionNumber = changeManager.getCurrentRevision();
            }
            finally {
                // Release for reads
                projectChangeWriteLock.unlock();
            }

            LOGGER.info(getProjectId(), "%s applied %d changes to %s", userId, appliedChanges.size(), getProjectId());

            if (!(changeListGenerator instanceof SilentChangeListGenerator)) {
                List<ProjectEvent<?>> highLevelEvents = new ArrayList<ProjectEvent<?>>();
                HighLevelEventGenerator hle = new HighLevelEventGenerator(this, userId, revisionNumber);
                highLevelEvents.addAll(hle.getHighLevelEvents(appliedChanges, revisionNumber));
                highLevelEvents.addAll(shortFormChangeComputer.getShortFormChanges(appliedChanges, getProjectId()));

                for(HierarchyChangeComputer<?> computer : computers) {
                    highLevelEvents.addAll(computer.get(appliedChanges));
                }
                if(changeListGenerator instanceof HasHighLevelEvents) {
                    highLevelEvents.addAll(((HasHighLevelEvents) changeListGenerator).getHighLevelEvents());
                }
                projectEventManager.postEvents(highLevelEvents);
            }
        }
        finally {
            changeProcesssingLock.unlock();
        }

        return finalResult;


    }

    private List<HierarchyChangeComputer<?>> createHierarchyChangeComputers() {
        List<HierarchyChangeComputer<?>> computers = new ArrayList<HierarchyChangeComputer<?>>();
        computers.add(new HierarchyChangeComputer<OWLClass>(getProjectId(), EntityType.CLASS, classHierarchyProvider, HierarchyId.CLASS_HIERARCHY) {
            @Override
            protected HierarchyChangedEvent<OWLClass, ?> createRemovedEvent(OWLClass child, OWLClass parent) {
                return new ClassHierarchyParentRemovedEvent(getProjectId(), child, parent, HierarchyId.CLASS_HIERARCHY);
            }

            @Override
            protected HierarchyChangedEvent<OWLClass, ?> createAddedEvent(OWLClass child, OWLClass parent) {
                return new ClassHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.CLASS_HIERARCHY);
            }
        });
        computers.add(new HierarchyChangeComputer<OWLObjectProperty>(getProjectId(), EntityType.OBJECT_PROPERTY, objectPropertyHierarchyProvider, HierarchyId.OBJECT_PROPERTY_HIERARCHY) {
            @Override
            protected HierarchyChangedEvent<OWLObjectProperty, ?> createRemovedEvent(OWLObjectProperty child, OWLObjectProperty parent) {
                return new ObjectPropertyHierarchyParentRemovedEvent(getProjectId(), child, parent, HierarchyId.OBJECT_PROPERTY_HIERARCHY);
            }

            @Override
            protected HierarchyChangedEvent<OWLObjectProperty, ?> createAddedEvent(OWLObjectProperty child, OWLObjectProperty parent) {
                return new ObjectPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.OBJECT_PROPERTY_HIERARCHY);
            }
        });
        computers.add(new HierarchyChangeComputer<OWLDataProperty>(getProjectId(), EntityType.DATA_PROPERTY, dataPropertyHierarchyProvider, HierarchyId.DATA_PROPERTY_HIERARCHY) {
            @Override
            protected HierarchyChangedEvent<OWLDataProperty, ?> createRemovedEvent(OWLDataProperty child, OWLDataProperty parent) {
                return new DataPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.DATA_PROPERTY_HIERARCHY);
            }

            @Override
            protected HierarchyChangedEvent<OWLDataProperty, ?> createAddedEvent(OWLDataProperty child, OWLDataProperty parent) {
                return new DataPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.DATA_PROPERTY_HIERARCHY);
            }
        });
        computers.add(new HierarchyChangeComputer<OWLAnnotationProperty>(getProjectId(), EntityType.ANNOTATION_PROPERTY, annotationPropertyHierarchyProvider, HierarchyId.ANNOTATION_PROPERTY_HIERARCHY) {
            @Override
            protected HierarchyChangedEvent<OWLAnnotationProperty, ?> createRemovedEvent(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
                return new AnnotationPropertyHierarchyParentRemovedEvent(getProjectId(), child, parent, HierarchyId.ANNOTATION_PROPERTY_HIERARCHY);
            }

            @Override
            protected HierarchyChangedEvent<OWLAnnotationProperty, ?> createAddedEvent(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
                return new AnnotationPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.ANNOTATION_PROPERTY_HIERARCHY);
            }
        });
        return computers;
    }

    private List<OWLOntologyChange> getMinimisedChanges(List<OWLOntologyChange> allChangesIncludingRenames) {
        Set<OWLAxiom> axiomsToAdd = new HashSet<OWLAxiom>();
        Set<OWLAxiom> axiomsToRemove = new HashSet<OWLAxiom>();
        for (OWLOntologyChange change : allChangesIncludingRenames) {
            if (change.isAddAxiom()) {
                axiomsToAdd.add(change.getAxiom());
                axiomsToRemove.remove(change.getAxiom());
            }
            else if (change.isRemoveAxiom()) {
                axiomsToRemove.add(change.getAxiom());
                axiomsToAdd.remove(change.getAxiom());
            }
        }

        // Minimise changes
        List<OWLOntologyChange> minimisedChanges = new ArrayList<OWLOntologyChange>();
        for (OWLOntologyChange change : allChangesIncludingRenames) {
            if (change.isAddAxiom()) {
                if (axiomsToAdd.contains(change.getAxiom())) {
                    minimisedChanges.add(change);
                }
            }
            else if (change.isRemoveAxiom()) {
                if (axiomsToRemove.contains(change.getAxiom())) {
                    minimisedChanges.add(change);
                }
            }
            else {
                minimisedChanges.add(change);
            }
        }
        return minimisedChanges;
    }

    /**
     * Renames a result if it is present.
     * @param result The result to process.
     * @param renameMap The rename map.
     * @param <R> The type of result.
     * @return The renamed (or untouched if no rename was necessary) result.
     */
    private <R> Optional<R> getRenamedResult(ChangeListGenerator<R> changeListGenerator, Optional<R> result, RenameMap renameMap) {
        Optional<R> renamedResult;
        if (result.isPresent()) {
            R actualResult = result.get();
            renamedResult = Optional.of(changeListGenerator.getRenamedResult(actualResult, renameMap));
        }
        else {
            renamedResult = result;
        }
        return renamedResult;
    }


    private <R> void logAppliedChanges(UserId userId, ChangeApplicationResult<R> finalResult, ChangeDescriptionGenerator<R> changeDescriptionGenerator) {
        // Generate a description for the changes that were actually applied
        String changeDescription = changeDescriptionGenerator.generateChangeDescription(finalResult);
        // Log the changes
        changeManager.logChanges(userId, finalResult.getChangeList(), changeDescription);
        // Log some metadata about when the changes were applied
        OWLAPIProjectMetadataManager metadataManager = OWLAPIProjectMetadataManager.getManager();
        metadataManager.setLastModifiedTime(getProjectId(), System.currentTimeMillis());
        metadataManager.setLastModifiedBy(getProjectId(), userId);
    }


    /**
     * Gets an ontology change which is a copy of an existing ontology change except for IRIs that are renamed.
     * Renamings
     * are specified by a rename map.
     * @param change The change to copy.
     * @param duplicator A duplicator used to rename IRIs
     * @return The ontology change with the renamings.
     */
    private OWLOntologyChange getRenamedChange(OWLOntologyChange change, final OWLObjectDuplicator duplicator) {
        return change.accept(new OWLOntologyChangeVisitorAdapterEx<OWLOntologyChange>() {

            @SuppressWarnings("unchecked")
            private <T extends OWLObject> T duplicate(T ax) {
                OWLObject object = duplicator.duplicateObject(ax);
                return (T) object;
            }

            @Override
            public OWLOntologyChange visit(RemoveAxiom change) {
                return new RemoveAxiom(change.getOntology(), duplicate(change.getAxiom()));
            }

            @Override
            public OWLOntologyChange visit(SetOntologyID change) {
                return change;
            }

            @Override
            public OWLOntologyChange visit(AddAxiom change) {
                return new AddAxiom(change.getOntology(), duplicate(change.getAxiom()));
            }

            @Override
            public OWLOntologyChange visit(AddImport change) {
                return change;
            }

            @Override
            public OWLOntologyChange visit(RemoveImport change) {
                return change;
            }

            @Override
            public OWLOntologyChange visit(AddOntologyAnnotation change) {
                return new AddOntologyAnnotation(change.getOntology(), duplicate(change.getAnnotation()));
            }

            @Override
            public OWLOntologyChange visit(RemoveOntologyAnnotation change) {
                return new RemoveOntologyAnnotation(change.getOntology(), duplicate(change.getAnnotation()));
            }
        });
    }

    @Override
    public boolean containsEntityInSignature(OWLEntity entity) {
        return getRootOntology().containsEntityInSignature(entity, true);
    }

    @Override
    public Set<OWLEntity> getEntitiesWithIRI(IRI iri) {
        return getRootOntology().getEntitiesInSignature(iri, true);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // TODO:

    private final ProjectPermissionsManager permissionsManager = new DummyPermissionsManager();

    private ProjectPermissionsManager getPermissionsManager() {
        return permissionsManager;
    }



    private static class DummyPermissionsManager implements ProjectPermissionsManager {

        @Override
        public boolean hasReadAccess(UserId userId) {
            return true;
        }

        @Override
        public boolean hasWriteAccess(UserId userId) {
            return true;
        }

        @Override
        public boolean hasCommentAccess(UserId userId) {
            return true;
        }
    }


    @Override
    public void dispose() {
        projectEventManager.dispose();
        classHierarchyProvider.dispose();
        objectPropertyHierarchyProvider.dispose();
        dataPropertyHierarchyProvider.dispose();
        annotationPropertyHierarchyProvider.dispose();
        projectAccessManager.dispose();

    }
}

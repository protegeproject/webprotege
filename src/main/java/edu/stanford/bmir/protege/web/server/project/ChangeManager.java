package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.crud.*;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.events.EventTranslatorManager;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLAnnotationPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLDataPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.owlapi.OWLEntityCreator;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.webhook.ProjectChangedWebhookInvoker;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;
import org.semanticweb.owlapi.vocab.Namespaces;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.access.Subject.forUser;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Jun 2017
 */
@ProjectSingleton
public class ChangeManager implements HasApplyChanges {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Nonnull
    private final ProjectChangedWebhookInvoker projectChangedWebhookInvoker;

    @Nonnull
    private final EventManager<ProjectEvent<?>> projectEventManager;

    @Nonnull
    private final Provider<EventTranslatorManager> eventTranslatorManagerProvider;

    @Nonnull
    private final ProjectEntityCrudKitHandlerCache entityCrudKitHandlerCache;

    @Nonnull
    private final RevisionManager changeManager;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ProjectDocumentStore documentStore;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final OWLObjectPropertyHierarchyProvider objectPropertyHierarchyProvider;

    @Nonnull
    private final OWLDataPropertyHierarchyProvider dataPropertyHierarchyProvider;

    @Nonnull
    private final OWLAnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider;

    @Nonnull
    private final ReadWriteLock projectChangeLock = new ReentrantReadWriteLock();

    @Nonnull
    private final Lock projectChangeReadLock = projectChangeLock.readLock();

    @Nonnull
    private final Lock projectChangeWriteLock = projectChangeLock.writeLock();

    @Nonnull
    private final Lock changeProcesssingLock = new ReentrantLock();

    @Inject
    public ChangeManager(@Nonnull ProjectId projectId,
                         @Nonnull OWLOntology rootOntology,
                         @Nonnull AccessManager accessManager,
                         @Nonnull ProjectDetailsRepository projectDetailsRepository,
                         @Nonnull ProjectChangedWebhookInvoker projectChangedWebhookInvoker,
                         @Nonnull EventManager<ProjectEvent<?>> projectEventManager,
                         @Nonnull Provider<EventTranslatorManager> eventTranslatorManagerProvider,
                         @Nonnull ProjectEntityCrudKitHandlerCache entityCrudKitHandlerCache,
                         @Nonnull RevisionManager changeManager,
                         @Nonnull OWLDataFactory dataFactory,
                         @Nonnull RenderingManager renderingManager,
                         @Nonnull ProjectDocumentStore documentStore,
                         @Nonnull ClassHierarchyProvider classHierarchyProvider,
                         @Nonnull OWLObjectPropertyHierarchyProvider objectPropertyHierarchyProvider,
                         @Nonnull OWLDataPropertyHierarchyProvider dataPropertyHierarchyProvider,
                         @Nonnull OWLAnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider) {
        this.projectId = projectId;
        this.rootOntology = rootOntology;
        this.accessManager = accessManager;
        this.projectDetailsRepository = projectDetailsRepository;
        this.projectChangedWebhookInvoker = projectChangedWebhookInvoker;
        this.projectEventManager = projectEventManager;
        this.eventTranslatorManagerProvider = eventTranslatorManagerProvider;
        this.entityCrudKitHandlerCache = entityCrudKitHandlerCache;
        this.changeManager = changeManager;
        this.dataFactory = dataFactory;
        this.renderingManager = renderingManager;
        this.documentStore = documentStore;
        this.classHierarchyProvider = classHierarchyProvider;
        this.objectPropertyHierarchyProvider = objectPropertyHierarchyProvider;
        this.dataPropertyHierarchyProvider = dataPropertyHierarchyProvider;
        this.annotationPropertyHierarchyProvider = annotationPropertyHierarchyProvider;
    }

    /**
     * Applies a list of changes to ontologies in this project.
     *
     * @param userId            The userId of the user applying the changes. Not {@code null}.
     * @param changes           The list of changes to be applied.  Not {@code null}.
     * @param changeDescription A description of the changes. Not {@code null}.
     * @return A {@link ChangeApplicationResult} that describes the changes which took place an any renaminings.
     * @throws NullPointerException if any parameters are {@code null}.
     * @deprecated Use {@link #applyChanges(UserId,
     * ChangeListGenerator, ChangeDescriptionGenerator)}
     */
    @Deprecated
    public ChangeApplicationResult<?> applyChanges(UserId userId,
                                                   List<OWLOntologyChange> changes,
                                                   String changeDescription) {
        return applyChanges(userId,
                            FixedChangeListGenerator.get(changes),
                            FixedMessageChangeDescriptionGenerator.get(changeDescription));
    }

    /**
     * Applies ontology changes to the ontologies contained within a project.
     *
     * @param userId                     The userId of the user applying the changes.  Not {@code null}.
     * @param changeListGenerator        A generator which creates a list of changes (based on the state of the project at
     *                                   the time of change application).  The idea behind passing in a change generator is that the list of changes to
     *                                   be applied can be created based on the state of the project immediately before they are applied.  This is
     *                                   necessary where the changes depend on the structure/state of the ontology.  This method guarantees that no third
     *                                   party
     *                                   ontology changes will take place between the {@link ChangeListGenerator#generateChanges(ChangeGenerationContext)}
     *                                   method being called and the changes being applied.
     * @param changeDescriptionGenerator A generator that describes the changes that took place.
     * @return A {@link ChangeApplicationResult} that describes the changes which took place an any renaminings.
     * @throws NullPointerException      if any parameters are {@code null}.
     * @throws PermissionDeniedException if the user identified by {@code userId} does not have permssion to write to
     *                                   ontologies in this project.
     */
    @Override
    public <R> ChangeApplicationResult<R> applyChanges(final UserId userId,
                                                       final ChangeListGenerator<R> changeListGenerator,
                                                       final ChangeDescriptionGenerator<R> changeDescriptionGenerator) throws PermissionDeniedException {
        //noinspection ResultOfMethodCallIgnored
        checkNotNull(userId);
        //noinspection ResultOfMethodCallIgnored
        checkNotNull(changeListGenerator);
        //noinspection ResultOfMethodCallIgnored
        checkNotNull(changeDescriptionGenerator);

        // Final check of whether the user can actually edit the project
        Subject subject = forUser(userId);
        ProjectResource projectResource = new ProjectResource(projectId);
        if (!accessManager.hasPermission(subject,
                                         projectResource,
                                         EDIT_ONTOLOGY.getActionId())) {
            throw new PermissionDeniedException("You do not have permission to edit this project");
        }

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
            OntologyChangeList<R> gen = changeListGenerator.generateChanges(context);

            // We have our changes

            List<OWLOntologyChange> changes = gen.getChanges();

            // We coin fresh entities for places where tmp: is the scheme - the name for the entity comes from
            // the fragment
            final Map<IRI, IRI> iriRenameMap = new HashMap<>();

            final ChangeSetEntityCrudSession session = getEntityCrudKitHandler().createChangeSetSession();
            Set<OWLOntologyChange> changesToRename = new HashSet<>();
            List<OWLOntologyChange> freshEntityChanges = new ArrayList<>();
            for (OWLOntologyChange change : changes) {
                for (OWLEntity entity : change.getSignature()) {
                    if (DataFactory.isFreshEntity(entity)) {
                        if (entity.isOWLClass()) {
                            if (!accessManager.hasPermission(subject, projectResource, CREATE_CLASS.getActionId())) {
                                throw new PermissionDeniedException("You do not have permission to create new classes");
                            }
                        }
                        else if (entity.isOWLObjectProperty() || entity.isOWLDataProperty() || entity.isOWLAnnotationProperty()) {
                            if (!accessManager.hasPermission(subject, projectResource, CREATE_PROPERTY.getActionId())) {
                                throw new PermissionDeniedException("You do not have permission to create new properties");
                            }
                        }
                        else if (entity.isOWLNamedIndividual()) {
                            if (!accessManager.hasPermission(subject,
                                                             projectResource,
                                                             CREATE_INDIVIDUAL.getActionId())) {
                                throw new PermissionDeniedException("You do not have permission to create new individuals");
                            }
                        }
                        else if (entity.isOWLDatatype()) {
                            if (!accessManager.hasPermission(subject, projectResource, CREATE_DATATYPE.getActionId())) {
                                throw new PermissionDeniedException("You do not have permission to create new datatypes");
                            }
                        }
                        changesToRename.add(change);
                        IRI currentIRI = entity.getIRI();
                        if (!iriRenameMap.containsKey(currentIRI)) {
                            String shortName = DataFactory.getFreshEntityShortName(entity);
                            OWLEntityCreator<? extends OWLEntity> creator = getEntityCreator(session,
                                                                                             userId,
                                                                                             shortName,
                                                                                             (EntityType<? extends OWLEntity>) entity
                                                                                                     .getEntityType());
                            freshEntityChanges.addAll(creator.getChanges());
                            IRI replacementIRI = creator.getEntity().getIRI();
                            iriRenameMap.put(currentIRI, replacementIRI);
                        }
                    }
                }
            }


            List<OWLOntologyChange> allChangesIncludingRenames = new ArrayList<>();
            final OWLObjectDuplicator duplicator = new OWLObjectDuplicator(dataFactory, iriRenameMap);
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

            final EventTranslatorManager eventTranslatorManager = eventTranslatorManagerProvider.get();
            eventTranslatorManager.prepareForOntologyChanges(minimisedChanges);

            // Now we do the actual changing, so we lock the project here.  No writes or reads can take place whilst
            // we apply the changes
            final Optional<Revision> revision;
            try {
                projectChangeWriteLock.lock();
                ProjectOWLOntologyManager manager = ((ProjectOWLOntologyManager) rootOntology.getOWLOntologyManager());
                List<OWLOntologyChange> effectiveChanges = getEffectiveChanges(minimisedChanges);
                manager.getDelegate().applyChanges(effectiveChanges);
                appliedChanges = effectiveChanges;
                final RenameMap renameMap = new RenameMap(iriRenameMap);
                Optional<R> renamedResult = getRenamedResult(changeListGenerator, gen.getResult(), renameMap);
                finalResult = new ChangeApplicationResult<>(renamedResult, appliedChanges, renameMap);
                if (!appliedChanges.isEmpty()) {
                    Revision rev = logAndBroadcastAppliedChanges(userId, finalResult, changeDescriptionGenerator);
                    revision = Optional.of(rev);
                    projectDetailsRepository.setModified(projectId, rev.getTimestamp(), userId);
                }
                else {
                    revision = Optional.empty();
                }
            } finally {
                // Release for reads
                projectChangeWriteLock.unlock();
            }


            if (revision.isPresent() && !(changeListGenerator instanceof SilentChangeListGenerator)) {
                List<ProjectEvent<?>> highLevelEvents = new ArrayList<>();
                Revision rev = revision.get();
                eventTranslatorManager.translateOntologyChanges(rev, appliedChanges, highLevelEvents);
                if (changeListGenerator instanceof HasHighLevelEvents) {
                    highLevelEvents.addAll(((HasHighLevelEvents) changeListGenerator).getHighLevelEvents());
                }
                projectEventManager.postEvents(highLevelEvents);
                projectChangedWebhookInvoker.invoke(userId,
                                                    rev.getRevisionNumber(),
                                                    rev.getTimestamp());
            }
        } finally {
            changeProcesssingLock.unlock();
        }

        return finalResult;


    }

    private List<OWLOntologyChange> getEffectiveChanges(List<OWLOntologyChange> minimisedChanges) {
        return minimisedChanges.stream()
                               .filter(this::isEffectiveChange)
                               .collect(toList());
    }

    private boolean isEffectiveChange(OWLOntologyChange chg) {
        return chg.accept(new OWLOntologyChangeVisitorEx<Boolean>() {
            @Nonnull
            @Override
            public Boolean visit(@Nonnull AddAxiom addAxiom) {
                return !addAxiom.getOntology().containsAxiom(addAxiom.getAxiom());
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull RemoveAxiom removeAxiom) {
                return removeAxiom.getOntology().containsAxiom(removeAxiom.getAxiom());
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull SetOntologyID setOntologyID) {
                return false;
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull AddImport addImport) {
                return !addImport.getOntology().getImportsDeclarations().contains(addImport.getImportDeclaration());
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull RemoveImport removeImport) {
                return removeImport.getOntology()
                                   .getImportsDeclarations()
                                   .contains(removeImport.getImportDeclaration());
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull AddOntologyAnnotation addOntologyAnnotation) {
                return !addOntologyAnnotation.getOntology()
                                             .getAnnotations()
                                             .contains(addOntologyAnnotation.getAnnotation());
            }

            @Nonnull
            @Override
            public Boolean visit(@Nonnull RemoveOntologyAnnotation removeOntologyAnnotation) {
                return removeOntologyAnnotation.getOntology()
                                               .getAnnotations()
                                               .contains(removeOntologyAnnotation.getAnnotation());
            }
        });
    }

    private List<OWLOntologyChange> getMinimisedChanges(List<OWLOntologyChange> allChangesIncludingRenames) {
        return new ChangeListMinimiser().getMinimisedChanges(allChangesIncludingRenames);
    }

    /**
     * Renames a result if it is present.
     *
     * @param result    The result to process.
     * @param renameMap The rename map.
     * @param <R>       The type of result.
     * @return The renamed (or untouched if no rename was necessary) result.
     */
    private <R> Optional<R> getRenamedResult(ChangeListGenerator<R> changeListGenerator,
                                             Optional<R> result,
                                             RenameMap renameMap) {
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


    private <R> Revision logAndBroadcastAppliedChanges(UserId userId,
                                                       ChangeApplicationResult<R> finalResult,
                                                       ChangeDescriptionGenerator<R> changeDescriptionGenerator) {
        // Generate a description for the changes that were actually applied
        String changeDescription = changeDescriptionGenerator.generateChangeDescription(finalResult);
        // Log the changes
        List<OWLOntologyChangeRecord> changeRecords = finalResult.getChangeList()
                                                                 .stream()
                                                                 .map(OWLOntologyChange::getChangeRecord)
                                                                 .collect(toList());
        Revision revision = changeManager.addRevision(userId, changeRecords, changeDescription);

        // TODO: THis list of "listeners" should be injected
        List<OWLOntologyChange> changes = finalResult.getChangeList();
        documentStore.saveOntologyChanges(changes);

        classHierarchyProvider.handleChanges(changes);
        objectPropertyHierarchyProvider.handleChanges(changes);
        dataPropertyHierarchyProvider.handleChanges(changes);
        annotationPropertyHierarchyProvider.handleChanges(changes);
//        metricsManager.handleOntologyChanges(changes);

        return revision;
    }


    /**
     * Gets an ontology change which is a copy of an existing ontology change except for IRIs that are renamed.
     * Renamings
     * are specified by a rename map.
     *
     * @param change     The change to copy.
     * @param duplicator A duplicator used to rename IRIs
     * @return The ontology change with the renamings.
     */
    private OWLOntologyChange getRenamedChange(OWLOntologyChange change, final OWLObjectDuplicator duplicator) {
        return change.accept(new OWLOntologyChangeVisitorEx<OWLOntologyChange>() {

            @SuppressWarnings("unchecked")
            private <T extends OWLObject> T duplicate(T ax) {
                OWLObject object = duplicator.duplicateObject(ax);
                return (T) object;
            }

            @Nonnull
            @Override
            public OWLOntologyChange visit(@Nonnull RemoveAxiom change) {
                return new RemoveAxiom(change.getOntology(), duplicate(change.getAxiom()));
            }

            @Nonnull
            @Override
            public OWLOntologyChange visit(@Nonnull SetOntologyID change) {
                return change;
            }

            @Nonnull
            @Override
            public OWLOntologyChange visit(@Nonnull AddAxiom change) {
                return new AddAxiom(change.getOntology(), duplicate(change.getAxiom()));
            }

            @Nonnull
            @Override
            public OWLOntologyChange visit(@Nonnull AddImport change) {
                return change;
            }

            @Nonnull
            @Override
            public OWLOntologyChange visit(@Nonnull RemoveImport change) {
                return change;
            }

            @Nonnull
            @Override
            public OWLOntologyChange visit(@Nonnull AddOntologyAnnotation change) {
                return new AddOntologyAnnotation(change.getOntology(), duplicate(change.getAnnotation()));
            }

            @Nonnull
            @Override
            public OWLOntologyChange visit(@Nonnull RemoveOntologyAnnotation change) {
                return new RemoveOntologyAnnotation(change.getOntology(), duplicate(change.getAnnotation()));
            }
        });
    }


    private <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(ChangeSetEntityCrudSession session,
                                                                       UserId userId,
                                                                       String shortName,
                                                                       EntityType<E> entityType) {
        Optional<E> entity = getEntityOfTypeIfPresent(entityType, shortName);
        if (entity.isPresent()) {
            return new OWLEntityCreator<>(entity.get(), Collections.emptyList());
        }
        OntologyChangeList.Builder<E> builder = OntologyChangeList.builder();
        EntityCrudKitHandler<EntityCrudKitSuffixSettings, ChangeSetEntityCrudSession> handler =
                getEntityCrudKitHandler();
        handler.createChangeSetSession();
        E ent = handler.create(session, entityType,
                               EntityShortForm.get(shortName),
                               getEntityCrudContext(userId),
                               builder);
        return new OWLEntityCreator<>(ent, builder.build().getChanges());

    }

    public EntityCrudContext getEntityCrudContext(UserId userId) {
        PrefixedNameExpander expander = PrefixedNameExpander.builder().withNamespaces(Namespaces.values()).build();
        return new EntityCrudContext(userId, rootOntology, dataFactory, expander);
    }

    @SuppressWarnings("unchecked")
    private <E extends OWLEntity> Optional<E> getEntityOfTypeIfPresent(EntityType<E> entityType, String shortName) {
        for (OWLEntity entity : renderingManager.getEntities(shortName)) {
            if (entity.isType(entityType)) {
                return Optional.of((E) entity);
            }
        }
        return Optional.empty();
    }


    @SuppressWarnings("unchecked")
    public <S extends EntityCrudKitSuffixSettings, C extends ChangeSetEntityCrudSession> EntityCrudKitHandler<S, C> getEntityCrudKitHandler() {
        return (EntityCrudKitHandler<S, C>) entityCrudKitHandlerCache.getHandler();
    }
}

package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
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
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLEntityCreator;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMapFactory;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryUpdatesProcessor;
import edu.stanford.bmir.protege.web.server.webhook.ProjectChangedWebhookInvoker;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

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

    private static final OWLOntologyChangeVisitorEx<Boolean> EFFECTIVE_CHANGE_FILTER = new OWLOntologyChangeVisitorEx<>() {
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
            return removeImport.getOntology().getImportsDeclarations().contains(removeImport.getImportDeclaration());
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull AddOntologyAnnotation addOntologyAnnotation) {
            return !addOntologyAnnotation
                    .getOntology()
                    .getAnnotations()
                    .contains(addOntologyAnnotation.getAnnotation());
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull RemoveOntologyAnnotation removeOntologyAnnotation) {
            return removeOntologyAnnotation
                    .getOntology()
                    .getAnnotations()
                    .contains(removeOntologyAnnotation.getAnnotation());
        }
    };

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final DictionaryUpdatesProcessor dictionaryUpdatesProcessor;

    @Nonnull
    private final ActiveLanguagesManager activeLanguagesManager;

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final PrefixDeclarationsStore prefixDeclarationsStore;

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
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final OWLObjectPropertyHierarchyProvider objectPropertyHierarchyProvider;

    @Nonnull
    private final OWLDataPropertyHierarchyProvider dataPropertyHierarchyProvider;

    @Nonnull
    private final OWLAnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider;

    @Nonnull
    private final UserInSessionFactory userInSessionFactory;

    @Nonnull
    private final EntityCrudContextFactory entityCrudContextFactory;

    @Nonnull
    private final ReadWriteLock projectChangeLock = new ReentrantReadWriteLock();

    @Nonnull
    private final Lock projectChangeWriteLock = projectChangeLock.writeLock();

    @Nonnull
    private final Lock changeProcesssingLock = new ReentrantLock();

    @Nonnull
    private final RenameMapFactory renameMapFactory;

    @Nonnull
    private final BuiltInPrefixDeclarations builtInPrefixDeclarations;

    @Inject
    public ChangeManager(@Nonnull ProjectId projectId,
                         @Nonnull OWLOntology rootOntology,
                         @Nonnull DictionaryUpdatesProcessor dictionaryUpdatesProcessor,
                         @Nonnull ActiveLanguagesManager activeLanguagesManager,
                         @Nonnull AccessManager accessManager,
                         @Nonnull PrefixDeclarationsStore prefixDeclarationsStore,
                         @Nonnull ProjectDetailsRepository projectDetailsRepository,
                         @Nonnull ProjectChangedWebhookInvoker projectChangedWebhookInvoker,
                         @Nonnull EventManager<ProjectEvent<?>> projectEventManager,
                         @Nonnull Provider<EventTranslatorManager> eventTranslatorManagerProvider,
                         @Nonnull ProjectEntityCrudKitHandlerCache entityCrudKitHandlerCache,
                         @Nonnull RevisionManager changeManager,
                         @Nonnull OWLDataFactory dataFactory,
                         @Nonnull DictionaryManager dictionaryManager,
                         @Nonnull ClassHierarchyProvider classHierarchyProvider,
                         @Nonnull OWLObjectPropertyHierarchyProvider objectPropertyHierarchyProvider,
                         @Nonnull OWLDataPropertyHierarchyProvider dataPropertyHierarchyProvider,
                         @Nonnull OWLAnnotationPropertyHierarchyProvider annotationPropertyHierarchyProvider,
                         @Nonnull UserInSessionFactory userInSessionFactory,
                         @Nonnull EntityCrudContextFactory entityCrudContextFactory,
                         @Nonnull RenameMapFactory renameMapFactory,
                         @Nonnull BuiltInPrefixDeclarations builtInPrefixDeclarations) {
        this.projectId = projectId;
        this.rootOntology = rootOntology;
        this.dictionaryUpdatesProcessor = dictionaryUpdatesProcessor;
        this.activeLanguagesManager = activeLanguagesManager;
        this.accessManager = accessManager;
        this.prefixDeclarationsStore = prefixDeclarationsStore;
        this.projectDetailsRepository = projectDetailsRepository;
        this.projectChangedWebhookInvoker = projectChangedWebhookInvoker;
        this.projectEventManager = projectEventManager;
        this.eventTranslatorManagerProvider = eventTranslatorManagerProvider;
        this.entityCrudKitHandlerCache = entityCrudKitHandlerCache;
        this.changeManager = changeManager;
        this.dataFactory = dataFactory;
        this.dictionaryManager = dictionaryManager;
        this.classHierarchyProvider = classHierarchyProvider;
        this.objectPropertyHierarchyProvider = objectPropertyHierarchyProvider;
        this.dataPropertyHierarchyProvider = dataPropertyHierarchyProvider;
        this.annotationPropertyHierarchyProvider = annotationPropertyHierarchyProvider;
        this.userInSessionFactory = userInSessionFactory;
        this.entityCrudContextFactory = entityCrudContextFactory;
        this.renameMapFactory = renameMapFactory;
        this.builtInPrefixDeclarations = builtInPrefixDeclarations;
    }

    /**
     * Applies ontology changes to the ontologies contained within a project.
     *
     * @param userId              The userId of the user applying the changes.  Not {@code null}.
     * @param changeListGenerator A generator which creates a list of changes (based on the state of the project at
     *                            the time of change application).  The idea behind passing in a change generator is
     *                            that the list of changes to be applied can be created based on the state of the
     *                            project immediately before they are applied.  This is necessary where the changes
     *                            depend on the structure/state of the ontology.  This method guarantees that no third
     *                            party ontology changes will take place between the
     *                            {@link ChangeListGenerator#generateChanges(ChangeGenerationContext)}
     *                            method being called and the changes being applied.
     * @return A {@link ChangeApplicationResult} that describes the changes which took place an any renaminings.
     * @throws NullPointerException      if any parameters are {@code null}.
     * @throws PermissionDeniedException if the user identified by {@code userId} does not have permssion to write to
     *                                   ontologies in this project.
     */
    @Override
    public <R> ChangeApplicationResult<R> applyChanges(@Nonnull final UserId userId,
                                                       @Nonnull final ChangeListGenerator<R> changeListGenerator) throws PermissionDeniedException {
        checkNotNull(userId);
        checkNotNull(changeListGenerator);

        // Final check of whether the user can actually edit the project
        throwEditPermissionDeniedIfNecessary(userId);

        final List<OWLOntologyChange> appliedChanges;
        final ChangeApplicationResult<R> changeApplicationResult;


        var crudContext = getEntityCrudContext(userId);

        // The following must take into consideration fresh entity IRIs.  Entity IRIs are minted on the server, so
        // ontology changes may contain fresh entity IRIs as place holders. We need to make sure these get replaced
        // with true entity IRIs
        try {
            // Compute the changes that need to take place.  We don't allow any other writes here because the
            // generation of the changes may depend upon the state of the project
            changeProcesssingLock.lock();

            var changeList = changeListGenerator.generateChanges(new ChangeGenerationContext(userId));

            // We have our changes
            var changes = changeList.getChanges();

            // We coin fresh IRIs for entities that have IRIs that follow the temp IRI pattern
            // See DataFactory#isFreshEntity
            var tempIri2MintedIri = new HashMap<IRI, IRI>();

            var changeSession = getEntityCrudKitHandler().createChangeSetSession();
            // Changes that refer to entities that have temp IRIs
            var changesToBeRenamed = new HashSet<OWLOntologyChange>();
            // Changes required to create fresh entities
            var changesToCreateFreshEntities = new ArrayList<OWLOntologyChange>();
            for(var change : changes) {
                for(var entityInSignature : change.getSignature()) {
                    if(isFreshEntity(entityInSignature)) {
                        throwCreatePermissionDeniedIfNecessary(entityInSignature, userId);
                        changesToBeRenamed.add(change);
                        var tempIri = entityInSignature.getIRI();
                        if(!tempIri2MintedIri.containsKey(tempIri)) {
                            var shortName = extractShortNameFromFreshEntity(entityInSignature);
                            var langTag = extractLangTagFromFreshEntity(entityInSignature);
                            var entityType = extractEntityTypeFromFreshEntity(entityInSignature);
                            var creator = getEntityCreator(changeSession, crudContext, userId, shortName, langTag, entityType);
                            changesToCreateFreshEntities.addAll(creator.getChanges());
                            var mintedIri = creator.getEntity().getIRI();
                            tempIri2MintedIri.put(tempIri, mintedIri);
                        }
                    }
                }
            }


            var allChangesIncludingRenames = new ArrayList<OWLOntologyChange>();
            var changeRenamer = new OWLObjectDuplicator(dataFactory, tempIri2MintedIri);
            for(var change : changes) {
                if(changesToBeRenamed.contains(change)) {
                    var replacementChange = getRenamedChange(change, changeRenamer);
                    allChangesIncludingRenames.add(replacementChange);
                }
                else {
                    allChangesIncludingRenames.add(change);
                }
            }

            allChangesIncludingRenames.addAll(changesToCreateFreshEntities);

            var minimisedChanges = getMinimisedChanges(allChangesIncludingRenames);

            final var eventTranslatorManager = eventTranslatorManagerProvider.get();
            eventTranslatorManager.prepareForOntologyChanges(minimisedChanges);

            // Now we do the actual changing, so we lock the project here.  No writes or reads can take place whilst
            // we apply the changes
            final Optional<Revision> revision;
            try {
                projectChangeWriteLock.lock();
                var manager = ((ProjectOWLOntologyManager) rootOntology.getOWLOntologyManager());
                var effectiveChanges = getEffectiveChanges(minimisedChanges);
                manager.getDelegate().applyChanges(effectiveChanges);
                appliedChanges = effectiveChanges;
                var renameMap = renameMapFactory.create(tempIri2MintedIri);
                var renamedResult = getRenamedResult(changeListGenerator, changeList.getResult(), renameMap);
                changeApplicationResult = new ChangeApplicationResult<>(renamedResult, appliedChanges, renameMap);
                if(!appliedChanges.isEmpty()) {
                    var rev = logAndProcessAppliedChanges(userId, changeListGenerator, changeApplicationResult);
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

            generateAndDispatchHighLevelEvents(userId, changeListGenerator, changeApplicationResult, eventTranslatorManager, revision);

        } finally {
            changeProcesssingLock.unlock();
        }

        return changeApplicationResult;
    }

    private void throwEditPermissionDeniedIfNecessary(UserId userId) {
        var subject = forUser(userId);
        var projectResource = new ProjectResource(projectId);
        if(!accessManager.hasPermission(subject, projectResource, EDIT_ONTOLOGY)) {
            throw new PermissionDeniedException("You do not have permission to edit this project", userInSessionFactory.getUserInSession(userId));
        }
    }

    private EntityCrudContext getEntityCrudContext(UserId userId) {
        var prefixNameExpanderBuilder = PrefixedNameExpander.builder();
        prefixDeclarationsStore.find(projectId)
                .getPrefixes()
                .forEach(prefixNameExpanderBuilder::withPrefixNamePrefix);
        builtInPrefixDeclarations.getPrefixDeclarations()
                .forEach(decl -> prefixNameExpanderBuilder.withPrefixNamePrefix(decl.getPrefixName(), decl.getPrefix()));
        var prefixNameExpander = prefixNameExpanderBuilder.build();
        return entityCrudContextFactory.create(userId, prefixNameExpander);
    }

    @SuppressWarnings("unchecked")
    public <S extends EntityCrudKitSuffixSettings, C extends ChangeSetEntityCrudSession> EntityCrudKitHandler<S, C> getEntityCrudKitHandler() {
        return (EntityCrudKitHandler<S, C>) entityCrudKitHandlerCache.getHandler();
    }

    private static boolean isFreshEntity(OWLEntity entity) {
        return DataFactory.isFreshEntity(entity);
    }

    private void throwCreatePermissionDeniedIfNecessary(OWLEntity entity,
                                                        UserId userId) {
        var subject = forUser(userId);
        var projectResource = new ProjectResource(projectId);
        if(entity.isOWLClass()) {
            if(!accessManager.hasPermission(subject, projectResource, CREATE_CLASS)) {
                throw new PermissionDeniedException("You do not have permission to create new classes", userInSessionFactory
                        .getUserInSession(userId));
            }
        }
        else if(entity.isOWLObjectProperty() || entity.isOWLDataProperty() || entity.isOWLAnnotationProperty()) {
            if(!accessManager.hasPermission(subject, projectResource, CREATE_PROPERTY)) {
                throw new PermissionDeniedException("You do not have permission to create new properties", userInSessionFactory
                        .getUserInSession(userId));
            }
        }
        else if(entity.isOWLNamedIndividual()) {
            if(!accessManager.hasPermission(subject, projectResource, CREATE_INDIVIDUAL)) {
                throw new PermissionDeniedException("You do not have permission to create new individuals", userInSessionFactory
                        .getUserInSession(userId));
            }
        }
        else if(entity.isOWLDatatype()) {
            if(!accessManager.hasPermission(subject, projectResource, CREATE_DATATYPE)) {
                throw new PermissionDeniedException("You do not have permission to create new datatypes", userInSessionFactory
                        .getUserInSession(userId));
            }
        }
    }

    private static String extractShortNameFromFreshEntity(OWLEntity freshEntity) {
        return DataFactory.getFreshEntityShortName(freshEntity);
    }

    private static Optional<String> extractLangTagFromFreshEntity(OWLEntity freshEntity) {
        return DataFactory.getFreshEntityLangTag(freshEntity);
    }

    private static EntityType<?> extractEntityTypeFromFreshEntity(OWLEntity freshEntity) {
        return freshEntity.getEntityType();
    }

    private <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(ChangeSetEntityCrudSession session,
                                                                       EntityCrudContext context,
                                                                       UserId userId,
                                                                       String shortName,
                                                                       Optional<String> langTag,
                                                                       EntityType<E> entityType) {
        Optional<E> entity = getEntityOfTypeIfPresent(entityType, shortName);
        if(entity.isPresent()) {
            return new OWLEntityCreator<>(entity.get(), Collections.emptyList());
        }
        OntologyChangeList.Builder<E> builder = OntologyChangeList.builder();
        EntityCrudKitHandler<EntityCrudKitSuffixSettings, ChangeSetEntityCrudSession> handler = getEntityCrudKitHandler();
        handler.createChangeSetSession();
        E ent = handler.create(session, entityType, EntityShortForm.get(shortName), langTag, context, builder);
        return new OWLEntityCreator<>(ent, builder.build(ent).getChanges());

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
    private OWLOntologyChange getRenamedChange(OWLOntologyChange change,
                                               final OWLObjectDuplicator duplicator) {
        return change.accept(new OWLOntologyChangeVisitorEx<>() {

            @Nonnull
            @Override
            public OWLOntologyChange visit(@Nonnull AddAxiom change) {
                return new AddAxiom(change.getOntology(), duplicate(change.getAxiom()));
            }

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

    private List<OWLOntologyChange> getMinimisedChanges(List<OWLOntologyChange> allChangesIncludingRenames) {
        return new ChangeListMinimiser().getMinimisedChanges(allChangesIncludingRenames);
    }

    private List<OWLOntologyChange> getEffectiveChanges(List<OWLOntologyChange> minimisedChanges) {
        return minimisedChanges.stream().filter(this::isEffectiveChange).collect(toList());
    }

    /**
     * Renames a result if it is present.
     *
     * @param result    The result to process.
     * @param renameMap The rename map.
     * @param <R>       The type of result.
     * @return The renamed (or untouched if no rename was necessary) result.
     */
    private <R> R getRenamedResult(ChangeListGenerator<R> changeListGenerator,
                                   R result,
                                   RenameMap renameMap) {
        return changeListGenerator.getRenamedResult(result, renameMap);
    }

    private <R> Revision logAndProcessAppliedChanges(UserId userId,
                                                     ChangeListGenerator<R> changeList,
                                                     ChangeApplicationResult<R> finalResult) {
        var changes = finalResult.getChangeList();
        // Update the rendering first so that a proper change message is generated
        activeLanguagesManager.handleChanges(changes);
        dictionaryUpdatesProcessor.handleChanges(changes);

        // Generate a description for the changes that were actually applied
        var changeDescription = changeList.getMessage(finalResult);
        // Log the changes
        var changeRecords = finalResult
                .getChangeList()
                .stream()
                .map(OWLOntologyChange::getChangeRecord)
                .collect(toList());
        var revision = changeManager.addRevision(userId, changeRecords, changeDescription);

        classHierarchyProvider.handleChanges(changes);
        objectPropertyHierarchyProvider.handleChanges(changes);
        dataPropertyHierarchyProvider.handleChanges(changes);
        annotationPropertyHierarchyProvider.handleChanges(changes);
        return revision;
    }

    private <R> void generateAndDispatchHighLevelEvents(UserId userId,
                                                        ChangeListGenerator<R> changeListGenerator,
                                                        ChangeApplicationResult<R> finalResult,
                                                        EventTranslatorManager eventTranslatorManager,
                                                        Optional<Revision> revision) {
        if(changeListGenerator instanceof SilentChangeListGenerator) {
            return;
        }
        revision.ifPresent(rev -> {
            var highLevelEvents = new ArrayList<ProjectEvent<?>>();
            eventTranslatorManager.translateOntologyChanges(rev, finalResult, highLevelEvents);
            if(changeListGenerator instanceof HasHighLevelEvents) {
                highLevelEvents.addAll(((HasHighLevelEvents) changeListGenerator).getHighLevelEvents());
            }
            projectEventManager.postEvents(highLevelEvents);
            projectChangedWebhookInvoker.invoke(userId, rev.getRevisionNumber(), rev.getTimestamp());
        });
    }

    @SuppressWarnings("unchecked")
    private <E extends OWLEntity> Optional<E> getEntityOfTypeIfPresent(EntityType<E> entityType,
                                                                       String shortName) {
        return dictionaryManager
                .getEntities(shortName)
                .filter(entity -> entity.getEntityType().equals(entityType))
                .map(entity -> (E) entity)
                .findFirst();

    }

    private boolean isEffectiveChange(OWLOntologyChange chg) {
        return chg.accept(EFFECTIVE_CHANGE_FILTER);
    }
}

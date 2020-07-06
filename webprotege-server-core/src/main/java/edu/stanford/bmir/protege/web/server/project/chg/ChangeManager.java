package edu.stanford.bmir.protege.web.server.project.chg;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.crud.*;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.events.EventTranslatorManager;
import edu.stanford.bmir.protege.web.server.hierarchy.AnnotationPropertyHierarchyProviderImpl;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProviderImpl;
import edu.stanford.bmir.protege.web.server.hierarchy.DataPropertyHierarchyProviderImpl;
import edu.stanford.bmir.protege.web.server.hierarchy.ObjectPropertyHierarchyProviderImpl;
import edu.stanford.bmir.protege.web.server.index.RootIndex;
import edu.stanford.bmir.protege.web.server.index.impl.IndexUpdater;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLEntityCreator;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMapFactory;
import edu.stanford.bmir.protege.web.server.project.BuiltInPrefixDeclarations;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.project.PrefixDeclarationsStore;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryUpdatesProcessor;
import edu.stanford.bmir.protege.web.server.util.IriReplacer;
import edu.stanford.bmir.protege.web.server.util.IriReplacerFactory;
import edu.stanford.bmir.protege.web.server.webhook.ProjectChangedWebhookInvoker;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.entity.FreshEntityIri;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;

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
    private final OWLDataFactory dataFactory;

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
    private final RootIndex rootIndex;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final ClassHierarchyProviderImpl classHierarchyProvider;

    @Nonnull
    private final ObjectPropertyHierarchyProviderImpl objectPropertyHierarchyProvider;

    @Nonnull
    private final DataPropertyHierarchyProviderImpl dataPropertyHierarchyProvider;

    @Nonnull
    private final AnnotationPropertyHierarchyProviderImpl annotationPropertyHierarchyProvider;

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

    @Nonnull
    private final IndexUpdater indexUpdater;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final IriReplacerFactory iriReplacerFactory;

    @Inject
    public ChangeManager(@Nonnull ProjectId projectId,
                         @Nonnull OWLDataFactory dataFactory,
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
                         @Nonnull RootIndex rootIndex,
                         @Nonnull DictionaryManager dictionaryManager,
                         @Nonnull ClassHierarchyProviderImpl classHierarchyProvider,
                         @Nonnull ObjectPropertyHierarchyProviderImpl objectPropertyHierarchyProvider,
                         @Nonnull DataPropertyHierarchyProviderImpl dataPropertyHierarchyProvider,
                         @Nonnull AnnotationPropertyHierarchyProviderImpl annotationPropertyHierarchyProvider,
                         @Nonnull UserInSessionFactory userInSessionFactory,
                         @Nonnull EntityCrudContextFactory entityCrudContextFactory,
                         @Nonnull RenameMapFactory renameMapFactory,
                         @Nonnull BuiltInPrefixDeclarations builtInPrefixDeclarations,
                         @Nonnull IndexUpdater indexUpdater,
                         @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                         @Nonnull IriReplacerFactory iriReplacerFactory) {
        this.projectId = projectId;
        this.dataFactory = dataFactory;
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
        this.rootIndex = rootIndex;
        this.dictionaryManager = dictionaryManager;
        this.classHierarchyProvider = classHierarchyProvider;
        this.objectPropertyHierarchyProvider = objectPropertyHierarchyProvider;
        this.dataPropertyHierarchyProvider = dataPropertyHierarchyProvider;
        this.annotationPropertyHierarchyProvider = annotationPropertyHierarchyProvider;
        this.userInSessionFactory = userInSessionFactory;
        this.entityCrudContextFactory = entityCrudContextFactory;
        this.renameMapFactory = renameMapFactory;
        this.builtInPrefixDeclarations = builtInPrefixDeclarations;
        this.indexUpdater = indexUpdater;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.iriReplacerFactory = iriReplacerFactory;
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
            var changesToBeRenamed = new HashSet<OntologyChange>();
            // Changes required to create fresh entities
            var changesToCreateFreshEntities = new ArrayList<OntologyChange>();
            for(var change : changes) {
                change.getSignature()
                      .forEach(entityInSignature -> {
                          if(isFreshEntity(entityInSignature)) {
                              throwCreatePermissionDeniedIfNecessary(entityInSignature, userId);
                              changesToBeRenamed.add(change);
                              var tempIri = entityInSignature.getIRI();
                              if(!tempIri2MintedIri.containsKey(tempIri)) {
                                  var freshEntityIri = FreshEntityIri.parse(tempIri.toString());
                                  var shortName = freshEntityIri.getSuppliedName();
                                  var langTag = Optional.<String>empty();
                                  if(!shortName.isEmpty()) {
                                      langTag = Optional.of(freshEntityIri.getLangTag());
                                  }
                                  var entityType = entityInSignature.getEntityType();
                                  var discriminator = freshEntityIri.getDiscriminator();
                                  var parents = freshEntityIri.getParentEntities(dataFactory, entityType);
                                  var creator = getEntityCreator(changeSession,
                                                                 crudContext,
                                                                 shortName,
                                                                 discriminator,
                                                                 langTag,
                                                                 parents,
                                                                 entityType);
                                  changesToCreateFreshEntities.addAll(creator.getChanges());
                                  var mintedIri = creator.getEntity()
                                                         .getIRI();
                                  tempIri2MintedIri.put(tempIri, mintedIri);
                              }
                          }
                      });
                if(isChangeForAnnotationAssertionWithFreshIris(change)) {
                    changesToBeRenamed.add(change);
                }

            }


            var allChangesIncludingRenames = new ArrayList<OntologyChange>();
            var changeRenamer = iriReplacerFactory.create(ImmutableMap.copyOf(tempIri2MintedIri));
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

            final var eventTranslatorManager = eventTranslatorManagerProvider.get();

            // Now we do the actual changing, so we lock the project here.  No writes or reads can take place whilst
            // we apply the changes
            projectChangeWriteLock.lock();
            final Optional<Revision> revision;
            try {
                var effectiveChanges = rootIndex.getEffectiveChanges(allChangesIncludingRenames);

                eventTranslatorManager.prepareForOntologyChanges(effectiveChanges);

                var renameMap = renameMapFactory.create(tempIri2MintedIri);
                var renamedResult = getRenamedResult(changeListGenerator, changeList.getResult(), renameMap);
                changeApplicationResult = new ChangeApplicationResult<>(renamedResult, effectiveChanges, renameMap);
                if(!effectiveChanges.isEmpty()) {
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

            generateAndDispatchHighLevelEvents(userId,
                                               changeListGenerator,
                                               changeApplicationResult,
                                               eventTranslatorManager,
                                               revision);

        } finally {
            changeProcesssingLock.unlock();
        }

        return changeApplicationResult;
    }

    private void throwEditPermissionDeniedIfNecessary(UserId userId) {
        var subject = forUser(userId);
        var projectResource = new ProjectResource(projectId);
        if(!accessManager.hasPermission(subject, projectResource, EDIT_ONTOLOGY)) {
            throw new PermissionDeniedException("You do not have permission to edit this project",
                                                userInSessionFactory.getUserInSession(userId));
        }
    }

    private EntityCrudContext getEntityCrudContext(UserId userId) {
        var prefixNameExpanderBuilder = PrefixedNameExpander.builder();
        prefixDeclarationsStore.find(projectId)
                               .getPrefixes()
                               .forEach(prefixNameExpanderBuilder::withPrefixNamePrefix);
        builtInPrefixDeclarations.getPrefixDeclarations()
                                 .forEach(decl -> prefixNameExpanderBuilder.withPrefixNamePrefix(decl.getPrefixName(),
                                                                                                 decl.getPrefix()));
        var prefixNameExpander = prefixNameExpanderBuilder.build();
        var defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyId();
        return entityCrudContextFactory.create(userId,
                                               prefixNameExpander,
                                               defaultOntologyId);
    }

    @SuppressWarnings("unchecked")
    private <S extends EntityCrudKitSuffixSettings, C extends ChangeSetEntityCrudSession> EntityCrudKitHandler<S, C> getEntityCrudKitHandler() {
        return (EntityCrudKitHandler<S, C>) entityCrudKitHandlerCache.getHandler();
    }

    private static boolean isFreshEntity(OWLEntity entity) {
        return FreshEntityIri.isFreshEntityIri(entity.getIRI());
    }

    private void throwCreatePermissionDeniedIfNecessary(OWLEntity entity,
                                                        UserId userId) {
        var subject = forUser(userId);
        var projectResource = new ProjectResource(projectId);
        if(entity.isOWLClass()) {
            if(!accessManager.hasPermission(subject, projectResource, CREATE_CLASS)) {
                throw new PermissionDeniedException("You do not have permission to create new classes",
                                                    userInSessionFactory
                                                            .getUserInSession(userId));
            }
        }
        else if(entity.isOWLObjectProperty() || entity.isOWLDataProperty() || entity.isOWLAnnotationProperty()) {
            if(!accessManager.hasPermission(subject, projectResource, CREATE_PROPERTY)) {
                throw new PermissionDeniedException("You do not have permission to create new properties",
                                                    userInSessionFactory
                                                            .getUserInSession(userId));
            }
        }
        else if(entity.isOWLNamedIndividual()) {
            if(!accessManager.hasPermission(subject, projectResource, CREATE_INDIVIDUAL)) {
                throw new PermissionDeniedException("You do not have permission to create new individuals",
                                                    userInSessionFactory
                                                            .getUserInSession(userId));
            }
        }
        else if(entity.isOWLDatatype()) {
            if(!accessManager.hasPermission(subject, projectResource, CREATE_DATATYPE)) {
                throw new PermissionDeniedException("You do not have permission to create new datatypes",
                                                    userInSessionFactory
                                                            .getUserInSession(userId));
            }
        }
    }

    private <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(ChangeSetEntityCrudSession session,
                                                                       EntityCrudContext context,
                                                                       String shortName,
                                                                       String discriminator,
                                                                       Optional<String> langTag,
                                                                       ImmutableList<OWLEntity> parents,
                                                                       EntityType<E> entityType) {
        if (discriminator.isEmpty() && !shortName.isEmpty()) {
            Optional<E> entity = getEntityOfTypeIfPresent(entityType, shortName);
            if(entity.isPresent()) {
                return new OWLEntityCreator<>(entity.get(), Collections.emptyList());
            }
        }
        OntologyChangeList.Builder<E> builder = OntologyChangeList.builder();
        EntityCrudKitHandler<EntityCrudKitSuffixSettings, ChangeSetEntityCrudSession> handler = getEntityCrudKitHandler();
        handler.createChangeSetSession();
        E ent = handler.create(session, entityType, EntityShortForm.get(shortName), langTag, parents, context, builder);
        return new OWLEntityCreator<>(ent,
                                      builder.build(ent)
                                             .getChanges());

    }

    private boolean isChangeForAnnotationAssertionWithFreshIris(OntologyChange change) {
        if(!change.isAxiomChange()) {
            return false;
        }
        var axiom = change.getAxiomOrThrow();
        if(!(axiom instanceof OWLAnnotationAssertionAxiom)) {
            return false;
        }
        var assertion = (OWLAnnotationAssertionAxiom) axiom;
        var subject = assertion.getSubject();
        if(subject instanceof IRI) {
            if(DataFactory.isFreshIri((IRI) subject)) {
                return true;
            }
        }
        var object = assertion.getValue();
        if(object instanceof IRI) {
            return DataFactory.isFreshIri((IRI) object);
        }
        return false;
    }

    /**
     * Gets an ontology change which is a copy of an existing ontology change except for IRIs that are renamed.
     * Renamings
     * are specified by a rename map.
     *
     * @param change      The change to copy.
     * @param iriReplacer An IRI replacer used to rename IRIs in OWL objects
     * @return The ontology change with the renamings.
     */
    private OntologyChange getRenamedChange(OntologyChange change,
                                            IriReplacer iriReplacer) {
        return change.replaceIris(iriReplacer);
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

        // Update indexes in response to the changes
        indexUpdater.updateIndexes(ImmutableList.copyOf(changes));


        // Update the rendering first so that a proper change message is generated
        activeLanguagesManager.handleChanges(changes);
        dictionaryUpdatesProcessor.handleChanges(changes);

        // Generate a description for the changes that were actually applied
        var changeDescription = changeList.getMessage(finalResult);

        // Log the changes
        var revision = changeManager.addRevision(userId, changes, changeDescription);

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
                .filter(entity -> entity.getEntityType()
                                        .equals(entityType))
                .map(entity -> (E) entity)
                .findFirst();

    }
}

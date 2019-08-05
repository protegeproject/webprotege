package edu.stanford.bmir.protege.web.server.inject.project;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManager;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManagerImpl;
import edu.stanford.bmir.protege.web.server.owlapi.*;
import edu.stanford.bmir.protege.web.server.project.ProjectDisposablesManager;
import edu.stanford.bmir.protege.web.server.renderer.*;
import edu.stanford.bmir.protege.web.server.renderer.LiteralRenderer;
import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;
import edu.stanford.bmir.protege.web.server.axiom.AxiomComparatorImpl;
import edu.stanford.bmir.protege.web.server.axiom.DefaultAxiomTypeOrdering;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.change.matcher.*;
import edu.stanford.bmir.protege.web.server.events.*;
import edu.stanford.bmir.protege.web.server.frame.PropertyValueSubsumptionChecker;
import edu.stanford.bmir.protege.web.server.frame.StructuralPropertyValueSubsumptionChecker;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsIndexCachingImpl;
import edu.stanford.bmir.protege.web.server.individuals.IndividualsIndex;
import edu.stanford.bmir.protege.web.server.individuals.IndividualsIndexImpl;
import edu.stanford.bmir.protege.web.server.inject.ProjectActionHandlersModule;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOWLOntologyChecker;
import edu.stanford.bmir.protege.web.server.mansyntax.render.*;
import edu.stanford.bmir.protege.web.server.match.MatchingEngine;
import edu.stanford.bmir.protege.web.server.match.MatchingEngineImpl;
import edu.stanford.bmir.protege.web.server.metrics.MetricCalculator;
import edu.stanford.bmir.protege.web.server.metrics.MetricCalculatorsProvider;
import edu.stanford.bmir.protege.web.server.object.OWLObjectComparatorImpl;
import edu.stanford.bmir.protege.web.server.obo.OBONamespaceCache;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.server.project.RootOntologyProvider;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.revision.RevisionManagerImpl;
import edu.stanford.bmir.protege.web.server.revision.RevisionStore;
import edu.stanford.bmir.protege.web.server.revision.RevisionStoreProvider;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.server.tag.*;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManagerImpl;
import edu.stanford.bmir.protege.web.server.watches.WatchTriggeredHandler;
import edu.stanford.bmir.protege.web.server.watches.WatchTriggeredHandlerImpl;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import edu.stanford.bmir.protege.web.shared.HasGetEntitiesWithIRI;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.object.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.HasHtmlBrowserText;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static dagger.internal.codegen.DaggerStreams.toImmutableSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 *         <p>
 *         A  module for a project.  The module ensures that any object graph contains project specific objects for the
 *         specified project (e.g. root ontology, short form provider etc.)
 */
@Module(includes = ProjectActionHandlersModule.class)
public class ProjectModule {

    private final ProjectId projectId;

    public ProjectModule(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Provides
    @ProjectSingleton
    OWLDataFactory providesDataFactory() {
        return new OWLDataFactoryImpl();
    }

    @Provides
    @ProjectSingleton
    public OWLEntityProvider providesOWLEntityProvider(OWLDataFactory dataFactory) {
        return dataFactory;
    }

    @Provides
    @ProjectSingleton
    public ProjectId provideProjectId() {
        return projectId;
    }

    @Provides
    @RootOntologyDocument
    public File provideRootOntologyDocument(RootOntologyDocumentProvider provider) {
        return provider.get();
    }

    @Provides
    @ProjectDirectory
    public File provideProjectDirectory(ProjectDirectoryProvider provider) {
        return provider.get();
    }

    @Provides
    @ChangeHistoryFile
    public File provideChangeHistoryFile(ChangeHistoryFileProvider provider) {
        return provider.get();
    }

    @Provides
    @ProjectSpecificUiConfigurationDataDirectory
    public File provideProjectSpecificUiConfigurationDataDirectory(ProjectSpecificUiConfigurationDataDirectoryProvider provider) {
        return provider.get();
    }

    @Provides
    @RootOntology
    @ProjectSingleton
    public OWLOntology provideRootOntology(RootOntologyProvider provider) {
        return provider.get();
    }

    @Provides
    public OWLAnnotationPropertyProvider provideAnnotationPropertyProvider(OWLDataFactory factory) {
        return factory;
    }

    @Provides
    public Set<ChangeMatcher> providesChangeMatchers(
            AnnotationAssertionChangeMatcher annotationAssertionChangeMatcher,
            PropertyDomainAxiomChangeMatcher propertyDomainAxiomChangeMatcher,
            PropertyRangeAxiomChangeMatcher propertyRangeAxiomChangeMatcher,
            EditedAnnotationAssertionChangeMatcher editedAnnotationAssertionChangeMatcher,
            FunctionalDataPropertyAxiomChangeMatcher functionalDataPropertyAxiomChangeMatcher,
            ClassAssertionAxiomMatcher classAssertionAxiomMatcher,
            SubClassOfAxiomMatcher subClassOfAxiomMatcher,
            ClassMoveChangeMatcher classMoveChangeMatcher,
            SubClassOfEditChangeMatcher subClassOfEditChangeMatcher,
            PropertyAssertionAxiomMatcher propertyAssertionAxiomMatcher,
            SameIndividualAxiomChangeMatcher sameIndividualAxiomChangeMatcher,
            EntityCreationMatcher entityCreationMatcher,
            EntityDeletionMatcher entityDeletionMatcher) {
        ImmutableSet<Object> matchers = ImmutableSet.of(annotationAssertionChangeMatcher, propertyDomainAxiomChangeMatcher, propertyRangeAxiomChangeMatcher, editedAnnotationAssertionChangeMatcher, functionalDataPropertyAxiomChangeMatcher, classAssertionAxiomMatcher, subClassOfAxiomMatcher, classMoveChangeMatcher, subClassOfEditChangeMatcher, propertyAssertionAxiomMatcher, sameIndividualAxiomChangeMatcher, entityCreationMatcher, entityDeletionMatcher);
        return matchers.stream().map(m -> (ChangeMatcher)m).collect(toImmutableSet());
    }

    @Provides
    @ClassHierarchyRoot
    public OWLClass providesClassHierarchyRoot(ClassHierarchyRootProvider provider) {
        return provider.get();
    }

    @Provides
    @ObjectPropertyHierarchyRoot
    public OWLObjectProperty providesObjectPropertyHierarchyRoot(ObjectPropertyHierarchyRootProvider provider) {
        return provider.get();
    }

    @Provides
    @DataPropertyHierarchyRoot
    public OWLDataProperty providesDataPropertyHierarchyRoot(DataPropertyHierarchyRootProvider provider) {
        return provider.get();
    }

    @Provides
    public HierarchyProvider<OWLObjectProperty>
    provideObjectPropertyHierarchyProvider(OWLObjectPropertyHierarchyProvider provider) {
        return provider;
    }


    @Provides
    public HierarchyProvider<OWLDataProperty>
    provideDataPropertyHierarchyProvider(
            OWLDataPropertyHierarchyProvider provider) {
        return provider;
    }

    @Provides
    public HierarchyProvider<OWLAnnotationProperty>
    provideAnnotationPropertyHierarchyProvider(
            OWLAnnotationPropertyHierarchyProvider provider) {
        return provider;
    }

    @Provides
    public HasGetAncestors<OWLClass> providesOWLClassAncestors(HierarchyProvider<OWLClass> hierarchyProvider) {
        return hierarchyProvider;
    }
    
    @Provides
    public HasGetAncestors<OWLObjectProperty> providesOWLObjectPropertyAncestors(HierarchyProvider<OWLObjectProperty> hierarchyProvider) {
        return hierarchyProvider;
    }
    
    @Provides
    public HasGetAncestors<OWLDataProperty> providesOWLDataPropertyAncestors(HierarchyProvider<OWLDataProperty> hierarchyProvider) {
        return hierarchyProvider;
    }

    @Provides
    public HasGetAncestors<OWLAnnotationProperty> providesOWLAnnotationPropertyAncestors(HierarchyProvider<OWLAnnotationProperty> hierarchyProvider) {
        return hierarchyProvider;
    }
    
    @Provides
    public WatchManager provideWatchManager(WatchManagerImpl impl) {
        // Attach it so that it listens for entity frame changed events
        // There's no need to detatch it because it is project scoped
        // and has the same lifetime as a project event manager.
        impl.attach();
        return impl;
    }

    @Provides
    public WatchTriggeredHandler provideWatchTriggeredHandler(WatchTriggeredHandlerImpl impl) {
        return impl;
    }

    @Provides
    public ShortFormProvider provideShortFormProvider(ShortFormAdapter shortFormAdapter) {
        return shortFormAdapter;
    }

    @Provides
    public IRIShortFormProvider provideIriShortFormProvider(WebProtegeIRIShortFormProvider provider) {
        return provider;
    }

    @Provides
    public HasGetRendering provideHasGetRendering(RenderingManager renderingManager) {
        return renderingManager;
    }

    @Provides
    public HasLang provideHasLang() {
        return () -> "en";
    }

    @Provides
    @ProjectSingleton
    public OntologyIRIShortFormProvider provideOntologyIRIShortFormProvider(WebProtegeOntologyIRIShortFormProvider provider) {
        return provider;
    }

    @Provides
    public EntityIRIChecker provideEntityIRIChecker(EntityIRICheckerImpl iriChecker) {
        return iriChecker;
    }

    @Provides
    public OWLOntologyChecker provideOntologyChecker(WebProtegeOWLOntologyChecker checker) {
        return checker;
    }

    @Provides
    public HighlightedEntityChecker provideHighlightedEntityChecker(NullHighlightedEntityChecker checker) {
        return checker;
    }

    @Provides
    @ProjectSingleton
    public EventManager<ProjectEvent<?>> providesEventManager(EventManagerProvider eventManagerProvider) {
        return eventManagerProvider.get();
    }

    @Provides
    public EventLifeTime provideEventLifeTime() {
        return EventManagerProvider.PROJECT_EVENT_LIFE_TIME;
    }

    @Provides
    public RevisionNumber provideRevisionNumber(RevisionNumberProvider provider) {
        return provider.get();
    }

    @Provides
    public HasGetChangeSubjects provideHasGetChangeSubjects(OntologyChangeSubjectProvider provider) {
        return provider;
    }

    @Provides
    public DeprecatedEntityChecker provideDeprecatedEntityChecker(DeprecatedEntityCheckerImpl checker) {
        return checker;
    }

    @Provides
    public HasGetRevisionSummary provideGetRevisionSummary(RevisionManagerImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public RevisionManager provideRevisionManager(RevisionManagerImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public RevisionStore provideRevisionStore(RevisionStoreProvider provider) {
        return provider.get();
    }

    @Provides
    List<MetricCalculator> providesMetricsCaculator(MetricCalculatorsProvider provider) {
        return provider.get();
    }

    @Provides
    ImmutableList<IRI> providesShortFormOrdering() {
        return DefaultShortFormAnnotationPropertyIRIs.asImmutableList();
    }

    @Provides
    @ProjectSingleton
    HasHtmlBrowserText providesHasHtmlBrowserText(RenderingManager rm) {
        return rm;
    }

    @Provides
    @ProjectSingleton
    OWLObjectRenderer providesOWLObjectRenderer(OWLObjectRendererImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    HasImportsClosure providesHasImportsClosure(RootOntologyProvider provider) {
        return provider.get();
    }

    @Provides
    HasGetEntitiesWithIRI providesHasGetEntitiesWithIRI(HasGetEntitiesWithIRIImpl impl) {
        return impl;
    }

    @Provides
    HasGetEntitiesInSignature providesHasGetEntitiesInSignature(HasGetEntitiesInSignatureImpl impl) {
        return impl;
    }

    @Provides
    HasAnnotationAssertionAxioms providesHasAnnotationAssertionAxioms(HasAnnotationAssertionAxiomsImpl impl) {
        return impl;
    }

    @Provides
    HasApplyChanges providesHasApplyChanges(ChangeManager manager) {
        return manager;
    }


    @Provides
    @ProjectSingleton
    HierarchyProvider<OWLClass> provideClassHierarchyProvider(ClassHierarchyProvider provider) {
        return provider;
    }

    @Provides
    OWLObjectSelector<OWLClassExpression> provideClassExpressionSelector(OWLClassExpressionSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<OWLObjectPropertyExpression> provideObjectPropertyExpressionSelector(
            OWLObjectPropertyExpressionSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<OWLDataPropertyExpression> provideDataPropertyExpressionSelector(OWLDataPropertyExpressionSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<OWLIndividual> provideIndividualSelector(OWLIndividualSelector selector) {
        return selector;
    }

    @Provides
    OWLObjectSelector<SWRLAtom> provideSWRLAtomSelector(SWRLAtomSelector selector) {
        return selector;
    }

    @Provides
    Comparator<? super OWLClass> providesClassComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLObjectProperty> providesObjectPropertyComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLDataProperty> providesDataPropertyComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLAnnotationProperty> providesAnnotationPropertyComparator(AnnotationPropertyComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLAnnotation> providesAnnotationComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLNamedIndividual> providesNamedIndividualComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super OWLDatatype> providesDatatypeComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<? super SWRLAtom> providesSWRLAtomComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    IRIIndexProvider provideIRIIndexProvider() {
        return IRIIndexProvider.withDefaultAnnotationPropertyOrdering();
    }

    @Provides
    Comparator<OWLOntologyChangeRecord> providesOWLOntologyChangeRecordComparator(ChangeRecordComparator comparator) {
        return comparator;
    }

    @Provides
    Comparator<OWLAxiom> providesAxiomComparator(AxiomComparatorImpl impl) {
        return impl;
    }

    @Provides
    Comparator<OWLObject> providesOWLObjectComparator(OWLObjectComparatorImpl impl) {
        return impl;
    }

    @Provides
    public Set<EventTranslator> providesEventTranslators(
            BrowserTextChangedEventComputer c0,
            HighLevelEventGenerator c1,
            OWLClassHierarchyChangeComputer c2,
            OWLObjectPropertyHierarchyChangeComputer c3,
            OWLDataPropertyHierarchyChangeComputer c4,
            OWLAnnotationPropertyHierarchyChangeComputer c5,
            EntityDeprecatedChangedEventTranslator c6,
            EntityTagsChangedEventComputer c7) {
        return ImmutableSet.of(c0, c1, c2, c3, c4, c5, c6, c7);
    }

    @Provides
    HasPostEvents<ProjectEvent<?>> provideHasPostEvents(EventManager<ProjectEvent<?>> eventManager) {
        return eventManager;
    }

    @Provides
    NullHighlightedEntityChecker providesNullHighlightedEntityChecker() {
        return NullHighlightedEntityChecker.get();
    }

    @Provides
    HasContainsEntityInSignature providesHasContainsEntityInSignature(HasContainsEntityInSignatureImpl impl) {
        return impl;
    }

    @Provides
    List<AxiomType<?>> providesAxiomTypeList() {
        return DefaultAxiomTypeOrdering.get();
    }

    @Provides
    PropertyValueSubsumptionChecker providePropertyValueSubsumptionChecker(StructuralPropertyValueSubsumptionChecker impl) {
        return impl;
    }
    
    @Provides
    HasHasAncestor<OWLClass, OWLClass> provideClassClassHasAncestor(ClassClassAncestorChecker checker) {
        return checker;
    }
    
    @Provides
    HasHasAncestor<OWLObjectProperty, OWLObjectProperty> provideObjectPropertyObjectPropertyHasAncestor(ObjectPropertyObjectPropertyAncestorChecker checker) {
        return checker;
    }


    @Provides
    HasHasAncestor<OWLDataProperty, OWLDataProperty> provideDataPropertyDataPropertyHasAncestor(DataPropertyDataPropertyAncestorChecker checker) {
        return checker;
    }

    @Provides
    HasHasAncestor<OWLNamedIndividual, OWLClass> provideNamedIndividualClassHasAncestor(NamedIndividualClassAncestorChecker checker) {
        return checker;
    }

    @Provides
    OBONamespaceCache providesOboNamespaceCache(@RootOntology OWLOntology rootOntology) {
        return OBONamespaceCache.get(rootOntology);
    }

    @Provides
    ActiveLanguagesManager provideActiveLanguagesManager(ActiveLanguagesManagerImpl impl) {
        return impl;
    }

    @Provides
    MultiLingualDictionary provideDictionary(MultiLingualDictionaryImpl dictionary, LanguageManager languageManager) {
        // Preload existing languages to avoid delays after loading in the UI
        dictionary.loadLanguages(languageManager.getActiveLanguages());
        return dictionary;
    }

    @Provides
    ShortFormCache provideShortFormCache() {
        return ShortFormCache.create();
    }

    @Provides
    BuiltInShortFormDictionary provideBuiltInShortFormDictionary(ShortFormCache cache, OWLEntityProvider provider) {
        BuiltInShortFormDictionary dictionary = new BuiltInShortFormDictionary(cache, provider);
        dictionary.load();
        return dictionary;
    }

    @Provides
    AnnotationAssertionAxiomsIndex provideAnnotationAssertionAxiomsIndex(AnnotationAssertionAxiomsIndexCachingImpl impl) {
        impl.attachOntologyListener();
        return impl;
    }

    @Provides
    MatchingEngine provideMatchingEngine(MatchingEngineImpl impl) {
        return impl;
    }

    @Provides
    TagRepository provideTagRepository(TagRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @Provides
    TagRepositoryCachingImpl provideTagRepositoryCachingImpl(TagRepositoryImpl impl) {
        return new TagRepositoryCachingImpl(impl);
    }

    @Provides
    EntityTagsRepository provideEntityTagsRepository(EntityTagsRepositoryImpl impl) {
        impl.ensureIndexes();
        return impl;
    }

    @ProjectSingleton
    @Provides
    EntityTagsRepositoryCachingImpl provideEntityTagsRepositoryCachingImpl(EntityTagsRepositoryImpl impl) {
        EntityTagsRepositoryCachingImpl rep = new EntityTagsRepositoryCachingImpl(impl);
        rep.preloadCache();
        return rep;
    }

    @Provides
    @ProjectSingleton
    IndividualsIndex provideIndividualsIndex(IndividualsIndexImpl impl) {
        return impl;
    }

    @ProjectSingleton
    @Provides
    ProjectDisposablesManager provideProjectDisposableObjectManager(DisposableObjectManager disposableObjectManager) {
        return new ProjectDisposablesManager(disposableObjectManager);
    }

    @Provides
    LiteralRenderer provideLiteralRenderer(@Nonnull
                                           StringFormatterLiteralRendererImpl impl) {
        return impl;
    }

    @Provides
    LiteralLexicalFormTransformer provideLiteralLexicalFormTransformer() {
        return lexicalForm -> lexicalForm;
    }

    @Provides
    LiteralLangTagTransformer provideLangTagTransformer() {
        return langTag -> langTag;
    }
}

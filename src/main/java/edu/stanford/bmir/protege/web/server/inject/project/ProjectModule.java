package edu.stanford.bmir.protege.web.server.inject.project;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.OntologyChangeSubjectProvider;
import edu.stanford.bmir.protege.web.server.change.HasGetRevisionSummary;
import edu.stanford.bmir.protege.web.server.change.matcher.*;
import edu.stanford.bmir.protege.web.server.events.*;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOWLOntologyChecker;
import edu.stanford.bmir.protege.web.server.metrics.MetricCalculator;
import edu.stanford.bmir.protege.web.server.metrics.MetricCalculatorsProvider;
import edu.stanford.bmir.protege.web.server.notes.OWLAPINotesManager;
import edu.stanford.bmir.protege.web.server.notes.OWLAPINotesManagerNotesAPIImpl;
import edu.stanford.bmir.protege.web.server.owlapi.*;
import edu.stanford.bmir.protege.web.server.owlapi.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.owlapi.change.RevisionManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.RevisionManagerImpl;
import edu.stanford.bmir.protege.web.server.owlapi.change.RevisionStore;
import edu.stanford.bmir.protege.web.server.owlapi.change.RevisionStoreProvider;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.RootOntologyProvider;
import edu.stanford.bmir.protege.web.server.render.*;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.server.watches.*;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import edu.stanford.bmir.protege.web.shared.HasGetChangeSubjects;
import edu.stanford.bmir.protege.web.shared.HasGetEntitiesWithIRI;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomComparatorImpl;
import edu.stanford.bmir.protege.web.shared.axiom.DefaultAxiomTypeOrdering;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.object.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.HasHtmlBrowserText;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.IRIShortFormProvider;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 *         <p>
 *         A  module for a project.  The module ensures that any object graph contains project specific objects for the
 *         specified project (e.g. root ontology, short form provider etc.)
 */
@Module
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
    @ImportsCacheDirectory
    public File provideImportsCacheDirectory(ImportsCacheDirectoryProvider provider) {
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
    @NotesOntologyDocument
    public File provideNotesOntologyDocument(NotesOntologyDocumentProvider provider) {
        return provider.get();
    }

    @Provides
    @WatchFile
    public File provideWatchFile(WatchFileProvider provider) {
        return provider.get();
    }

    @Provides
    @RootOntology
    @ProjectSingleton
    public OWLOntology provideRootOntology(RootOntologyProvider provider) {
        return provider.get();
    }

    @Provides
    public OWLEntityProvider provideOWLEntityProvider(OWLDataFactory dataFactory) {
        return dataFactory;
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
            EditedAnnotationAssertion editedAnnotationAssertion,
            FunctionalDataPropertyAxiomChangeMatcher functionalDataPropertyAxiomChangeMatcher,
            ClassAssertionAxiomMatcher classAssertionAxiomMatcher,
            SubClassOfAxiomMatcher subClassOfAxiomMatcher,
            SubClassOfChangeMatcher subClassOfChangeMatcher) {
        return ImmutableSet.of(
                annotationAssertionChangeMatcher,
                propertyDomainAxiomChangeMatcher,
                propertyRangeAxiomChangeMatcher,
                editedAnnotationAssertion,
                functionalDataPropertyAxiomChangeMatcher,
                classAssertionAxiomMatcher,
                subClassOfAxiomMatcher,
                subClassOfChangeMatcher
        );
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
    public OWLObjectHierarchyProvider<OWLObjectProperty>
    provideObjectPropertyHierarchyProvider(OWLObjectPropertyHierarchyProvider provider) {
        return provider;
    }


    @Provides
    public OWLObjectHierarchyProvider<OWLDataProperty>
    provideDataPropertyHierarchyProvider(
            OWLDataPropertyHierarchyProvider provider) {
        return provider;
    }

    @Provides
    public OWLObjectHierarchyProvider<OWLAnnotationProperty>
    provideAnnotationPropertyHierarchyProvider(
            OWLAnnotationPropertyHierarchyProvider provider) {
        return provider;
    }

    @Provides
    public WatchManager provideWatchManager(WatchManagerImpl impl) {
        return impl;
    }

    @Provides
    public WatchEventManager provideWatchEventManager(WatchEventManagerImpl impl) {
        return impl;
    }

    @Provides
    public WatchStore provideWatchStore(WatchStoreImpl impl) {
        return impl;
    }

    @Provides
    public WatchTriggeredHandler provideWatchTriggeredHandler(WatchTriggeredHandlerImpl impl) {
        return impl;
    }

    @Provides
    public ShortFormProvider provideShortFormProvider(WebProtegeShortFormProvider provider) {
        return provider;
    }

    @Provides
    public IRIShortFormProvider provideIriShortFormProvider(WebProtegeIRIShortFormProvider provider) {
        return provider;
    }

    @Provides
    public BrowserTextProvider provideBrowserTextProvider(RenderingManager renderingManager) {
        return renderingManager;
    }

    @Provides
    public HasLang provideHasLang() {
        return () -> "en";
    }

    @Provides
    @ProjectSingleton
    public BidirectionalShortFormProvider provideBidirectionalShortFormProvider(WebProtegeBidirectionalShortFormProvider provider) {
        return provider;
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
    @ProjectSingleton
    public OWLAPINotesManager provideOWLAPINotesManager(OWLAPINotesManagerNotesAPIImpl impl) {
        return impl;
    }

    @Provides
    public LegacyEntityDataProvider provideLegacyEntityDataProvider(RenderingManager renderingManager) {
        return renderingManager;
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
    HasApplyChanges providesHasApplyChanges(OWLAPIProject project) {
        return project;
    }


    @Provides
    @ProjectSingleton
    OWLObjectHierarchyProvider<OWLClass> provideClassHierarchyProvider(AssertedClassHierarchyProvider provider) {
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
    Comparator<? super OWLAnnotationProperty> providesAnnotationPropertyComparator(OWLObjectComparatorImpl impl) {
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
            EntityDeprecatedChangedEventTranslator c6) {
        return ImmutableSet.of(c0, c1, c2, c3, c4, c5, c6);
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
}

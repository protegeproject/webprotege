package edu.stanford.bmir.protege.web.server.inject.project;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import edu.stanford.bmir.protege.web.server.OntologyChangeSubjectProvider;
import edu.stanford.bmir.protege.web.server.change.HasGetRevisionSummary;
import edu.stanford.bmir.protege.web.server.events.EventLifeTime;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.events.HasPostEvents;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOWLOntologyChecker;
import edu.stanford.bmir.protege.web.server.metrics.MetricCalculator;
import edu.stanford.bmir.protege.web.server.metrics.MetricCalculatorsProvider;
import edu.stanford.bmir.protege.web.server.notes.OWLAPINotesManager;
import edu.stanford.bmir.protege.web.server.notes.OWLAPINotesManagerNotesAPIImpl;
import edu.stanford.bmir.protege.web.server.owlapi.*;
import edu.stanford.bmir.protege.web.server.owlapi.change.*;
import edu.stanford.bmir.protege.web.server.render.*;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.server.watches.*;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import edu.stanford.bmir.protege.web.shared.HasGetChangeSubjects;
import edu.stanford.bmir.protege.web.shared.HasGetEntitiesWithIRI;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomComparatorImpl;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomTypeOrdering;
import edu.stanford.bmir.protege.web.shared.axiom.DefaultAxiomTypeOrdering;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
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
import java.util.concurrent.TimeUnit;

import static com.google.inject.Scopes.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 *
 * A Guice module for a project.  The module ensures that any object graph contains project specific objects for the
 * specified project (e.g. root ontology, short form provider etc.)
 */
public class ProjectModule extends AbstractModule {

    private final ProjectId projectId;


    public ProjectModule(ProjectId projectId) {
        this.projectId = projectId;
    }

    public static final EventLifeTime PROJECT_EVENT_LIFE_TIME = EventLifeTime.get(60, TimeUnit.SECONDS);

    @Override
    protected void configure() {

        bind(ProjectId.class).toInstance(projectId);


        bind(OWLOntology.class)
                .annotatedWith(RootOntology.class)
                .toProvider(RootOntologyProvider.class)
                .asEagerSingleton();


        bind(File.class).annotatedWith(ProjectDirectory.class)
                .toProvider(ProjectDirectoryProvider.class);
        bind(File.class).annotatedWith(ChangeHistoryFile.class).toProvider(ChangeHistoryFileProvider.class);

        bind(OWLDataFactory.class).toInstance(new OWLDataFactoryImpl(false, false));

        bind(OWLAnnotationPropertyProvider.class).to(OWLDataFactoryImpl.class);

        bind(OWLClass.class)
                .annotatedWith(ClassHierarchyRoot.class)
                .toProvider(ClassHierarchyRootProvider.class);

        bind(OWLObjectProperty.class).annotatedWith(ObjectPropertyHierarchyRoot.class).toProvider(ObjectPropertyHierarchyRootProvider.class);
        bind(OWLDataProperty.class).annotatedWith(DataPropertyHierarchyRoot.class).toProvider(DataPropertyHierarchyRootProvider.class);


        bind(OWLEntityProvider.class).to(OWLDataFactoryImpl.class);


        bind(ShortFormProvider.class).to(WebProtegeShortFormProvider.class);
        bind(IRIShortFormProvider.class).to(WebProtegeIRIShortFormProvider.class);

        // TODO: Needs annotation
        bind(new TypeLiteral<ImmutableList<IRI>>(){}).toInstance(ImmutableList.<IRI>builder()
                .addAll(DefaultShortFormAnnotationPropertyIRIs.asImmutableList()).build());

        bind(HasLang.class).toInstance(new HasLang() {
            @Override
            public String getLang() {
                return "en";
            }
        });

        bind(HasAnnotationAssertionAxioms.class).to(HasAnnotationAssertionAxiomsImpl.class);

        bind(EntityIRIChecker.class).to(EntityIRICheckerImpl.class);

        // TODO: CHECK
        bind(BidirectionalShortFormProvider.class).to(WebProtegeBidirectionalShortFormProvider.class).in(SINGLETON);

        bind(OntologyIRIShortFormProvider.class).to(WebProtegeOntologyIRIShortFormProvider.class);
        bind(OWLOntologyChecker.class).to(WebProtegeOWLOntologyChecker.class);

        bind(HighlightedEntityChecker.class).to(NullHighlightedEntityChecker.class).asEagerSingleton();

        bind(new TypeLiteral<OWLObjectHierarchyProvider<OWLClass>>() {})
                .to(AssertedClassHierarchyProvider.class).in(SINGLETON);
        bind(new TypeLiteral<OWLObjectHierarchyProvider<OWLObjectProperty>>(){})
                .to(OWLObjectPropertyHierarchyProvider.class).in(SINGLETON);
        bind(new TypeLiteral<OWLObjectHierarchyProvider<OWLDataProperty>>(){})
                .to(OWLDataPropertyHierarchyProvider.class).in(SINGLETON);
        bind(new TypeLiteral<OWLObjectHierarchyProvider<OWLAnnotationProperty>>(){})
                .to(OWLAnnotationPropertyHierarchyProvider.class).in(SINGLETON);


        bind(File.class).annotatedWith(WatchFile.class).toProvider(WatchFileProvider.class);
        bind(WatchManager.class).to(WatchManagerImpl.class).in(SINGLETON);
        bind(WatchEventManager.class).toProvider(WatchEventManagerProvider.class).asEagerSingleton();
        bind(WatchStore.class).to(WatchStoreImpl.class).in(SINGLETON);
        bind(WatchTriggeredHandler.class).to(WatchTriggeredHandlerImpl.class);

        bind(BrowserTextProvider.class).to(RenderingManager.class);

        bind(RenderingManager.class).in(SINGLETON);

        bind(EventManager.class).asEagerSingleton();

        bind(new TypeLiteral<EventManager<ProjectEvent<?>>>(){}).toInstance(new EventManager<ProjectEvent<?>>(PROJECT_EVENT_LIFE_TIME));

        bind(new TypeLiteral<HasPostEvents<ProjectEvent<?>>>(){}).to(new TypeLiteral<EventManager<ProjectEvent<?>>>(){});

        bind(EventLifeTime.class).toInstance(PROJECT_EVENT_LIFE_TIME);

        bind(RevisionNumber.class).toProvider(RevisionNumberProvider.class);

        bind(HasContainsEntityInSignature.class).to(HasContainsEntityInSignatureImpl.class);
        bind(HasGetEntitiesWithIRI.class).to(HasGetEntitiesWithIRIImpl.class);
        bind(HasGetEntitiesInSignature.class).to(HasGetEntitiesInSignatureImpl.class);

        bind(HasGetChangeSubjects.class).to(OntologyChangeSubjectProvider.class);

        bind(DeprecatedEntityChecker.class).to(DeprecatedEntityCheckerImpl.class);

        bind(HasGetRevisionSummary.class).to(RevisionManagerImpl.class);

        bind(RevisionManager.class).to(RevisionManagerImpl.class).asEagerSingleton();
        bind(RevisionStore.class).toProvider(RevisionStoreProvider.class).asEagerSingleton();

        bind(WatchManager.class).to(WatchManagerImpl.class).asEagerSingleton();
        bind(WatchStore.class).to(WatchStoreImpl.class).asEagerSingleton();

        bind(File.class).annotatedWith(NotesOntologyDocument.class).toProvider(NotesOntologyDocumentProvider.class);
        bind(OWLAPINotesManager.class).to(OWLAPINotesManagerNotesAPIImpl.class).in(SINGLETON);

        bind(LegacyEntityDataProvider.class).to(RenderingManager.class);


        bind(new TypeLiteral<OWLObjectSelector<OWLClassExpression>>(){})
                .to(OWLClassExpressionSelector.class);


        bind(new TypeLiteral<OWLObjectSelector<OWLObjectPropertyExpression>>(){})
                .to(OWLObjectPropertyExpressionSelector.class);

        bind(new TypeLiteral<OWLObjectSelector<OWLDataPropertyExpression>>(){})
                .to(OWLDataPropertyExpressionSelector.class);

        bind(new TypeLiteral<OWLObjectSelector<OWLIndividual>>(){})
                .to(OWLIndividualSelector.class);

        bind(new TypeLiteral<OWLObjectSelector<OWLClassExpression>>(){})
                .to(OWLClassExpressionSelector.class);


        bind(new TypeLiteral<OWLObjectSelector<SWRLAtom>>(){})
                .to(SWRLAtomSelector.class);


        bind(new TypeLiteral<Comparator<? super OWLAnnotation>>(){})
                .to(OWLObjectComparatorImpl.class);

        bind(new TypeLiteral<Comparator<? super OWLClass>>(){})
                .to(OWLObjectComparatorImpl.class);

        bind(new TypeLiteral<Comparator<? super OWLObjectProperty>>(){})
                .to(OWLObjectComparatorImpl.class);

        bind(new TypeLiteral<Comparator<? super OWLDataProperty>>(){})
                .to(OWLObjectComparatorImpl.class);

        bind(new TypeLiteral<Comparator<? super OWLAnnotationProperty>>(){})
                .to(OWLObjectComparatorImpl.class);

        bind(new TypeLiteral<Comparator<? super OWLDatatype>>(){})
                .to(OWLObjectComparatorImpl.class);

        bind(new TypeLiteral<Comparator<? super OWLNamedIndividual>>(){})
                .to(OWLObjectComparatorImpl.class);

        bind(new TypeLiteral<Comparator<? super SWRLAtom>>(){})
                .to(OWLObjectComparatorImpl.class);

        bind(new TypeLiteral<Comparator<OWLAxiom>>(){})
                .to(AxiomComparatorImpl.class);

        bind(new TypeLiteral<Comparator<OWLObject>>(){})
                .to(OWLObjectComparatorImpl.class);


        bind(new TypeLiteral<List<AxiomType<?>>>(){}).annotatedWith(AxiomTypeOrdering.class)
                .toInstance(DefaultAxiomTypeOrdering.get());

        bind(HasHtmlBrowserText.class)
                .to(RenderingManager.class);

        bind(OWLObjectRenderer.class)
                .to(OWLObjectRendererImpl.class);

        bind(HasImportsClosure.class)
                .toProvider(RootOntologyProvider.class)
                .asEagerSingleton();


        bind(new TypeLiteral<List<MetricCalculator>>(){})
                .toProvider(MetricCalculatorsProvider.class);

    }
}

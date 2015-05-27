package edu.stanford.bmir.protege.web.server.inject.project;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import edu.stanford.bmir.protege.web.server.OntologyChangeSubjectProvider;
import edu.stanford.bmir.protege.web.server.change.HasGetRevisionSummary;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOWLOntologyChecker;
import edu.stanford.bmir.protege.web.server.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.server.render.DeprecatedEntityCheckerImpl;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.watches.*;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.HasGetChangeSubjects;
import edu.stanford.bmir.protege.web.shared.HasGetEntitiesWithIRI;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.frame.HasFreshEntities;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.io.File;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 *
 * A Guice module for a project.  The module ensures that any object graph contains project specific objects for the
 * specified project (e.g. root ontology, short form provider etc.)
 */
public class ProjectModule extends AbstractModule {

    private ProjectId projectId;

    private OWLAPIProject project;

    public ProjectModule(OWLAPIProject project) {
        this.project = project;
        this.projectId = project.getProjectId();
    }


    @Override
    protected void configure() {
        bind(ProjectId.class).toInstance(projectId);

        bind(File.class).annotatedWith(ProjectDirectory.class).toProvider(ProjectDirectoryProvider.class);

        bind(OWLDataFactory.class).toInstance(project.getDataFactory());
        bind(OWLEntityProvider.class).toInstance(project.getDataFactory());
        bind(OWLOntology.class).annotatedWith(RootOntology.class).toInstance(project.getRootOntology());
        bind(ShortFormProvider.class).toInstance(project.getRenderingManager().getShortFormProvider());
        bind(BidirectionalShortFormProvider.class).toInstance(project.getRenderingManager().getShortFormProvider());
        bind(OntologyIRIShortFormProvider.class).to(WebProtegeOntologyIRIShortFormProvider.class);

        bind(OWLOntologyChecker.class).to(WebProtegeOWLOntologyChecker.class);

        bind(new TypeLiteral<OWLObjectHierarchyProvider<OWLClass>>(){}).to(AssertedClassHierarchyProvider.class);
        bind(new TypeLiteral<OWLObjectHierarchyProvider<OWLObjectProperty>>(){}).to(OWLObjectPropertyHierarchyProvider.class);
        bind(new TypeLiteral<OWLObjectHierarchyProvider<OWLDataProperty>>(){}).to(OWLDataPropertyHierarchyProvider.class);
        bind(new TypeLiteral<OWLObjectHierarchyProvider<OWLAnnotationProperty>>(){}).to(OWLAnnotationPropertyHierarchyProvider.class);

        bind(AssertedClassHierarchyProvider.class).toInstance(project.getClassHierarchyProvider());
        bind(OWLObjectPropertyHierarchyProvider.class).toInstance(project.getObjectPropertyHierarchyProvider());
        bind(OWLDataPropertyHierarchyProvider.class).toInstance(project.getDataPropertyHierarchyProvider());
        bind(OWLAnnotationPropertyHierarchyProvider.class).toInstance(project.getAnnotationPropertyHierarchyProvider());

        bind(File.class).annotatedWith(WatchFile.class).toProvider(WatchFileProvider.class);
        bind(WatchManager.class).to(WatchManagerImpl.class).asEagerSingleton();
        bind(WatchStore.class).to(WatchStoreImpl.class).asEagerSingleton();
        bind(WatchTriggeredHandler.class).to(WatchTriggeredHandlerImpl.class);

        bind(BrowserTextProvider.class).toInstance(project.getRenderingManager());

        bind(EventManager.class).toInstance(project.getEventManager());
        bind(new TypeLiteral<EventManager<ProjectEvent<?>>>(){}).toInstance(project.getEventManager());

        bind(RevisionNumber.class).toProvider(new Provider<RevisionNumber>() {
            @Override
            public RevisionNumber get() {
                return project.getRevisionNumber();
            }
        });

        bind(HasContainsEntityInSignature.class).toInstance(project);
        bind(HasGetChangeSubjects.class).to(OntologyChangeSubjectProvider.class);
        bind(HasGetEntitiesWithIRI.class).toInstance(project);

        bind(DeprecatedEntityChecker.class).to(DeprecatedEntityCheckerImpl.class);
        bind(HasGetEntitiesInSignature.class).toInstance(project);

        bind(HasGetRevisionSummary.class).toInstance(project);

        bind(WatchManager.class).to(WatchManagerImpl.class).asEagerSingleton();
        bind(WatchStore.class).to(WatchStoreImpl.class).asEagerSingleton();
        bind(File.class).annotatedWith(WatchFile.class).toProvider(WatchFileProvider.class);
    }
}

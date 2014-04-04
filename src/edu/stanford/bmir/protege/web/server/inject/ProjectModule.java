package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOWLEntityChecker;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOWLOntologyChecker;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.server.owlapi.AssertedClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.protege.editor.owl.model.hierarchy.OWLAnnotationPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLDataPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLObjectPropertyHierarchyProvider;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 *
 * A Guice module for a project.  The module ensures that any object graph contains project specific objects for the
 * specified project (e.g. root ontology, short form provider etc.)
 */
public class ProjectModule extends AbstractModule {

    private OWLAPIProject project;

    public ProjectModule(OWLAPIProject project) {
        this.project = project;
    }

    @Override
    protected void configure() {
        bind(OWLDataFactory.class).toInstance(project.getDataFactory());
        bind(OWLOntology.class).annotatedWith(RootOntology.class).toInstance(project.getRootOntology());
        bind(ShortFormProvider.class).toInstance(project.getRenderingManager().getShortFormProvider());
        bind(BidirectionalShortFormProvider.class).toInstance(project.getRenderingManager().getShortFormProvider());
        bind(OntologyIRIShortFormProvider.class).to(WebProtegeOntologyIRIShortFormProvider.class);

        bind(OWLEntityChecker.class).to(WebProtegeOWLEntityChecker.class);
        bind(OWLOntologyChecker.class).to(WebProtegeOWLOntologyChecker.class);

        bind(AssertedClassHierarchyProvider.class).toInstance(project.getClassHierarchyProvider());
        bind(OWLObjectPropertyHierarchyProvider.class).toInstance(project.getObjectPropertyHierarchyProvider());
        bind(OWLDataPropertyHierarchyProvider.class).toInstance(project.getDataPropertyHierarchyProvider());
        bind(OWLAnnotationPropertyHierarchyProvider.class).toInstance(project.getAnnotationPropertyHierarchyProvider());
    }
}

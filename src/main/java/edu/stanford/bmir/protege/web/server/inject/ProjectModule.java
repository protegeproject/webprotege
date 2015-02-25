package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOWLEntityChecker;
import edu.stanford.bmir.protege.web.server.mansyntax.WebProtegeOWLOntologyChecker;
import edu.stanford.bmir.protege.web.server.render.*;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.server.owlapi.AssertedClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomTypeOrdering;
import edu.stanford.bmir.protege.web.shared.axiom.DefaultAxiomComparator;
import edu.stanford.bmir.protege.web.shared.axiom.DefaultAxiomTypeOrdering;
import edu.stanford.bmir.protege.web.shared.object.*;
import org.protege.editor.owl.model.hierarchy.OWLAnnotationPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLDataPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLObjectPropertyHierarchyProvider;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.Comparator;
import java.util.List;

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
        bind(EntityIRIChecker.class).to(DefaultEntityIRIChecker.class);
        bind(OWLOntologyChecker.class).to(WebProtegeOWLOntologyChecker.class);

        bind(AssertedClassHierarchyProvider.class).toInstance(project.getClassHierarchyProvider());
        bind(OWLObjectPropertyHierarchyProvider.class).toInstance(project.getObjectPropertyHierarchyProvider());
        bind(OWLDataPropertyHierarchyProvider.class).toInstance(project.getDataPropertyHierarchyProvider());
        bind(OWLAnnotationPropertyHierarchyProvider.class).toInstance(project.getAnnotationPropertyHierarchyProvider());

        bind(new TypeLiteral<Comparator<OWLAxiom>>(){}).to(DefaultAxiomComparator.class);
        bind(new TypeLiteral<Comparator<OWLObject>>(){}).to(DefaultOWLObjectComparator.class);

        bind(new TypeLiteral<List<AxiomType<?>>>(){})
                .annotatedWith(AxiomTypeOrdering.class)
                .toInstance(DefaultAxiomTypeOrdering.get());

        bind(OWLObjectRenderer.class).to(ManchesterSyntaxObjectRenderer.class);
        bind(LiteralStyle.class).toInstance(LiteralStyle.REGULAR);
        bind(HttpLinkRenderer.class).to(DefaultHttpLinkRenderer.class);
        bind(LiteralRenderer.class).to(MarkdownLiteralRenderer.class);


        bind(new TypeLiteral<OWLObjectSelector<OWLClassExpression>>(){}).to(OWLClassExpressionSelector.class);
        bind(new TypeLiteral<OWLObjectSelector<OWLObjectPropertyExpression>>(){}).to(OWLObjectPropertyExpressionSelector.class);
        bind(new TypeLiteral<OWLObjectSelector<OWLDataPropertyExpression>>(){}).to(OWLDataPropertyExpressionSelector.class);
        bind(new TypeLiteral<OWLObjectSelector<OWLIndividual>>(){}).to(OWLIndividualSelector.class);
        bind(new TypeLiteral<OWLObjectSelector<SWRLAtom>>(){}).to(SWRLAtomSelector.class);



    }
}

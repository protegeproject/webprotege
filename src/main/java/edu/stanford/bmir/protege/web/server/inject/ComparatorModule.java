package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomComparatorImpl;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomSubjectProvider;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomTypeOrdering;
import edu.stanford.bmir.protege.web.shared.axiom.DefaultAxiomTypeOrdering;
import edu.stanford.bmir.protege.web.shared.object.*;
import org.semanticweb.owlapi.model.*;

import java.util.Comparator;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class ComparatorModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(new TypeLiteral<List<AxiomType<?>>>(){})
                .annotatedWith(AxiomTypeOrdering.class)
                .toInstance(DefaultAxiomTypeOrdering.get());

        bind(new TypeLiteral<Comparator<OWLObject>>() {})
                .to(OWLObjectComparatorImpl.class);

        bind(new TypeLiteral<Comparator<OWLAxiom>>(){})
                .to(AxiomComparatorImpl.class);

        bind(new TypeLiteral<Comparator<? super OWLClass>>(){})
                .to(new TypeLiteral<Comparator<OWLObject>>() {});
        bind(new TypeLiteral<Comparator<? super OWLObjectProperty>>() {})
                .to(new TypeLiteral<Comparator<OWLObject>>() {});
        bind(new TypeLiteral<Comparator<? super OWLDataProperty>>() {})
                .to(new TypeLiteral<Comparator<OWLObject>>(){});
        bind(new TypeLiteral<Comparator<? super OWLAnnotationProperty>>() {})
                .to(new TypeLiteral<Comparator<OWLObject>>(){});
        bind(new TypeLiteral<Comparator<? super OWLNamedIndividual>>() {})
                .to(new TypeLiteral<Comparator<OWLObject>>(){});
        bind(new TypeLiteral<Comparator<? super OWLDatatype>>() {})
                .to(new TypeLiteral<Comparator<OWLObject>>(){});
        bind(new TypeLiteral<Comparator<? super SWRLAtom>>() {})
                .to(new TypeLiteral<Comparator<OWLObject>>(){});

        bind(new TypeLiteral<OWLObjectSelector<OWLClassExpression>>() {})
                .to(OWLClassExpressionSelector.class);
        bind(new TypeLiteral<OWLObjectSelector<OWLObjectPropertyExpression>>(){})
                .to(OWLObjectPropertyExpressionSelector.class);
        bind(new TypeLiteral<OWLObjectSelector<OWLDataPropertyExpression>>(){})
                .to(OWLDataPropertyExpressionSelector.class);
        bind(new TypeLiteral<OWLObjectSelector<OWLIndividual>>(){})
                .to(OWLIndividualSelector.class);
        bind(new TypeLiteral<OWLObjectSelector<SWRLAtom>>(){})
                .to(SWRLAtomSelector.class);
    }
}

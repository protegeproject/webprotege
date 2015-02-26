package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class AnnotationsSectionRenderer<S extends OWLEntity> extends AbstractOWLAxiomItemSectionRenderer<S, OWLAnnotationAssertionAxiom, OWLObject> {


    public static final String VALUE_SEPARATOR = "<span style=\"color: darkgray;\"> : </span>";

    public static final String PROPERTY_VALUES_SEPARATOR = " ";

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.ANNOTATIONS;
    }

    @Override
    protected Set<OWLAnnotationAssertionAxiom> getAxiomsInOntology(S subject, OWLOntology ontology) {
        return ontology.getAnnotationAssertionAxioms(subject.getIRI());
    }

    @Override
    public List<OWLObject> getRenderablesForItem(S subject, OWLAnnotationAssertionAxiom item, OWLOntology ontology) {
        return Lists.<OWLObject>newArrayList(item.getProperty(), item.getValue());
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List<OWLObject> renderables) {
        return renderableIndex == 0 ? PROPERTY_VALUES_SEPARATOR : VALUE_SEPARATOR;
    }

    @Override
    protected List<OWLAnnotationAssertionAxiom> sort(Set<OWLAnnotationAssertionAxiom> items, final ShortFormProvider shortFormProvider) {
        List<OWLAnnotationAssertionAxiom> sorted = Lists.newArrayList(items);
        Collections.sort(sorted, new Comparator<OWLAnnotationAssertionAxiom>() {
            @Override
            public int compare(OWLAnnotationAssertionAxiom owlAnnotationAssertionAxiom,
                               OWLAnnotationAssertionAxiom owlAnnotationAssertionAxiom2) {
                IRIIndexProvider iriIndexProvider = IRIIndexProvider.withDefaultAnnotationPropertyOrdering();
                return new AnnotationPropertyComparatorImpl(shortFormProvider, iriIndexProvider).compare
                        (owlAnnotationAssertionAxiom.getProperty(), owlAnnotationAssertionAxiom2.getProperty());
            }
        });
        return sorted;
    }
}

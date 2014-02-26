package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class ClassRangeOfSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLClass, OWLObjectPropertyRangeAxiom, OWLClassExpression> {

    @Override
    protected Set<OWLObjectPropertyRangeAxiom> getAxiomsInOntology(OWLClass subject, OWLOntology ontology) {
        final Set<OWLObjectPropertyRangeAxiom> result = Sets.newHashSet();
        for(OWLAxiom ax : ontology.getReferencingAxioms(subject)) {
            if(ax instanceof OWLObjectPropertyRangeAxiom) {
                result.add((OWLObjectPropertyRangeAxiom) ax);
            }
        }
        return result;
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.SUBCLASS_OF;
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLClass subject,
                                                                   OWLObjectPropertyRangeAxiom item,
                                                                   OWLOntology ontology) {
        return Lists.newArrayList(item.asOWLSubClassOfAxiom().getSuperClass());
    }
}

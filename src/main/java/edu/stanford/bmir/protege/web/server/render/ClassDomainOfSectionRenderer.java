package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class ClassDomainOfSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLClass, OWLPropertyDomainAxiom<?>, OWLObject> {

    @Override
    protected Set<OWLPropertyDomainAxiom<?>> getAxiomsInOntology(OWLClass subject, OWLOntology ontology) {
        final Set<OWLPropertyDomainAxiom<?>> result = Sets.newHashSet();
        for(OWLAxiom ax : ontology.getReferencingAxioms(subject)) {
            ax.accept(new OWLAxiomVisitorAdapter() {
                @Override
                public void visit(OWLDataPropertyDomainAxiom axiom) {
                    result.add(axiom);
                }

                @Override
                public void visit(OWLObjectPropertyDomainAxiom axiom) {
                    result.add(axiom);
                }

                @Override
                public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
                    // TODO?
                }
            });
        }

        return result;
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.SUPERCLASS_OF;
    }

    @Override
    public List<OWLObject> getRenderablesForItem(OWLClass subject,
                                                 OWLPropertyDomainAxiom<?> item,
                                                 OWLOntology ontology) {
        return Lists.<OWLObject>newArrayList(item.asOWLSubClassOfAxiom().getSubClass());
    }
}

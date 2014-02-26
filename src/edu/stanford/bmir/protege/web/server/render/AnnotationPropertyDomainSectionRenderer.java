package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class AnnotationPropertyDomainSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLAnnotationProperty, OWLAnnotationPropertyDomainAxiom, OWLObject> {


    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DOMAIN;
    }

    @Override
    protected Set<OWLAnnotationPropertyDomainAxiom> getAxiomsInOntology(OWLAnnotationProperty subject,
                                                                        OWLOntology ontology) {
        return ontology.getAnnotationPropertyDomainAxioms(subject);
    }


    @Override
    public List<OWLObject> getRenderablesForItem(OWLAnnotationProperty subject,
                                                 OWLAnnotationPropertyDomainAxiom item,
                                                 OWLOntology ontology) {
        Set<OWLEntity> domain = ontology.getEntitiesInSignature(item.getDomain(), true);
        if(domain.isEmpty()) {
            return Lists.<OWLObject>newArrayList(item.getDomain());
        }
        else {
            return Lists.<OWLObject>newArrayList(domain);
        }
    }
}

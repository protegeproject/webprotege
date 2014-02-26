package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class AnnotationPropertyRangeSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLAnnotationProperty, OWLAnnotationPropertyRangeAxiom, OWLObject> {


    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.RANGE;
    }

    @Override
    protected Set<OWLAnnotationPropertyRangeAxiom> getAxiomsInOntology(OWLAnnotationProperty subject,
                                                                       OWLOntology ontology) {
        return ontology.getAnnotationPropertyRangeAxioms(subject);
    }


    @Override
    public List<OWLObject> getRenderablesForItem(OWLAnnotationProperty subject,
                                                 OWLAnnotationPropertyRangeAxiom item,
                                                 OWLOntology ontology) {
        Set<OWLEntity> range = ontology.getEntitiesInSignature(item.getRange(), true);
        if(range.isEmpty()) {
            for(EntityType<?> entityType : EntityType.values()) {
                OWLEntity entity = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLEntity(entityType, item.getRange());
                if(entity.isBuiltIn()) {
                    return Lists.<OWLObject>newArrayList(entity);
                }
            }
            return Lists.<OWLObject>newArrayList(item.getRange());
        }
        else {
            return Lists.<OWLObject>newArrayList(range);
        }
    }
}

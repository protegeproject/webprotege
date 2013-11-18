package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2012
 */
public class OWLNamedIndividualTripleMapper extends TripleMapper<OWLNamedIndividual> {

    public OWLNamedIndividualTripleMapper(OWLAPIProject project, OWLNamedIndividual entity, AnnotationsTreatment annotationsTreatment, NonAnnotationTreatment nonAnnotationTreatment) {
        super(project, entity, annotationsTreatment, nonAnnotationTreatment);
    }

    @Override
    public List<Triple> getNonAnnotationAssertionTriples() {
        List<Triple> result = new ArrayList<Triple>();
        OWLNamedIndividual ind = getEntity();
        for(OWLOntology ont : getRootOntologyImportsClosure()) {
            for(OWLObjectPropertyAssertionAxiom ax : ont.getObjectPropertyAssertionAxioms(ind)) {
                if (!ax.getProperty().isAnonymous()) {
                    RenderingManager rm = getRenderingManager();
                    PropertyEntityData propData = rm.getPropertyEntityData(ax.getProperty().asOWLObjectProperty());
                    OWLIndividual object = ax.getObject();
                    EntityData valueData;
                    if(object.isAnonymous()) {
                        valueData = rm.getEntityData(object.asOWLAnonymousIndividual());
                    }
                    else {
                        valueData = rm.getEntityData(object.asOWLNamedIndividual());
                    }
                    EntityData subjectData = rm.getEntityData(ind);
                    result.add(new Triple(subjectData, propData, valueData));
                }
            }
            for(OWLDataPropertyAssertionAxiom ax : ont.getDataPropertyAssertionAxioms(ind)) {
                RenderingManager rm = getRenderingManager();
                PropertyEntityData propData = rm.getPropertyEntityData(ax.getProperty().asOWLDataProperty());
                EntityData subjectData = rm.getEntityData(ind);
                result.add(new Triple(subjectData, propData, rm.getEntityData(ax.getObject())));
            }

        }
        return result;
    }

}

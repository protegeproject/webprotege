package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2012
 */
public class OWLObjectPropertyTripleMapper extends TripleMapper<OWLObjectProperty> {

    public OWLObjectPropertyTripleMapper(OWLAPIProject project, OWLObjectProperty entity, AnnotationsTreatment annotationsTreatment, NonAnnotationTreatment nonAnnotationTreatment) {
        super(project, entity, annotationsTreatment, nonAnnotationTreatment);
    }

    @Override
    public List<Triple> getNonAnnotationAssertionTriples() {
        return Collections.emptyList();
    }
}

package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class OWLAPIProjectObjectPropertyCountMetric extends OWLAPIProjectEntityCountMetric {

    public OWLAPIProjectObjectPropertyCountMetric(OWLAPIProject project) {
        super(project, EntityType.OBJECT_PROPERTY);
    }

    @Override
    protected int getEntityCount() {
        return getRootOntology().getObjectPropertiesInSignature(true).size();
    }
}

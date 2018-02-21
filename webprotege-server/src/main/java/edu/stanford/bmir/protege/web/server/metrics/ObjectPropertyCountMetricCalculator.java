package edu.stanford.bmir.protege.web.server.metrics;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class ObjectPropertyCountMetricCalculator extends EntityCountMetricCalculator {

    public ObjectPropertyCountMetricCalculator(OWLOntology project) {
        super(project, EntityType.OBJECT_PROPERTY);
    }

    @Override
    protected int getEntityCount() {
        return getRootOntology().getObjectPropertiesInSignature(true).size();
    }
}

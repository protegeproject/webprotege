package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class AnnotationPropertyCountMetricCalculator extends EntityCountMetricCalculator {

    public AnnotationPropertyCountMetricCalculator(OWLOntology project) {
        super(project, EntityType.ANNOTATION_PROPERTY);
    }

    @Override
    protected int getEntityCount() {
        return getRootOntology().getAnnotationPropertiesInSignature().size();
    }
}

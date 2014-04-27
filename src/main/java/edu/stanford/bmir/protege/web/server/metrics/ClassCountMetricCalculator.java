package edu.stanford.bmir.protege.web.server.metrics;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class ClassCountMetricCalculator extends EntityCountMetricCalculator {

    public ClassCountMetricCalculator(OWLOntology project) {
        super(project, EntityType.CLASS);
    }

    @Override
    protected int getEntityCount() {
        return getRootOntology().getClassesInSignature(true).size();
    }
}

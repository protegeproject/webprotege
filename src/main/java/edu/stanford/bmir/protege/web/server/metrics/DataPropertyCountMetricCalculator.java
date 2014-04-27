package edu.stanford.bmir.protege.web.server.metrics;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class DataPropertyCountMetricCalculator extends EntityCountMetricCalculator {

    public DataPropertyCountMetricCalculator(OWLOntology project) {
        super(project, EntityType.DATA_PROPERTY);
    }

    @Override
    protected int getEntityCount() {
        return getRootOntology().getDataPropertiesInSignature(true).size();
    }
}

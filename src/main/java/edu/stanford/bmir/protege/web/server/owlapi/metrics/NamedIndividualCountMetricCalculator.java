package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class NamedIndividualCountMetricCalculator extends EntityCountMetricCalculator {

    public NamedIndividualCountMetricCalculator(OWLOntology project) {
        super(project, EntityType.NAMED_INDIVIDUAL);
    }

    @Override
    protected int getEntityCount() {
        return getRootOntology().getIndividualsInSignature(true).size();
    }
}

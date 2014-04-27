package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

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

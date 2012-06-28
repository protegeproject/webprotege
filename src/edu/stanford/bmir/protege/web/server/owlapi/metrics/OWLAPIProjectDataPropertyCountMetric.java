package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class OWLAPIProjectDataPropertyCountMetric extends OWLAPIProjectEntityCountMetric {

    public OWLAPIProjectDataPropertyCountMetric(OWLAPIProject project) {
        super(project, EntityType.DATA_PROPERTY);
    }

    @Override
    protected int getEntityCount() {
        return getRootOntology().getDataPropertiesInSignature().size();
    }
}

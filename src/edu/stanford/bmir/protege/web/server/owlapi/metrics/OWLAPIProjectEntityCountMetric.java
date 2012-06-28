package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetric;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricIntValue;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricState;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricValue;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 * <P>
 *     An abstract base class for different kinds of entity count.
 * </P>
 */
public abstract class OWLAPIProjectEntityCountMetric extends OWLAPIProjectMetric {

    private EntityType<?> entityType;

    private String metricName;
    
    /**
     * Constructs an entity count metric that counts entities of the specified type.
     * @param project The project over which the value for the metric is computed.
     * @param entityType The entity type.
     */
    public OWLAPIProjectEntityCountMetric(OWLAPIProject project, EntityType<?> entityType) {
        super(project);
        this.entityType = entityType;
        StringBuilder sb = new StringBuilder();
        String typeName = entityType.getName();
        for(int i = 0; i < typeName.length(); i++) {
            char ch = typeName.charAt(i);
            if(Character.isUpperCase(ch)) {
                sb.append(" ");
            }
            sb.append(ch);
        }
        metricName = sb.toString().trim() + " count";
    }

    @Override
    protected final OWLAPIProjectMetricValue computeValue() {
        int entityCount = getEntityCount();
        
        return new OWLAPIProjectMetricIntValue(metricName, entityCount);
    }
    
    protected abstract int getEntityCount();

    @Override
    protected OWLAPIProjectMetricState getStateAfterChanges(List<OWLOntologyChange> changes) {
        for(OWLOntologyChange change : changes) {
            for(OWLEntity entity : change.getSignature()) {
                if(entity.isType(entityType)) {
                    return OWLAPIProjectMetricState.DIRTY;
                }
            }
        }
        return OWLAPIProjectMetricState.CLEAN;
    }
}

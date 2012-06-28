package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2012
 */
public class GetRelatedPropertiesStrategy extends OntologyServiceStrategy<List<Triple>> {
    
    private String className;

    public GetRelatedPropertiesStrategy(OWLAPIProject project, UserId userId, String className) {
        super(project, userId);
        this.className = className;
    }

    @Override
    public List<Triple> execute() {
        List<Triple> result = new ArrayList<Triple>();
        
        RenderingManager rm = getProject().getRenderingManager();
        for(OWLEntity entity : rm.getEntities(className)) {
            TripleMapperSelector selector = new TripleMapperSelector(getProject(), AnnotationsTreatment.EXCLUDE_ANNOTATIONS, NonAnnotationTreatment.INCLUDE_NON_ANNOTATIONS);
            TripleMapper<?> mapper = selector.getMapper(entity);
            result.addAll(mapper.getTriples());
        }
        
        return result;
    }
}

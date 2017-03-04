package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2012
 */
public class GetDirectTypesStrategy extends OntologyServiceStrategy<List<EntityData>> {

    private String instanceName;

    public GetDirectTypesStrategy(OWLAPIProject project, UserId userId, String instanceName) {
        super(project, userId);
        this.instanceName = instanceName;
    }

    @Override
    public List<EntityData> execute() {
        List<EntityData> result = new ArrayList<EntityData>();
        
        OWLNamedIndividual individual = getRenderingManager().getEntity(instanceName, EntityType.NAMED_INDIVIDUAL);
        Set<OWLOntology> importsClosure = getRootOntologyImportsClosure();
        for(OWLClassExpression ce : EntitySearcher.getTypes(individual, importsClosure)) {
            if(!ce.isAnonymous()) {
                result.add(getRenderingManager().getEntityData(ce.asOWLClass()));
            }
        }        
        return result;
    }

}

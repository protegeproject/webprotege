package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2012
 */
public class SimpleOWLEntityCreatorFactory extends OWLEntityCreatorFactory {

    
    
    @Override
    public <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(OWLAPIProject project, UserId userId, String shortName, EntityType<E> entityType) {
        OWLOntology rootOntology = project.getRootOntology();
        OWLOntologyID id = rootOntology.getOntologyID();
        String escapedShortName = escapeShortName(shortName);
        String base = getDefaultIRIBase(id);
        IRI entityIRI = IRI.create(base + escapedShortName);
        OWLDataFactory dataFactory = project.getDataFactory();
        E entity = dataFactory.getOWLEntity(entityType, entityIRI);
        OWLDeclarationAxiom ax = dataFactory.getOWLDeclarationAxiom(entity);
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        changes.add(new AddAxiom(rootOntology, ax));
        return new OWLEntityCreator<E>(entity, changes);
    }
    
}

package edu.stanford.bmir.protege.web.server.render;

import edu.stanford.bmir.protege.web.server.inject.RootOntology;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 20/02/2014
 */
public class EntityIRICheckerImpl implements EntityIRIChecker {

    private OWLOntology rootOntology;

    @Inject
    public EntityIRICheckerImpl(@RootOntology OWLOntology rootOntology) {
        this.rootOntology = rootOntology;
    }

    @Override
    public boolean isEntityIRI(IRI iri) {
        return iri.isTopEntity() || iri.isBottomEntity() || rootOntology.containsEntityInSignature(iri, true);
    }

    @Override
    public Collection<OWLEntity> getEntitiesWithIRI(IRI iri) {
        Set<OWLEntity> result = new HashSet<>();
        OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager().getOWLDataFactory();
        for(EntityType<?> entityType : EntityType.values()) {
            OWLEntity entity = dataFactory.getOWLEntity(entityType, iri);
            if(rootOntology.containsEntityInSignature(entity, true)) {
                result.add(entity);
            }
        }
        return result;
    }
}

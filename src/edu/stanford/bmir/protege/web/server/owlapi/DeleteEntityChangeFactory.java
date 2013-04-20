package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public class DeleteEntityChangeFactory extends OWLOntologyChangeFactory {

    private String entityName;

    /**
     * Constructs a change enactor that deletes an entity from a project.  This corresponds to removing all axioms
     * which refer
     * @param project The project which the entity will be removed from.
     * @param changeDescription The high level description of the change.
     * @param entityName The name (IRI) of the entity to be deleted.
     */
    public DeleteEntityChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String entityName) {
        super(project, userId, changeDescription);
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        OWLOntologyManager rootOntologyManager = getRootOntologyManager();
        OWLOntology rootOntology = getRootOntology();
        Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
        OWLEntityRemover entityRemover = new OWLEntityRemover(rootOntologyManager, importsClosure);

        RenderingManager rm = getRenderingManager();
        Set<OWLEntity> entities = rm.getEntities(entityName);
        for(OWLEntity entity : entities) {
            entity.accept(entityRemover);
        }
        changeListToFill.addAll(entityRemover.getChanges());
    }

}

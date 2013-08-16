package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class CreateEntityChangeListGenerator<E extends OWLEntity> implements ChangeListGenerator<E> {

    private EntityType<E> entityType;

    private EntityShortForm shortForm;

    private EntityCrudContext crudContext;

    private EntityCrudKit crudKit;

    public CreateEntityChangeListGenerator(EntityType<E> entityType, EntityShortForm shortForm, EntityCrudContext crudContext, EntityCrudKit crudKit) {
        this.entityType = entityType;
        this.shortForm = shortForm;
        this.crudContext = crudContext;
        this.crudKit = crudKit;
    }

    @Override
    public OntologyChangeList<E> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
        OntologyChangeList.Builder<E> builder = OntologyChangeList.builder();
        crudKit.create(entityType, shortForm, crudContext, builder);
        return builder.build();
    }

    @Override
    public E getRenamedResult(E result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}

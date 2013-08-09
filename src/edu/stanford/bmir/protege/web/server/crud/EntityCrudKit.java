package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.crud.HasKitId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public interface EntityCrudKit extends HasKitId {

    EntityCrudKitSettings getSettings();

    ChangeListGenerator<OWLEntity> create(EntityType<?> entityType, EntityShortForm shortForm, EntityCrudContext context);

    ChangeListGenerator<OWLEntity> update(OWLEntity entity, EntityShortForm shortForm, EntityCrudContext context);

    ChangeListGenerator<OWLEntity> delete(OWLEntity entity, EntityCrudContext context);
}

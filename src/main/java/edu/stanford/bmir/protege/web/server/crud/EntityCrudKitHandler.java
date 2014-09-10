package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.shared.crud.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public interface EntityCrudKitHandler<S extends EntityCrudKitSuffixSettings> extends HasKitId {

    EntityCrudKitPrefixSettings getPrefixSettings();

    S getSuffixSettings();

    EntityCrudKitSettings<S> getSettings();

    <E extends OWLEntity> E create(EntityType<E> entityType, EntityShortForm shortForm, EntityCrudContext context, OntologyChangeList.Builder<E> changeListBuilder) throws CannotGenerateFreshEntityIdException;

    <E extends OWLEntity> void update(E entity, EntityShortForm shortForm, EntityCrudContext context, OntologyChangeList.Builder<E> changeListBuilder);

    <E extends OWLEntity> String getShortForm(E entity, EntityCrudContext context);

}

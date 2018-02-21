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
public interface EntityCrudKitHandler<S extends EntityCrudKitSuffixSettings, C extends ChangeSetEntityCrudSession>
        extends HasKitId {

    EntityCrudKitPrefixSettings getPrefixSettings();

    S getSuffixSettings();

    EntityCrudKitSettings<S> getSettings();

    /**
     * Creates a fresh change set session.  Each time a set of changes is applied to an ontology a session is created
     * and passes to the {@link #create(ChangeSetEntityCrudSession, EntityType, EntityShortForm, EntityCrudContext,
     * OntologyChangeList.Builder)}
     * and {@link #update(ChangeSetEntityCrudSession, OWLEntity, EntityShortForm, EntityCrudContext,
     * OntologyChangeList.Builder)}
     * methods.  The session can be used to persist things like counters over multiple entity creations.
     *
     * @return A {@link C}.  Not {@code null}.
     */
    C createChangeSetSession();

    <E extends OWLEntity> E create(
            C session,
            EntityType<E> entityType,
            EntityShortForm shortForm,
            EntityCrudContext context,
            OntologyChangeList.Builder<E> changeListBuilder) throws CannotGenerateFreshEntityIdException;

    <E extends OWLEntity> void update(
            C session,
            E entity,
            EntityShortForm shortForm,
            EntityCrudContext context,
            OntologyChangeList.Builder<E> changeListBuilder);

    <E extends OWLEntity> String getShortForm(E entity, EntityCrudContext context);

}

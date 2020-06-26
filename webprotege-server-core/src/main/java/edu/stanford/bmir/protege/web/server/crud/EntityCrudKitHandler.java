package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.shared.crud.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

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
     * and passes to the {@link #create(ChangeSetEntityCrudSession, EntityType, EntityShortForm, Optional, ImmutableList, EntityCrudContext, OntologyChangeList.Builder)}
     * method.  The session can be used to persist things like counters over multiple entity creations.
     *
     * @return A {@link C}.  Not {@code null}.
     */
    C createChangeSetSession();

    <E extends OWLEntity> E create(
            @Nonnull C session,
            @Nonnull EntityType<E> entityType,
            @Nonnull EntityShortForm shortForm,
            @Nonnull Optional<String> langTag,
            @Nonnull ImmutableList<OWLEntity> parents,
            @Nonnull EntityCrudContext context,
            @Nonnull OntologyChangeList.Builder<E> changeListBuilder) throws CannotGenerateFreshEntityIdException;
}

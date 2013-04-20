package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/12/2012
 */
public class MutableFreshEntitiesHandler implements FreshEntitiesHandler {


    private final Table<String, EntityType<?>, OWLEntity> entitiesTable = HashBasedTable.create();

    /**
     * Gets the policy supported by this handler.
     * @return The {@link edu.stanford.bmir.protege.web.client.ui.frame.FreshEntitiesPolicy}.  Not {@code null}.
     */
    @Override
    public FreshEntitiesPolicy getFreshEntitiesPolicy() {
        return FreshEntitiesPolicy.ALLOWED;
    }

    /**
     * Gets the error message for when an entity is a fresh entity.
     * @param browserText The browser text being parsed.
     * @return An error message.  Not {@code null}.
     */
    @Override
    public String getErrorMessage(String browserText) {
        return "<b>" + browserText + "</b> is a new name.";
    }

    /**
     * Gets a fresh entity of the given type with the specified browser text.
     * @param browserText The browser text. Not {@code null}.
     * @param type The type.  Not {@code null}.
     * @param <E> The fresh entity type.
     * @return The fresh entity.  Not {@code null}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <E extends OWLEntity> E getFreshEntity(String browserText, EntityType<E> type) {
        OWLEntity entity = entitiesTable.get(browserText, type);
        if(entity != null) {
            return (E) entity;
        }
        UserId userId = Application.get().getUserId();
        E freshEntity = DataFactory.getFreshOWLEntity(type, browserText);
        entitiesTable.put(browserText, type, freshEntity);
        return freshEntity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends OWLEntity> Optional<E> getRegisteredFreshEntity(String browserText, EntityType<E> entityType) {
        E entity = (E) entitiesTable.get(browserText, entityType);
        if(entity == null) {
            return Optional.absent();
        }
        else {
            return Optional.of(entity);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends OWLEntity> Optional<E> getRegisteredFreshEntity(String browserText) {
        final Collection<OWLEntity> values = entitiesTable.row(browserText).values();
        if(values == null || values.isEmpty()) {
            return Optional.absent();
        }
        E entity = (E) values.iterator().next();
        return Optional.of(entity);
    }

    @Override
    public boolean isRegisteredFreshEntity(String browserText) {
        return entitiesTable.containsRow(browserText);
    }
}

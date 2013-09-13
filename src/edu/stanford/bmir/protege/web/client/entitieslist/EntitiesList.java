package edu.stanford.bmir.protege.web.client.entitieslist;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.util.Collection;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public interface EntitiesList<E extends OWLEntityData> extends HasSelectionHandlers<E>, IsWidget {

    void clear();

    void setListData(List<E> entities);

    void setSelectedEntity(E entity);

    Optional<E> getSelectedEntity();

    void remove(E entity);

    void removeAll(Collection<E> entities);

    void addAll(Collection<E> entities);
}

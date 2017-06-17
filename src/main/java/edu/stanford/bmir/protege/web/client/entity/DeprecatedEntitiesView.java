package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.pagination.HasPages;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.EntitySelectionChangedHandler;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
public interface DeprecatedEntitiesView extends IsWidget, HasPages {

    void setSelectedEntity(OWLEntityData entity);

    Optional<OWLEntityData> getSelectedEntity();

    void setSelectionChangedHandler(SelectionHandler<OWLEntityData> handler);

    void setEntities(List<OWLEntityData> entities);

    void addEntity(OWLEntityData entityData);

    void removeEntity(OWLEntityData entityData);
}

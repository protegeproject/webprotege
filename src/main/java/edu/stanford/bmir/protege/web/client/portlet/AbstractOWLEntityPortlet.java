package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@SuppressWarnings("deprecated")
public abstract class AbstractOWLEntityPortlet extends AbstractEntityPortlet {

    protected AbstractOWLEntityPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
    }

    /**
     * Gets the {@link EntityType} of the selected entity.
     * @return The {@link EntityType} of the selected entity.  Not {@code null}.
     */
    public Optional<EntityType<?>> getSelectedEntityType() {
        Optional<OWLEntity> selectedEntity = getSelectedEntity();
        if(!selectedEntity.isPresent()) {
            return Optional.absent();
        }
        else {
            return Optional.<EntityType<?>>of(selectedEntity.get().getEntityType());
        }
    }

    public boolean isSelected(OWLEntity entity) {
        Optional<OWLEntity> sel = getSelectedEntity();
        return sel.isPresent() && sel.get().equals(entity);
    }
//    /**
//     * Gets the selected entity.
//     * @return The selected entity as an {@link Optional}.
//     */
//    public Optional<OWLEntity> getSelectedEntity() {
//        Optional<OWLEntity> sel = getSelectedEntity();
//        if(!sel.isPresent()) {
//            return Optional.absent();
//        }
//        else {
//            return Optional.of(sel.get());
//        }
//    }

//    protected Optional<OWLEntity> toOWLEntity(EntityData entityData) {
//        Optional<OWLEntityData> ed = toOWLEntityData(entityData);
//        if(ed.isPresent()) {
//            return Optional.of(ed.get().getEntity());
//        }
//        else {
//            return Optional.absent();
//        }
//    }

    protected Optional<OWLClass> toOWLClass(EntityData entityData) {
        if(entityData == null) {
            return Optional.absent();
        }
        String name = entityData.getName();
        if(name == null) {
            return Optional.absent();
        }
        IRI iri = IRI.create(name);
        return Optional.of(DataFactory.getOWLClass(iri));
    }
}

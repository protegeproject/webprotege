package edu.stanford.bmir.protege.web.client.ui.portlet;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyType;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@SuppressWarnings("deprecated")
public abstract class AbstractOWLEntityPortlet extends AbstractEntityPortlet {

    protected AbstractOWLEntityPortlet(Project project) {
        super(project);
    }

    protected AbstractOWLEntityPortlet(Project project, boolean initialize) {
        super(project, initialize);
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
    /**
     * Gets the selected entity.
     * @return The selected entity as an {@link Optional}.
     */
    public Optional<OWLEntity> getSelectedEntity() {
        Optional<OWLEntityData> sel = getSelectedEntityData();
        if(!sel.isPresent()) {
            return Optional.absent();
        }
        else {
            return Optional.of(sel.get().getEntity());
        }
    }

    protected Optional<OWLEntity> toOWLEntity(EntityData entityData) {
        Optional<OWLEntityData> ed = toOWLEntityData(entityData);
        if(ed.isPresent()) {
            return Optional.of(ed.get().getEntity());
        }
        else {
            return Optional.absent();
        }
    }

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

    @Override
    public Collection<EntityData> getSelection() {
        return Collections.emptyList();
    }
}

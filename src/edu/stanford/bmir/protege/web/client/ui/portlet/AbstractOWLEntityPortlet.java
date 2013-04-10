package edu.stanford.bmir.protege.web.client.ui.portlet;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyType;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
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

    public Optional<OWLEntityData> getSelectedEntityData() {
        Collection<EntityData> selection = getSelection();
        // Not sure what the difference is here
        if(selection == null || selection.isEmpty()) {
            EntityData entityData = getEntity();
            if(entityData != null) {
                selection = Collections.singleton(entityData);
            }
        }

        if(selection == null) {
            return Optional.absent();
        }
        if(selection.isEmpty()) {
            return Optional.absent();
        }
        EntityData entityData = selection.iterator().next();
        if(entityData instanceof PropertyEntityData) {
            PropertyEntityData propertyEntityData = (PropertyEntityData) entityData;
            PropertyType propertyType = propertyEntityData.getPropertyType();
            if (propertyType != null) {
                switch(propertyType) {
                    case OBJECT:
                        return Optional.<OWLEntityData>of(new OWLObjectPropertyData(DataFactory.getOWLObjectProperty(entityData.getName()), entityData.getBrowserText()));
                    case DATATYPE:
                        return Optional.<OWLEntityData>of(new OWLDataPropertyData(DataFactory.getOWLDataProperty(entityData.getName()), entityData.getBrowserText()));
                    case ANNOTATION:
                        return Optional.<OWLEntityData>of(new OWLAnnotationPropertyData(DataFactory.getOWLAnnotationProperty(entityData.getName()), entityData.getBrowserText()));

                }
            }
        }
        else if(entityData.getValueType() == ValueType.Cls) {
            return Optional.<OWLEntityData>of(new OWLClassData(DataFactory.getOWLClass(entityData.getName()), entityData.getBrowserText()));
        }
        else if(entityData.getValueType() == ValueType.Instance) {
            return Optional.<OWLEntityData>of(new OWLNamedIndividualData(DataFactory.getOWLNamedIndividual(entityData.getName()), entityData.getBrowserText()));
        }

        else if(entityData.getValueType() == ValueType.Property) {
            return Optional.absent();
        }
        return Optional.absent();
    }
}

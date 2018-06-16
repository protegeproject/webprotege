package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Jun 2018
 */
public class EntityTypeCriteriaViewImpl extends Composite implements EntityTypeCriteriaView {

    private static final List<EntityType<?>> VALUES = ImmutableList.copyOf(EntityType.values());

    interface EntityTypeCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, EntityTypeCriteriaViewImpl> {

    }

    private static EntityTypeCriteriaViewImplUiBinder ourUiBinder = GWT.create(EntityTypeCriteriaViewImplUiBinder.class);

    @UiField
    CheckBox annotationPropertyCheckBox;

    @UiField
    CheckBox dataPropertyCheckBox;

    @UiField
    CheckBox objectPropertyCheckBox;

    @UiField
    CheckBox datatypeCheckBox;

    @UiField
    CheckBox individualCheckBox;

    @UiField
    CheckBox classCheckBox;

    @Inject
    public EntityTypeCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public Set<EntityType<?>> getEntityTypes() {
        Set<EntityType<?>> types = new HashSet<>();
        if(classCheckBox.getValue()) {
            types.add(EntityType.CLASS);
        }
        if(individualCheckBox.getValue()) {
            types.add(EntityType.NAMED_INDIVIDUAL);
        }
        if(datatypeCheckBox.getValue()) {
            types.add(EntityType.DATATYPE);
        }
        if(objectPropertyCheckBox.getValue()) {
            types.add(EntityType.OBJECT_PROPERTY);
        }
        if(dataPropertyCheckBox.getValue()) {
            types.add(EntityType.DATA_PROPERTY);
        }
        if(annotationPropertyCheckBox.getValue()) {
            types.add(EntityType.ANNOTATION_PROPERTY);
        }
        return types;
    }

    @Override
    public void setEntityTypes(@Nonnull Set<EntityType<?>> entityTypes) {
        classCheckBox.setValue(entityTypes.contains(EntityType.CLASS));
        individualCheckBox.setValue(entityTypes.contains(EntityType.NAMED_INDIVIDUAL));
        datatypeCheckBox.setValue(entityTypes.contains(EntityType.DATATYPE));
        objectPropertyCheckBox.setValue(entityTypes.contains(EntityType.OBJECT_PROPERTY));
        dataPropertyCheckBox.setValue(entityTypes.contains(EntityType.DATA_PROPERTY));
        annotationPropertyCheckBox.setValue(entityTypes.contains(EntityType.ANNOTATION_PROPERTY));
    }
}
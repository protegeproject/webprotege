package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

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
    ListBox typeListBox;

    @Inject
    public EntityTypeCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        for(EntityType<?> entityType : VALUES) {
            typeListBox.addItem(entityType.getPrintName());
        }
        typeListBox.setSelectedIndex(0);
    }

    @Nonnull
    @Override
    public EntityType<?> getEntityType() {
        int selIndex = typeListBox.getSelectedIndex();
        return VALUES.get(selIndex);
    }

    @Override
    public void setEntityType(@Nonnull EntityType<?> entityType) {
        typeListBox.setSelectedIndex(VALUES.indexOf(entityType));
    }
}
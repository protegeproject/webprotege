package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public class EntityRelationshipCriteriaViewImpl extends Composite implements EntityRelationshipCriteriaView {

    private Runnable valueMatchTypeChangedHandler = () -> {};

    interface EntityRelationshipCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, EntityRelationshipCriteriaViewImpl> {

    }

    private static EntityRelationshipCriteriaViewImplUiBinder ourUiBinder = GWT.create(
            EntityRelationshipCriteriaViewImplUiBinder.class);

    @UiField
    ListBox valueMatchTypeSelector;

    @UiField
    SimplePanel valueMatchCriteriaContainer;

    @UiField(provided = true)
    PrimitiveDataEditor propertyField;

    @Inject
    public EntityRelationshipCriteriaViewImpl(@Nonnull PrimitiveDataEditor propertyField) {
        this.propertyField = propertyField;
        initWidget(ourUiBinder.createAndBindUi(this));
        valueMatchTypeSelector.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                valueMatchTypeChangedHandler.run();
            }
        });
    }

    @Override
    public void setValueMatchTypeChangedHandler(@Nonnull Runnable runnable) {
        this.valueMatchTypeChangedHandler = checkNotNull(runnable);
    }

    @Override
    public void setValueMatchType(@Nonnull RelationshipValueMatchType matchType) {
        valueMatchTypeSelector.setSelectedIndex(matchType.ordinal());
    }

    @Override
    public RelationshipValueMatchType getValueMatchType() {
        return RelationshipValueMatchType.values()[valueMatchTypeSelector.getSelectedIndex()];
    }

    @Override
    public void clearProperty() {
        propertyField.clearValue();
    }

    @Override
    public void setProperty(@Nonnull OWLPropertyData propertyData) {
        propertyField.setValue(propertyData);
    }

    @Nonnull
    @Override
    public Optional<OWLPropertyData> getProperty() {
        return propertyField.getValue()
                .filter(propertyData -> propertyData instanceof OWLPropertyData)
                .map(propertyData -> (OWLPropertyData) propertyData);
    }

    @Override
    public void setPropertyChangeHandler(@Nonnull Runnable handler) {

    }

    @Nonnull
    @Override
    public AcceptsOneWidget getValueEntityCriteriaContainer() {
        return valueMatchCriteriaContainer;
    }
}

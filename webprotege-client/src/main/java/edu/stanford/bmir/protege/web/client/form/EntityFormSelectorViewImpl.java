package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.shared.form.FormPurpose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
public class EntityFormSelectorViewImpl extends Composite implements EntityFormSelectorView {

    interface EntityFormSelectorViewImplUiBinder extends UiBinder<HTMLPanel, EntityFormSelectorViewImpl> {

    }

    private static EntityFormSelectorViewImplUiBinder ourUiBinder = GWT.create(EntityFormSelectorViewImplUiBinder.class);

    @UiField
    SimplePanel container;
    @UiField
    RadioButton entityEditingPurposeRadio;
    @UiField
    RadioButton entityCreationPurposeRadio;
    @UiField
    Label criteriaLabel;
    @UiField
    FormsMessages msg;

    @Inject
    public EntityFormSelectorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        entityEditingPurposeRadio.addValueChangeHandler(event -> updateLabels());
        entityCreationPurposeRadio.addValueChangeHandler(event -> updateLabels());
    }

    @Override
    public void clear() {
        entityEditingPurposeRadio.setValue(true);
        updateLabels();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSelectorCriteriaContainer() {
        return container;
    }

    @Nonnull
    @Override
    public FormPurpose getPurpose() {
        if(entityEditingPurposeRadio.getValue()) {
            return FormPurpose.ENTITY_EDITING;
        }
        else {
            return FormPurpose.ENTITY_CREATION;
        }
    }

    @Override
    public void setPurpose(FormPurpose purpose) {
        if(purpose.equals(FormPurpose.ENTITY_EDITING)) {
            entityEditingPurposeRadio.setValue(true);
        }
        else {
            entityCreationPurposeRadio.setValue(true);
        }
        updateLabels();
    }



    private void updateLabels() {
        if(entityEditingPurposeRadio.getValue()) {
            criteriaLabel.setText(msg.entityEditing_entityCriteria());
        }
        else {
            criteriaLabel.setText(msg.entityCreation_parentEntityCriteria());
        }
    }
}

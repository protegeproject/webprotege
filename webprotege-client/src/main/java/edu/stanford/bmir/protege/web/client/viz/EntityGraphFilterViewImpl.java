package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public class EntityGraphFilterViewImpl extends Composite implements EntityGraphFilterView {

    interface EntityGraphFilterViewImplUiBinder extends UiBinder<HTMLPanel, EntityGraphFilterViewImpl> {

    }

    private static EntityGraphFilterViewImplUiBinder ourUiBinder = GWT.create(EntityGraphFilterViewImplUiBinder.class);

    @UiField
    SimplePanel includeCriteriaContainer;

    @UiField
    SimplePanel excludeCriteriaContainer;

    @UiField
    TextBox nameField;

    @UiField
    TextBox descriptionField;

    @UiField
    CheckBox activeCheckBox;

    @Inject
    public EntityGraphFilterViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public boolean isActive() {
        return activeCheckBox.getValue();
    }

    @Override
    public void setActive(boolean active) {
        activeCheckBox.setValue(active);
    }

    @Override
    public void setName(@Nonnull String name) {
        nameField.setText(name);
    }

    @Nonnull
    @Override
    public String getName() {
        return nameField.getText().trim();
    }

    @Override
    public void setDescription(@Nonnull String description) {
        descriptionField.setText(description);
    }

    @Nonnull
    @Override
    public String getDescription() {
        return descriptionField.getText().trim();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getInclusionCriteriaContainer() {
        return includeCriteriaContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getExclusionCriteriaContainer() {
        return excludeCriteriaContainer;
    }
}

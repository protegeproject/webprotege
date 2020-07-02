package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class EntityCrudKitPrefixSettingsViewImpl extends Composite implements EntityCrudKitPrefixSettingsView {

    interface EntityCrudKitPrefixSettingsViewImplUiBinder extends UiBinder<HTMLPanel, EntityCrudKitPrefixSettingsViewImpl> {

    }

    private static EntityCrudKitPrefixSettingsViewImplUiBinder ourUiBinder = GWT.create(
            EntityCrudKitPrefixSettingsViewImplUiBinder.class);

    @UiField
    TextBox fallbackPrefix;

    @UiField
    SimplePanel conditionalPrefixesViewContainer;

    @Inject
    public EntityCrudKitPrefixSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setFallbackPrefix(@Nonnull String prefix) {
        fallbackPrefix.setText(prefix);
    }

    @Nonnull
    @Override
    public String getFallbackPrefix() {
        return fallbackPrefix.getText().trim();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getCriteriaListContainer() {
        return conditionalPrefixesViewContainer;
    }
}

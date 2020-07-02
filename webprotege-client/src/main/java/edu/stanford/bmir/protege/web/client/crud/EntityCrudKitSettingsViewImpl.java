package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class EntityCrudKitSettingsViewImpl extends Composite implements EntityCrudKitSettingsView {

    interface EntityCrudKitSettingsViewImplUiBinder extends UiBinder<HTMLPanel, EntityCrudKitSettingsViewImpl> {

    }

    private static EntityCrudKitSettingsViewImplUiBinder ourUiBinder = GWT.create(EntityCrudKitSettingsViewImplUiBinder.class);

    @UiField
    SimplePanel prefixSettingsViewContainer;

    @UiField
    SimplePanel suffixSettingsViewContainer;

    @Inject
    public EntityCrudKitSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getPrefixSettingsViewContainer() {
        return prefixSettingsViewContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSuffixSettingsViewContainer() {
        return suffixSettingsViewContainer;
    }
}

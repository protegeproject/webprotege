package edu.stanford.bmir.protege.web.client.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2018
 */
public class SettingsSectionViewContainerImpl extends Composite implements SettingsSectionViewContainer {

    interface SettingsSectionViewContainerImplUiBinder extends UiBinder<HTMLPanel, SettingsSectionViewContainerImpl> {

    }

    private static SettingsSectionViewContainerImplUiBinder ourUiBinder = GWT.create(SettingsSectionViewContainerImplUiBinder.class);

    @UiField
    Label nameField;

    @UiField
    SimplePanel container;

    @Inject
    public SettingsSectionViewContainerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setSectionName(@Nonnull String sectionName) {
        nameField.setText(checkNotNull(sectionName));
    }

    @Override
    public void setWidget(IsWidget w) {
        container.setWidget(checkNotNull(w));
    }
}
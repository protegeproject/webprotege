package edu.stanford.bmir.protege.web.client.crud.supplied;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class SuppliedNameSuffixSettingsViewImpl extends Composite implements SuppliedNameSuffixSettingsView {

    interface SuppliedNameSuffixSettingsViewImplUiBinder extends UiBinder<HTMLPanel, SuppliedNameSuffixSettingsViewImpl> {

    }

    private static SuppliedNameSuffixSettingsViewImplUiBinder ourUiBinder = GWT.create(
            SuppliedNameSuffixSettingsViewImplUiBinder.class);

    @Inject
    public SuppliedNameSuffixSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}

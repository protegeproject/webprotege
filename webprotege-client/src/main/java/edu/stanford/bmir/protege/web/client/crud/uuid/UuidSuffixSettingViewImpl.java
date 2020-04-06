package edu.stanford.bmir.protege.web.client.crud.uuid;

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
public class UuidSuffixSettingViewImpl extends Composite implements UuidSuffixSettingsView {

    interface UuidSuffixSettingViewImplUiBinder extends UiBinder<HTMLPanel, UuidSuffixSettingViewImpl> {

    }

    private static UuidSuffixSettingViewImplUiBinder ourUiBinder = GWT.create(UuidSuffixSettingViewImplUiBinder.class);

    @Inject
    public UuidSuffixSettingViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}

package edu.stanford.bmir.protege.web.client.crud.uuid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidFormat;

import javax.annotation.Nonnull;
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

    @UiField
    RadioButton base62Radio;

    @UiField
    RadioButton standardRadio;

    @Inject
    public UuidSuffixSettingViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public UuidFormat getFormat() {
        if(base62Radio.getValue()) {
            return UuidFormat.BASE62;
        }
        else {
            return UuidFormat.STANDARD;
        }
    }

    @Override
    public void setFormat(@Nonnull UuidFormat format) {
        if(format == UuidFormat.BASE62) {
            base62Radio.setValue(true);
        }
        else {
            standardRadio.setValue(true);
        }
    }
}

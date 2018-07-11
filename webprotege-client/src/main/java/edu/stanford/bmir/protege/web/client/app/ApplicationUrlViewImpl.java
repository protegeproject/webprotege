package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2018
 */
public class ApplicationUrlViewImpl extends Composite implements ApplicationUrlView {

    interface ApplicationUrlViewImplUiBinder extends UiBinder<HTMLPanel, ApplicationUrlViewImpl> {

    }

    private static ApplicationUrlViewImplUiBinder ourUiBinder = GWT.create(ApplicationUrlViewImplUiBinder.class);

    @UiField
    ListBox applicationSchemeField;

    @UiField
    PlaceholderTextBox applicationHostField;

    @UiField
    PlaceholderTextBox applicationPathField;

    @UiField
    PlaceholderTextBox applicationPortField;

    @Inject
    public ApplicationUrlViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setScheme(@Nonnull SchemeValue scheme) {
        applicationSchemeField.setSelectedIndex(scheme.ordinal());
    }

    @Nonnull
    @Override
    public SchemeValue getScheme() {
        return SchemeValue.valueOf(applicationSchemeField.getSelectedValue());
    }

    @Override
    public void setHost(@Nonnull String host) {
        applicationHostField.setText(checkNotNull(host));
        applicationHostField.doValidation();
    }

    @Nonnull
    @Override
    public String getHost() {
        return applicationHostField.getText().trim();
    }

    @Override
    public void setPath(@Nonnull String path) {
        applicationPathField.setText(path);
        applicationPathField.doValidation();
    }

    @Nonnull
    @Override
    public String getPath() {
        return applicationPathField.getText().trim();
    }

    @Override
    public void setPort(@Nonnull String port) {
        applicationPortField.setText(port);
        applicationPortField.doValidation();
    }

    @Nonnull
    @Override
    public String getPort() throws NumberFormatException {
        return applicationPortField.getText().trim();
    }

}

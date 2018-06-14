package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class SimpleStringCriteriaViewImpl extends Composite implements SimpleStringCriteriaView {

    interface SimpleStringCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, SimpleStringCriteriaViewImpl> {

    }

    private static SimpleStringCriteriaViewImplUiBinder ourUiBinder = GWT.create(SimpleStringCriteriaViewImplUiBinder.class);

    @UiField
    TextBox valueTextBox;

    @UiField
    CheckBox ignoreCaseCheckBox;


    @Inject
    public SimpleStringCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public String getValue() {
        return valueTextBox.getValue();
    }

    @Override
    public void setValue(@Nonnull String value) {
        valueTextBox.setValue(checkNotNull(value));
    }

    @Override
    public boolean isIgnoreCase() {
        return ignoreCaseCheckBox.getValue();
    }

    @Override
    public void setIgnoreCase(boolean ignoreCase) {
        ignoreCaseCheckBox.setValue(ignoreCase);
    }
}
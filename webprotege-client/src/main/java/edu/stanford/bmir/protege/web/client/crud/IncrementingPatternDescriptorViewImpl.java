package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class IncrementingPatternDescriptorViewImpl extends Composite implements IncrementingPatternDescriptorView {

    interface IncrementingPatternDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, IncrementingPatternDescriptorViewImpl> {

    }

    private static IncrementingPatternDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            IncrementingPatternDescriptorViewImplUiBinder.class);

    @UiField
    TextBox startingValueField;

    @UiField
    TextBox formatField;

    @Inject
    public IncrementingPatternDescriptorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setStartingValue(int startingValue) {
        startingValueField.setValue(Integer.toString(startingValue));
    }

    @Override
    public void setFormat(@Nonnull String format) {
        formatField.setValue(format);
    }

    @Override
    public void clear() {
        startingValueField.setValue("0");
    }

    @Override
    public int getStartingValue() {
        try {
            return Integer.parseInt(startingValueField.getValue());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String getFormat() {
        return formatField.getValue().trim();
    }
}
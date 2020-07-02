package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class NumberControlDescriptorViewImpl extends Composite implements NumberControlDescriptorView {

    private static final int DEFAULT_LENGTH = 10;

    interface NumberControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, NumberControlDescriptorViewImpl> {

    }

    private static NumberControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            NumberControlDescriptorViewImplUiBinder.class);


    @UiField
    SimplePanel rangeViewContainer;

    @UiField
    TextBox numberFormatField;

    @UiField
    TextBox numberLengthField;

    @UiField(provided = true)
    LanguageMapEditor placeholderEditor;

    private int lastLength = DEFAULT_LENGTH;

    @Inject
    public NumberControlDescriptorViewImpl(@Nonnull LanguageMapEditor placeholderEditor) {
        this.placeholderEditor = checkNotNull(placeholderEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setFormat(@Nonnull String format) {
        numberFormatField.setText(format);
    }

    @Override
    public String getFormat() {
        return numberFormatField.getText().trim();
    }

    @Override
    public void setPlaceholder(LanguageMap placeholder) {
        placeholderEditor.setValue(placeholder);
    }

    @Nonnull
    @Override
    public LanguageMap getPlaceholder() {
        return placeholderEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setLength(int length) {
        numberLengthField.setText(Integer.toString(length));
        lastLength = length;
    }

    @Override
    public int getLength() {
        try {
            return Integer.parseInt(numberLengthField.getText());
        } catch(NumberFormatException e) {
            return lastLength;
        }
    }

    @Nonnull
    public AcceptsOneWidget getRangeViewContainer() {
        return rangeViewContainer;
    }
}

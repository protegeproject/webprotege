package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.Counter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public class ChoiceListSourceDescriptorViewImpl extends Composite implements ChoiceListSourceDescriptorView {

    private SourceTypeChangedHandler sourceTypeChangedHandler = () -> {};

    interface ChoiceListSourceDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, ChoiceListSourceDescriptorViewImpl> {

    }

    private static ChoiceListSourceDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            ChoiceListSourceDescriptorViewImplUiBinder.class);

    @UiField
    RadioButton fixedRadioButton;

    @UiField(provided = true)
    static Counter counter = new Counter();

    @UiField
    RadioButton dynamicRadioButton;

    @UiField
    SimplePanel descriptorContainer;

    @Inject
    public ChoiceListSourceDescriptorViewImpl() {
        counter.increment();
        initWidget(ourUiBinder.createAndBindUi(this));
        fixedRadioButton.addValueChangeHandler(event -> sourceTypeChangedHandler.handleSourceTypeChanged());
        dynamicRadioButton.addValueChangeHandler(event -> sourceTypeChangedHandler.handleSourceTypeChanged());
    }

    @Override
    public void setFixedList(boolean fixedList) {
        fixedRadioButton.setValue(fixedList);
        dynamicRadioButton.setValue(!fixedList);
    }

    @Override
    public boolean isFixedList() {
        return fixedRadioButton.getValue();
    }

    @Override
    public void setSourceTypeChangedHandler(@Nonnull SourceTypeChangedHandler handler) {
        this.sourceTypeChangedHandler = checkNotNull(handler);
    }

    @Nonnull
    public AcceptsOneWidget getDescriptorContainer() {
        return descriptorContainer;
    }
}

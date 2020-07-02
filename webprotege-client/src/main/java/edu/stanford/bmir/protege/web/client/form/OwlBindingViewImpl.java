package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class OwlBindingViewImpl extends Composite implements OwlBindingView {

    interface OwlBindingViewImplUiBinder extends UiBinder<HTMLPanel, OwlBindingViewImpl> {

    }

    private static OwlBindingViewImplUiBinder ourUiBinder = GWT.create(OwlBindingViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditor propertyField;

    @UiField(provided = true)
    static Counter counter = new Counter();

    @UiField
    RadioButton classRadioButton;

    @UiField
    RadioButton instanceRadioButton;

    @UiField
    RadioButton subClassRadioButton;

    @UiField
    RadioButton propertyRadioButton;

    @UiField
    SimplePanel valuesFilterViewContainer;

    @Inject
    public OwlBindingViewImpl(PrimitiveDataEditor propertyField) {
        counter.increment();
        this.propertyField = checkNotNull(propertyField);
        initWidget(ourUiBinder.createAndBindUi(this));
        propertyRadioButton.addValueChangeHandler(event -> {
           propertyField.setEnabled(event.getValue());
        });
    }

    @Override
    public void clear() {
        propertyRadioButton.setValue(true);
        propertyField.setEnabled(true);
        propertyField.clearValue();

    }

    @Override
    public void setOwlClassBinding(boolean classBinding) {
        classRadioButton.setValue(classBinding);
    }

    @Override
    public boolean isOwlClassBinding() {
        return classRadioButton.getValue();
    }

    @Override
    public void setOwlInstanceBinding(boolean instanceBinding) {
        instanceRadioButton.setValue(instanceBinding);
    }

    @Override
    public boolean isOwlInstanceBinding() {
        return instanceRadioButton.getValue();
    }

    @Override
    public void setOwlSubClassBinding(boolean subClassBinding) {
        subClassRadioButton.setValue(subClassBinding);
    }

    @Override
    public boolean isOwlSubClassBinding() {
        return subClassRadioButton.getValue();
    }

    @Override
    public void setProperty(@Nonnull OWLEntityData entity) {
        propertyField.setValue(entity);
        propertyField.setEnabled(true);
        propertyRadioButton.setValue(true);
    }

    @Override
    public Optional<OWLEntityData> getEntity() {
        return propertyField.getValue().map(pd -> (OWLEntityData) pd);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getValuesFilterViewContainer() {
        return valuesFilterViewContainer;
    }
}

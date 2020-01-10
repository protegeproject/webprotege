package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;

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
    PrimitiveDataEditor primitiveDataField;

    @UiField(provided = true)
    static Counter counter = new Counter();

    @UiField
    RadioButton classRadioButton;

    @UiField
    RadioButton propertyRadioButton;

    @Inject
    public OwlBindingViewImpl(PrimitiveDataEditor primitiveDataEditor) {
        counter.increment();
        this.primitiveDataField = checkNotNull(primitiveDataEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
        propertyRadioButton.addValueChangeHandler(event -> {
           primitiveDataEditor.setEnabled(event.getValue());
        });
    }

    @Override
    public void clear() {
        propertyRadioButton.setValue(true);
        primitiveDataField.setEnabled(true);
        primitiveDataField.clearValue();

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
    public void setProperty(@Nonnull OWLEntityData entity) {
        primitiveDataField.setValue(entity);
        primitiveDataField.setEnabled(true);
        propertyRadioButton.setValue(true);
    }

    @Override
    public Optional<OWLEntityData> getEntity() {
        return primitiveDataField.getValue().map(pd -> (OWLEntityData) pd);
    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class ChoiceDescriptorViewImpl extends Composite implements ChoiceDescriptorView, HasRequestFocus {

    private ChoiceValueChangedHandler choiceValueChangedHandler = () -> {};

    interface ChoiceDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, ChoiceDescriptorViewImpl> {

    }

    private static ChoiceDescriptorViewImplUiBinder ourUiBinder = GWT.create(ChoiceDescriptorViewImplUiBinder.class);

    @UiField(provided = true)
    LanguageMapEditor labelEditor;

    @UiField(provided = true)
    PrimitiveDataEditor primitiveDataEditor;

    @Inject
    public ChoiceDescriptorViewImpl(LanguageMapEditor labelEditor,
                                    PrimitiveDataEditor primitiveDataEditor) {
        this.labelEditor = checkNotNull(labelEditor);
        this.primitiveDataEditor = checkNotNull(primitiveDataEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
        primitiveDataEditor.addValueChangeHandler(event -> choiceValueChangedHandler.handleChoiceValueChanged());
    }

    @Override
    public void setChoiceValueChangedHandler(@Nonnull ChoiceValueChangedHandler handler) {
        this.choiceValueChangedHandler = checkNotNull(handler);
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {
        labelEditor.setValue(label);
    }

    @Nonnull
    @Override
    public LanguageMap getLabel() {
        return labelEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void clear() {
        labelEditor.clearValue();
        primitiveDataEditor.clearValue();
    }

    @Override
    public void setPrimitiveData(@Nonnull OWLPrimitiveData dataValue) {
        primitiveDataEditor.setValue(dataValue);
    }

    @Override
    public Optional<OWLPrimitiveData> getDataValue() {
        return primitiveDataEditor.getValue();
    }

    @Override
    public void requestFocus() {
        primitiveDataEditor.requestFocus();
    }
}

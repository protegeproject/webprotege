package edu.stanford.bmir.protege.web.client.shortform;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;
import edu.stanford.bmir.protege.web.client.primitive.LanguageEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class AnnotationAssertionDictionaryLanguageViewImpl extends Composite implements AnnotationAssertionDictionaryLanguageView {

    interface AnnotationAssertionDictionaryLanguageViewImplUiBinder extends UiBinder<HTMLPanel, AnnotationAssertionDictionaryLanguageViewImpl> {
    }

    private static AnnotationAssertionDictionaryLanguageViewImplUiBinder ourUiBinder = GWT.create(
            AnnotationAssertionDictionaryLanguageViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl annotationPropertyEditor;

    @UiField(provided = true)
    DefaultLanguageEditor languageTagEditor;

    @Inject
    public AnnotationAssertionDictionaryLanguageViewImpl(PrimitiveDataEditor annotationPropertyEditor,
                                                         LanguageEditor languageEditor) {
        this.annotationPropertyEditor = (PrimitiveDataEditorImpl) annotationPropertyEditor;
        this.languageTagEditor = (DefaultLanguageEditor) languageEditor;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getAnnotationProperty() {
        return annotationPropertyEditor.getValueAsAnnotationPropertyData();
    }

    @Override
    public void setAnnotationProperty(@Nonnull OWLAnnotationPropertyData annotationProperty) {
        annotationPropertyEditor.setValue(annotationProperty);
    }

    @Nonnull
    @Override
    public String getLanguageTag() {
        return languageTagEditor.getValue().orElse("");
    }

    @Override
    public void setLanguageTag(@Nonnull String languageTag) {
        languageTagEditor.setValue(languageTag);
    }

    @Override
    public void clearAnnotationProperty() {
        annotationPropertyEditor.clearValue();
    }

    @Override
    public void requestFocus() {
        annotationPropertyEditor.requestFocus();
    }
}
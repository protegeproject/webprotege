package edu.stanford.bmir.protege.web.client.shortform;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class AnnotationAssertionPathDictionaryLanguageViewImpl extends Composite implements AnnotationAssertionPathDictionaryLanguageView {

    interface AnnotationAssertionPathDictionaryLanguageViewImplUiBinder extends UiBinder<HTMLPanel, AnnotationAssertionPathDictionaryLanguageViewImpl> {
    }

    @UiField(provided = true)
    protected ValueListEditor<OWLPrimitiveData> pathEditor;

    @UiField(provided = true)
    protected DefaultLanguageEditor languageEditor;

    private static AnnotationAssertionPathDictionaryLanguageViewImplUiBinder ourUiBinder = GWT.create(
            AnnotationAssertionPathDictionaryLanguageViewImplUiBinder.class);

    @Inject
    public AnnotationAssertionPathDictionaryLanguageViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                                                             DefaultLanguageEditor languageEditor) {
        this.pathEditor = new ValueListFlexEditorImpl<>(() -> {
            PrimitiveDataEditor editor =  primitiveDataEditorProvider.get();
            editor.setAnnotationPropertiesAllowed(true);
            return editor;
        });
        this.pathEditor.setEnabled(true);
        this.languageEditor = languageEditor;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public ImmutableList<OWLAnnotationPropertyData> getPath() {
        return pathEditor.getValue()
                .orElse(ImmutableList.of())
                .stream()
                .filter(p -> p instanceof OWLAnnotationPropertyData)
                .map(p -> (OWLAnnotationPropertyData) p)
                .collect(toImmutableList());
    }

    @Nonnull
    @Override
    public String getLangTag() {
        return languageEditor.getValue().orElse("");
    }

    @Override
    public void setLangTag(@Nonnull String langTag) {
        languageEditor.setValue(langTag);
    }

    @Override
    public void setPath(@Nonnull ImmutableList<OWLAnnotationPropertyData> props) {
        pathEditor.setValue(ImmutableList.copyOf(props));
    }


}
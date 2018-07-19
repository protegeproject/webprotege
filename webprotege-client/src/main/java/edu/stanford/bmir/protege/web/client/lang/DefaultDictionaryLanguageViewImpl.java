package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class DefaultDictionaryLanguageViewImpl extends Composite implements DefaultDictionaryLanguageView {

    interface EntityDefaultLanguagesViewImplUiBinder extends UiBinder<HTMLPanel, DefaultDictionaryLanguageViewImpl> {

    }

    private static EntityDefaultLanguagesViewImplUiBinder ourUiBinder = GWT.create(EntityDefaultLanguagesViewImplUiBinder.class);


    @UiField(provided = true)
    PrimitiveDataEditorImpl propertyEditor;

    @UiField(provided = true)
    DefaultLanguageEditor languageTagEditor;

    @Inject
    public DefaultDictionaryLanguageViewImpl(@Nonnull PrimitiveDataEditorImpl propertyEditor,
                                             @Nonnull DefaultLanguageEditor languageTagEditor) {
        this.propertyEditor = checkNotNull(propertyEditor);
        this.languageTagEditor = checkNotNull(languageTagEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getAnnotationProperty() {
        return propertyEditor.getValue()
                             .filter(val -> val instanceof OWLAnnotationPropertyData)
                             .map(val -> ((OWLAnnotationPropertyData) val));
    }

    @Override
    public void setAnnotationProperty(@Nonnull OWLAnnotationPropertyData annotationProperty) {
        propertyEditor.setValue(annotationProperty);
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
        propertyEditor.clearValue();
    }
}
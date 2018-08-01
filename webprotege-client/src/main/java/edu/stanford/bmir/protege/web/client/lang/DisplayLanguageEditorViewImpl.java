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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
public class DisplayLanguageEditorViewImpl extends Composite implements DisplayLanguageEditorView {

    @Nonnull
    private ChangeHandler changeHandler = () -> {};

    interface DisplayLanguageEditorViewImplUiBinder extends UiBinder<HTMLPanel, DisplayLanguageEditorViewImpl> {

    }

    private static DisplayLanguageEditorViewImplUiBinder ourUiBinder = GWT.create(DisplayLanguageEditorViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl primaryPropertyField;

    @UiField(provided = true)
    DefaultLanguageEditor primaryLangField;

    @UiField(provided = true)
    PrimitiveDataEditorImpl secondaryPropertyField;

    @UiField(provided = true)
    DefaultLanguageEditor secondaryLangField;

    @Inject
    public DisplayLanguageEditorViewImpl(@Nonnull PrimitiveDataEditorImpl primaryPropertyField,
                                         @Nonnull DefaultLanguageEditor primaryLangField,
                                         @Nonnull PrimitiveDataEditorImpl secondaryPropertyField,
                                         @Nonnull DefaultLanguageEditor secondaryLangField) {
        this.primaryPropertyField = checkNotNull(primaryPropertyField);
        this.primaryLangField = checkNotNull(primaryLangField);
        this.secondaryPropertyField = checkNotNull(secondaryPropertyField);
        this.secondaryLangField = checkNotNull(secondaryLangField);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setPrimaryDisplayLanguage(@Nonnull OWLAnnotationPropertyData property, @Nonnull String lang) {
        primaryPropertyField.setValue(property);
        primaryLangField.setValue(lang);
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getPrimaryLanguageProperty() {
        return primaryPropertyField.getValue().map(prop -> (OWLAnnotationPropertyData) prop);
    }

    @Nonnull
    @Override
    public String getPrimaryLanguageTag() {
        return primaryLangField.getValue().orElse("");
    }

    @Override
    public void setSecondaryDisplayLanguage(@Nonnull OWLAnnotationPropertyData property, @Nonnull String lang) {
        secondaryPropertyField.setValue(property);
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getSecondaryLanguageProperty() {
        return secondaryPropertyField.getValue().map(prop -> (OWLAnnotationPropertyData) prop);
    }

    @Nonnull
    @Override
    public String getSecondaryLanguageTag() {
        return secondaryLangField.getValue().orElse("");
    }

    @Override
    public void setChangeHandler(@Nonnull ChangeHandler changeHandler) {
        this.changeHandler = checkNotNull(changeHandler);
    }
}
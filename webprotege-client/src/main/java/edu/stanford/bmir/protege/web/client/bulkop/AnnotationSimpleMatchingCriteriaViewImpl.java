package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public class AnnotationSimpleMatchingCriteriaViewImpl extends Composite implements AnnotationSimpleMatchingCriteriaView {

    interface AnnotationSimpleMatchingCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, AnnotationSimpleMatchingCriteriaViewImpl> {

    }

    private static AnnotationSimpleMatchingCriteriaViewImplUiBinder ourUiBinder = GWT.create(AnnotationSimpleMatchingCriteriaViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl propertyField;

    @UiField
    TextBox valueField;

    @UiField
    CheckBox regExCheckBox;

    @UiField(provided = true)
    DefaultLanguageEditor langField;

    @UiField
    RadioButton anyValueRadio;

    @UiField
    RadioButton specificValueRadio;

    @UiField
    RadioButton anyLangTagRadio;

    @UiField
    RadioButton specificLangTagRadio;

    @UiField
    HTMLPanel valueGroup;

    @UiField
    HTMLPanel langTagGroup;

    @UiField
    RadioButton specificPropertyRadio;

    @UiField
    RadioButton anyPropertyRadio;

    @UiField
    HTMLPanel propertyGroup;

    @Inject
    public AnnotationSimpleMatchingCriteriaViewImpl(@Nonnull PrimitiveDataEditorImpl annotationPropertyField,
                                                    @Nonnull DefaultLanguageEditor langField) {
        this.propertyField = annotationPropertyField;
        this.langField = langField;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void requestFocus() {
        propertyField.requestFocus();
    }

    @UiHandler("anyPropertyRadio")
    protected void handlePropertyMatchTypeChangedAny(ValueChangeEvent<Boolean> event) {
        propertyGroup.setVisible(!event.getValue());
    }

    @UiHandler("specificPropertyRadio")
    protected void handlePropertyMatchTypeChangedSpecific(ValueChangeEvent<Boolean> event) {
        propertyGroup.setVisible(event.getValue());
    }

    @UiHandler("anyValueRadio")
    protected void handleValueMatchTypeChangedAny(ValueChangeEvent<Boolean> event) {
        valueGroup.setVisible(!event.getValue());
    }

    @UiHandler("specificValueRadio")
    protected void handleValueMatchTypeChangedSpecific(ValueChangeEvent<Boolean> event) {
        valueGroup.setVisible(event.getValue());
    }

    @UiHandler("specificLangTagRadio")
    protected void handleLangTagMatchTypeChangedSpecific(ValueChangeEvent<Boolean> event) {
        langTagGroup.setVisible(event.getValue());
    }


    @UiHandler("anyLangTagRadio")
    protected void handleLangTagMatchTypeChangedAny(ValueChangeEvent<Boolean> event) {
        langTagGroup.setVisible(!event.getValue());
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getProperty() {
        return propertyField.getValue()
                .filter(prop -> prop instanceof OWLAnnotationPropertyData)
                .map(prop -> (OWLAnnotationPropertyData) prop);
    }

    @Override
    public boolean isMatchLexicalValue() {
        return specificValueRadio.getValue();
    }

    @Nonnull
    @Override
    public String getLexicalValueExpression() {
        return valueField.getValue();
    }

    @Override
    public boolean isLexicalValueRegEx() {
        return regExCheckBox.getValue();
    }

    @Override
    public boolean isMatchLangTag() {
        return specificLangTagRadio.getValue();
    }

    @Nonnull
    @Override
    public Optional<String> getLangTag() {
        if (specificLangTagRadio.getValue()) {
            return Optional.of(langField.getValue().orElse(""));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public boolean isMatchProperty() {
        return specificPropertyRadio.getValue();
    }
}
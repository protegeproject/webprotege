package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.bulkop.Operation;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static javaemul.internal.InternalPreconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class EditAnnotationsViewImpl extends Composite implements EditAnnotationsView {

    interface ReplaceAnnotationsValueViewImplUiBinder extends UiBinder<HTMLPanel, EditAnnotationsViewImpl> {

    }

    private static ReplaceAnnotationsValueViewImplUiBinder ourUiBinder = GWT.create(ReplaceAnnotationsValueViewImplUiBinder.class);

    @UiField(provided = true)
    protected AnnotationSimpleMatchingCriteriaViewImpl criteriaView;


    @UiField
    ListBox operationCombo;

    @UiField
    HTMLPanel newAnnotationsSection;

    @UiField(provided = true)
    DefaultLanguageEditor newLangField;

    @UiField
    TextBox newValue;

    @UiField(provided = true)
    PrimitiveDataEditorImpl newPropertyField;

    @UiField
    HTMLPanel propertyGroup;

    @UiField
    HTMLPanel valueGroup;

    @UiField
    RadioButton matchedPropertyRadio;

    @UiField
    RadioButton otherPropertyRadio;

    @UiField
    RadioButton matchedValueRadio;

    @UiField
    RadioButton otherValueRadio;

    @UiField
    RadioButton matchedLangTagRadio;

    @UiField
    RadioButton otherLangTagRadio;

    @UiField
    HTMLPanel langTagGroup;

    @Inject
    public EditAnnotationsViewImpl(AnnotationSimpleMatchingCriteriaViewImpl view,
                                   PrimitiveDataEditorImpl newPropertyField,
                                   DefaultLanguageEditor newLangField) {
        this.criteriaView = Preconditions.checkNotNull(view);
        this.newPropertyField = newPropertyField;
        this.newLangField = newLangField;
        initWidget(ourUiBinder.createAndBindUi(this));
        updateOperationSection();
    }



    @UiHandler("matchedPropertyRadio")
    protected void handleMatchedPropertyRadioChanged(ValueChangeEvent<Boolean> event) {
        propertyGroup.setVisible(!event.getValue());
    }

    @UiHandler("matchedValueRadio")
    protected void handleMatchedValueRadioChanged(ValueChangeEvent<Boolean> event) {
        valueGroup.setVisible(!event.getValue());
    }

    @UiHandler("matchedLangTagRadio")
    protected void handleMatchedLangTagRadioChanged(ValueChangeEvent<Boolean> event) {
        langTagGroup.setVisible(!event.getValue());
    }

    @UiHandler("otherPropertyRadio")
    protected void handlePropertyRadioChanged(ValueChangeEvent<Boolean> event) {
        propertyGroup.setVisible(event.getValue());
    }

    @UiHandler("otherValueRadio")
    protected void handleValueRadioChanged(ValueChangeEvent<Boolean> event) {
        valueGroup.setVisible(event.getValue());
    }

    @UiHandler("otherLangTagRadio")
    protected void handleLangTagRadioChanged(ValueChangeEvent<Boolean> event) {
        langTagGroup.setVisible(event.getValue());
    }


    @Nonnull
    @Override
    public Operation getOperation() {
        return Operation.values()[operationCombo.getSelectedIndex()];
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getAnnotationProperty() {
        return criteriaView.getProperty();
    }

    @Nonnull
    @Override
    public String getMatch() {
        return criteriaView.getValue();
    }

    @Override
    public boolean isRegEx() {
        return criteriaView.isValueRegularExpression();
    }

    @Nonnull
    @Override
    public ImmutableSet<PropertyAnnotationValue> getReplacement() {
        return ImmutableSet.of();
    }

    @UiHandler("operationCombo")
    public void operationComboChange(ChangeEvent event) {
        updateOperationSection();
    }

    private void updateOperationSection() {
        newAnnotationsSection.setVisible(getOperation() != Operation.DELETE);
    }
}
package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static javaemul.internal.InternalPreconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class ReplaceAnnotationsValueViewImpl extends Composite implements ReplaceAnnotationValuesView {

    interface ReplaceAnnotationsValueViewImplUiBinder extends UiBinder<HTMLPanel, ReplaceAnnotationsValueViewImpl> {

    }

    private static ReplaceAnnotationsValueViewImplUiBinder ourUiBinder = GWT.create(ReplaceAnnotationsValueViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl propertyField;

    @UiField
    TextBox replaceField;

    @UiField
    TextBox matchExpressionField;

    @UiField
    CheckBox regExCheckBox;

    @Inject
    public ReplaceAnnotationsValueViewImpl(@Nonnull PrimitiveDataEditorImpl annotationPropertyField) {
        this.propertyField = checkNotNull(annotationPropertyField);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getAnnotationProperty() {
        return propertyField.getValue()
                .filter(prop -> prop instanceof OWLAnnotationPropertyData)
                .map(prop -> (OWLAnnotationPropertyData) prop);
    }

    @Nonnull
    @Override
    public String getMatch() {
        return matchExpressionField.getValue().trim();
    }

    @Override
    public boolean isRegEx() {
        return regExCheckBox.getValue();
    }

    @Nonnull
    @Override
    public String getReplacement() {
        return replaceField.getValue().trim();
    }
}
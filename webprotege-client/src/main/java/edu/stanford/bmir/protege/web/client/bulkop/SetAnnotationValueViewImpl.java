package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;
import edu.stanford.bmir.protege.web.client.primitive.LanguageEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class SetAnnotationValueViewImpl extends Composite implements SetAnnotationValueView {

    interface SetAnnotationValueViewImplUiBinder extends UiBinder<HTMLPanel, SetAnnotationValueViewImpl> {

    }

    private static SetAnnotationValueViewImplUiBinder ourUiBinder = GWT.create(SetAnnotationValueViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl propertyField;

    @UiField(provided = true)
    PrimitiveDataEditorImpl valueField;

    @UiField(provided = true)
    DefaultLanguageEditor langEditor;

    @Inject
    public SetAnnotationValueViewImpl(@Nonnull PrimitiveDataEditorImpl propertyField,
                                      @Nonnull PrimitiveDataEditorImpl valueField) {
        this.propertyField = checkNotNull(propertyField);
        this.valueField = checkNotNull(valueField);
        this.langEditor = (DefaultLanguageEditor) valueField.getLanguageEditor();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        requestFocus();
    }

    @Override
    public void requestFocus() {
        propertyField.requestFocus();
    }

    @Nonnull
    @Override
    public Optional<OWLAnnotationPropertyData> getProperty() {
        return propertyField.getValue()
                .filter(v -> v instanceof OWLAnnotationPropertyData)
                .map(v -> (OWLAnnotationPropertyData) v);
    }

    @Nonnull
    @Override
    public Optional<OWLPrimitiveData> getValue() {
        return valueField.getValue();
    }
}
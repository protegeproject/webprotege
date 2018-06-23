package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class AnnotationPropertyCriteriaViewImpl extends Composite implements AnnotationPropertyCriteriaView {

    interface AnnotationPropertyCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, AnnotationPropertyCriteriaViewImpl> {

    }

    private static AnnotationPropertyCriteriaViewImplUiBinder ourUiBinder = GWT.create(AnnotationPropertyCriteriaViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl propertyField;

    @Inject
    public AnnotationPropertyCriteriaViewImpl(@Nonnull PrimitiveDataEditorImpl primitiveDataEditor) {
        this.propertyField = checkNotNull(primitiveDataEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setProperty(@Nonnull OWLAnnotationPropertyData property) {
        propertyField.setValue(property);
    }

    @Override
    public Optional<OWLAnnotationProperty> getProperty() {
        return propertyField.getValue().map(prop -> ((OWLAnnotationPropertyData) prop).getEntity());
    }

    @Override
    public void clear() {
        propertyField.clearValue();
    }
}
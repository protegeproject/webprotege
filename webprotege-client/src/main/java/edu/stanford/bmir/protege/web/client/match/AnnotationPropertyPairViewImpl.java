package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class AnnotationPropertyPairViewImpl extends Composite implements AnnotationPropertyPairView {

    private static AnnotationPropertyPairViewImplUiBinder ourUiBinder = GWT.create(AnnotationPropertyPairViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl firstEditor;

    @UiField(provided = true)
    PrimitiveDataEditorImpl secondEditor;

    @Inject
    public AnnotationPropertyPairViewImpl(@Nonnull PrimitiveDataEditorImpl firstEditor,
                                          @Nonnull PrimitiveDataEditorImpl secondEditor) {
        this.firstEditor = checkNotNull(firstEditor);
        this.secondEditor = checkNotNull(secondEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public Optional<OWLAnnotationProperty> getFirstProperty() {
        return firstEditor.getValue()
                          .filter(ed -> ed.getObject() instanceof OWLAnnotationProperty)
                          .map(ed -> (OWLAnnotationProperty) ed.getObject());
    }

    @Override
    public Optional<OWLAnnotationProperty> getSecondProperty() {
        return secondEditor.getValue()
                           .filter(ed -> ed.getObject() instanceof OWLAnnotationProperty)
                           .map(ed -> (OWLAnnotationProperty) ed.getObject());
    }

    interface AnnotationPropertyPairViewImplUiBinder extends UiBinder<HTMLPanel, AnnotationPropertyPairViewImpl> {

    }
}
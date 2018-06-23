package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class ClassSelectorViewImpl extends Composite implements ClassSelectorView {

    private static ClassSelectorViewImplUiBinder ourUiBinder = GWT.create(ClassSelectorViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl editorField;

    @Inject
    public ClassSelectorViewImpl(@Nonnull PrimitiveDataEditorImpl editorField) {
        this.editorField = checkNotNull(editorField);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public Optional<OWLClass> getOwlClass() {
        return editorField.getValue()
                          .filter(pd -> pd instanceof OWLClassData)
                          .map(pd -> ((OWLClassData) pd).getEntity());
    }

    @Override
    public void setOwlClass(@Nonnull OWLClassData owlClassData) {
        editorField.setValue(owlClassData);
    }

    interface ClassSelectorViewImplUiBinder extends UiBinder<HTMLPanel, ClassSelectorViewImpl> {

    }
}
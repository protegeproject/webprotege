package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class RelationshipEdgePropertyEqualsCriteriaViewImpl extends Composite implements RelationshipEdgePropertyEqualsCriteriaView {

    interface RelationshipEdgePropertyEqualsCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, RelationshipEdgePropertyEqualsCriteriaViewImpl> {

    }

    private static RelationshipEdgePropertyEqualsCriteriaViewImplUiBinder ourUiBinder = GWT.create(
            RelationshipEdgePropertyEqualsCriteriaViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditor propertyEditor;

    @Inject
    public RelationshipEdgePropertyEqualsCriteriaViewImpl(@Nonnull PrimitiveDataEditor editor) {
        this.propertyEditor = checkNotNull(editor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setProperty(@Nonnull OWLPropertyData propertyData) {
        propertyEditor.setValue(propertyData);
    }

    @Nonnull
    @Override
    public Optional<OWLPropertyData> getProperty() {
        return propertyEditor.getValue()
                .filter(value -> value instanceof OWLPropertyData)
                .map(value -> (OWLPropertyData) value);
    }
}

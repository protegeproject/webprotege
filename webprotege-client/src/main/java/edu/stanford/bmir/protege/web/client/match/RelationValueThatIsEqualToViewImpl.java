package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public class RelationValueThatIsEqualToViewImpl extends Composite implements RelationValueThatIsEqualToView {

    interface RelationValueThatIsEqualToViewImplUiBinder extends UiBinder<HTMLPanel, RelationValueThatIsEqualToViewImpl> {

    }

    private static RelationValueThatIsEqualToViewImplUiBinder ourUiBinder = GWT.create(
            RelationValueThatIsEqualToViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditor valueField;

    @Inject
    public RelationValueThatIsEqualToViewImpl(@Nonnull PrimitiveDataEditor valueField) {
        this.valueField = valueField;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setValue(@Nonnull OWLPrimitiveData value) {
        valueField.setValue(value);
    }

    @Nonnull
    @Override
    public Optional<OWLPrimitiveData> getValue() {
        return valueField.getValue();
    }
}

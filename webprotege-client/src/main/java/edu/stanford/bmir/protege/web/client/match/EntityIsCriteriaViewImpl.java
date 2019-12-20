package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-09
 */
public class EntityIsCriteriaViewImpl extends Composite implements EntityIsCriteriaView {

    interface EntityIsCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, EntityIsCriteriaViewImpl> {

    }

    private static EntityIsCriteriaViewImplUiBinder ourUiBinder = GWT.create(EntityIsCriteriaViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditor entityField;

    @Inject
    public EntityIsCriteriaViewImpl(@Nonnull PrimitiveDataEditor editor) {
        this.entityField = editor;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setEntity(@Nonnull OWLEntityData entity) {
        entityField.setValue(entity);
    }

    @Nonnull
    @Override
    public Optional<OWLEntityData> getEntityData() {
        return entityField.getValue()
                .filter(OWLPrimitiveData::isOWLEntity)
                .map(data -> (OWLEntityData) data);
    }
}

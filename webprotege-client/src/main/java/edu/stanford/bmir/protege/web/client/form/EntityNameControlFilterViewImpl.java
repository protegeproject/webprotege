package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityNameControlFilterViewImpl extends Composite implements EntityNameControlFilterView {

    interface EntityNameControlFilterViewImplUiBinder extends UiBinder<HTMLPanel, EntityNameControlFilterViewImpl> {
    }

    private static EntityNameControlFilterViewImplUiBinder ourUiBinder = GWT.create(
            EntityNameControlFilterViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditor entityEditor;

    @Inject
    public EntityNameControlFilterViewImpl(@Nonnull PrimitiveDataEditor entityEditor) {
        this.entityEditor = checkNotNull(entityEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setEntityMatchCriteria(@Nonnull CompositeRootCriteria criteria) {
        entityEditor.setCriteria(criteria);
    }

    @Override
    public Optional<OWLEntity> getEntity() {
        return entityEditor.getValue().filter(OWLPrimitiveData::isOWLEntity)
                           .flatMap(OWLPrimitiveData::asEntity);
    }

    @Override
    public void setEntity(OWLEntityData entity) {
        entityEditor.setValue(entity);
    }

    @Override
    public void clear() {
        entityEditor.clearValue();
    }
}
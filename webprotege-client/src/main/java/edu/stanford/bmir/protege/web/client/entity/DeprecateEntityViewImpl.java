package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeprecateEntityViewImpl extends Composite implements DeprecateEntityView {


    interface DeprecateEntityViewImplUiBinder extends UiBinder<HTMLPanel, DeprecateEntityViewImpl> {

    }

    private static DeprecateEntityViewImplUiBinder ourUiBinder = GWT.create(DeprecateEntityViewImplUiBinder.class);

    @UiField
    SimplePanel entityFormContainer;

    @UiField(provided = true)
    PrimitiveDataEditor replacementEntityEditor;

    @UiField
    Label referencesCountField;

    @UiField
    HTMLPanel replaceWithSubView;

    @Inject
    public DeprecateEntityViewImpl(PrimitiveDataEditor replacementEntityEditor) {
        this.replacementEntityEditor = checkNotNull(replacementEntityEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void setParentEntityType(@Nonnull PrimitiveType entityType) {
        replacementEntityEditor.setAllowedTypes(Collections.singleton(entityType));
    }

    @Nonnull
    @Override
    public Optional<OWLEntity> getReplacementEntity() {
        return replacementEntityEditor.getValue()
                .filter(v -> v instanceof OWLEntityData)
                .map(v -> (OWLEntityData) v)
                .map(OWLEntityData::getEntity);
    }

    @Override
    public void setReferencesCount(long referencesCount) {
        if(referencesCount == 0) {
            replaceWithSubView.setVisible(false);
            referencesCountField.setText("This entity has no other usages");
        }
        else {
            replaceWithSubView.setVisible(true);
            referencesCountField.setText("Warning: This entity is used in " + referencesCount + " places");
        }
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormContainer() {
        return entityFormContainer;
    }

    @Override
    public void requestFocus() {
        replacementEntityEditor.requestFocus();
    }
}
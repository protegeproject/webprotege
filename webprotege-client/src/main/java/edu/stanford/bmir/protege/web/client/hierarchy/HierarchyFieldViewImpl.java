package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Sep 2018
 */
public class HierarchyFieldViewImpl extends Composite implements HierarchyFieldView {

    @Nonnull
    private MoveToParentHandler moveToParentHandler = () -> {};

    @Nonnull
    private MoveToChildHandler moveToChildHandler = () -> {};

    @Nonnull
    private MoveToSiblingHanler moveToSiblingHandler = () -> {};

    @Nonnull
    private EntityChangedHandler entityChangedHandler = () -> {};

    interface HierarchyFieldViewImplUiBinder extends UiBinder<HTMLPanel, HierarchyFieldViewImpl> {

    }

    private static HierarchyFieldViewImplUiBinder ourUiBinder = GWT.create(HierarchyFieldViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl entityField;

    @UiField
    Button moveToSiblingButton;

    @UiField
    Button moveToChildButton;

    @UiField
    Button moveToParentButton;

    @Inject
    public HierarchyFieldViewImpl(@Nonnull PrimitiveDataEditorImpl entityField) {
        this.entityField = checkNotNull(entityField);
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @UiHandler("moveToParentButton")
    public void moveToParentButtonClick(ClickEvent event) {
        moveToParentHandler.handleMoveToParent();
    }

    @UiHandler("moveToSiblingButton")
    public void moveToSiblingButtonClick(ClickEvent event) {
        moveToSiblingHandler.handleMoveToSibling();
    }

    @UiHandler("moveToChildButton")
    public void moveToChildButtonClick(ClickEvent event) {
        moveToChildHandler.handleMoveToChild();
    }

    @UiHandler("entityField")
    public void handleEntityChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        entityChangedHandler.handleEntityChanged();
    }

    @Override
    public void setMoveToParentHandler(@Nonnull MoveToParentHandler handler) {
        this.moveToParentHandler = checkNotNull(handler);
    }

    @Override
    public void setMoveToChildHandler(@Nonnull MoveToChildHandler handler) {
        this.moveToChildHandler = checkNotNull(handler);
    }

    @Override
    public void setMoveToSiblingHandler(@Nonnull MoveToSiblingHanler handler) {
        this.moveToSiblingHandler = checkNotNull(handler);
    }

    @Override
    public void setEntityChangedHandler(@Nonnull EntityChangedHandler handler) {
        this.entityChangedHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public Optional<OWLEntityData> getEntity() {
        return entityField.getValue()
                          .filter(d -> d instanceof OWLEntityData)
                          .map(d -> (OWLEntityData) d);
    }

    @Override
    public void setEntity(@Nonnull OWLEntityData entity) {
        entityField.setValue(entity);
    }

    @Override
    public void clearEntity() {
        entityField.clearValue();
    }

    @Override
    public void setMoveToParentButtonEnabled(boolean enabled) {
        moveToParentButton.setEnabled(enabled);
    }

    @Override
    public void setMoveToChildButtonEnabled(boolean enabled) {
        moveToChildButton.setEnabled(enabled);
    }

    @Override
    public void setMoveToSiblingButtonEnabled(boolean enabled) {
        moveToSiblingButton.setEnabled(enabled);
    }
}
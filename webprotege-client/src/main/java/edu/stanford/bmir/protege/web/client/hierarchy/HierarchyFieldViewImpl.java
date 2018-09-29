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
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Sep 2018
 */
public class HierarchyFieldViewImpl extends Composite implements HierarchyFieldView {

    @Nonnull
    private SyncClassWithLastSelectedClassHandler syncClassWithLastSelectedClassHandler = () -> {};

    @Nonnull
    private MoveToParentHandler moveToParentHandler = (target) -> {};

    @Nonnull
    private MoveToChildHandler moveToChildHandler = (uiObject) -> {};

    @Nonnull
    private MoveToSiblingHandler moveToSiblingHandler = (target) -> {};

    @Nonnull
    private EntityChangedHandler entityChangedHandler = () -> {};

    @Nonnull
    private ShowPopupHierarchyHandler showPopupHierarchyHandler = (target) -> {};

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

    @UiField
    Button syncSelectionButton;

    @UiField
    Button selectFromHierarchyButton;

    @UiField
    Button searchButton;

    @Inject
    public HierarchyFieldViewImpl(@Nonnull PrimitiveDataEditorImpl entityField) {
        this.entityField = checkNotNull(entityField);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("moveToParentButton")
    public void moveToParentButtonClick(ClickEvent event) {
        moveToParentHandler.handleMoveToParent(moveToParentButton);
    }

    @UiHandler("moveToSiblingButton")
    public void moveToSiblingButtonClick(ClickEvent event) {
        moveToSiblingHandler.handleMoveToSibling(moveToSiblingButton);
    }

    @UiHandler("moveToChildButton")
    public void moveToChildButtonClick(ClickEvent event) {
        moveToChildHandler.handleMoveToChild(moveToChildButton);
    }

    @UiHandler("entityField")
    public void handleEntityChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        entityChangedHandler.handleEntityChanged();
    }

    @UiHandler("syncSelectionButton")
    public void syncSelectionButtonClick(ClickEvent event) {
        syncClassWithLastSelectedClassHandler.handleSyncWithLastSelectedClass();
    }

    @UiHandler("selectFromHierarchyButton")
    public void selectFromHierarchyButtonClick(ClickEvent event) {
        showPopupHierarchyHandler.handleShowPopupHierarchy(selectFromHierarchyButton);
    }

    @UiHandler("searchButton")
    public void searchButtonClick(ClickEvent event) {
        entityField.clearValue();
        entityField.requestFocus();
    }


    @Override
    public void setSyncClassWithLastSelectedClassHandler(@Nonnull SyncClassWithLastSelectedClassHandler handler) {
        this.syncClassWithLastSelectedClassHandler = checkNotNull(handler);
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
    public void setMoveToSiblingHandler(@Nonnull MoveToSiblingHandler handler) {
        this.moveToSiblingHandler = checkNotNull(handler);
    }

    @Override
    public void setEntityChangedHandler(@Nonnull EntityChangedHandler handler) {
        this.entityChangedHandler = checkNotNull(handler);
    }

    @Override
    public void setShowPopupHierarchyHandler(@Nonnull ShowPopupHierarchyHandler handler) {
        this.showPopupHierarchyHandler = checkNotNull(handler);
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
    public void setSyncClassWithLastSelectedClassEnabled(boolean enabled) {
        syncSelectionButton.setEnabled(enabled);
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

    @Override
    public void setEntityType(@Nonnull PrimitiveType entityType) {
        entityField.setAllowedTypes(Collections.singleton(entityType));
    }

    @Override
    public void setSyncWithCurrentSelectionVisible(boolean visible) {
        syncSelectionButton.setVisible(visible);
    }

    @Override
    public void requestFocus() {
        entityField.requestFocus();
    }
}
package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import org.semanticweb.owlapi.model.EntityType;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public class ManchesterSyntaxFrameEditorErrorMessageViewImpl extends Composite implements ManchesterSyntaxFrameEditorErrorMessageView {

    interface ManchesterSyntaxFrameEditorErrorMessageViewImplUiBinder extends UiBinder<HTMLPanel, ManchesterSyntaxFrameEditorErrorMessageViewImpl> {
    }

    private static ManchesterSyntaxFrameEditorErrorMessageViewImplUiBinder ourUiBinder = GWT.create(ManchesterSyntaxFrameEditorErrorMessageViewImplUiBinder.class);

    @UiField
    protected HasText errorMessageView;

    @UiField
    protected HasText currentTokenNameField;

    @UiField
    protected Button createAsClass;

    @UiField
    protected Button createAsObjectProperty;

    @UiField
    protected Button createAsDataProperty;

    @UiField
    protected Button createAsAnnotationProperty;

    @UiField
    protected Button createAsIndividual;

    @UiField
    protected Button createAsDatatype;

    @UiField
    protected Widget createAsPanel;

    private CreateAsEntityTypeHandler handler = new CreateAsEntityTypeHandler() {
        @Override
        public void handleCreateHasEntity(String name, EntityType<?> entityType) {

        }
    };

    private Map<EntityType<?>, Button> entityTypeButtonMap = Maps.newHashMap();

    public ManchesterSyntaxFrameEditorErrorMessageViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        entityTypeButtonMap.put(EntityType.CLASS, createAsClass);
        entityTypeButtonMap.put(EntityType.OBJECT_PROPERTY, createAsObjectProperty);
        entityTypeButtonMap.put(EntityType.DATA_PROPERTY, createAsDataProperty);
        entityTypeButtonMap.put(EntityType.ANNOTATION_PROPERTY, createAsAnnotationProperty);
        entityTypeButtonMap.put(EntityType.NAMED_INDIVIDUAL, createAsIndividual);
        entityTypeButtonMap.put(EntityType.DATATYPE, createAsDatatype);
    }

    @UiHandler("createAsClass")
    protected void handleCreateAsClass(ClickEvent event) {
        handler.handleCreateHasEntity(currentTokenNameField.getText(), EntityType.CLASS);
    }

    @UiHandler("createAsObjectProperty")
    protected void handleCreateAsObjectProperty(ClickEvent event) {
        handler.handleCreateHasEntity(currentTokenNameField.getText(), EntityType.OBJECT_PROPERTY);
    }

    @UiHandler("createAsDataProperty")
    protected void handleCreateAsDataProperty(ClickEvent event) {
        handler.handleCreateHasEntity(currentTokenNameField.getText(), EntityType.DATA_PROPERTY);
    }

    @UiHandler("createAsAnnotationProperty")
    protected void handleCreateAsAnnotationProperty(ClickEvent event) {
        handler.handleCreateHasEntity(currentTokenNameField.getText(), EntityType.ANNOTATION_PROPERTY);
    }

    @UiHandler("createAsIndividual")
    protected void handleCreateAsIndividual(ClickEvent event) {
        handler.handleCreateHasEntity(currentTokenNameField.getText(), EntityType.NAMED_INDIVIDUAL);
    }

    @UiHandler("createAsDatatype")
    protected void handleCreateAsDatatype(ClickEvent event) {
        handler.handleCreateHasEntity(currentTokenNameField.getText(), EntityType.DATATYPE);
    }

    public void setCreateAsEntityTypeHandler(CreateAsEntityTypeHandler handler) {
        this.handler = checkNotNull(handler);
    }

    @Override
    public void setErrorMessage(String message) {
        errorMessageView.setText(message);
    }

    @Override
    public void setCurrentToken(String currentToken) {
        currentTokenNameField.setText(currentToken);
    }

    @Override
    public void setExpectedEntityTypes(List<EntityType<?>> expectedEntityTypes) {
        if(expectedEntityTypes.isEmpty()) {
            createAsPanel.setVisible(false);
        }
        else {
            for(EntityType<?> entityType : entityTypeButtonMap.keySet()) {
                Button button = entityTypeButtonMap.get(entityType);
                button.setVisible(false);
            }
            for(EntityType<?> expectedEntityType : expectedEntityTypes) {
                Button button = entityTypeButtonMap.get(expectedEntityType);
                button.setVisible(true);
            }
            createAsPanel.setVisible(true);
        }

    }
}

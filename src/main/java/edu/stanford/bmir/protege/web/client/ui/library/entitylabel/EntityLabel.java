package edu.stanford.bmir.protege.web.client.ui.library.entitylabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class EntityLabel extends Composite {

    interface EntityLabelUiBinder extends UiBinder<HTMLPanel, EntityLabel> {

    }

    private static EntityLabelUiBinder ourUiBinder = GWT.create(EntityLabelUiBinder.class);

    @UiField
    protected InlineLabel typeLabel;

    @UiField
    protected InlineHyperlink browserTextLabel;

    @UiField
    protected InlineLabel iriLabel;

    private OWLEntityData entityData;

    private SelectionModel selectionModel;

    public EntityLabel() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }


    public void setEntity(OWLEntityData entityData) {
        this.entityData = entityData;
        updateDisplay();
    }

    public void setSelectionModel(SelectionModel selectionModel) {
        this.selectionModel = selectionModel;
    }

    public OWLEntityData getEntityData() {
        return entityData;
    }



    private void updateDisplay() {
        typeLabel.setText(entityData.getEntity().getEntityType().getName() + ": ");
        browserTextLabel.setText(entityData.getBrowserText());
        browserTextLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (selectionModel != null) {
                    selectionModel.setSelection(entityData);
                }
            }
        });
        iriLabel.setText("(" + entityData.getEntity().getIRI().toQuotedString() + ")");
    }
}
package edu.stanford.bmir.protege.web.client.ui.library.entitylabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

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
    protected InlineLabel browserTextLabel;

    @UiField
    protected InlineLabel iriLabel;

    private OWLEntityData entityData;

    public EntityLabel() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }


    public void setEntity(OWLEntityData entityData) {
        this.entityData = entityData;
        updateDisplay();
    }

    public OWLEntityData getEntityData() {
        return entityData;
    }



    private void updateDisplay() {
        typeLabel.setText(entityData.getEntity().getEntityType().getName() + ": ");
        browserTextLabel.setText(entityData.getBrowserText());
        iriLabel.setText("(" + entityData.getEntity().getIRI().toQuotedString() + ")");
    }
}
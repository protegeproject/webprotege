package edu.stanford.bmir.protege.web.client.projectfeed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.entitylabel.EntityLabel;
import edu.stanford.bmir.protege.web.client.library.timelabel.ElapsedTimeLabel;
import edu.stanford.bmir.protege.web.client.user.UserIcon;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectChangeEventView extends Composite implements ProjectFeedItemDisplay {

    @UiField
    protected SimplePanel userIconHolder;

    @UiField
    protected InlineLabel userNameLabel;

    @UiField
    protected ElapsedTimeLabel timeLabel;

    @UiField
    protected FlexTable changedEntitiesTable;

    @UiField
    protected InlineLabel descriptionField;


    private SelectionModel selectionModel;

    interface ChangeEventPanelUiBinder extends UiBinder<HTMLPanel, ProjectChangeEventView> {

    }

    private static ChangeEventPanelUiBinder ourUiBinder = GWT.create(ChangeEventPanelUiBinder.class);

    @Inject
    public ProjectChangeEventView(SelectionModel selectionModel) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        this.selectionModel = selectionModel;
        initWidget(rootElement);
    }

    @Override
    public UserId getUserId() {
        return UserId.getUserId(userNameLabel.getText());
    }

    public void setUserName(String userName) {
        userIconHolder.setWidget(UserIcon.get(UserId.getUserId(userName)));
        userNameLabel.setText(userName);
    }

    public void setTimestamp(long timestamp) {
        timeLabel.setBaseTime(timestamp);

    }

    public void setDescription(String description) {
        descriptionField.setText(description);
    }

    public void setChangedEntities(final Set<OWLEntityData> entities) {
        changedEntitiesTable.removeAllRows();
        int row = 0;
        for(OWLEntityData entityData : entities) {
            final EntityLabel changedEntityLabel = new EntityLabel();
            changedEntityLabel.setEntity(entityData);
            changedEntityLabel.setSelectionModel(selectionModel);
            changedEntitiesTable.setWidget(row, 0, changedEntityLabel);
            row++;
        }
    }

    @Override
    public void updateElapsedTimeDisplay() {
        timeLabel.update();
    }
}
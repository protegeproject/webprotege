package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.ui.library.timelabel.ElapsedTimeLabel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public class UserStartedViewingProjectEventPanel extends Composite implements ProjectFeedItemDisplay {

    interface UserStartedViewingProjectEventPanelUiBinder extends UiBinder<HTMLPanel, UserStartedViewingProjectEventPanel> {

    }

    private static UserStartedViewingProjectEventPanelUiBinder ourUiBinder = GWT.create(UserStartedViewingProjectEventPanelUiBinder.class);

    @UiField
    protected Label userNameLabel;

    @UiField
    protected ElapsedTimeLabel timeLabel;

    public UserStartedViewingProjectEventPanel() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    public void setUserId(UserId userId) {
        userNameLabel.setText(userId.getUserName());
    }

    public void setTimestamp(long timestamp) {
        timeLabel.setBaseTime(timestamp);
    }

    @Override
    public void updateElapsedTimeDisplay() {
        timeLabel.update();
    }
}
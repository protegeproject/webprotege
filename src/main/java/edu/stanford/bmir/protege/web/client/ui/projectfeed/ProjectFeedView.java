package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectFeedView extends Composite {


    private static final int MAX_FEED_SIZE = 300;

    private static final int ONE_MINUTE = 60 * 1000;


    private Set<UserId> hiddenUsersActivity = new HashSet<>();

    private boolean showOntologyChanges = true;

    private Timer elapsedTimesUpdateTimer = new Timer() {
        @Override
        public void run() {
            updateElapsedTimes();
        }
    };


    @UiField
    protected FlexTable changeEventTable;

    interface RollingProjectChangedEventPanelUiBinder extends UiBinder<HTMLPanel, ProjectFeedView> {

    }

    private static RollingProjectChangedEventPanelUiBinder ourUiBinder = GWT.create(RollingProjectChangedEventPanelUiBinder.class);

    @Inject
    public ProjectFeedView() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    public void setUserActivityVisible(UserId userId, boolean visible) {
        if (!visible) {
            hiddenUsersActivity.add(userId);
        }
        else {
            hiddenUsersActivity.remove(userId);
        }
        applyFilter();
    }

    public void setOntologyChangesVisible(boolean visible) {
        this.showOntologyChanges = visible;
        applyFilter();
    }

    private void applyFilter() {
        for(int i = 0; i < changeEventTable.getRowCount(); i++) {
            ProjectFeedItemDisplay widget = (ProjectFeedItemDisplay) changeEventTable.getWidget(i, 0);
            boolean visible = isVisible(widget);
            widget.setVisible(visible);
        }
    }

    private boolean isVisible(ProjectFeedItemDisplay widget) {
        boolean isHiddenUser = hiddenUsersActivity.contains(widget.getUserId());
        boolean isHiddenProjectChange = !showOntologyChanges && widget instanceof ProjectChangeEventView;
        return !isHiddenUser && !isHiddenProjectChange;
    }


    public void insertWidgetIntoFeed(ProjectFeedItemDisplay widget) {
        widget.setVisible(isVisible(widget));
        changeEventTable.insertRow(0);
        changeEventTable.setWidget(0, 0, widget);
        pruneIfNecessary();
    }

    private void pruneIfNecessary() {
        if(changeEventTable.getRowCount() > MAX_FEED_SIZE) {
            changeEventTable.removeRow(changeEventTable.getRowCount() - 1);
        }
    }


    private void updateElapsedTimes() {
        for(int rowIndex = 0; rowIndex < changeEventTable.getRowCount(); rowIndex++) {
            ProjectFeedItemDisplay display = (ProjectFeedItemDisplay) changeEventTable.getWidget(rowIndex, 0);
            display.updateElapsedTimeDisplay();
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        elapsedTimesUpdateTimer.scheduleRepeating(ONE_MINUTE);
    }

    @Override
    protected void onUnload() {
        elapsedTimesUpdateTimer.cancel();
    }
}
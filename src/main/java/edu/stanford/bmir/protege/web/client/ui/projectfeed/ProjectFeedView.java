package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.event.NotePostedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.UserStartingViewingProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.UserStoppedViewingProjectEvent;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Date;
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

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private SelectionModel selectionModel;

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

    private Set<NoteId> noteIds = new HashSet<NoteId>();

    interface RollingProjectChangedEventPanelUiBinder extends UiBinder<HTMLPanel, ProjectFeedView> {

    }

    private static RollingProjectChangedEventPanelUiBinder ourUiBinder = GWT.create(RollingProjectChangedEventPanelUiBinder.class);

    public ProjectFeedView(ProjectId projectId, SelectionModel selectionModel) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        this.selectionModel = selectionModel;
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
        boolean isHiddenProjectChange = !showOntologyChanges && widget instanceof ProjectChangeEventPanel;
        return !isHiddenUser && !isHiddenProjectChange;
    }

    public void postChangeEvent(ProjectChangedEvent event) {
        if(event.getRevisionNumber().getValue() <= lastRevisionNumber.getValue()) {
            return;
        }
        lastRevisionNumber = event.getRevisionNumber();
        final ProjectChangeEventPanel changePanel = new ProjectChangeEventPanel(selectionModel);
        changePanel.setUserName(event.getUserId().getUserName());
        changePanel.setTimestamp(event.getTimestamp());
        changePanel.setChangedEntities(event.getSubjects());
        changePanel.setDescription(event.getRevisionSummary().getDescription());
        insertWidgetIntoFeed(changePanel);
    }


    public void postNotePostedEvent(NotePostedEvent event) {
        final NoteId noteId = event.getNoteDetails().getNoteHeader().getNoteId();
        if(noteIds.contains(noteId)) {
            return;
        }
        noteIds.add(noteId);
        final NotePostedEventPanel notePostedEventPanel = new NotePostedEventPanel(selectionModel);
        notePostedEventPanel.setValue(event);
        insertWidgetIntoFeed(notePostedEventPanel);

    }

    private void insertWidgetIntoFeed(ProjectFeedItemDisplay widget) {
        widget.setVisible(isVisible(widget));
        changeEventTable.insertRow(0);
        changeEventTable.setWidget(0, 0, widget);
        pruneIfNecessary();
    }

    public void postUserStartedViewingProjectEvent(UserStartingViewingProjectEvent event) {
        UserStartedViewingProjectEventPanel panel = new UserStartedViewingProjectEventPanel();
        panel.setUserId(event.getUserId());
        panel.setTimestamp(new Date().getTime());
        insertWidgetIntoFeed(panel);
    }

    public void postUserStoppedViewingProjectEvent(UserStoppedViewingProjectEvent event) {
        UserStoppedViewingProjectEventPanel panel = new UserStoppedViewingProjectEventPanel();
        panel.setUserId(event.getUserId());
        panel.setTimestamp(new Date().getTime());
        insertWidgetIntoFeed(panel);
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
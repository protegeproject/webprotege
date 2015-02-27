package edu.stanford.bmir.protege.web.client.change;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.Filter;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesAction;
import edu.stanford.bmir.protege.web.shared.change.GetProjectChangesResult;
import edu.stanford.bmir.protege.web.shared.change.ProjectChange;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Date;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class ChangeListViewPresenter {

    private ChangeListView changeListView;
    private DispatchServiceManager dispatchServiceManager;

    public ChangeListViewPresenter(ChangeListView changeListView, DispatchServiceManager dispatchServiceManager) {
        this.changeListView = changeListView;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void setChangesForEntity(ProjectId projectId, OWLEntity entity) {
        changeListView.clear();
        dispatchServiceManager.execute(new GetProjectChangesAction(projectId, Optional.of(entity)), new DispatchServiceCallback<GetProjectChangesResult>() {
            @Override
            public void handleSuccess(GetProjectChangesResult result) {
                fillView(result);
            }
        });
    }

    private void fillView(GetProjectChangesResult result) {
        changeListView.clear();
        long previousTimeStamp = 0;
        ImmutableList<ProjectChange> changes = result.getChanges();
        for(ProjectChange projectChange : changes.reverse()) {

            long changeTimeStamp = projectChange.getTimestamp();
            if(!TimeUtil.isSameCalendarDay(previousTimeStamp, changeTimeStamp)) {
                previousTimeStamp = changeTimeStamp;
                Date date = new Date(changeTimeStamp);
                changeListView.addSeparator("\u25C9   Changes on " + DateTimeFormat.getFormat("EEE, d MMM yyyy").format(date));
            }

            ChangeDetailsView view = new ChangeDetailsViewImpl();
            view.setAuthor(projectChange.getAuthor());
            view.setHighLevelDescription(projectChange.getSummary());
            view.setDiff(projectChange.getDiff().getPageElements());
            view.setTimestamp(changeTimeStamp);
            changeListView.addChangeDetailsView(view);
        }
    }

}

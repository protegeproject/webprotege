package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.filter.FilterViewImpl;
import edu.stanford.bmir.protege.web.client.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletActionHandler;
import edu.stanford.bmir.protege.web.shared.filter.Filter;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import javax.inject.Inject;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class ProjectFeedPortlet extends AbstractOWLEntityPortlet {

    public static final FilterId SHOW_MY_ACTIVITY_FILTER = new FilterId("Show my activity");

    public static final FilterId SHOW_PROJECT_CHANGES_FILTER = new FilterId("Show project changes");

    private final LoggedInUserProvider loggedInUserProvider;

    private final ProjectFeedBasePanel basePanel;

    @Inject
    public ProjectFeedPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserManager) {
        super(selectionModel, eventBus, projectId, loggedInUserManager);
        this.loggedInUserProvider = loggedInUserManager;
        basePanel = new ProjectFeedBasePanel(getProjectId(), this, selectionModel);
        setTitle("Project feed");
        getContentHolder().setWidget(basePanel);

        FilterView filterView = new FilterViewImpl();
        filterView.addFilterGroup("Project feed settings");
        filterView.addFilter(SHOW_MY_ACTIVITY_FILTER, FilterSetting.ON);
        filterView.addFilter(SHOW_PROJECT_CHANGES_FILTER, FilterSetting.ON);
        filterView.closeCurrentGroup();
        filterView.addValueChangeHandler(event -> applyFilters(event.getValue()));
        setFilter(filterView);
    }

    private void applyFilters(FilterSet filterSet) {
        FilterSetting showMyActivity = filterSet.getFilterSetting(SHOW_MY_ACTIVITY_FILTER, FilterSetting.ON);
        basePanel.setUserActivityVisible(loggedInUserProvider.getCurrentUserId(), showMyActivity == FilterSetting.ON);

        FilterSetting showProjectChanges = filterSet.getFilterSetting(SHOW_PROJECT_CHANGES_FILTER, FilterSetting.ON);
        basePanel.setUserActivityVisible(loggedInUserProvider.getCurrentUserId(), showProjectChanges == FilterSetting.ON);

    }

}

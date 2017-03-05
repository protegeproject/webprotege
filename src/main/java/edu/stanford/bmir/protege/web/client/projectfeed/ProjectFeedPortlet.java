package edu.stanford.bmir.protege.web.client.projectfeed;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortlet;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.filter.FilterSetting.ON;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
@Portlet(id = "portlets.ProjectFeed", title = "Project Feed")
public class ProjectFeedPortlet extends AbstractWebProtegePortlet {

    public static final FilterId SHOW_MY_ACTIVITY_FILTER = new FilterId("Show my activity");

    public static final FilterId SHOW_PROJECT_CHANGES_FILTER = new FilterId("Show project changes");

    private final LoggedInUserProvider loggedInUserProvider;

    private final ProjectFeedPresenter presenter;

    private final FilterView filterView;

    @Inject
    public ProjectFeedPortlet(ProjectFeedPresenter presenter,
                              FilterView filterView,
                              SelectionModel selectionModel,
                              ProjectId projectId,
                              LoggedInUserProvider loggedInUserManager) {
        super(selectionModel, projectId);
        this.loggedInUserProvider = loggedInUserManager;
        this.presenter = presenter;
        this.filterView = filterView;
        filterView.addFilterGroup("Project feed settings");
        filterView.addFilter(SHOW_MY_ACTIVITY_FILTER, ON);
        filterView.addFilter(SHOW_PROJECT_CHANGES_FILTER, ON);
        filterView.closeCurrentGroup();
        filterView.addValueChangeHandler(event -> applyFilters(event.getValue()));
    }

    @Override
    public void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setViewTitle("Project feed");
        presenter.start(eventBus);
        portletUi.setWidget(presenter.getView());
        portletUi.setFilterView(filterView);
    }

    private void applyFilters(FilterSet filterSet) {
        FilterSetting showMyActivity = filterSet.getFilterSetting(SHOW_MY_ACTIVITY_FILTER, ON);
        presenter.setUserActivityVisible(loggedInUserProvider.getCurrentUserId(), showMyActivity == ON);

        FilterSetting showProjectChanges = filterSet.getFilterSetting(SHOW_PROJECT_CHANGES_FILTER, ON);
        presenter.setUserActivityVisible(loggedInUserProvider.getCurrentUserId(), showProjectChanges == ON);

    }

}

package edu.stanford.bmir.protege.web.client.projectlist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.project.AvailableProject;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/10/2013
 */
public class AvailableProjectsListViewImpl extends Composite implements AvailableProjectsListView {

    private final List<AvailableProjectPresenter> entries = new ArrayList<>();

    interface AvailableProjectsListViewUIImplUiBinder extends UiBinder<HTMLPanel, AvailableProjectsListViewImpl> {

    }

    private static AvailableProjectsListViewUIImplUiBinder ourUiBinder = GWT.create(AvailableProjectsListViewUIImplUiBinder.class);

    private final AvailableProjectPresenterFactory presenterFactory;


    @UiField
    protected FlowPanel itemContainer;

    @Inject
    public AvailableProjectsListViewImpl(AvailableProjectPresenterFactory presenterFactory) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        this.presenterFactory = presenterFactory;
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<ProjectId> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    @Override
    public void setSelectedProject(ProjectId projectId) {
    }

    @Override
    public void setListData(List<AvailableProject> availableProjects) {
        itemContainer.clear();
        entries.clear();
        for(final AvailableProject project : availableProjects) {
            AvailableProjectPresenter itemPresenter = presenterFactory.create(project);
            itemPresenter.start();
            entries.add(itemPresenter);
            itemContainer.add(itemPresenter.getView());
        }
    }

    @Override
    public void addListData(AvailableProject details) {
        AvailableProjectPresenter itemPresenter = presenterFactory.create(details);
        itemPresenter.start();
        itemContainer.insert(itemPresenter.getView(), 0);
        entries.add(0, itemPresenter);
    }

}

package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/10/2013
 */
public class ProjectListViewImpl extends Composite implements ProjectListView {

    private final List<ProjectDetailsPresenter> entries = new ArrayList<>();

    interface ProjectListViewUIImplUiBinder extends UiBinder<HTMLPanel, ProjectListViewImpl> {

    }

    private static ProjectListViewUIImplUiBinder ourUiBinder = GWT.create(ProjectListViewUIImplUiBinder.class);

    private final ProjectDetailsPresenterFactory presenterFactory;


    @UiField
    protected FlowPanel itemContainer;

    @Inject
    public ProjectListViewImpl(ProjectDetailsPresenterFactory presenterFactory) {
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
    public void setListData(List<ProjectDetails> projectDetails) {
        itemContainer.clear();
        entries.clear();
        for(final ProjectDetails details : projectDetails) {
            ProjectDetailsPresenter itemPresenter = presenterFactory.create(details);
            itemPresenter.start();
            entries.add(itemPresenter);
            itemContainer.add(itemPresenter.getView());
        }
    }

    @Override
    public void addListData(ProjectDetails details) {
        ProjectDetailsPresenter itemPresenter = presenterFactory.create(details);
        itemPresenter.start();
        itemContainer.insert(itemPresenter.getView(), 0);
        entries.add(0, itemPresenter);
    }

}

package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.ProjectManagerPresenter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class ProjectListActivity extends AbstractActivity {

    private ProjectManagerPresenter presenter;

    @Inject
    public ProjectListActivity(ProjectManagerPresenter presenter) {
        this.presenter = presenter;
    }

    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.start(panel);
    }
}

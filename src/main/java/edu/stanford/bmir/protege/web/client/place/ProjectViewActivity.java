package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.project.ProjectPresenter;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class ProjectViewActivity extends AbstractActivity {

    private final ProjectPresenter projectPresenter;

    private final ProjectViewPlace place;

    @Inject
    public ProjectViewActivity(ProjectPresenter projectPresenter, ProjectViewPlace place) {
        this.projectPresenter = projectPresenter;
        this.place = place;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        projectPresenter.start(panel, place);
    }
}

package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.Objects;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.project.ProjectPresenter;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

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
        this.projectPresenter = checkNotNull(projectPresenter);
        this.place = checkNotNull(place);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        projectPresenter.start(panel, place);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectPresenter);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectViewActivity)) {
            return false;
        }
        ProjectViewActivity other = (ProjectViewActivity) obj;
        return this.projectPresenter.equals(other.projectPresenter);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectViewActivity")
                .addValue(projectPresenter)
                .toString();
    }
}

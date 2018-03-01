package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.project.ProjectPrefixDeclarationsPresenter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Feb 2018
 */
public class ProjectPrefixDeclarationsActivity extends AbstractActivity {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectPrefixDeclarationsPresenter presenter;

    @Inject
    public ProjectPrefixDeclarationsActivity(@Nonnull ProjectId projectId,
                                             @Nonnull ProjectPrefixDeclarationsPresenter presenter) {
        this.projectId = checkNotNull(projectId);
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.start(panel, eventBus);
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectPrefixDeclarationsActivity)) {
            return false;
        }
        // Equality only depends upon the projectId and not on the presenter
        ProjectPrefixDeclarationsActivity other = (ProjectPrefixDeclarationsActivity) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectPrefixDeclarationsActivity")
                .addValue(projectId)
                .toString();
    }
}

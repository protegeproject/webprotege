package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.list.ListBoxCellRenderer;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-13
 */
public class ProjectDetailsListBoxCellRenderer implements ListBoxCellRenderer<ProjectDetails> {


    private final Provider<ProjectDetailsView> projectDetailsViewProvider;

    @Inject
    public ProjectDetailsListBoxCellRenderer(Provider<ProjectDetailsView> projectDetailsViewProvider) {
        this.projectDetailsViewProvider = checkNotNull(projectDetailsViewProvider);
    }

    @Override
    public IsWidget render(ProjectDetails element) {
        ProjectDetailsView view = projectDetailsViewProvider.get();
        view.setDisplayName(element.getDisplayName());
        view.setDescription(element.getDescription());
        return view;
    }
}

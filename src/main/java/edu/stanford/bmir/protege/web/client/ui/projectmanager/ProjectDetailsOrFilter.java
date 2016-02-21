package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/16
 */
public class ProjectDetailsOrFilter implements ProjectDetailsFilter {

    private final List<ProjectDetailsFilter> filterList = new ArrayList<>();

    public ProjectDetailsOrFilter(List<ProjectDetailsFilter> filterList) {
        this.filterList.addAll(filterList);
    }

    @Override
    public boolean isIncluded(ProjectDetails projectDetails) {
        for(ProjectDetailsFilter filter : filterList) {
            if(filter.isIncluded(projectDetails)) {
                return true;
            }
        }
        return false;
    }
}

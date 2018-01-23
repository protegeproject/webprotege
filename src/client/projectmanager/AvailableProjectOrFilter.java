package edu.stanford.bmir.protege.web.client.projectmanager;

import edu.stanford.bmir.protege.web.shared.project.AvailableProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/16
 */
public class AvailableProjectOrFilter implements AvailableProjectFilter {

    private final List<AvailableProjectFilter> filterList = new ArrayList<>();

    public AvailableProjectOrFilter(List<AvailableProjectFilter> filterList) {
        this.filterList.addAll(filterList);
    }

    @Override
    public boolean isIncluded(AvailableProject availableProject) {
        for(AvailableProjectFilter filter : filterList) {
            if(filter.isIncluded(availableProject)) {
                return true;
            }
        }
        return false;
    }
}

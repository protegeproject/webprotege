package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 11/04/2014
 */
public class ProjectDetailsCache {

    private Map<ProjectId, ProjectDetails> cache = Maps.newHashMap();

    public void add(ProjectDetails projectDetails) {
        cache.put(projectDetails.getProjectId(), projectDetails);
    }

    public boolean remove(ProjectId projectId) {
        return cache.remove(projectId) != null;
    }

    public void setProjectDetails(Collection<ProjectDetails> projectDetails) {
        cache.clear();
        for(ProjectDetails details : projectDetails) {
            cache.put(details.getProjectId(), details);
        }
    }

    public List<ProjectDetails> getProjectDetailsList() {
        List<ProjectDetails> result = Lists.newArrayList(cache.values());
        Collections.sort(result);
        return result;
    }

    public Optional<ProjectDetails> getProjectDetails(ProjectId projectId) {
        ProjectDetails projectDetails = cache.get(projectId);
        return Optional.fromNullable(projectDetails);
    }

    public boolean setInTrash(ProjectId projectId, boolean inTrash) {
        ProjectDetails details = cache.get(projectId);
        if(details == null) {
            return false;
        }
        if(details.isInTrash() == inTrash) {
            return false;
        }
        ProjectDetails replacementDetails = details.builder().setInTrash(inTrash).build();
        cache.put(projectId, replacementDetails);
        return true;
    }


}

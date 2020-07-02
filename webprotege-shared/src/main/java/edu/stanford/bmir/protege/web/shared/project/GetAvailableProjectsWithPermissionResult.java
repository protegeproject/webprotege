package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
public class GetAvailableProjectsWithPermissionResult implements Result {

    private ImmutableList<ProjectDetails> projects;

    public GetAvailableProjectsWithPermissionResult(@Nonnull ImmutableList<ProjectDetails> projects) {
        this.projects = checkNotNull(projects);
    }

    @GwtSerializationConstructor
    private GetAvailableProjectsWithPermissionResult() {
    }

    @Nonnull
    public ImmutableList<ProjectDetails> getProjects() {
        return projects;
    }
}

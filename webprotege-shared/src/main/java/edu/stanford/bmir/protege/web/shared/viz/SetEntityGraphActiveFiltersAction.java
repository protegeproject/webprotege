package edu.stanford.bmir.protege.web.shared.viz;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public class SetEntityGraphActiveFiltersAction implements ProjectAction<SetEntityGraphActiveFiltersResult> {

    private ProjectId projectId;

    private ImmutableList<FilterName> activeFilters;

    public SetEntityGraphActiveFiltersAction(@Nonnull ProjectId projectId,
                                             @Nonnull ImmutableList<FilterName> activeFilters) {
        this.projectId = checkNotNull(projectId);
        this.activeFilters = checkNotNull(activeFilters);
    }

    @GwtSerializationConstructor
    private SetEntityGraphActiveFiltersAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableList<FilterName> getActiveFilters() {
        return activeFilters;
    }
}

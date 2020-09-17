package edu.stanford.bmir.protege.web.shared.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
public class SetSearchSettingsAction implements ProjectAction<SetSearchSettingsResult> {

    private ProjectId projectId;

    private ImmutableList<EntitySearchFilter> from;

    private ImmutableList<EntitySearchFilter> to;

    public SetSearchSettingsAction(@Nonnull ProjectId projectId,
                                   @Nonnull ImmutableList<EntitySearchFilter> from,
                                   @Nonnull ImmutableList<EntitySearchFilter> to) {
        this.projectId = checkNotNull(projectId);
        this.from = checkNotNull(from);
        this.to = checkNotNull(to);
    }

    @GwtSerializationConstructor
    private SetSearchSettingsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableList<EntitySearchFilter> getFrom() {
        return from;
    }

    @Nonnull
    public ImmutableList<EntitySearchFilter> getTo() {
        return to;
    }
}

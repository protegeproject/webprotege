package edu.stanford.bmir.protege.web.shared.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
public class CopyFormDescriptorsFromProjectAction implements ProjectAction<CopyFormDescriptorsFromProjectResult> {

    private ProjectId projectId;

    private ProjectId projectIdToCopyFrom;

    private ImmutableList<FormId> formIdsToCopy;

    public CopyFormDescriptorsFromProjectAction(ProjectId projectId,
                                                ProjectId projectIdToCopyFrom,
                                                ImmutableList<FormId> formIdsToCopy) {
        this.projectId = checkNotNull(projectId);
        this.projectIdToCopyFrom = checkNotNull(projectIdToCopyFrom);
        this.formIdsToCopy = checkNotNull(formIdsToCopy);
    }

    @GwtSerializationConstructor
    private CopyFormDescriptorsFromProjectAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ProjectId getProjectIdToCopyFrom() {
        return projectIdToCopyFrom;
    }

    @Nonnull
    public ImmutableList<FormId> getFormIdsToCopy() {
        return formIdsToCopy;
    }
}

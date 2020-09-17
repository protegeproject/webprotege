package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
public class UpdateFormDescriptorAction implements ProjectAction<UpdateFormDescriptorResult> {

    private ProjectId projectId;

    private FormDescriptor formDescriptor;

    public UpdateFormDescriptorAction(@Nonnull ProjectId projectId,
                                      @Nonnull FormDescriptor descriptor) {
        this.projectId = checkNotNull(projectId);
        this.formDescriptor = checkNotNull(descriptor);
    }

    private UpdateFormDescriptorAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }
}

package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
public class GetEntityFormDescriptorAction implements ProjectAction<GetEntityFormDescriptorResult> {

    private ProjectId projectId;

    private FormId formId;

    public GetEntityFormDescriptorAction(ProjectId projectId,
                                         FormId formId) {
        this.projectId = checkNotNull(projectId);
        this.formId = checkNotNull(formId);
    }

    @GwtSerializationConstructor
    private GetEntityFormDescriptorAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public FormId getFormId() {
        return formId;
    }
}

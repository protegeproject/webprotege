package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
public class FormsManagerService {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private ProjectId projectId;

    @Inject
    public FormsManagerService(@Nonnull DispatchServiceManager dispatch,
                               @Nonnull ProjectId projectId) {
        this.dispatch = dispatch;
        this.projectId = projectId;
    }

    public void getForms(@Nonnull HasBusy busyIndicator,
                         @Nonnull BiConsumer<ImmutableList<FormDescriptor>, ImmutableList<EntityFormSelector>> forms) {
        dispatch.execute(new GetProjectFormDescriptorsAction(projectId),
                         busyIndicator,
                         result -> forms.accept(result.getFormDescriptors(), result.getFormSelectors()));
    }

    public void setForms(@Nonnull ImmutableList<FormDescriptor> formDescriptors,
                         @Nonnull HasBusy busyIndicator,
                         @Nonnull Runnable completeHandler) {
        dispatch.execute(new SetProjectFormDescriptorsAction(projectId, formDescriptors),
                         busyIndicator,
                         result -> completeHandler.run());
    }

    public void updateForm(@Nonnull FormDescriptor formDescriptor,
                           @Nonnull HasBusy busyIndicator,
                           @Nonnull Runnable completeHandler) {
        dispatch.execute(new UpdateFormDescriptorAction(projectId, formDescriptor),
                         busyIndicator,
                         result -> completeHandler.run());
    }

    public void deleteForm(@Nonnull FormId formId,
                           @Nonnull HasBusy busyIndicator,
                           @Nonnull Runnable completeHandler) {
        dispatch.execute(new DeleteFormAction(projectId, formId),
                         busyIndicator,
                         result -> completeHandler.run());
    }
}

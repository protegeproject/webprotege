package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.BiConsumer;

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

    public void importForms(@Nonnull String formJsonSerialization,
                            @Nonnull HasBusy busyIndicator,
                            @Nonnull Runnable completeHandler,
                            @Nonnull Runnable errorHandler) {
        try {
            String importFormsUrl = "/data/projects/" + projectId.getId() + "/forms";
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST,
                                                               importFormsUrl);
            requestBuilder.setRequestData(formJsonSerialization);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    busyIndicator.setBusy(false);
                    completeHandler.run();
                    if(response.getStatusCode() != Response.SC_CREATED) {
                        errorHandler.run();
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    busyIndicator.setBusy(false);
                    completeHandler.run();
                }
            });
            requestBuilder.setHeader("Content-Type", "application/json");
            busyIndicator.setBusy(true);
            requestBuilder.send();
        } catch (RequestException e) {
            GWT.log(e.getMessage());
        }
    }
}

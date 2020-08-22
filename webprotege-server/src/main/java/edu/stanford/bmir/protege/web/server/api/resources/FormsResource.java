package edu.stanford.bmir.protege.web.server.api.resources;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.shared.form.EntityFormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSelector;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.GetProjectFormDescriptorsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;
import static dagger.internal.codegen.DaggerStreams.toImmutableList;
import static java.util.stream.Collectors.toMap;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
public class FormsResource {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ActionExecutor executor;

    @Nonnull
    private final FormResourceFactory formResourceFactory;

    @AutoFactory
    public FormsResource(@Nonnull ProjectId projectId,
                         @Provided @Nonnull ActionExecutor executor,
                         @Provided @Nonnull FormResourceFactory formResourceFactory) {
        this.projectId = checkNotNull(projectId);
        this.executor = checkNotNull(executor);
        this.formResourceFactory = checkNotNull(formResourceFactory);
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/")
    public Response getForms(@Context UserId userId) {
        var formsResult = executor.execute(new GetProjectFormDescriptorsAction(projectId), userId);
        var formDescriptors = formsResult.getFormDescriptors();
        var formSelectorsMap = formsResult.getFormSelectors().stream().collect(toMap(EntityFormSelector::getFormId,
                                                                                     EntityFormSelector::getCriteria,
                                                                                     (left, right) -> left));
        var result = formDescriptors.stream()
                       .map(descriptor -> {
                           var formId = descriptor.getFormId();
                           var criteria = formSelectorsMap.get(descriptor.getFormId());
                           return EntityFormDescriptor.valueOf(projectId, formId, descriptor, criteria);
                       })
                       .collect(toImmutableList());;
        return Response.accepted(result).build();
    }

    @Path("{formId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public FormResource locateProjectResource(@PathParam("formId") FormId formId) {
        return formResourceFactory.create(projectId, formId);
    }
}

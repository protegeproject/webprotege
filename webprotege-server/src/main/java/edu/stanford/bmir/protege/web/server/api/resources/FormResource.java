package edu.stanford.bmir.protege.web.server.api.resources;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.shared.form.EntityFormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.GetEntityFormDescriptorAction;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
public class FormResource {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final FormId formId;

    @Nonnull
    private final ActionExecutor actionExecutor;

    @AutoFactory
    public FormResource(@Nonnull ProjectId projectId, @Nonnull FormId formId,
                        @Provided @Nonnull ActionExecutor actionExecutor) {
        this.projectId = projectId;
        this.formId = formId;
        this.actionExecutor = actionExecutor;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/")
    public Response getForm(@Context UserId userId) {
        var actionResult = actionExecutor.execute(new GetEntityFormDescriptorAction(projectId, formId),
                               userId);
        var formDescriptor = actionResult.getFormDescriptor().orElse(FormDescriptor.empty(formId));
        var criteria = actionResult.getFormSelectorCriteria().orElse(CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ANY));
        var result = EntityFormDescriptor.valueOf(projectId, formId, formDescriptor, criteria);
        return Response.accepted(result).build();
    }


}

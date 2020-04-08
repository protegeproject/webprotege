package edu.stanford.bmir.protege.web.server.api.axioms;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.util.stream.Stream;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
public class PostedAxiomsActionExecutor {

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final ActionExecutor executor;

    @Inject
    public PostedAxiomsActionExecutor(@Nonnull AccessManager accessManager,
                                      @Nonnull ActionExecutor executor) {
        this.accessManager = accessManager;
        this.executor = executor;
    }

    public Response loadAxiomsAndExecuteAction(@Nonnull ProjectId projectId,
                                               @Nonnull UserId userId,
                                               @Nonnull UriInfo uriInfo,
                                               @Nonnull InputStream inputStream,
                                               @Nonnull String commitMessage,
                                               @Nonnull OWLDocumentFormat documentFormat,
                                               @Nonnull String mimeType,
                                               @Nonnull ActionFactory actionFactory,
                                               @Nonnull ActionId actionId) {
        if(!accessManager.hasPermission(Subject.forUser(userId),
                                        ProjectResource.forProject(projectId),
                                        actionId)) {
            return Response.status(FORBIDDEN)
                           .entity("You do not have permission to make changes to this project")
                           .build();
        }

        PostedAxiomsLoader axiomsLoader = new PostedAxiomsLoader(projectId,
                                                                 documentFormat,
                                                                 mimeType);
        PostedAxiomsLoadResponse loadResponse = axiomsLoader.loadAxioms(inputStream);
        if(loadResponse.isSuccess()) {
            Action<?> action = actionFactory.createAction(loadResponse.axioms(), commitMessage);
            Result result = executor.execute(action, userId);
            return Response.created(uriInfo.getAbsolutePath())
                           .entity(result)
                           .build();
        }
        else {
            return loadResponse.toResponse();
        }
    }

    public interface ActionFactory {

        Action<?> createAction(Stream<OWLAxiom> axioms, String commitMessage);
    }
}

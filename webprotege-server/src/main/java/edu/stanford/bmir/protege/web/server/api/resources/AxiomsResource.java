package edu.stanford.bmir.protege.web.server.api.resources;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.server.api.axioms.PostedAxiomsLoadResponse;
import edu.stanford.bmir.protege.web.server.api.axioms.PostedAxiomsLoader;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.DeleteAxiomsAction;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RioTurtleDocumentFormat;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.download.DownloadFormat.*;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class AxiomsResource {

    private static final String ADDED_EXTERNAL_AXIOMS = "Added external axioms";

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final ActionExecutor executor;

    @Nonnull
    private final ProjectId projectId;

    @AutoFactory
    public AxiomsResource(@Provided @Nonnull AccessManager accessManager,
                          @Provided @Nonnull ActionExecutor executor,
                          @Nonnull ProjectId projectId) {
        this.accessManager = checkNotNull(accessManager);
        this.executor = checkNotNull(executor);
        this.projectId = checkNotNull(projectId);
    }

    @POST
    @Consumes("application/rdf+xml")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleAddAxiomsInRdfXml(@Context UserId userId,
                                            @Context UriInfo uriInfo,
                                            InputStream inputStream,
                                            @QueryParam("msg") @DefaultValue(ADDED_EXTERNAL_AXIOMS) String msg) {
        return loadAndAddAxioms(userId,
                                uriInfo,
                                inputStream, msg,
                                new RDFXMLDocumentFormat(),
                                RDF_XML.getMimeType());
    }

    @POST
    @Consumes("text/ttl")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleAddAxiomsInTurtle(@Context UserId userId,
                                            @Context UriInfo uriInfo,
                                            InputStream inputStream,
                                            @QueryParam("msg") @DefaultValue(ADDED_EXTERNAL_AXIOMS) String msg) {
        return loadAndAddAxioms(userId,
                                uriInfo,
                                inputStream, msg,
                                new RioTurtleDocumentFormat(),
                                RDF_TURLE.getMimeType());
    }

    @POST
    @Consumes("text/owl-functional")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleAddAxiomsInFunctionalSyntax(@Context UserId userId,
                                                      @Context UriInfo uriInfo,
                                                      InputStream inputStream,
                                                      @QueryParam("msg") @DefaultValue(ADDED_EXTERNAL_AXIOMS) String msg) {
        return loadAndAddAxioms(userId,
                                uriInfo,
                                inputStream, msg,
                                new FunctionalSyntaxDocumentFormat(),
                                FUNCTIONAL_SYNTAX.getMimeType());
    }

    private Response loadAndAddAxioms(@Nonnull UserId userId,
                                      @Nonnull UriInfo uriInfo,
                                      @Nonnull InputStream inputStream,
                                      @Nonnull String commitMessage,
                                      @Nonnull OWLDocumentFormat documentFormat,
                                      @Nonnull String mimeType) {
        return loadAxiomsAndExecuteAction(userId,
                                          uriInfo,
                                          inputStream,
                                          commitMessage,
                                          documentFormat,
                                          mimeType,
                                          (axioms, msg) -> new AddAxiomsAction(projectId, axioms, msg));
    }

    private Response loadAndDeleteAxioms(@Nonnull UserId userId,
                                         @Nonnull InputStream inputStream,
                                         @Nonnull String commitMessage,
                                         @Nonnull OWLDocumentFormat documentFormat,
                                         @Nonnull String mimeType) {
        return loadAxiomsAndExecuteAction(userId,
                                          null,
                                          inputStream,
                                          commitMessage,
                                          documentFormat,
                                          mimeType,
                                          (axioms, msg) -> new DeleteAxiomsAction(projectId, axioms, msg));
    }

    private Response loadAxiomsAndExecuteAction(@Nonnull UserId userId,
                                                @Nonnull UriInfo uriInfo,
                                                @Nonnull InputStream inputStream,
                                                @Nonnull String commitMessage,
                                                @Nonnull OWLDocumentFormat documentFormat,
                                                @Nonnull String mimeType,
                                                ActionFactory actionFactory) {
        if (!accessManager.hasPermission(Subject.forUser(userId),
                                         ProjectResource.forProject(projectId),
                                         BuiltInAction.EDIT_ONTOLOGY)) {
            return Response.status(FORBIDDEN)
                           .entity("You do not have permission to make changes to this project")
                           .build();
        }

        PostedAxiomsLoader axiomsLoader = new PostedAxiomsLoader(projectId,
                                                                 documentFormat,
                                                                 mimeType);
        PostedAxiomsLoadResponse loadResponse = axiomsLoader.loadAxioms(inputStream);
        if (loadResponse.isSuccess()) {
            Action<?> action = actionFactory.createAction(loadResponse.axioms(), commitMessage);
            Result result = executor.execute(action, userId);
            return Response.created(uriInfo.getAbsolutePath()).entity(result).build();
        }
        else {
            return loadResponse.toResponse();
        }
    }


    private interface ActionFactory {

        Action<?> createAction(Stream<OWLAxiom> axioms, String commitMessage);
    }
}

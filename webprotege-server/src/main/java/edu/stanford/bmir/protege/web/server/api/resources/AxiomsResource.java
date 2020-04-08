package edu.stanford.bmir.protege.web.server.api.resources;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.access.ProjectResource;
import edu.stanford.bmir.protege.web.server.access.Subject;
import edu.stanford.bmir.protege.web.server.api.ActionExecutor;
import edu.stanford.bmir.protege.web.server.api.axioms.PostedAxiomsActionExecutor;
import edu.stanford.bmir.protege.web.server.api.axioms.PostedAxiomsLoadResponse;
import edu.stanford.bmir.protege.web.server.api.axioms.PostedAxiomsLoader;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsAction;
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
    private final PostedAxiomsActionExecutor postedAxiomsActionExecutor;

    @Nonnull
    private final ProjectId projectId;

    @AutoFactory
    public AxiomsResource(@Provided @Nonnull PostedAxiomsActionExecutor postedAxiomsActionExecutor,
                          @Nonnull ProjectId projectId) {
        this.postedAxiomsActionExecutor = postedAxiomsActionExecutor;
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
                                                      @QueryParam("msg") @DefaultValue(
                                                              ADDED_EXTERNAL_AXIOMS) String msg) {
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
                                          (axioms, msg) -> new AddAxiomsAction(projectId,
                                                                               axioms,
                                                                               msg));
    }

    @Nonnull
    private Response loadAxiomsAndExecuteAction(@Nonnull UserId userId,
                                                @Nonnull UriInfo uriInfo,
                                                @Nonnull InputStream inputStream,
                                                @Nonnull String commitMessage,
                                                @Nonnull OWLDocumentFormat documentFormat,
                                                @Nonnull String mimeType,
                                                @Nonnull PostedAxiomsActionExecutor.ActionFactory actionFactory) {
        return postedAxiomsActionExecutor.loadAxiomsAndExecuteAction(projectId,
                                                                     userId,
                                                                     uriInfo,
                                                                     inputStream,
                                                                     commitMessage,
                                                                     documentFormat,
                                                                     mimeType,
                                                                     actionFactory,
                                                                     BuiltInAction.EDIT_ONTOLOGY.getActionId());
    }
}

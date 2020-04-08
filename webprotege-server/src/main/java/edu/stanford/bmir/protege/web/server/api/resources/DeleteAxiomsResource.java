package edu.stanford.bmir.protege.web.server.api.resources;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.api.axioms.PostedAxiomsActionExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.actions.DeleteAxiomsAction;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RioTurtleDocumentFormat;
import org.semanticweb.owlapi.model.OWLDocumentFormat;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;

import static edu.stanford.bmir.protege.web.server.download.DownloadFormat.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
public class DeleteAxiomsResource {

    private static final String DELETED_AXIOMS = "Deleted external axioms";

    @Nonnull
    private final PostedAxiomsActionExecutor postedAxiomsActionExecutor;

    @Nonnull
    private final ProjectId projectId;

    @AutoFactory
    public DeleteAxiomsResource(@Provided @Nonnull PostedAxiomsActionExecutor postedAxiomsActionExecutor,
                                @Nonnull ProjectId projectId) {
        this.postedAxiomsActionExecutor = postedAxiomsActionExecutor;
        this.projectId = projectId;
    }

    @POST
    @Consumes("text/owl-functional")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleDeleteAxiomsInFunctionalSyntax(@Context UserId userId,
                                                      @Context UriInfo uriInfo,
                                                      InputStream inputStream,
                                                      @QueryParam("msg") @DefaultValue(DELETED_AXIOMS) String msg) {
        return loadAndDeleteAxioms(userId,
                                   uriInfo,
                                   inputStream, msg,
                                   new FunctionalSyntaxDocumentFormat(),
                                   FUNCTIONAL_SYNTAX.getMimeType());
    }

    @POST
    @Consumes("application/rdf+xml")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleDeleteAxiomsInRdfXml(@Context UserId userId,
                                            @Context UriInfo uriInfo,
                                            InputStream inputStream,
                                            @QueryParam("msg") @DefaultValue(DELETED_AXIOMS) String msg) {
        return loadAndDeleteAxioms(userId,
                                   uriInfo,
                                   inputStream, msg,
                                   new RDFXMLDocumentFormat(),
                                   RDF_XML.getMimeType());
    }
    
    @POST
    @Consumes("text/ttl")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleDeleteAxiomsInTurtle(@Context UserId userId,
                                            @Context UriInfo uriInfo,
                                            InputStream inputStream,
                                            @QueryParam("msg") @DefaultValue(DELETED_AXIOMS) String msg) {
        return loadAndDeleteAxioms(userId,
                                   uriInfo,
                                   inputStream, msg,
                                   new RioTurtleDocumentFormat(),
                                   RDF_TURLE.getMimeType());
    }


    private Response loadAndDeleteAxioms(@Nonnull UserId userId,
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
                                          (axioms, msg) -> new DeleteAxiomsAction(projectId, axioms, msg));
    }

    private Response loadAxiomsAndExecuteAction(@Nonnull UserId userId,
                                                @Nonnull UriInfo uriInfo,
                                                @Nonnull InputStream inputStream,
                                                @Nonnull String commitMessage,
                                                @Nonnull OWLDocumentFormat documentFormat,
                                                @Nonnull String mimeType,
                                                PostedAxiomsActionExecutor.ActionFactory actionFactory) {
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

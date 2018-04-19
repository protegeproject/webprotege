package edu.stanford.bmir.protege.web.server.api;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.io.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class AxiomsResource {

    @Nonnull
    private final ActionExecutor executor;

    @Nonnull
    private final ProjectId projectId;

    @AutoFactory
    public AxiomsResource(@Provided @Nonnull ActionExecutor executor, @Nonnull ProjectId projectId) {
        this.executor = checkNotNull(executor);
        this.projectId = checkNotNull(projectId);
    }

    @POST
    @Path("/")
    public Response addAxioms(@Context UserId userId,
                              InputStream inputStream,
                              @QueryParam("msg") @DefaultValue("Added external axioms") String msg) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            OWLOntologyManager manager = WebProtegeOWLManager.createOWLOntologyManager();
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(bufferedInputStream);
            AddAxiomsAction action = new AddAxiomsAction(projectId,
                                                         ImmutableList.copyOf(ontology.getAxioms()),
                                                         msg);
            executor.execute(action, userId);
            return Response.ok().build();
        } catch (OWLOntologyCreationException e) {
            return Response.notModified().build();
        }
    }
}

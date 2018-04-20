package edu.stanford.bmir.protege.web.server.api.axioms;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.io.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Apr 2018
 *
 * Loads axioms posted to a URL.  An appropriate response is returned that indicates whether or not
 * the axioms were successfully loaded.  If posted axioms cannot be parsed then an HTTP 400 (Bad Request)
 * status code is returned along with information about the error.  If the axioms are parsed successfully
 * then an HTTP 200 (OK) status code is returned along with the number of parsed axioms.
 */
public class PostedAxiomsLoader {

    private static final String ONTOLOGY_IRI_TEMPLATE = "http://webprotege.stanford.edu/projects/%s/ontologies/temp";

    private static final Logger logger = LoggerFactory.getLogger(PostedAxiomsLoader.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OWLDocumentFormat documentFormat;

    @Nonnull
    private final String mimeType;

    @Inject
    public PostedAxiomsLoader(@Nonnull ProjectId projectId,
                              @Nonnull OWLDocumentFormat documentFormat,
                              @Nonnull String mimeType) {
        this.projectId = projectId;
        this.documentFormat = documentFormat;
        this.mimeType = mimeType;
    }

    public PostedAxiomsLoadResponse loadAxioms(@Nonnull InputStream inputStream) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.info("{} Processing posted axioms (format: {} mime-type: {})", projectId, documentFormat.getKey(), mimeType);
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            OWLOntologyManager manager = WebProtegeOWLManager.createOWLOntologyManager();
            IRI tempDocumentIri = IRI.create(String.format(ONTOLOGY_IRI_TEMPLATE,
                                                           projectId.getId()));
            OWLOntologyDocumentSource source = new StreamDocumentSource(bufferedInputStream,
                                                                        tempDocumentIri,
                                                                        documentFormat,
                                                                        mimeType);
            OWLOntologyLoaderConfiguration configuration = new OWLOntologyLoaderConfiguration();
            configuration = configuration.setReportStackTraces(false);
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(source, configuration);
            Set<OWLAxiom> axioms = ontology.getAxioms(Imports.INCLUDED);
            logger.info("{} Successfully parsed {} posted axioms", projectId, axioms.size());
            return new PostedAxiomsLoadSuccessResponse(axioms.size(), axioms.stream());
        } catch (OWLOntologyCreationException e) {
            logger.info("{} An error occurred whilst parsing posted axioms: {}", projectId, e.getMessage());
            return translateOntologyCreationExceptionToResponse(e);

        } catch (IOException e) {
            logger.info("{} An IO error occurred whilst parsing posted axioms: {}", projectId, e.getMessage());
            return translateIOExceptionToResponse(e);
        } finally {
            stopwatch.stop();
            logger.info("{} Finished processing posted axioms in {} ms", projectId, stopwatch.elapsed(MILLISECONDS));
        }
    }

    private PostedAxiomsLoadFailureResponse translateIOExceptionToResponse(IOException e) {
        return new PostedAxiomsLoadFailureResponse(Response.Status.INTERNAL_SERVER_ERROR,
                                                   "An IO exception has occurred", -1, -1);
    }

    private PostedAxiomsLoadFailureResponse translateOntologyCreationExceptionToResponse(OWLOntologyCreationException e) {
        if (e instanceof UnparsableOntologyException) {
            return translateUnparsableOntologyExceptionToResponse((UnparsableOntologyException) e);
        }
        else {
            return new PostedAxiomsLoadFailureResponse(Response.Status.BAD_REQUEST,
                                                       "An error occurred while parsing the posted axioms",
                                                       -1, -1);
        }
    }

    private PostedAxiomsLoadFailureResponse translateUnparsableOntologyExceptionToResponse(UnparsableOntologyException e) {
        Map<OWLParser, OWLParserException> exceptions = e.getExceptions();
        return exceptions.values().stream()
                         .map(ex -> new PostedAxiomsLoadFailureResponse(Response.Status.BAD_REQUEST,
                                                                        ex.getMessage(),
                                                                        ex.getLineNumber(),
                                                                        ex.getColumnNumber()))
                         .findFirst()
                         .orElseThrow(IllegalStateException::new);
    }
}


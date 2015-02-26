package edu.stanford.bmir.protege.web.server.merge;

import edu.stanford.bmir.protege.web.client.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.client.rpc.data.DocumentId;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.filesubmission.FileUploadConstants;
import edu.stanford.bmir.protege.web.server.owlapi.*;
import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.server.util.TempFileFactoryImpl;
import edu.stanford.bmir.protege.web.server.util.ZipInputStreamChecker;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectAction;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectResult;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class MergeUploadedProjectActionHandler extends AbstractHasProjectActionHandler<MergeUploadedProjectAction, MergeUploadedProjectResult> {

    @Override
    protected RequestValidator<MergeUploadedProjectAction> getAdditionalRequestValidator(MergeUploadedProjectAction action, RequestContext requestContext) {
        return UserHasProjectWritePermissionValidator.get();
    }

    @Override
    protected MergeUploadedProjectResult execute(MergeUploadedProjectAction action, OWLAPIProject project, ExecutionContext executionContext) {
        try {
            DocumentId documentId = action.getUploadedDocumentId();
            final OWLOntology uploadedRootOntology = loadUploadedOntology(documentId);
            final OWLOntology projectRootOntology = project.getRootOntology();
            if(isUploadedOntologyMergableIntoRootOntology(uploadedRootOntology, projectRootOntology)) {
                generateAndApplyChanges(action.getCommitMessage(), project, projectRootOntology, uploadedRootOntology, executionContext);
            }
        } catch (IOException | OWLOntologyCreationException e) {
            throw new ActionExecutionException(e);
        }
        return new MergeUploadedProjectResult();
    }

    private void generateAndApplyChanges(String commitMessage, OWLAPIProject project, final OWLOntology projectRootOntology, final OWLOntology uploadedRootOntology, ExecutionContext executionContext) {
        project.applyChanges(executionContext.getUserId(), new ChangeListGenerator<Void>() {
            @Override
            public OntologyChangeList<Void> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
                OntologyChangeList.Builder<Void> builder = OntologyChangeList.builder();

                for(OWLAxiom ax : uploadedRootOntology.getAxioms()) {
                    if(!projectRootOntology.containsAxiom(ax)) {
                        builder.addAxiom(projectRootOntology, ax);
                    }
                }
                for(OWLAxiom ax : projectRootOntology.getAxioms()) {
                    if(!uploadedRootOntology.containsAxiom(ax)) {
                        builder.removeAxiom(projectRootOntology, ax);
                    }
                }
                return builder.build();
            }

            @Override
            public Void getRenamedResult(Void result, RenameMap renameMap) {
                return null;
            }
        }, new FixedMessageChangeDescriptionGenerator<Void>(commitMessage + " [Merged from external edit]"));
    }

    private boolean isUploadedOntologyMergableIntoRootOntology(OWLOntology uploadedRootOntology, OWLOntology projectRootOntology) {
        return projectRootOntology.getOntologyID().equals(uploadedRootOntology.getOntologyID());
    }

    @Override
    public Class<MergeUploadedProjectAction> getActionClass() {
        return MergeUploadedProjectAction.class;
    }

    private OWLOntology loadUploadedOntology(DocumentId documentId) throws IOException, OWLOntologyCreationException {
        // Extract sources
        UploadedProjectSourcesExtractor extractor = new UploadedProjectSourcesExtractor(
                new ZipInputStreamChecker(),
                new ZipArchiveProjectSourcesExtractor(
                        new TempFileFactoryImpl(),
                        new RootOntologyDocumentMatcherImpl()),
                new SingleDocumentProjectSourcesExtractor());
        // Load sources
        OWLOntologyManager rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
        final File file = new File(FileUploadConstants.UPLOADS_DIRECTORY, documentId.getDocumentId());
        RawProjectSources rawProjectSources = extractor.extractProjectSources(file);
        OWLOntologyLoaderConfiguration loaderConfig = new OWLOntologyLoaderConfiguration();
        RawProjectSourcesImporter importer = new RawProjectSourcesImporter(rootOntologyManager, loaderConfig);
        return importer.importRawProjectSources(rawProjectSources);
    }

}

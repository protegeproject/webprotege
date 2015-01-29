package edu.stanford.bmir.protege.web.server.merge;

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
import edu.stanford.bmir.protege.web.server.util.DefaultTempFileFactory;
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
        DocumentId documentId = action.getUploadedDocumentId();
        final File file = new File(FileUploadConstants.UPLOADS_DIRECTORY, documentId.getDocumentId());

        UploadedProjectSourcesExtractor extractor = new UploadedProjectSourcesExtractor(
                new ZipInputStreamChecker(),
                new ZipArchiveProjectSourcesExtractor(
                        new DefaultTempFileFactory(),
                        new DefaultRootOntologyDocumentMatcher()),
                new SingleDocumentProjectSourcesExtractor());

        try {
            OWLOntologyManager rootOntologyManager = WebProtegeOWLManager.createOWLOntologyManager();
            RawProjectSources rawProjectSources = extractor.extractProjectSources(file);
            OWLOntologyLoaderConfiguration loaderConfig = new OWLOntologyLoaderConfiguration();
            RawProjectSourcesImporter importer = new RawProjectSourcesImporter(rootOntologyManager, loaderConfig);
            final OWLOntology uploadedRootOntology = importer.importRawProjectSources(rawProjectSources);
            System.out.println("Loaded uploadedRootOntology");
            OWLOntology projectRootOntology = project.getRootOntology();
            boolean rootIsEqual = projectRootOntology.getOntologyID().equals(uploadedRootOntology.getOntologyID());
            System.out.println("Root is equal: " + rootIsEqual);
            if(rootIsEqual) {
                project.applyChanges(executionContext.getUserId(), new ChangeListGenerator<Void>() {
                    @Override
                    public OntologyChangeList<Void> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
                        OWLOntology projectRootOntology = project.getRootOntology();
                        OntologyChangeList.Builder<Void> builder = OntologyChangeList.builder();

                        for(OWLAxiom ax : uploadedRootOntology.getAxioms()) {
                            if(!projectRootOntology.containsAxiom(ax)) {
                                System.out.println("+    " + ax);
                                builder.addAxiom(projectRootOntology, ax);
                            }
                        }
                        for(OWLAxiom ax : projectRootOntology.getAxioms()) {
                            if(!uploadedRootOntology.containsAxiom(ax)) {
                                System.out.println("-    " + ax);
                                builder.removeAxiom(projectRootOntology, ax);
                            }
                        }
                        return builder.build();
                    }

                    @Override
                    public Void getRenamedResult(Void result, RenameMap renameMap) {
                        return null;
                    }
                }, new FixedMessageChangeDescriptionGenerator<Void>("Merged from external edit"));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Class<MergeUploadedProjectAction> getActionClass() {
        return MergeUploadedProjectAction.class;
    }
}

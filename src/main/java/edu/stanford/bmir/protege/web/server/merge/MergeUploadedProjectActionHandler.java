package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.client.rpc.data.DocumentId;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.diff.OntologyDiff2OntologyChanges;
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
import edu.stanford.bmir.protege.web.shared.merge.Diff;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectAction;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectResult;
import edu.stanford.bmir.protege.web.shared.merge.OntologyDiff;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            ModifiedProjectOntologiesCalculator calculator = new ModifiedProjectOntologiesCalculator(
                    ImmutableSet.copyOf(projectRootOntology.getImportsClosure()),
                    ImmutableSet.copyOf(uploadedRootOntology.getImportsClosure()),
                    new OntologyDiffCalculator(new AnnotationDiffCalculator(), new AxiomDiffCalculator()));

            Set<OntologyDiff> ontologyDiffSet = calculator.getModifiedOntologyDiffs();
            List<OWLOntologyChange> changeList = new ArrayList<>();
            HasGetOntologyById man = project.getRootOntology().getOWLOntologyManager();
            for(OntologyDiff diff : ontologyDiffSet) {
                OntologyDiff2OntologyChanges diff2Changes = new OntologyDiff2OntologyChanges();
                List<OWLOntologyChange> changes = diff2Changes.getOntologyChangesFromDiff(diff, man);
                changeList.addAll(changes);
            }
            applyChanges(action.getCommitMessage(), project, changeList, executionContext);
        } catch (IOException | OWLOntologyCreationException e) {
            throw new ActionExecutionException(e);
        }
        return new MergeUploadedProjectResult();
    }

    private void applyChanges(String commitMessage, OWLAPIProject project, final List<OWLOntologyChange> changes, ExecutionContext executionContext) {
        project.applyChanges(executionContext.getUserId(), new ChangeListGenerator<Void>() {
            @Override
            public OntologyChangeList<Void> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
                OntologyChangeList.Builder<Void> builder = OntologyChangeList.builder();
                builder.addAll(changes);
                return builder.build();
            }

            @Override
            public Void getRenamedResult(Void result, RenameMap renameMap) {
                return null;
            }
        }, new FixedMessageChangeDescriptionGenerator<Void>(commitMessage + " [Merged from external edit]"));
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

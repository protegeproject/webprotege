package edu.stanford.bmir.protege.web.server.merge;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.upload.UploadedOntologiesCache;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectAction;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectResult;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.UPLOAD_AND_MERGE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class MergeUploadedProjectActionHandler extends AbstractProjectActionHandler<MergeUploadedProjectAction, MergeUploadedProjectResult> {

    @Nonnull
    private final OntologyPatcher ontologyPatcher;

    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;

    @Nonnull
    private final UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final ModifiedProjectOntologiesCalculatorFactory diffCalculatorFactory;

    @Inject
    public MergeUploadedProjectActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull OntologyPatcher ontologyPatcher,
                                             @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder,
                                             @Nonnull UploadedOntologiesCache uploadedOntologiesCache,
                                             @Nonnull ModifiedProjectOntologiesCalculatorFactory modifiedProjectOntologiesCalculatorFactory) {
        super(accessManager);
        this.ontologyPatcher = ontologyPatcher;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.diffCalculatorFactory = modifiedProjectOntologiesCalculatorFactory;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(MergeUploadedProjectAction action) {
        return Arrays.asList(UPLOAD_AND_MERGE, EDIT_ONTOLOGY);
    }

    @Nonnull
    @Override
    public MergeUploadedProjectResult execute(@Nonnull MergeUploadedProjectAction action,
                                              @Nonnull ExecutionContext executionContext) {
        try {
            var documentId = action.getUploadedDocumentId();
            var uploadedOntologies = uploadedOntologiesCache.getUploadedOntologies(documentId);
            var projectOntologies = projectOntologiesBuilder.buildProjectOntologies();
            var diffCalculator = diffCalculatorFactory.create(projectOntologies, uploadedOntologies);
            var ontologyDiffSet = diffCalculator.getModifiedOntologyDiffs();
            var commitMessage = action.getCommitMessage();
            ontologyPatcher.applyPatch(ontologyDiffSet, commitMessage, executionContext);

        } catch(IOException | OWLOntologyCreationException e) {
            throw new ActionExecutionException(e);
        }
        return new MergeUploadedProjectResult();
    }

    @Nonnull
    @Override
    public Class<MergeUploadedProjectAction> getActionClass() {
        return MergeUploadedProjectAction.class;
    }

}

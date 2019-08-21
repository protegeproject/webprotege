package edu.stanford.bmir.protege.web.server.merge;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.upload.UploadedOntologiesProcessor;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectAction;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectResult;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;

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
    private final UploadedOntologiesProcessor uploadedOntologiesProcessor;

    @Nonnull
    private final ModifiedProjectOntologiesCalculatorFactory diffCalculatorFactory;

    @Inject
    public MergeUploadedProjectActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull OntologyPatcher ontologyPatcher,
                                             @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder,
                                             @Nonnull UploadedOntologiesProcessor uploadedOntologiesProcessor,
                                             @Nonnull ModifiedProjectOntologiesCalculatorFactory modifiedProjectOntologiesCalculatorFactory) {
        super(accessManager);
        this.ontologyPatcher = ontologyPatcher;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
        this.uploadedOntologiesProcessor = uploadedOntologiesProcessor;
        this.diffCalculatorFactory = modifiedProjectOntologiesCalculatorFactory;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(UPLOAD_AND_MERGE, EDIT_ONTOLOGY);
    }

    @Nonnull
    @Override
    public MergeUploadedProjectResult execute(@Nonnull MergeUploadedProjectAction action,
                                              @Nonnull ExecutionContext executionContext) {
        try {
            var documentId = action.getUploadedDocumentId();
            var uploadedOntologies = uploadedOntologiesProcessor.getUploadedOntologies(documentId);
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

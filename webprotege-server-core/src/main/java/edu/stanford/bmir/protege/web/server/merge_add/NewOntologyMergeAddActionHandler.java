package edu.stanford.bmir.protege.web.server.merge_add;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.merge.ProjectOntologiesBuilder;
import edu.stanford.bmir.protege.web.server.upload.UploadedOntologiesCache;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.merge_add.NewOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.shared.merge_add.NewOntologyMergeAddResult;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;

public class NewOntologyMergeAddActionHandler extends AbstractProjectActionHandler<NewOntologyMergeAddAction, NewOntologyMergeAddResult> {

    private static final Logger logger = LoggerFactory.getLogger(NewOntologyMergeAddActionHandler.class);

    @Nonnull
    private final UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;

    @Nonnull
    private final MergeOntologyCalculator mergeCalculator;

    @Nonnull
    private final OntologyMergeAddPatcher patcher;

    @Inject
    public NewOntologyMergeAddActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull UploadedOntologiesCache uploadedOntologiesCache,
                                            @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder,
                                            @Nonnull HasApplyChanges changeManager) {
        super(accessManager);
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
        this.mergeCalculator = new MergeOntologyCalculator();
        this.patcher = new OntologyMergeAddPatcher(changeManager);
    }

    @Nonnull
    @Override
    public NewOntologyMergeAddResult execute(@Nonnull NewOntologyMergeAddAction action,
                                             @Nonnull ExecutionContext executionContext){
        try{
            var documentId = action.getDocumentId();

            var uploadedOntologies = uploadedOntologiesCache.getUploadedOntologies(documentId);
            var projectOntologies = projectOntologiesBuilder.buildProjectOntologies();

            var ontologyList = action.getOntologyList();

            var axioms = mergeCalculator.getMergeAxioms(projectOntologies, uploadedOntologies, ontologyList);
            var annotations = mergeCalculator.getMergeAnnotations(projectOntologies, uploadedOntologies, ontologyList);

            OWLOntologyID newOntologyID = new OWLOntologyID();

            List<OntologyChange> changes = patcher.addAxiomsAndAnnotations(axioms, annotations, newOntologyID);

            patcher.applyChanges(changes, executionContext);

            return new NewOntologyMergeAddResult();
        }
        catch (Exception e){
            logger.info("An error occurred while merging(adding axioms) ontologies", e);
            throw new RuntimeException(e);
        }
    }


    @Nonnull
    @Override
    public Class<NewOntologyMergeAddAction> getActionClass(){
        return NewOntologyMergeAddAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(NewOntologyMergeAddAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE_ADDITIONS);
    }
}

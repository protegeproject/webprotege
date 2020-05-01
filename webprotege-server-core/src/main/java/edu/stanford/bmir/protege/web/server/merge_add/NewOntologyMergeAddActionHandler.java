package edu.stanford.bmir.protege.web.server.merge_add;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.merge.ProjectOntologiesBuilder;
import edu.stanford.bmir.protege.web.server.owlapi.WebProtegeOWLManager;
import edu.stanford.bmir.protege.web.server.project.chg.ProjectOWLOntologyManager;
import edu.stanford.bmir.protege.web.server.upload.UploadedOntologiesCache;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.merge_add.NewOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.shared.merge_add.NewOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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
    private final ProjectId projectId;

    @Nonnull
    private final UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final ProjectOntologiesBuilder projectOntologiesBuilder;

    @Nonnull
    private final ProjectOWLOntologyManager ontologyManager;

    private MergeOntologyCalculator mergeCalculator;

    private OntologyMergeAddPatcher patcher;

    @Inject
    public NewOntologyMergeAddActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull UploadedOntologiesCache uploadedOntologiesCache,
                                            @Nonnull ProjectOntologiesBuilder projectOntologiesBuilder,
                                            @Nonnull ProjectOWLOntologyManager ontologyManager,
                                            @Nonnull HasApplyChanges changeManager) {
        super(accessManager);
        this.projectId = projectId;
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.projectOntologiesBuilder = projectOntologiesBuilder;
        this.ontologyManager = ontologyManager;
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

            IRI iri = IRI.create(action.getIri());

            OWLOntologyManager delegateManager = WebProtegeOWLManager.createConcurrentOWLOntologyManager();

            ontologyManager.setDelegate(delegateManager);

            OWLOntology newOntology = ontologyManager.createOntology(iri);

            List<OntologyChange> changes = patcher.addAxiomsAndAnnotations(axioms, annotations, newOntology.getOntologyID());

            patcher.applyChanges(changes, executionContext);

            ontologyManager.sealDelegate();

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
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE_ADDITIONS);
    }
}

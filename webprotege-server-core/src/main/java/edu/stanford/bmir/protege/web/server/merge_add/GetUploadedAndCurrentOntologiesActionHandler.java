package edu.stanford.bmir.protege.web.server.merge_add;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.index.OntologyIdIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.Ontology;
import edu.stanford.bmir.protege.web.server.upload.UploadedOntologiesCache;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.merge_add.GetUploadedAndCurrentOntologiesAction;
import edu.stanford.bmir.protege.web.shared.merge_add.GetUploadedAndCurrentOntologiesResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;

import static dagger.internal.codegen.DaggerStreams.toImmutableList;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.UPLOAD_AND_MERGE_ADDITIONS;

public class GetUploadedAndCurrentOntologiesActionHandler extends AbstractProjectActionHandler<GetUploadedAndCurrentOntologiesAction, GetUploadedAndCurrentOntologiesResult> {

    private static final Logger logger = LoggerFactory.getLogger(NewOntologyMergeAddActionHandler.class);

    @Nonnull
    private final UploadedOntologiesCache uploadedOntologiesCache;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public GetUploadedAndCurrentOntologiesActionHandler(@Nonnull AccessManager accessManager,
                                                        @Nonnull UploadedOntologiesCache uploadedOntologiesCache,
                                                        @Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        super(accessManager);
        this.uploadedOntologiesCache = uploadedOntologiesCache;
        this.projectOntologiesIndex = projectOntologiesIndex;
    }

    @Nonnull
    @Override
    public GetUploadedAndCurrentOntologiesResult execute(@Nonnull GetUploadedAndCurrentOntologiesAction action,
                                                         @Nonnull ExecutionContext executionContext){
        try{
            var documentId = action.getDocumentId();

            var uploadedOntologies = uploadedOntologiesCache.getUploadedOntologies(documentId)
                    .stream()
                    .map(Ontology::getOntologyID)
                    .collect(toImmutableList());

            var projectOntologies = projectOntologiesIndex.getOntologyDocumentIds()
                    .collect(toImmutableList());

            return new GetUploadedAndCurrentOntologiesResult(uploadedOntologies, projectOntologies);
        }
        catch (Exception e){
            logger.info("An error occurred while merging(adding axioms) ontologies", e);
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public Class<GetUploadedAndCurrentOntologiesAction> getActionClass(){
        return GetUploadedAndCurrentOntologiesAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(GetUploadedAndCurrentOntologiesAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, UPLOAD_AND_MERGE_ADDITIONS);
    }
}

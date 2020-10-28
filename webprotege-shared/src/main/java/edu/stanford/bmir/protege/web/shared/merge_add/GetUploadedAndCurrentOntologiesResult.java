package edu.stanford.bmir.protege.web.shared.merge_add;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetUploadedAndCurrentOntologiesResult implements Result {

    private ImmutableList<OWLOntologyID> uploadedOntologyIds;

    private ImmutableList<OntologyDocumentId> currentOntologyDocumentIds;


    @GwtSerializationConstructor
    private GetUploadedAndCurrentOntologiesResult() {
    }

    public GetUploadedAndCurrentOntologiesResult(@Nonnull ImmutableList<OWLOntologyID> uploadedOntologyIds,
                                                 @Nonnull ImmutableList<OntologyDocumentId> currentOntologyDocumentIds) {
        this.uploadedOntologyIds = checkNotNull(uploadedOntologyIds);
        this.currentOntologyDocumentIds = checkNotNull(currentOntologyDocumentIds);
    }

    @Nonnull
    public ImmutableList<OWLOntologyID> getUploadedOntologies() {
        return uploadedOntologyIds;
    }

    @Nonnull
    public ImmutableList<OntologyDocumentId> getCurrentOntologyDocumentIds() {
        return currentOntologyDocumentIds;
    }
}

package edu.stanford.bmir.protege.web.shared.individuals;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsResult implements Result {


    @SuppressWarnings("GwtInconsistentSerializableClass" )
    @Nullable
    private OWLClassData type;

    private Page<EntityNode> result;

    private long totalIndividuals;

    @GwtSerializationConstructor
    private GetIndividualsResult() {
    }

    public GetIndividualsResult(Optional<OWLClassData> type,
                                Page<EntityNode> result, long totalIndividuals) {
        this.type = type.orElse(null);
        this.result = result;
        this.totalIndividuals = totalIndividuals;
    }

    public Optional<OWLClassData> getType() {
        return Optional.ofNullable(type);
    }

    public Page<EntityNode> getPaginatedResult() {
        return result;
    }

    public long getTotalIndividuals() {
        return totalIndividuals;
    }

    public List<EntityNode> getIndividuals() {
        return result.getPageElements();
    }
}

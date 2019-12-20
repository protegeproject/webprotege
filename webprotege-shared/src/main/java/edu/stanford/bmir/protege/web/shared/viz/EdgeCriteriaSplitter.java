package edu.stanford.bmir.protege.web.shared.viz;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
public class EdgeCriteriaSplitter {


    @Nonnull
    public Optional<SplitEdgeCriteria> splitCriteria(@Nonnull CompositeEdgeCriteria edgeCriteria) {
        List<EdgeCriteria> criteriaList = edgeCriteria.getCriteria();
        if(criteriaList.size() != 2) {
            return Optional.empty();
        }
        EdgeCriteria first = criteriaList.get(0);
        EdgeCriteria second = criteriaList.get(1);
        if(first instanceof NegatedEdgeCriteria) {
            if(second instanceof NegatedEdgeCriteria) {
                // Both negated Malformed
                return Optional.empty();
            }
            else {
                // First negated, second not negated
                return getSplitCriteria(second, (NegatedEdgeCriteria) first);
            }
        }
        else {
            if(second instanceof NegatedEdgeCriteria) {
                // First positive, second negated
                return getSplitCriteria(first, (NegatedEdgeCriteria) second);
            }
            else {
                // Both positive
                return Optional.empty();
            }
        }
        // Not the right shape
    }

    private Optional<SplitEdgeCriteria> getSplitCriteria(EdgeCriteria positive, NegatedEdgeCriteria negative) {
        if(!(positive instanceof CompositeEdgeCriteria)) {
            return Optional.empty();
        }
        if(!(negative.getNegatedCriteria() instanceof CompositeEdgeCriteria)) {
            return Optional.empty();
        }
        CompositeEdgeCriteria includeCriteria = (CompositeEdgeCriteria) positive;
        CompositeEdgeCriteria excludeCriteria = (CompositeEdgeCriteria) negative.getNegatedCriteria();
        return Optional.of(SplitEdgeCriteria.get(
                includeCriteria,
                excludeCriteria
        ));
    }

}

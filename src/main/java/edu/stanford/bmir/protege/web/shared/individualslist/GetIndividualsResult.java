package edu.stanford.bmir.protege.web.shared.individualslist;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsResult implements Result {


    @SuppressWarnings("GwtInconsistentSerializableClass" )
    private OWLClassData type;

    private Page<OWLNamedIndividualData> result;

    private int totalIndividuals;

    private int matchedIndividuals;

    @GwtSerializationConstructor
    private GetIndividualsResult() {
    }

    public GetIndividualsResult(OWLClassData type,
                                Page<OWLNamedIndividualData> result, int totalIndividuals, int matchedIndividuals) {
        this.type = type;
        this.result = result;
        this.totalIndividuals = totalIndividuals;
        this.matchedIndividuals = matchedIndividuals;
    }

    public OWLClassData getType() {
        return type;
    }

    public Page<OWLNamedIndividualData> getPaginatedResult() {
        return result;
    }

    public int getTotalIndividuals() {
        return totalIndividuals;
    }

    public int getMatchedIndividuals() {
        return matchedIndividuals;
    }

    public List<OWLNamedIndividualData> getIndividuals() {
        return result.getPageElements();
    }
}

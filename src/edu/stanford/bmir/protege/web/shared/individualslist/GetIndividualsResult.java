package edu.stanford.bmir.protege.web.shared.individualslist;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
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

    private Page<OWLNamedIndividualData> result;

    /**
     * For serialization purposes only
     */
    private GetIndividualsResult() {
    }

    public GetIndividualsResult(Page<OWLNamedIndividualData> result) {
        this.result = result;
    }

    public Page<OWLNamedIndividualData> getPaginatedResult() {
        return result;
    }

    public List<OWLNamedIndividualData> getIndividuals() {
        return result.getPageElements();
    }
}

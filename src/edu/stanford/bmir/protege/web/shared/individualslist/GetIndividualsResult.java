package edu.stanford.bmir.protege.web.shared.individualslist;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsResult implements Result {

    private List<OWLNamedIndividualData> individuals = new ArrayList<OWLNamedIndividualData>();

    /**
     * For serialization purposes only
     */
    private GetIndividualsResult() {
    }

    public GetIndividualsResult(List<OWLNamedIndividualData> individuals) {
        this.individuals = individuals;
    }

    public List<OWLNamedIndividualData> getIndividuals() {
        return new ArrayList<OWLNamedIndividualData>(individuals);
    }
}

package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsResult implements Result {


    private Set<OWLNamedIndividualData> individuals = new HashSet<OWLNamedIndividualData>();

    private CreateNamedIndividualsResult() {
    }

    public CreateNamedIndividualsResult(Set<OWLNamedIndividualData> individuals) {
        this.individuals = individuals;
    }

    public Set<OWLNamedIndividualData> getIndividuals() {
        return new HashSet<OWLNamedIndividualData>(individuals);
    }
}

package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
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


    private Set<EntityNode> individuals = new HashSet<>();

    private CreateNamedIndividualsResult() {
    }

    public CreateNamedIndividualsResult(Set<EntityNode> individuals) {
        this.individuals = individuals;
    }

    public Set<EntityNode> getIndividuals() {
        return new HashSet<>(individuals);
    }
}

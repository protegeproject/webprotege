package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Sep 2018
 */
public class IndividualsQueryResult {

    private final Page<OWLNamedIndividual> queryResultPage;

    private final long individualsCount;

    public IndividualsQueryResult(Page<OWLNamedIndividual> queryResultPage, long individualsCount) {
        this.queryResultPage = checkNotNull(queryResultPage);
        this.individualsCount = individualsCount;
    }

    public Page<OWLNamedIndividual> getIndividuals() {
        return queryResultPage;
    }

    public long getIndividualsCount() {
        return individualsCount;
    }
}

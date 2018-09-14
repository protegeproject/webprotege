package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Sep 2018
 */
public interface IndividualsIndex {

    @Nonnull
    IndividualsQueryResult getIndividuals(@Nonnull String searchString,
                                          @Nonnull PageRequest pageRequest);

    @Nonnull
    IndividualsQueryResult getIndividuals(@Nonnull OWLClass byType,
                                          @Nonnull InstanceRetrievalMode mode,
                                          @Nonnull String searchString,
                                          @Nonnull PageRequest pageRequest);
}

package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Sep 2018
 */
@ProjectSingleton
public interface IndividualsIndex {

    /**
     * Get individuals that have a display name that matches the specified search string
     * @param searchString The search string.  Spaces separate multiple words.
     * @param pageRequest A page request that determines the individuals to be retrieved.
     */
    @Nonnull
    IndividualsQueryResult getIndividuals(@Nonnull String searchString,
                                          @Nonnull PageRequest pageRequest);

    /**
     * Get individuals that are either directly or indirect an instance of the specified type.  The individuals
     * will be filtered by the specified search string so that only individuals whose display name matches the
     * specified string will be returned.
     * @param searchString The search string.  Spaces separate multiple words.
     * @param pageRequest A page request that determines the individuals to be retrieved.
     * @param type The type for the individuals to be retrieved.
     * @param mode The mode of retrieval.
     */
    @Nonnull
    IndividualsQueryResult getIndividuals(@Nonnull OWLClass type,
                                          @Nonnull InstanceRetrievalMode mode,
                                          @Nonnull String searchString,
                                          @Nonnull PageRequest pageRequest);
}

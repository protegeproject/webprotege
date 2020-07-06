package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Sep 2018
 */
@ProjectSingleton
public interface IndividualsIndex extends Index {

    /**
     * Get individuals that have a display name that matches the specified search string
     *
     * @param searchString The search string.  Spaces separate multiple words.
     * @param pageRequest  A page request that determines the individuals to be retrieved.
     */
    @Nonnull
    IndividualsQueryResult getIndividuals(@Nonnull String searchString,
                                          @Nonnull PageRequest pageRequest);

    /**
     * Get individuals that are either directly or indirect an instance of the specified type.  The individuals
     * will be filtered by the specified search string so that only individuals whose display name matches the
     * specified string will be returned.
     *
     * @param searchString The search string.  Spaces separate multiple words.
     * @param pageRequest  A page request that determines the individuals to be retrieved.
     * @param type         The type for the individuals to be retrieved.
     * @param mode         The mode of retrieval.
     */
    @Nonnull
    IndividualsQueryResult getIndividuals(@Nonnull OWLClass type,
                                          @Nonnull InstanceRetrievalMode mode,
                                          @Nonnull String searchString,
                                          @Nonnull PageRequest pageRequest);

    /**
     * Gets a page containing the specified individual if the individual exists in the signature of the set of
     * project ontologies.  The page will first and foremost contain the specified individual.  If possible, the
     * set of pages that the page is from will be pages for the specified preferred type.  In addition to this,
     * if possible, the pages will contain individuals that are direct or indirect individuals as specified by
     * the preferred mode.
     * @param individual The individual
     * @param preferredType The preferred type of individuals in the returned pages
     * @param preferredMode The preferred type of instance retrieval mode
     * @param pageSize The required page size
     */
    @Nonnull
    IndividualsQueryResult getIndividualsPageContaining(@Nonnull OWLNamedIndividual individual,
                                                          @Nonnull Optional<OWLClass> preferredType,
                                                          @Nonnull InstanceRetrievalMode preferredMode,
                                                          int pageSize);


    @Nonnull
    Stream<OWLClass> getTypes(@Nonnull OWLNamedIndividual individual);
}

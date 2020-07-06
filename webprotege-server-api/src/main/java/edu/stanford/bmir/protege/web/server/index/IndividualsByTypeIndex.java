package edu.stanford.bmir.protege.web.server.index;


import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
@ProjectSingleton
public interface IndividualsByTypeIndex extends Index {

    /**
     * Retrieve individuals that have the specified direct or indirect asserted type
     * @param type The type
     * @param retrievalMode specifying direct or indirect type retrieval
     */
    @Nonnull
    Stream<OWLNamedIndividual> getIndividualsByType(@Nonnull OWLClass type,
                                                    @Nonnull InstanceRetrievalMode retrievalMode);
}

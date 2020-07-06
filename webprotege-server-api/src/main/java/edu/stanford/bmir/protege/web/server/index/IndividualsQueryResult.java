package edu.stanford.bmir.protege.web.server.index;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class IndividualsQueryResult {

    public static IndividualsQueryResult get(Page<OWLNamedIndividual> queryResultPage,
                                             long individualsCount,
                                             OWLClass type,
                                             InstanceRetrievalMode mode) {
        return new AutoValue_IndividualsQueryResult(queryResultPage,
                                                    individualsCount,
                                                    type,
                                                    mode);
    }

    @Nonnull
    public abstract Page<OWLNamedIndividual> getIndividuals();

    public abstract long getIndividualsCount();

    @Nonnull
    public abstract OWLClass getType();

    @Nonnull
    public abstract InstanceRetrievalMode getMode();
}

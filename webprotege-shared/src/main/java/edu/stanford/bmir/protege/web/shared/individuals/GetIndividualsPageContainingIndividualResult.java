package edu.stanford.bmir.protege.web.shared.individuals;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetIndividualsPageContainingIndividualResult implements Result {

    public static GetIndividualsPageContainingIndividualResult get(@Nonnull OWLNamedIndividual individual,
                                                                   @Nonnull Page<EntityNode> page,
                                                                   @Nonnull EntityNode actualType,
                                                                   @Nonnull InstanceRetrievalMode actualMode,
                                                                   @Nonnull ImmutableSet<EntityNode> types) {
        return new AutoValue_GetIndividualsPageContainingIndividualResult(individual,
                                                                          actualType,
                                                                          actualMode,
                                                                          page,
                                                                          types);
    }

    @Nonnull
    public abstract OWLNamedIndividual getIndividual();

    @Nonnull
    public abstract EntityNode getActualType();

    @Nonnull
    public abstract InstanceRetrievalMode getActualMode();

    @Nonnull
    public abstract Page<EntityNode> getPage();

    @Nonnull
    public abstract ImmutableSet<EntityNode> getTypes();
}

package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SubClassOf")
public abstract class SubClassOfCriteria implements EntityMatchCriteria {

    @Nonnull
    public abstract OWLClass getTarget();

    public static SubClassOfCriteria get(@Nonnull OWLClass target) {
        return new AutoValue_SubClassOfCriteria(target);
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

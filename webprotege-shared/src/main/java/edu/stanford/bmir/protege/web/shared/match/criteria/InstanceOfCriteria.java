package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("InstanceOf")
public abstract class InstanceOfCriteria implements EntityMatchCriteria, HierarchyPositionCriteria {

    private static final String TARGET = "target";

    private static final String FILTER_TYPE = "filterType";

    @JsonProperty(TARGET)
    @Nonnull
    public abstract OWLClass getTarget();

    @JsonProperty(FILTER_TYPE)
    public abstract HierarchyFilterType getFilterType();

    @JsonCreator
    @Nonnull
    public static InstanceOfCriteria get(@Nonnull @JsonProperty(TARGET) OWLClass target,
                                         @Nonnull @JsonProperty(FILTER_TYPE) HierarchyFilterType filterType) {
        return new AutoValue_InstanceOfCriteria(target, filterType);
    }


    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull HierarchyPositionCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

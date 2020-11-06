package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-06
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("NotSubClassOf")
public abstract class NotSubClassOfCriteria implements EntityMatchCriteria, HierarchyPositionCriteria {

    private static final String TARGET = "target";

    private static final String FILTER_TYPE = "filterType";

    @JsonProperty(TARGET)
    @Nonnull
    public abstract OWLClass getTarget();

    @JsonProperty(FILTER_TYPE)
    public abstract HierarchyFilterType getFilterType();

    @JsonCreator
    @Nonnull
    public static NotSubClassOfCriteria get(@Nonnull @JsonProperty(TARGET) OWLClass target,
                                            @Nonnull @JsonProperty(FILTER_TYPE) HierarchyFilterType filterType) {
        return new AutoValue_NotSubClassOfCriteria(target, filterType);
    }

    @Override
    public <R> R accept(@Nonnull HierarchyPositionCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public CompositeRootCriteria asCompositeRootCriteria() {
        return CompositeRootCriteria.get(ImmutableList.of(this), MultiMatchType.ALL);
    }
}

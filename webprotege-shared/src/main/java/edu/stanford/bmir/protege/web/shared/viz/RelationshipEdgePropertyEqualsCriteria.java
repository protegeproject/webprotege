package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-05
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("RelationshipEdgePropertyEquals")
public abstract class RelationshipEdgePropertyEqualsCriteria implements EdgeCriteria {

    @JsonCreator
    public static RelationshipEdgePropertyEqualsCriteria get(@Nonnull @JsonProperty("property") OWLProperty property) {
        return new AutoValue_RelationshipEdgePropertyEqualsCriteria(property);
    }

    @Override
    public <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    public abstract OWLProperty getProperty();

    @Nonnull
    @Override
    public EdgeCriteria simplify() {
        return this;
    }
}

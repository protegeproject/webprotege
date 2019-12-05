package edu.stanford.bmir.protege.web.shared.match.criteria;

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
 * 2019-12-02
 */
@AutoValue
@JsonTypeName("PropertyEquals")
@GwtCompatible(serializable = true)
public abstract class RelationshipPropertyEqualsCriteria implements RelationshipPropertyCriteria {

    private static final String PROPERTY = "property";

    @JsonCreator
    @Nonnull
    public static RelationshipPropertyEqualsCriteria get(@Nonnull @JsonProperty(PROPERTY) OWLProperty property) {
        return new AutoValue_RelationshipPropertyEqualsCriteria(property);
    }

    @Override
    public <R> R accept(@Nonnull RelationshipPropertyCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @JsonProperty(PROPERTY)
    @Nonnull
    public abstract OWLProperty getProperty();
}

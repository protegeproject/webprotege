package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EntityHasConflictingBooleanAnnotationValues")
public abstract class EntityHasConflictingBooleanAnnotationValuesCriteria implements EntityMatchCriteria {

    @JsonProperty("property")
    @Nonnull
    public abstract AnnotationPropertyCriteria getPropertyCriteria();

    @JsonCreator
    @Nonnull
    public static EntityHasConflictingBooleanAnnotationValuesCriteria get(@Nonnull @JsonProperty("property") AnnotationPropertyCriteria propertyCriteria) {
        return new AutoValue_EntityHasConflictingBooleanAnnotationValuesCriteria(propertyCriteria);
    }

    @Override
    public <R> R accept(RootCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

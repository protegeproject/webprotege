package edu.stanford.bmir.protege.web.shared.viz;

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
public abstract class RelationshipEdgeWithPropertyCriteria implements EdgeCriteria {

    public static RelationshipEdgeWithPropertyCriteria get(@Nonnull OWLProperty property) {
        return new AutoValue_RelationshipEdgeWithPropertyCriteria(property);
    }

    @Override
    public <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    public abstract OWLProperty getProperty();
}

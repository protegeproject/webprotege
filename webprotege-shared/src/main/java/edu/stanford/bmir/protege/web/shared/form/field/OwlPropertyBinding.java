package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRelationshipValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(OwlPropertyBinding.TYPE)
public abstract class OwlPropertyBinding implements OwlBinding {

    public static final String TYPE = "PROPERTY";

    public static final String PROPERTY = "property";

    @JsonCreator
    public static OwlPropertyBinding get(@JsonProperty(PROPERTY) @Nonnull OWLProperty property,
                                         @JsonProperty(VALUES_CRITERIA) @Nullable CompositeRelationshipValueCriteria criteria) {
        return new AutoValue_OwlPropertyBinding(property, criteria);
    }

    public static OwlPropertyBinding get(@JsonProperty(PROPERTY) @Nonnull OWLProperty property) {
        return get(property, null);
    }

    @Nonnull
    public abstract OWLProperty getProperty();

    @Nonnull
    @JsonIgnore
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.of(getProperty());
    }


    @JsonIgnore
    @Nonnull
    public Optional<CompositeRelationshipValueCriteria> getValuesCriteria() {
        return Optional.ofNullable(getValuesCriteriaInternal());
    }

    @JsonProperty(VALUES_CRITERIA)
    @Nullable
    protected abstract CompositeRelationshipValueCriteria getValuesCriteriaInternal();
}

package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRelationshipValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(OwlClassBinding.TYPE)
public abstract class OwlClassBinding implements OwlBinding {

    public static final String TYPE = "CLASS";

    @JsonCreator
    @Nonnull
    public static OwlClassBinding get(@JsonProperty(VALUES_CRITERIA) @Nullable EntityMatchCriteria valuesFilter) {
        return new AutoValue_OwlClassBinding();
    }

    @Nonnull
    public static OwlClassBinding get() {
        return get(null);
    }

    @Nonnull
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.empty();
    }
}

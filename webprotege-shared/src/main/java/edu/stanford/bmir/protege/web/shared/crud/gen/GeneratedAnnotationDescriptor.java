package edu.stanford.bmir.protege.web.shared.crud.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyPositionCriteria;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GeneratedAnnotationDescriptor {

    public static final String PROPERTY = "property";

    public static final String VALUE = "value";

    private static final String ACTIVATED_BY = "activatedBy";

    @JsonCreator
    public static GeneratedAnnotationDescriptor get(@JsonProperty(PROPERTY) @Nonnull OWLAnnotationProperty property,
                                                    @JsonProperty(VALUE) @Nonnull GeneratedValueDescriptor valueDescriptor,
                                                    @JsonProperty(ACTIVATED_BY) @Nullable HierarchyPositionCriteria criteria) {
        return new AutoValue_GeneratedAnnotationDescriptor(property, valueDescriptor, criteria);
    }

    @JsonProperty(PROPERTY)
    @Nonnull
    public abstract OWLAnnotationProperty getProperty();

    @JsonProperty(VALUE)
    @Nonnull
    public abstract GeneratedValueDescriptor getValueDescriptor();

    @JsonIgnore
    @Nullable
    abstract HierarchyPositionCriteria getActivatedByInternal();

    @JsonProperty(ACTIVATED_BY)
    @Nonnull
    public Optional<HierarchyPositionCriteria> getActivatedBy() {
        return Optional.ofNullable(getActivatedByInternal());
    }

}

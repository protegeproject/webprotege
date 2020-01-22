package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = OwlPropertyBinding.class, name = OwlPropertyBinding.TYPE),
                      @JsonSubTypes.Type(value = OwlClassBinding.class, name = OwlClassBinding.TYPE)
              })
public interface OwlBinding {

    String VALUES_FILTER = "valuesFilter";

    @JsonIgnore
    @Nonnull
    Optional<OWLProperty> getOwlProperty();

    @JsonIgnore
    @Nonnull
    default Optional<CompositeRootCriteria> getValuesFilter() {
        return Optional.ofNullable(getValuesFilterInternal());
    }

    @JsonProperty(VALUES_FILTER)
    @Nullable
    CompositeRootCriteria getValuesFilterInternal();





}

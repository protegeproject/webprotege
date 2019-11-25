package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
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
    public static OwlClassBinding get(@Nonnull @JsonProperty("classes") ImmutableList<OWLClass> classes) {
        return new AutoValue_OwlClassBinding(classes);
    }

    public abstract ImmutableList<OWLClass> getClasses();

    @Nonnull
    @Override
    public Optional<OWLProperty> getOwlProperty() {
        return Optional.empty();
    }
}

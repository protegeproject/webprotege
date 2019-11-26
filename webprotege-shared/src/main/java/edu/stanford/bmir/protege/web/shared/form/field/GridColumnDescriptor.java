package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
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
public abstract class GridColumnDescriptor {

    @JsonCreator
    @Nonnull
    public static GridColumnDescriptor get(@Nonnull @JsonProperty("id") GridColumnId id,
                                           @Nullable @JsonProperty("owlBinding") OwlBinding owlBinding,
                                           @Nonnull @JsonProperty("label") LanguageMap columnLabel,
                                           @Nonnull @JsonProperty("formFieldDescriptor") FormFieldDescriptor formFieldDescriptor) {
        return new AutoValue_GridColumnDescriptor(id, owlBinding, columnLabel, formFieldDescriptor);
    }

    @JsonUnwrapped
    @Nonnull
    public abstract GridColumnId getId();

    @JsonIgnore
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @Nonnull
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract FormFieldDescriptor getFieldDescriptor();

    // TODO: Column width, grow, shrink

}

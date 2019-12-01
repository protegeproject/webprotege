package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

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
public abstract class GridColumnDescriptor {

    @JsonCreator
    @Nonnull
    public static GridColumnDescriptor get(@Nonnull @JsonProperty("id") GridColumnId id,
                                           @Nullable @JsonProperty("owlBinding") OwlBinding owlBinding,
                                           @Nonnull @JsonProperty("label") LanguageMap columnLabel,
                                           @Nonnull @JsonProperty("fieldDescriptor") FormControlDescriptor formControlDescriptor) {
        return new AutoValue_GridColumnDescriptor(id, owlBinding, columnLabel, formControlDescriptor);
    }

    public static GridColumnDescriptor getDefaultColumnDescriptor() {
        return GridColumnDescriptor.get(
                GridColumnId.get(""),
                null,
                LanguageMap.empty(),
                TextControlDescriptor.getDefault()
        );
    }

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
    public abstract FormControlDescriptor getFieldDescriptor();

    // TODO: Column width, grow, shrink

}

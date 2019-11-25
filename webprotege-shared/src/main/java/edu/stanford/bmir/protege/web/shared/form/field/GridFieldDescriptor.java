package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@JsonTypeName(GridFieldDescriptor.TYPE)
@AutoValue
public abstract class GridFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "GRID";

    public static String getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return getType();
    }

    public static GridFieldDescriptor get(@Nonnull @JsonProperty("columnDescriptors") ImmutableList<GridColumnDescriptor> columnDescriptors) {
        return new AutoValue_GridFieldDescriptor(columnDescriptors);
    }

    @Nonnull
    public abstract ImmutableList<GridColumnDescriptor> getColumnDescriptors();

}

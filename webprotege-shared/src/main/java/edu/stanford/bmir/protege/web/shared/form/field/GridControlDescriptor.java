package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@JsonTypeName(GridControlDescriptor.TYPE)
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridControlDescriptor implements FormControlDescriptor {

    protected static final String TYPE = "GRID";

    public static String getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return getType();
    }

    @JsonCreator
    @Nonnull
    public static GridControlDescriptor get(@Nonnull @JsonProperty("columns") ImmutableList<GridColumnDescriptor> columnDescriptors) {
        return new AutoValue_GridControlDescriptor(columnDescriptors == null ? ImmutableList.of() : columnDescriptors);
    }

    @JsonProperty("columns")
    @Nonnull
    public abstract ImmutableList<GridColumnDescriptor> getColumns();

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

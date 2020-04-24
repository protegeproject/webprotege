package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

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
    public static GridControlDescriptor get(@Nonnull @JsonProperty("columns") ImmutableList<GridColumnDescriptor> columnDescriptors,
                                            @Nullable @JsonProperty("subjectFactoryDescriptor") FormSubjectFactoryDescriptor subjectFactoryDescriptor) {
        return new AutoValue_GridControlDescriptor(columnDescriptors == null ? ImmutableList.of() : columnDescriptors,
                                                   subjectFactoryDescriptor);
    }

    @JsonProperty("columns")
    @Nonnull
    public abstract ImmutableList<GridColumnDescriptor> getColumns();

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public int getNestedColumnCount() {
        int count = 0;
        for(GridColumnDescriptor columnDescriptor : getColumns()) {
            count += columnDescriptor.getNestedColumnCount();
        }
        return count;
    }


    @JsonIgnore
    @Nullable
    protected abstract FormSubjectFactoryDescriptor getSubjectFactoryDescriptorInternal();

    public Optional<FormSubjectFactoryDescriptor> getSubjectFactoryDescriptor() {
        return Optional.ofNullable(getSubjectFactoryDescriptorInternal());
    }

}

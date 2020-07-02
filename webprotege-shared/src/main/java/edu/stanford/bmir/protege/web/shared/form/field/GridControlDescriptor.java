package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

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
                                                   subjectFactoryDescriptor == null ? FormSubjectFactoryDescriptor.get(
                                                           EntityType.CLASS, null, Optional.empty()) : subjectFactoryDescriptor);
    }

    @JsonProperty("columns")
    @Nonnull
    public abstract ImmutableList<GridColumnDescriptor> getColumns();

    @Override
    public <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @JsonIgnore
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

    @JsonIgnore
    @Nonnull
    public Stream<GridColumnDescriptor> getLeafColumns() {
        return getColumns()
                .stream()
                .flatMap(GridColumnDescriptor::getLeafColumnDescriptors);
    }

    /**
     * Returns a map of leaf column Ids to top level columns in this grid.  If a column does not
     * have nested columns then the column will map to itself.
     */
    @JsonIgnore
    public ImmutableMap<GridColumnId, GridColumnId> getLeafColumnToTopLevelColumnMap() {
        ImmutableMap.Builder<GridColumnId, GridColumnId> builder = ImmutableMap.builder();
        getColumns()
                .forEach(topLevelColumn -> {
                    topLevelColumn.getLeafColumnDescriptors()
                                  .map(GridColumnDescriptor::getId)
                                  .forEach(leafColumnId -> {
                                      builder.put(leafColumnId, topLevelColumn.getId());
                                  });
                });
        return builder.build();
    }

    /**
     * Gets the index of the specified columnId.
     * @param columnId The {@link GridColumnId}
     * @return The column index of the specified column Id.  A value of -1 is returned if the
     * {@link GridColumnId} does not identify a column in this grid.
     */
    @JsonIgnore
    public int getColumnIndex(GridColumnId columnId) {
        ImmutableList<GridColumnDescriptor> columns = getColumns();
        for(int i = 0; i < columns.size(); i++) {
            if(columns.get(i).getId().equals(columnId)) {
                return i;
            }
        }
        return -1;
    }

}

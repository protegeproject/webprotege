package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridControlOrderBy {

    public static final String COLUMN_ID = "columnId";

    public static final String DIRECTION = "direction";

    @JsonCreator
    @Nonnull
    public static GridControlOrderBy get(@JsonProperty(COLUMN_ID) @Nonnull GridColumnId columnId,
                                         @JsonProperty(DIRECTION) @Nonnull GridControlOrderByDirection direction) {
        return new AutoValue_GridControlOrderBy(columnId, direction);
    }

    @JsonProperty(COLUMN_ID)
    @Nonnull
    public abstract GridColumnId getColumnId();

    @JsonProperty(DIRECTION)
    @Nonnull
    public abstract GridControlOrderByDirection getDirection();

    @JsonIgnore
    public boolean isAscending() {
        return getDirection().equals(GridControlOrderByDirection.ASC);
    }
}

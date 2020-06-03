package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;


@AutoValue
@GwtCompatible(serializable = true)
@Nonnull
public abstract class GridControlOrdering {

    public static GridControlOrdering get(@Nonnull FormFieldId formFieldId,
                                          @Nonnull ImmutableList<GridControlOrderBy> ordering) {
        return new AutoValue_GridControlOrdering(formFieldId, ordering);
    }

    @Nonnull
    public abstract FormFieldId getFieldId();

    @Nonnull
    public abstract ImmutableList<GridControlOrderBy> getOrdering();
}

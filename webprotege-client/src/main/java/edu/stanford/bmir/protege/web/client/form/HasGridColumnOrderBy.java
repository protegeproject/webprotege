package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlOrderBy;

import javax.annotation.Nonnull;

public interface HasGridColumnOrderBy {

    interface ChangeHandler {
        void handleGridColumnOrderByChanged();
    }

    void setGridColumnOrderByChangeHandler(@Nonnull ChangeHandler handler);

    @Nonnull
    ImmutableList<GridControlOrderBy> getOrderBy();
}

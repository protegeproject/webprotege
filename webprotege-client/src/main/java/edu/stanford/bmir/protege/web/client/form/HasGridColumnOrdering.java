package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;

import javax.annotation.Nonnull;

public interface HasGridColumnOrdering {

    interface ChangeHandler {
        void handleGridColumnOrderingChanged();
    }

    void setGridColumnOrderingChangeHandler(@Nonnull ChangeHandler handler);

    @Nonnull
    ImmutableList<FormRegionOrdering> getGridColumnOrdering();
}

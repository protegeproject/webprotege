package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

public class GridColumnDescriptorObjectListPresenter extends ObjectListPresenter<GridColumnDescriptor> {

    @Inject
    public GridColumnDescriptorObjectListPresenter(@Nonnull ObjectListView view,
                                                   @Nonnull Provider<ObjectPresenter<GridColumnDescriptor>> objectListPresenter,
                                                   @Nonnull Provider<ObjectListViewHolder> objectViewHolderProvider,
                                                   @Nonnull UuidV4Provider uuidV4Provider) {
        super(view, objectListPresenter, objectViewHolderProvider, () -> getDefaultColumnDescriptor(uuidV4Provider));
    }

    private static GridColumnDescriptor getDefaultColumnDescriptor(UuidV4Provider uuidV4Provider) {
        return GridColumnDescriptor.get(
                GridColumnId.get(uuidV4Provider.get()),
                Optionality.REQUIRED,
                Repeatability.NON_REPEATABLE,
                null,
                LanguageMap.empty(),
                TextControlDescriptor.getDefault()
        );
    }
}

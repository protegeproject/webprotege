package edu.stanford.bmir.protege.web.client.search;

import edu.stanford.bmir.protege.web.client.form.*;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
public class EntitySearchFilterListPresenter extends ObjectListPresenter<EntitySearchFilter> {



    @Inject
    public EntitySearchFilterListPresenter(@Nonnull ObjectListView view,
                                           @Nonnull Provider<EntitySearchFilterPresenter> entitySearchFilterPresenterProvider,
                                           @Nonnull Provider<ObjectListViewHolder> objectViewHolderProvider,
                                           @Nonnull EntitySearchFilterProvider defaultObjectProvider) {
        super(view, entitySearchFilterPresenterProvider::get, objectViewHolderProvider, defaultObjectProvider);
        setAddObjectText("Add search filter");
    }
}

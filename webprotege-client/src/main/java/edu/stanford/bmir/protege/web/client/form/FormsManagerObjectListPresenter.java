package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-21
 */
public class FormsManagerObjectListPresenter extends ObjectListPresenter<FormDescriptor> {

    @Inject
    public FormsManagerObjectListPresenter(@Nonnull ObjectListView view,
                                           @Nonnull Provider<FormsManagerObjectPresenter> objectListPresenter,
                                           @Nonnull Provider<ObjectListViewHolder> objectViewHolderProvider,
                                           @Nonnull FormsManagerObjectListDefaultObjectProvider defaultObjectProvider,
                                           @Nonnull FormsMessages messages) {
        super(view, objectListPresenter::get, objectViewHolderProvider, defaultObjectProvider);
        setAddObjectText(messages.addForm());
    }
}

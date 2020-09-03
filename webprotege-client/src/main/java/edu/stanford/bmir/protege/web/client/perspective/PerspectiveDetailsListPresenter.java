package edu.stanford.bmir.protege.web.client.perspective;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.form.ObjectListPresenter;
import edu.stanford.bmir.protege.web.client.form.ObjectListView;
import edu.stanford.bmir.protege.web.client.form.ObjectListViewHolder;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDetails;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public class PerspectiveDetailsListPresenter extends ObjectListPresenter<PerspectiveDetails> {

    @Inject
    public PerspectiveDetailsListPresenter(@Nonnull ObjectListView view,
                                           @Nonnull Provider<PerspectiveDetailsPresenter> objectListPresenter,
                                           @Nonnull Provider<ObjectListViewHolder> objectViewHolderProvider,
                                           @Nonnull PerspectiveDetailsDefaultValueProvider defaultValueProvider,
                                           @Nonnull Messages messages) {
        super(view, objectListPresenter::get, objectViewHolderProvider, defaultValueProvider);
        setAddObjectText(messages.perspective_addTab());
        view.setDeleteConfirmationTitle(messages::perspective_deleteConfirmation_title);
        view.setDeleteConfirmationMessage(messages::perspective_deleteConfirmation_message);
    }
}

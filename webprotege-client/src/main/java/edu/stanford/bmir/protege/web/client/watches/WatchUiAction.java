package edu.stanford.bmir.protege.web.client.watches;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2018
 */
public class WatchUiAction extends AbstractUiAction {

    @Nonnull
    private final WatchPresenter watchPresenter;

    @Nonnull
    private final SelectionModel selectionModel;

    @Inject
    public WatchUiAction(@Nonnull WatchPresenter watchPresenter,
                         @Nonnull Messages messages,
                         @Nonnull SelectionModel selectionModel) {
        super(messages.watch() + "...");
        this.watchPresenter = checkNotNull(watchPresenter);
        this.selectionModel = selectionModel;
        setRequiresSelection(true);
    }

    @Override
    public void execute() {
        selectionModel.getSelection().ifPresent(watchPresenter::start);
    }
}

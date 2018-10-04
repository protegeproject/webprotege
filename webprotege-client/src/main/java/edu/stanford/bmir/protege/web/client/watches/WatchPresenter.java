package edu.stanford.bmir.protege.web.client.watches;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalCloser;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.OK;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class WatchPresenter {

    @Nonnull
    private final WatchView view;

    @Nonnull
    private final Messages messages;

    private final DispatchServiceManager dispatchServiceManager;

    private final ProjectId projectId;

    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final ModalManager modalManager;

    private OWLEntity currentEntity;

    @Inject
    public WatchPresenter(@Nonnull ProjectId projectId,
                          @Nonnull WatchView view,
                          @Nonnull Messages messages,
                          @Nonnull LoggedInUserProvider loggedInUserProvider,
                          @Nonnull DispatchServiceManager dispatchServiceManager,
                          @Nonnull ModalManager modalManager) {
        this.view = checkNotNull(view);
        this.messages = checkNotNull(messages);
        this.projectId = checkNotNull(projectId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.loggedInUserProvider = checkNotNull(loggedInUserProvider);
        this.modalManager = modalManager;
    }

    public void start(@Nonnull OWLEntity forEntity) {
        this.currentEntity = checkNotNull(forEntity);
        UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetWatchesAction(projectId, userId, forEntity),
                                       this::handleRetrivedWatches);
    }

    private void handleApplyChanges(@Nonnull ModalCloser modalCloser) {
        WatchTypeSelection type = view.getSelectedType();
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        Optional<Watch> watch = getWatchFromType(type, currentEntity);
        ImmutableSet<Watch> watches = watch.map(ImmutableSet::of).orElse(ImmutableSet.of());
        dispatchServiceManager.execute(new SetEntityWatchesAction(projectId, userId, currentEntity, watches),
                                       result -> modalCloser.closeModal());
    }

    private void handleRetrivedWatches(@Nonnull GetWatchesResult result) {
        Set<Watch> watches = result.getWatches();
        setWatchesInView(watches);
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(messages.watch_watches());
        modalPresenter.setView(view);
        modalPresenter.setEscapeButton(CANCEL);
        modalPresenter.setPrimaryButton(OK);
        modalPresenter.setButtonHandler(OK, this::handleApplyChanges);
        modalManager.showModal(modalPresenter);
    }

    private void setWatchesInView(@Nonnull Set<Watch> watches) {
        GWT.log("[WatchPresenter] Updating settings for watches: " + watches);
        view.setSelectedType(WatchTypeSelection.NONE_SELECTED);
        for(Watch watch : watches) {
            if(watch.getType() == WatchType.ENTITY) {
                view.setSelectedType(WatchTypeSelection.ENTITY_SELECTED);
                break;
            }
            else if(watch.getType() == WatchType.BRANCH) {
                view.setSelectedType(WatchTypeSelection.BRANCH_SELECTED);
                break;
            }
        }
    }

    private Optional<Watch> getWatchFromType(final WatchTypeSelection type, final OWLEntity entity) {
        switch (type) {
            case NONE_SELECTED:
                return Optional.empty();
            case ENTITY_SELECTED:
                return Optional.of(new Watch(loggedInUserProvider.getCurrentUserId(),
                                             entity,
                                             WatchType.ENTITY));
            case BRANCH_SELECTED:
                return Optional.of(new Watch(loggedInUserProvider.getCurrentUserId(),
                                             entity,
                                             WatchType.BRANCH));
            default:
                return Optional.empty();
        }
    }
}

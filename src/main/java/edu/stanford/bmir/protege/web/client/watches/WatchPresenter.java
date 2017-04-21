package edu.stanford.bmir.protege.web.client.watches;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogCloser;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class WatchPresenter {

    private final WatchTypeDialogController controller;

    private final DispatchServiceManager dispatchServiceManager;

    private final ProjectId projectId;

    private final LoggedInUserProvider loggedInUserProvider;

    @Inject
    public WatchPresenter(ProjectId projectId, WatchTypeDialogController controller, LoggedInUserProvider loggedInUserProvider, DispatchServiceManager dispatchServiceManager) {
        this.controller = controller;
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    public void showDialog(final OWLEntity forEntity) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        dispatchServiceManager.execute(new GetWatchesAction(projectId, userId, forEntity), new DispatchServiceCallback<GetWatchesResult>() {
            @Override
            public void handleSuccess(GetWatchesResult result) {
                Set<Watch> watches = result.getWatches();
                updateDialog(watches);
                WebProtegeDialog<WatchTypeSelection> dlg = new WebProtegeDialog<>(controller);
                dlg.show();
                controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<WatchTypeSelection>() {
                    @Override
                    public void handleHide(WatchTypeSelection data, WebProtegeDialogCloser closer) {
                        closer.hide();
                        handleWatchTypeForEntity(data, forEntity);
                    }
                });
            }
        });

    }

    private void updateDialog(Set<Watch> watches) {
        GWT.log("[WatchPresenter] Updating dialog for watches: " + watches);
        controller.setSelectedType(WatchTypeSelection.NONE_SELECTED);
        for(Watch watch : watches) {
            if(watch.getType() == WatchType.ENTITY) {
                controller.setSelectedType(WatchTypeSelection.ENTITY_SELECTED);
                break;
            }
            else if(watch.getType() == WatchType.BRANCH) {
                controller.setSelectedType(WatchTypeSelection.BRANCH_SELECTED);
                break;
            }
        }
    }

    private void handleWatchTypeForEntity(final WatchTypeSelection type, final OWLEntity entity) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        Optional<Watch> watch = getWatchFromType(type, entity);
        ImmutableSet<Watch> watches = ImmutableSet.copyOf(watch.asSet());
            dispatchServiceManager.execute(new SetEntityWatchesAction(projectId, userId, entity, watches), new DispatchServiceCallback<SetEntityWatchesResult>() {
                @Override
                public void handleSuccess(SetEntityWatchesResult setEntityWatchesResult) {

                }
            });


    }

    private Optional<Watch> getWatchFromType(final WatchTypeSelection type, final OWLEntity entity) {
        switch (type) {
            case NONE_SELECTED:
                return Optional.absent();
            case ENTITY_SELECTED:
                return Optional.of(new Watch(loggedInUserProvider.getCurrentUserId(),
                                             entity,
                                             WatchType.ENTITY));
            case BRANCH_SELECTED:
                return Optional.of(new Watch(loggedInUserProvider.getCurrentUserId(),
                                             entity,
                                             WatchType.BRANCH));
            default:
                return Optional.absent();
        }
    }
}

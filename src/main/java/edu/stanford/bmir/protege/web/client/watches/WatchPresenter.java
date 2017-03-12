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
                Set<EntityBasedWatch> watches = result.getWatches();
                updateDialog(watches);
                WebProtegeDialog<WatchType> dlg = new WebProtegeDialog<>(controller);
                dlg.show();
                controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<WatchType>() {
                    @Override
                    public void handleHide(WatchType data, WebProtegeDialogCloser closer) {
                        closer.hide();
                        handleWatchTypeForEntity(data, forEntity);
                    }
                });
            }
        });

    }

    private void updateDialog(Set<EntityBasedWatch> watches) {
        GWT.log("[WatchPresenter] Updating dialog for watches: " + watches);
        controller.setSelectedType(WatchType.NONE);
        for(Watch<?> watch : watches) {
            if(watch instanceof EntityFrameWatch) {
                controller.setSelectedType(WatchType.ENTITY);
                break;
            }
            else if(watch instanceof HierarchyBranchWatch) {
                controller.setSelectedType(WatchType.BRANCH);
                break;
            }
        }
    }

    private void handleWatchTypeForEntity(final WatchType type, final OWLEntity entity) {
        final UserId userId = loggedInUserProvider.getCurrentUserId();
        Optional<EntityBasedWatch> watch = getWatchFromType(type, entity);
        ImmutableSet<EntityBasedWatch> watches = ImmutableSet.copyOf(watch.asSet());
            dispatchServiceManager.execute(new SetEntityWatchesAction(projectId, userId, entity, watches), new DispatchServiceCallback<SetEntityWatchesResult>() {
                @Override
                public void handleSuccess(SetEntityWatchesResult setEntityWatchesResult) {

                }
            });


    }

    private Optional<EntityBasedWatch> getWatchFromType(final WatchType type, final OWLEntity entity) {
        switch (type) {
            case NONE:
                return Optional.<EntityBasedWatch>absent();
            case ENTITY:
                return Optional.<EntityBasedWatch>of(new EntityFrameWatch(entity));
            case BRANCH:
                return Optional.<EntityBasedWatch>of(new HierarchyBranchWatch(entity));
            default:
                return Optional.<EntityBasedWatch>absent();
        }
    }
}

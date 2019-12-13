package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.viz.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2018
 */
public class VizPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final VizView view;

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final EntityGraphPresenter entityGraphPresenter;

    @Nonnull
    private final LargeGraphMessagePresenter largeGraphMessagePresenter;

    @Nonnull
    private final EntityGraphSettingsPresenter entityGraphSettingsPresenter;

    @Nonnull
    private Optional<OWLEntity> currentEntity = Optional.empty();

    @Nonnull
    private HasBusy hasBusy = busy -> {
    };

    @Inject
    public VizPresenter(@Nonnull ProjectId projectId,
                        @Nonnull DispatchServiceManager dispatch,
                        @Nonnull VizView view,
                        @Nonnull PlaceController placeController,
                        @Nonnull EntityGraphPresenter entityGraphPresenter,
                        @Nonnull LargeGraphMessagePresenter largeGraphMessagePresenter,
                        @Nonnull EntityGraphSettingsPresenter entityGraphSettingsPresenter) {
        this.projectId = checkNotNull(projectId);
        this.dispatch = checkNotNull(dispatch);
        this.view = checkNotNull(view);
        this.placeController = checkNotNull(placeController);
        this.entityGraphPresenter = entityGraphPresenter;
        this.largeGraphMessagePresenter = largeGraphMessagePresenter;
        this.entityGraphSettingsPresenter = entityGraphSettingsPresenter;
    }

    public void setEntityDisplay(EntityDisplay entityDisplay) {
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);

        entityGraphSettingsPresenter.setSettingsAppliedHandler(this::displayGraphView);
        entityGraphSettingsPresenter.setCancelHandler(this::displayGraphView);
        entityGraphSettingsPresenter.setHasBusy(hasBusy);

        entityGraphPresenter.setLargeGraphHandler(this::displayLargeGraphMessage);
        entityGraphPresenter.setHasBusy(hasBusy);
        entityGraphPresenter.setViewSettingsHandler(this::displaySettingsView);

        largeGraphMessagePresenter.setDisplayGraphHandler(this::displayLargeGraph);
        largeGraphMessagePresenter.setDisplaySettingsHandler(this::displaySettingsView);
    }

    private void displayGraphView() {
        entityGraphPresenter.start(view);
        currentEntity.ifPresent(entityGraphPresenter::displayEntity);
    }

    private void displayLargeGraph() {
        entityGraphPresenter.start(view);
        currentEntity.ifPresent(entityGraphPresenter::displayLargeGraph);
    }

    private void displaySettingsView() {
        entityGraphSettingsPresenter.start(view);
    }

    private void displayLargeGraphMessage() {
        entityGraphPresenter.getCurrentEntityGraph()
                            .ifPresent(currentGraph -> {
                                largeGraphMessagePresenter.setMessage(currentGraph.getRoot(),
                                                                      currentGraph.getNodeCount(),
                                                                      currentGraph.getEdgeCount());
                            });
        largeGraphMessagePresenter.start(view);
    }

    public void displayEntity(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        this.currentEntity = Optional.of(entity);
        displayGraphView();
    }
}

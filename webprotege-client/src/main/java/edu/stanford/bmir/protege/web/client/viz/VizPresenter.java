package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.graphlib.EntityGraph2Graph;
import edu.stanford.bmir.protege.web.client.graphlib.Graph;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraph;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingAction;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingResult;
import elemental.dom.Element;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

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
    private HasBusy hasBusy = busy -> {};

    private Graph currentGraph;

    private EntityGraph currentEntityGraph;

    @Inject
    public VizPresenter(@Nonnull ProjectId projectId,
                        @Nonnull DispatchServiceManager dispatch,
                        @Nonnull VizView view) {
        this.projectId = checkNotNull(projectId);
        this.dispatch = checkNotNull(dispatch);
        this.view = checkNotNull(view);
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull WebProtegeEventBus eventBus) {
        container.setWidget(view);
        view.setSettingsChangedHandler(this::handleSettingsChanged);
        view.setLoadHandler(this::handleLoad);
        view.setDownloadHandler(this::handleDownload);
    }

    private void handleLoad() {
        layoutAndDisplayGraph();
    }

    private void handleDownload() {
        if(currentGraph == null) {
            return;
        }
        layoutAndDisplayGraph();
        DownloadSvg saver = new DownloadSvg();
        Element element = view.getSvgElement();
        saver.save(element, currentGraph.getWidth(), currentGraph.getHeight(), "entity-graph");
    }

    private void handleSettingsChanged() {
        layoutAndDisplayGraph();
    }

    private void layoutAndDisplayGraph() {
        layoutCurrentGraph();
        displayGraph();
    }

    protected void displayEntity(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        dispatch.execute(new GetEntityDotRenderingAction(projectId, entity),
                         hasBusy,
                         this::handleRendering);

    }

    private void handleRendering(@Nonnull GetEntityDotRenderingResult result) {
        currentEntityGraph = result.getEntityGraph();
        layoutAndDisplayGraph();
    }

    private void layoutCurrentGraph() {
        if(currentEntityGraph == null) {
            return;
        }
        currentGraph = new EntityGraph2Graph(view.getTextMeasurer()).convertGraph(currentEntityGraph);
        currentGraph.setRankSep((int) (20 * view.getRankSpacing()));
        currentGraph.layout();
    }

    private void displayGraph() {
        if(currentGraph == null) {
            return;
        }
        view.setGraph(currentGraph);
    }
}

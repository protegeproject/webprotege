package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
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
        view.setLoadHandler(this::doLayout);
    }

    private void handleSettingsChanged() {
        displayCurrentRendering();
    }

    protected void displayEntity(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        dispatch.execute(new GetEntityDotRenderingAction(projectId, entity),
                         hasBusy,
                         this::handleRendering);

    }

    private void handleRendering(@Nonnull GetEntityDotRenderingResult result) {
        currentEntityGraph = result.getEntityGraph();
        doLayout();
    }

    private void doLayout() {
        if(currentEntityGraph == null) {
            return;
        }
        currentGraph = new EntityGraph2Graph(view.getTextMeasurer()).convertGraph(currentEntityGraph);
        displayCurrentRendering();

    }

    private void displayCurrentRendering() {
        if(currentGraph == null) {
            return;
        }
        currentGraph.setRankSep((int) (20 * view.getRankSpacing()));
        currentGraph.layout();
        view.setGraph(currentGraph);
//        Viz viz = new Viz();
//        String rendering = replaceVariables(currentRendering);
//        GWT.log("[Viz]" + rendering);
//        viz.render(rendering, view::setRendering);
    }

    private String replaceVariables(@Nonnull String rendering) {
        return rendering
                .replace("${title}", "Entity Graph")
                .replace("${layout}", "dot")
                .replace("${rankdir}", "BT")
                .replace("${concentrate}", "true")
                .replace("${splines}", "true")
                .replace("${ranksep}", Double.toString(view.getRankSpacing()))
                .replace("${nodesep}", "0.3")
                .replace("${fontname}", "Helvetica Neue")
                .replace("${node.style}", "rounded")
                .replace("${node.shape}", "box")
                .replace("${node.color}", "#d0d0d0")
                .replace("${node.ind.color}", "#e0b0ff")
                .replace("${node.margin}", "0.03")
                .replace("${node.fontcolor}", "#505050")
                .replace("${edge.rel.color}", "#4784d1")
                .replace("${edge.isa.color}", "#b0b0b0")
                .replace("${edge.arrowsize}", "0.8");
    }
}

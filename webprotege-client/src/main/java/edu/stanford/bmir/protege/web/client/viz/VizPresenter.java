package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.graphlib.EdgeDetails;
import edu.stanford.bmir.protege.web.client.graphlib.EntityGraph2Graph;
import edu.stanford.bmir.protege.web.client.graphlib.Graph;
import edu.stanford.bmir.protege.web.client.graphlib.NodeDetails;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.ui.ElementalUtil;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraph;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingAction;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingResult;
import elemental.dom.Element;
import elemental.events.Event;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2018
 */
public class VizPresenter {

    private static final int LARGE_GRAPH_EDGE_COUNT = 500;

    private static final String G_MUTED = "wp-graph__g--muted";

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final SelectionModel selectionModel;

    @Nonnull
    private final VizView view;

    @Nonnull
    private HasBusy hasBusy = busy -> {
    };

    private Graph currentGraph;

    private EntityGraph currentEntityGraph;

    private PopupPanel popupPanel = new PopupPanel();

    @Nonnull
    private Optional<OWLEntity> currentEntity = Optional.empty();

    private EntityDisplay entityDisplay;

    @Inject
    public VizPresenter(@Nonnull ProjectId projectId,
                        @Nonnull DispatchServiceManager dispatch,
                        @Nonnull SelectionModel selectionModel, @Nonnull VizView view) {
        this.projectId = checkNotNull(projectId);
        this.dispatch = checkNotNull(dispatch);
        this.selectionModel = checkNotNull(selectionModel);
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
        view.setNodeClickHandler(this::handleNodeClicked);
        view.setNodeDoubleClickHandler(this::handleNodeDoubleClicked);
        view.setNodeContextMenuClickHandler(this::handleNodeContextMenuClick);
        view.setNodeMouseOverHandler(this::handleNodeMouseOver);
        view.setNodeMouseLeaveHandler(this::handleNodeMouseLeave);
        view.addContextMenuAction(new AbstractUiAction("Hide node") {
            @Override
            public void execute() {
                view.getMostRecentTargetNode().ifPresent(n -> {
                    if (currentGraph != null) {
                        GWT.log("[VizPresenter] Removing " + n);
                        currentGraph.removeNode(n.getId());
                        layoutCurrentGraph(false);
                        displayGraph();
                    }
                });
            }
        });
        view.addContextMenuAction(new AbstractUiAction("Move focus to node") {
            @Override
            public void execute() {
                view.getMostRecentTargetNode().ifPresent(n -> selectionModel.setSelection(n.getEntity()));
            }
        });
    }

    private void handleNodeMouseLeave(NodeDetails nodeDetails, Event event) {
        GWT.log("[VizPresenter] Handle mouse leave");
        Element topGroup = ElementalUtil.firstChildGroupElement(view.getSvgElement());
        ElementalUtil.elementsByTagName(topGroup, "g")
                .forEach(element -> ElementalUtil.removeClassName(element, G_MUTED));
    }

    private void handleNodeMouseOver(NodeDetails nodeDetails, Event event) {
        if(currentGraph == null) {
            return;
        }
        Set<NodeDetails> nodes = currentGraph.getNodes().collect(toSet());
        HashSet<String> reachableNodes = new HashSet<>();
        HashSet<EdgeDetails> reachableEdges = new HashSet<>();
        collectReachableNodesAndEdges(nodeDetails,
                                      reachableNodes,
                                      reachableEdges,
                                      new HashSet<>());
        Element topGroup = ElementalUtil.firstChildGroupElement(view.getSvgElement());
        Element nodeGroup = ElementalUtil.firstChildGroupElement(topGroup);
        Element edgeGroup = ElementalUtil.nthChildGroupElement(topGroup, 1).orElseThrow(() -> new RuntimeException("Missing edge group"));
        Stream<Element> nodeGroups = ElementalUtil.childElementsByTagName(nodeGroup, "g");
        nodeGroups.forEach(
                nodeElement -> {
                    String nodeId = nodeElement.getAttribute("data-node-id");
                    if(reachableNodes.contains(nodeId)) {
                        ElementalUtil.removeClassName(nodeElement, G_MUTED);
                    }
                    else {
                        ElementalUtil.addClassName(nodeElement, G_MUTED);
                    }
                }
        );
        Stream<Element> edgeGroups = ElementalUtil.childElementsByTagName(edgeGroup, "g");
        edgeGroups.forEach(edgeElement -> {
            String headNodeId = edgeElement.getAttribute("data-head");
            if(reachableNodes.contains(headNodeId)) {
                ElementalUtil.removeClassName(edgeElement, G_MUTED);
            }
            else {
                ElementalUtil.addClassName(edgeElement, G_MUTED);
            }
        });
    }

    private void collectReachableNodesAndEdges(@Nonnull NodeDetails from,
                                               @Nonnull Set<String> reachableNodeIds,
                                               @Nonnull Set<EdgeDetails> edgeDetails,
                                               @Nonnull Set<String> processed) {
        if(processed.contains(from.getId())) {
            return;
        }
        processed.add(from.getId());
        reachableNodeIds.add(from.getId());
        currentGraph.getPredecessors(from.getId()).forEach(node -> {
            collectReachableNodesAndEdges(node, reachableNodeIds, edgeDetails, processed);
        });
    }

    private void handleNodeContextMenuClick(@Nonnull NodeDetails nodeDetails) {

    }

    private void handleNodeClicked(@Nonnull NodeDetails nodeDetails) {

    }

    private void handleNodeDoubleClicked(@Nonnull NodeDetails node) {
        if (currentGraph == null) {
            return;
        }
        selectionModel.setSelection(node.getEntity());
    }

    private void handleLoad() {
        resetCurrentGraph();
    }

    private void handleDownload() {
        if (currentGraph == null) {
            return;
        }
        layoutAndDisplayGraph();
        GraphSvgDownloader saver = new GraphSvgDownloader();
        Element element = view.getSvgElement();
        saver.save(element, currentGraph.getWidth(), currentGraph.getHeight(), "entity-graph");
    }

    private void handleSettingsChanged() {
        layoutAndDisplayGraph();
    }

    private void resetCurrentGraph() {
        layoutAndDisplayGraph();
    }

    private void layoutAndDisplayGraph() {
        if(layoutCurrentGraph(true)) {
            displayGraph();
        }
    }

    protected void displayEntity(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        this.currentEntity = Optional.of(entity);
        dispatch.execute(new GetEntityDotRenderingAction(projectId, entity),
                         hasBusy,
                         this::handleRendering);

    }

    private void handleRendering(@Nonnull GetEntityDotRenderingResult result) {
        if(!isGraphForCurrentEntity(result)) {
            return;
        }
        entityDisplay.setDisplayedEntity(Optional.of(result.getEntityGraph().getRoot()));
        currentEntityGraph = result.getEntityGraph();
        resetCurrentGraph();
    }

    private Boolean isGraphForCurrentEntity(@Nonnull GetEntityDotRenderingResult result) {
        return currentEntity.map(e -> e.equals(result.getEntityGraph().getRootEntity())).orElse(false);
    }

    private boolean layoutCurrentGraph(boolean regenerate) {
            if (!view.isVisible()) {
                return false;
            }
            if (currentEntityGraph == null) {
                view.clearGraph();
                return false;
            }
            if (currentEntityGraph.getNodes().isEmpty()) {
                view.clearGraph();
                currentGraph = null;
                return false;
            }
            Runnable layoutRunner = () -> {
                if (regenerate) {
                    currentGraph = new EntityGraph2Graph(view.getTextMeasurer(), currentEntityGraph)
                            .convertGraph();
                }
                currentGraph.setMarginX(10);
                currentGraph.setMarginY(10);
                currentGraph.setRankDirBottomToTop();
                currentGraph.setRankSep((int) (20 * view.getRankSpacing()));
                currentGraph.setNodeSep(10);
                currentGraph.setRankerToLongestPath();
                currentGraph.layout();
            };
        int edgeCount = currentEntityGraph.getEdges().size();
            if(edgeCount > LARGE_GRAPH_EDGE_COUNT) {
                int nodesCount = currentEntityGraph.getNodes().size();
                GWT.log("[VizPresenter] Large graph");
                view.displayLargeGraphMessage(currentEntityGraph.getRoot(),
                                              nodesCount,
                                              edgeCount,
                                              () -> {
                                                  layoutRunner.run();
                                                  displayGraph();
                                              });
                return false;
            }
            else {
                layoutRunner.run();
                return true;
            }
    }

    private void displayGraph() {
        if (currentGraph == null) {
            view.clearGraph();
        }
        else {
            view.setGraph(currentGraph);
        }
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }
}

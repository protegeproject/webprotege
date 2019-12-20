package edu.stanford.bmir.protege.web.client.viz;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.EntityItemMapper;
import edu.stanford.bmir.protege.web.client.graphlib.EdgeDetails;
import edu.stanford.bmir.protege.web.client.graphlib.EntityGraph2Graph;
import edu.stanford.bmir.protege.web.client.graphlib.Graph;
import edu.stanford.bmir.protege.web.client.graphlib.NodeDetails;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.ui.ElementalUtil;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.perspective.EntityTypePerspectiveMapper;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.Item;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.viz.*;
import elemental.dom.Element;
import elemental.events.Event;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.graphlib.GraphConstants.*;
import static edu.stanford.bmir.protege.web.client.graphlib.GraphConstants.WP_GRAPH__G_MUTED;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-12
 */
public class EntityGraphPresenter {

    @Nonnull
    private final EntityGraphView view;

    private static final int LARGE_GRAPH_EDGE_COUNT = 500;

    private static final int FIRST = 0;

    private static final int SECOND = 1;

    @Nonnull
    private final EntityGraphFilterTokenPresenter filterTokenPresenter;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final EntityTypePerspectiveMapper typePerspectiveMapper;

    @Nonnull
    private final PlaceController placeController;

    private boolean displayLargeGraph = false;

    @Nonnull
    private HasBusy hasBusy = busy -> { };

    private Graph currentGraph;

    private EntityGraph currentEntityGraph;

    @Nonnull
    private Optional<OWLEntity> currentEntity = Optional.empty();

    private EntityDisplay entityDisplay;

    private LargeGraphHandler largeGraphHandler = () -> {};

    private int rankSpacing = 10;

    @Inject
    public EntityGraphPresenter(@Nonnull EntityGraphView view,
                                @Nonnull EntityGraphFilterTokenPresenter filterTokenPresenter,
                                @Nonnull ProjectId projectId,
                                @Nonnull DispatchServiceManager dispatch,
                                @Nonnull EntityTypePerspectiveMapper typePerspectiveMapper,
                                @Nonnull PlaceController placeController) {
        this.view = view;
        this.filterTokenPresenter = filterTokenPresenter;
        this.projectId = projectId;
        this.dispatch = dispatch;
        this.typePerspectiveMapper = typePerspectiveMapper;
        this.placeController = placeController;
        addContextMenuItemsToView();
    }

    public void setLargeGraphHandler(@Nonnull LargeGraphHandler largeGraphHandler) {
        this.largeGraphHandler = checkNotNull(largeGraphHandler);
    }

    public void setViewSettingsHandler(ViewSettingsHandler viewSettingsHandler) {
        view.setViewSettingsHandler(viewSettingsHandler);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        filterTokenPresenter.start(view.getFilterContainer());
        filterTokenPresenter.setActiveFiltersChangedHandler(this::handleActiveFiltersChanged);
        addHandlersToView();
    }

    private void handleActiveFiltersChanged() {
        List<FilterName> activeFilters = filterTokenPresenter.getActiveFilters();
        dispatch.execute(new SetEntityGraphActiveFiltersAction(projectId,
                                                               ImmutableList.copyOf(activeFilters)),
                         hasBusy,
                         result -> redisplayCurrentEntity());
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    private void addHandlersToView() {
        view.setDownloadHandler(this::handleDownload);
        view.setNodeClickHandler(this::handleNodeClicked);
        view.setNodeDoubleClickHandler(this::handleNodeDoubleClicked);
        view.setNodeContextMenuClickHandler(this::handleNodeContextMenuClick);
        view.setNodeMouseOverHandler(this::handleNodeMouseOver);
        view.setNodeMouseOutHandler(this::handleNodeMouseOut);
        view.setNodeMouseEnterHandler(this::handleMouseEnter);
        view.setNodeMouseLeaveHandler(this::handleMouseLeave);
    }

    private void addContextMenuItemsToView() {
        view.addContextMenuAction(new HideNodeUiAction());
        view.addContextMenuAction(new SelectNodeUiAction());
    }

    private void handleMouseEnter(NodeDetails nodeDetails, Event event) {
        if (currentGraph == null) {
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
        applyMutedStylesToNonReachableNodes(reachableNodes, topGroup);
        applyMutedStylesToNonReachableEdges(reachableNodes, topGroup);
    }

    private void handleMouseLeave(NodeDetails nodeDetails, Event event) {
        Element topGroup = ElementalUtil.firstChildGroupElement(view.getSvgElement());
        ElementalUtil.elementsByTagName(topGroup, "g")
                     .forEach(element -> ElementalUtil.removeClassName(element, WP_GRAPH__G_MUTED));
    }

    public void displayEntity(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        this.currentEntity = Optional.of(entity);
        displayLargeGraph = false;
        redisplayCurrentEntity();
    }

    public void displayLargeGraph(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        this.currentEntity = Optional.of(entity);
        displayLargeGraph = true;
        redisplayCurrentEntity();
    }

    private void redisplayCurrentEntity() {
        currentEntityGraph = null;
        currentEntity.ifPresent(entity -> {
            dispatch.execute(new GetEntityGraphAction(projectId, entity),
                             hasBusy,
                             this::handleEntityGraph);
        });
    }

    private void handleNodeMouseOut(NodeDetails nodeDetails, Event event) {
    }

    private void handleNodeMouseOver(NodeDetails nodeDetails, Event event) {

    }

    private void applyMutedStylesToNonReachableNodes(HashSet<String> reachableNodes, Element topGroup) {
        Element nodeGroup = ElementalUtil.nthChildGroupElementOrError(topGroup,
                                                                      FIRST,
                                                                      "Missing nodes group");
        Stream<Element> nodeGroups = ElementalUtil.childElementsByTagName(nodeGroup, "g");
        applyMutedStylesToElements(reachableNodes, nodeGroups, DATA_NODE_ID);
    }

    private void applyMutedStylesToNonReachableEdges(HashSet<String> reachableNodes, Element topGroup) {
        Element edgeGroup = ElementalUtil.nthChildGroupElementOrError(topGroup,
                                                                      SECOND,
                                                                      "Missing edges group");
        Stream<Element> edgeGroups = ElementalUtil.childElementsByTagName(edgeGroup, "g");
        applyMutedStylesToElements(reachableNodes, edgeGroups, DATA_HEAD);
    }

    private void applyMutedStylesToElements(HashSet<String> reachableNodes,
                                            Stream<Element> nodeGroups,
                                            String nodeIdAttributeName) {
        nodeGroups.forEach(
                nodeElement -> {
                    String nodeId = nodeElement.getAttribute(nodeIdAttributeName);
                    if (reachableNodes.contains(nodeId)) {
                        ElementalUtil.removeClassName(nodeElement, WP_GRAPH__G_MUTED);
                    }
                    else {
                        ElementalUtil.addClassName(nodeElement, WP_GRAPH__G_MUTED);
                    }
                }
        );
    }

    private void collectReachableNodesAndEdges(@Nonnull NodeDetails from,
                                               @Nonnull Set<String> reachableNodeIds,
                                               @Nonnull Set<EdgeDetails> edgeDetails,
                                               @Nonnull Set<String> processed) {
        if (processed.contains(from.getId())) {
            return;
        }
        processed.add(from.getId());
        reachableNodeIds.add(from.getId());
        currentGraph.getPredecessors(from.getId())
                    .forEach(node -> collectReachableNodesAndEdges(node, reachableNodeIds, edgeDetails, processed));
    }

    private void handleNodeContextMenuClick(@Nonnull NodeDetails nodeDetails) {

    }

    private void handleNodeClicked(@Nonnull NodeDetails nodeDetails) {

    }

    private void handleNodeDoubleClicked(@Nonnull NodeDetails node) {
        if (currentGraph == null) {
            return;
        }
        navigateToEntity(node);
    }

    private void navigateToEntity(NodeDetails node) {
        Optional<Item<?>> item = EntityItemMapper.getItem(node.getEntity());
        item.ifPresent(i -> {
            PerspectiveId perspectiveId = typePerspectiveMapper
                    .getPerspectiveId(node.getEntity().getEntityType());
            Place place = placeController.getWhere();
            if(place instanceof ProjectViewPlace) {
                ProjectViewPlace nextPlace = ((ProjectViewPlace) place).builder()
                                                                       .withPerspectiveId(perspectiveId)
                                                                       .clearSelection()
                                                                       .withSelectedItem(i)
                                                                       .build();
                placeController.goTo(nextPlace);
            }
        });
    }

    private void resetCurrentGraph() {
        layoutAndDisplayGraph();
    }

    private void layoutAndDisplayGraph() {
        if (layoutCurrentGraph(true)) {
            displayGraphInView();
        }
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
        if (!displayLargeGraph && isLargeGraph(currentEntityGraph)) {
            handleLargeGraph();
            return false;
        }
        doLayout(regenerate);
        return true;
    }

    private void displayGraphInView() {
        if (currentGraph == null) {
            view.clearGraph();
        }
        else {
            view.setGraph(currentEntityGraph.getRootEntity(), currentGraph);
        }
    }

    private boolean isLargeGraph(@Nonnull EntityGraph entityGraph) {
        return entityGraph.getEdgeCount() > LARGE_GRAPH_EDGE_COUNT;
    }

    public Optional<EntityGraph> getCurrentEntityGraph() {
        return Optional.ofNullable(currentEntityGraph);
    }

    private void handleLargeGraph() {
        this.largeGraphHandler.handleLargeGraph();
    }

    interface LargeGraphHandler {
        void handleLargeGraph();
    }

    private void doLayout(boolean regenerate) {
        if (regenerate) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            GWT.log("[VizPresenter] Creating graph");
            currentGraph = new EntityGraph2Graph(view.getTextMeasurer(), currentEntityGraph)
                    .convertGraph();
            stopwatch.stop();
            GWT.log("[VizPresenter] Created graph in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        }
        currentGraph.setRankSep(rankSpacing);
        currentGraph.setMarginX(10);
        currentGraph.setMarginY(10);
        currentGraph.setRankDirBottomToTop();
        currentGraph.setNodeSep(10);
        currentGraph.setRankerToLongestPath();
        GWT.log("[VizPresenter] Laying out graph");
        Stopwatch stopwatch = Stopwatch.createStarted();
        currentGraph.layout();
        stopwatch.stop();
        GWT.log("[VizPresenter] Laid out graph in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");

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

    private void handleEntityGraph(@Nonnull GetEntityGraphResult result) {
        if(result.getEntityGraph().equals(currentEntityGraph)) {
            return;
        }
        EntityGraphSettings entityGraphSettings = result.getEntityGraphSettings();
        this.rankSpacing = (int)(20 * entityGraphSettings
                                            .getRankSpacing());
        currentEntityGraph = result.getEntityGraph();
        view.setPrunedToLimit(currentEntityGraph.isPrunedToEdgeLimit());
        if (entityDisplay != null) {
            entityDisplay.setDisplayedEntity(Optional.of(result.getEntityGraph().getRoot()));
        }
        ImmutableList<FilterName> activeFilters = entityGraphSettings.getActiveFilterNames();
        filterTokenPresenter.setActiveFilters(activeFilters);
        filterTokenPresenter.setFilters(entityGraphSettings.getFilterNames());
        resetCurrentGraph();
    }

    private Boolean isGraphForCurrentEntity(@Nonnull GetEntityGraphResult result) {
        return Objects.equals(currentEntityGraph, result.getEntityGraph());
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    private class HideNodeUiAction extends AbstractUiAction {

        public HideNodeUiAction() {
            super("Hide node");
        }

        @Override
        public void execute() {
            if (currentGraph == null) {
                return;
            }
            view.getMostRecentTargetNode()
                .ifPresent(n -> {
                    currentGraph.removeNode(n.getId());
                    layoutCurrentGraph(false);
                    displayGraphInView();
                });
        }
    }

    private class SelectNodeUiAction extends AbstractUiAction {

        public SelectNodeUiAction() {
            super("Move focus to node");
        }

        @Override
        public void execute() {
            view.getMostRecentTargetNode()
                .ifPresent(EntityGraphPresenter.this::navigateToEntity);
        }
    }


    interface DownloadHandler {
        void handleDownload();
    }

    interface ViewSettingsHandler {
        void handleViewSettings();
    }
}

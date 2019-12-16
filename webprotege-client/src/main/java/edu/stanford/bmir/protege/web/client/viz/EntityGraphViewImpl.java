package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.JSON;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.d3.Selection;
import edu.stanford.bmir.protege.web.client.d3.Transform;
import edu.stanford.bmir.protege.web.client.d3.Zoom;
import edu.stanford.bmir.protege.web.client.graphlib.Graph;
import edu.stanford.bmir.protege.web.client.graphlib.Graph2Svg;
import edu.stanford.bmir.protege.web.client.graphlib.NodeDetails;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;
import edu.stanford.bmir.protege.web.client.viz.EntityGraphPresenter.DownloadHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import elemental.dom.Element;
import elemental.events.Event;
import elemental.events.MouseEvent;
import org.semanticweb.owlapi.model.OWLEntity;
import edu.stanford.bmir.protege.web.client.d3.d3;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.viz.EntityGraphPresenter.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-12
 */
public class EntityGraphViewImpl extends Composite implements EntityGraphView {

    private DownloadHandler downloadHandler = () -> {};

    private ViewSettingsHandler viewSettingsHandler = () -> {};

    interface EntityGraphViewImplUiBinder extends UiBinder<HTMLPanel, EntityGraphViewImpl> {

    }

    private static EntityGraphViewImplUiBinder ourUiBinder = GWT.create(EntityGraphViewImplUiBinder.class);

    @UiField
    HTMLPanel canvas;

    @Inject
    public EntityGraphViewImpl() {
        popupMenu = new PopupMenu();
        initWidget(ourUiBinder.createAndBindUi(this));
        viewSettingsButton.addClickHandler(event -> viewSettingsHandler.handleViewSettings());
    }


    private final PopupMenu popupMenu;

    private final LinkedHashMap<OWLEntity, TransformCoordinates> previousTransforms = new LinkedHashMap<>();

    @UiField
    Button downloadButton;

    @UiField
    TextMeasurerImpl textMeasurer;

    @UiField
    Button viewSettingsButton;

    @UiField
    SimplePanel filterTokenContainer;

    @UiField
    Label prunedMessage;

    private Consumer<NodeDetails> nodeClickHandler = n -> {
    };

    private Consumer<NodeDetails> nodeDoubleClickHandler = n -> {
    };

    private Consumer<NodeDetails> nodeContextClickHandler = n -> {
    };

    private double scaleFactor = 1.0;

    private Optional<NodeDetails> mostRecentTargetNode = Optional.empty();

    private BiConsumer<NodeDetails, Event> nodeMouseOverHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeMouseOutHandler = (n, e) -> {
    };

    @Nonnull
    private Runnable displayLargeGraphRunnable = () -> {
    };

    private double scale = 1.00;

    private Optional<OWLEntity> currentEntity = Optional.empty();

    private Optional<Graph> currentGraph = Optional.empty();

    private BiConsumer<NodeDetails, Event> nodeMouseEnterHandler = (n, e) -> {};

    private BiConsumer<NodeDetails, Event> nodeMouseExitHandler = (n, e) -> {};

    private List<Tooltip> tooltips = new ArrayList<>();

    private void handleKeyDown(KeyDownEvent event) {

    }

    private void handleNodeClick(NodeDetails n, Event e) {
        mostRecentTargetNode = Optional.of(n);
        nodeClickHandler.accept(n);
    }

    @Override
    public void setNodeClickHandler(@Nonnull Consumer<NodeDetails> nodeClickHandler) {
        this.nodeClickHandler = checkNotNull(nodeClickHandler);
    }

    private void handleNodeMouseOver(NodeDetails n, Event e) {
        mostRecentTargetNode = Optional.of(n);
        nodeMouseOverHandler.accept(n, e);
    }

    private void handleNodeMouseOut(NodeDetails n, Event e) {
        mostRecentTargetNode = Optional.of(n);
        nodeMouseOutHandler.accept(n, e);
    }

    @Override
    public void setNodeMouseEnterHandler(BiConsumer<NodeDetails, Event> nodeMouseEnterHandler) {
        this.nodeMouseEnterHandler = checkNotNull(nodeMouseEnterHandler);
    }

    @Override
    public void setNodeMouseLeaveHandler(BiConsumer<NodeDetails, Event> nodeMouseLeaveHandler) {
        this.nodeMouseExitHandler = checkNotNull(nodeMouseLeaveHandler);
    }

    @Override
    public void setNodeDoubleClickHandler(@Nonnull Consumer<NodeDetails> nodeDoubleClickHandler) {
        this.nodeDoubleClickHandler = checkNotNull(nodeDoubleClickHandler);
    }

    private void handleNodeContextMenuClick(NodeDetails n, Event e) {
        e.preventDefault();
        e.stopPropagation();
        mostRecentTargetNode = Optional.of(n);
        handleContextMenu(n, e);
        nodeContextClickHandler.accept(n);
    }

    private void handleContextMenu(@Nonnull NodeDetails nodeDetails, @Nonnull Event event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            popupMenu.show(mouseEvent.getClientX(),
                           mouseEvent.getY());
        }
    }    @Override
    public void setNodeContextMenuClickHandler(@Nonnull Consumer<NodeDetails> nodeContextMenuClickHandler) {
        this.nodeContextClickHandler = checkNotNull(nodeContextMenuClickHandler);
    }

    private void handleNodeDoubleClick(NodeDetails n, Event e) {
        mostRecentTargetNode = Optional.of(n);
        nodeDoubleClickHandler.accept(n);
    }

    @UiHandler("downloadButton")
    public void downloadButtonClick(ClickEvent event) {
        Element e = (Element) canvas.getElement().getElementsByTagName("svg").getItem(0);
        downloadHandler.handleDownload();
    }

    @Override
    public void setNodeMouseOverHandler(BiConsumer<NodeDetails, Event> nodeMouseOverHandler) {
        this.nodeMouseOverHandler = checkNotNull(nodeMouseOverHandler);
    }

    private void applyZoomAndPanTransformFromLastEvent() {
        edu.stanford.bmir.protege.web.client.d3.Event event = d3.getEvent();
        if (event == null) {
            return;
        }
        Transform transform = event.getTransform();
        if (transform == null) {
            return;
        }
        applyZoomAndPanTransform(transform);
    }    @Override
    public void setNodeMouseOutHandler(BiConsumer<NodeDetails, Event> nodeMouseOutHandler) {
        this.nodeMouseOutHandler = checkNotNull(nodeMouseOutHandler);
    }

    private void applyZoomAndPanTransform(Transform transform) {
        Selection svgTopLevelGroupElement = d3.selectElement(getSvgElement()).select("g");
        int transformX = transform.getX();
        int transformY = transform.getY();
        double transformK = transform.getK();
        svgTopLevelGroupElement.attr("transform", "translate(" + transformX + " " + transformY + ")" + " scale(" + transformK + ")");
        scale = transformK;
        GWT.log("[VizViewImpl] Setting previous transform for " + currentEntity + " to " + JSON.stringify(transform));
        TransformCoordinates transformCoords = TransformCoordinates.get(transformX, transformY, transformK);
        currentEntity.ifPresent(entity -> previousTransforms.put(entity, transformCoords));
    }

    @Override
    public void addContextMenuAction(@Nonnull UIAction uiAction) {
        popupMenu.addItem(uiAction);
    }

    @Override
    public void setDownloadHandler(@Nonnull DownloadHandler handler) {
        this.downloadHandler = checkNotNull(handler);
    }



    @Nonnull
    @Override
    public TextMeasurer getTextMeasurer() {
        return textMeasurer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFilterContainer() {
        return filterTokenContainer;
    }

    @Override
    public Element getSvgElement() {
        return (Element) canvas.getElement().getElementsByTagName("svg").getItem(0);
    }

    @Override
    public void clearGraph() {
        clearCanvas();
    }

    @Override
    public Optional<NodeDetails> getMostRecentTargetNode() {
        return mostRecentTargetNode;
    }

    @Override
    public void setGraph(@Nonnull OWLEntity rootEntity, @Nonnull Graph graph) {
        currentEntity = Optional.of(checkNotNull(rootEntity));
        currentGraph = Optional.of(graph);
        tooltips.forEach(Tooltip::dispose);
        tooltips.clear();
        clearGraph();
        Graph2Svg graph2Svg = createGraph2Svg(graph);
        Element svg = graph2Svg.createSvg();
        graph2Svg.getGeneratedTooltips();
        tooltips.addAll(graph2Svg.getGeneratedTooltips());
        Element element = getCanvasElement();
        element.appendChild(svg);
        setupZoom();
    }

    @Override
    public void setPrunedToLimit(boolean b) {
        prunedMessage.setVisible(b);
    }


    private void setupZoom() {
        Selection svgElement = d3.selectElement(getSvgElement());
        Zoom zoom = d3.zoom();
        Object zoomFunc = zoom.on("zoom", this::applyZoomAndPanTransformFromLastEvent);
        svgElement.call(zoomFunc).on("dblclick.zoom", null);
        Transform transform = getInitialTransform(svgElement);
        zoom.transform(svgElement, transform);
    }

    @Nonnull
    private Transform getInitialTransform(@Nonnull Selection svgElement) {
        GWT.log("[VizViewImpl] getInitialTransform: " + currentEntity);
        if (currentEntity.isPresent()) {
            TransformCoordinates transformCoords = previousTransforms.getOrDefault(currentEntity.get(), getDefaultTransform(svgElement));
            return d3.zoomTransform(svgElement.node())
                     .translate(transformCoords.getX(), transformCoords.getY())
                     .scale(transformCoords.getK());
        }
        else {
            return toTransform(getDefaultTransform(svgElement), svgElement);
        }
    }

    private Transform toTransform(TransformCoordinates coordinates, Selection selection) {
        return d3.zoomTransform(selection.node())
                 .translate(coordinates.getX(), coordinates.getY())
                 .scale(coordinates.getK());
    }

    private TransformCoordinates getDefaultTransform(@Nonnull Selection svgElement) {
        double x = 0;
        double y = 0;
        if (currentGraph.isPresent()) {
            Element element = svgElement.node().getParentElement();
            Graph graph = currentGraph.get();
            x = (((element.getClientWidth() - graph.getWidth() * scale)) / 2.0);
            y = (((element.getClientHeight() - graph.getHeight() * scale)) / 2.0);
        }
        return TransformCoordinates.get(x, y, scale);
    }





    @Override
    public void updateGraph(Graph graph) {
        Element canvasElement = getCanvasElement();
        Graph2Svg graph2Svg = createGraph2Svg(graph);
        graph2Svg.updateSvg(canvasElement.getFirstElementChild(), graph);
    }

    @Override
    public void setViewSettingsHandler(@Nonnull ViewSettingsHandler handler) {
        this.viewSettingsHandler = checkNotNull(handler);
    }

    private Graph2Svg createGraph2Svg(Graph graph) {
        Graph2Svg graph2Svg = new Graph2Svg(textMeasurer, graph);
        graph2Svg.setNodeClickHandler(this::handleNodeClick);
        graph2Svg.setNodeDoubleClickHandler(this::handleNodeDoubleClick);
        graph2Svg.setNodeContextMenuClickHandler(this::handleNodeContextMenuClick);
        graph2Svg.setNodeMouseOverHandler(this::handleNodeMouseOver);
        graph2Svg.setNodeMouseOutHandler(this::handleNodeMouseOut);
        graph2Svg.setNodeMouseEnterHandler(this::handleNodeMouseEnter);
        graph2Svg.setNodeMouseLeaveHandler(this::handleNodeMouseExit);
        return graph2Svg;
    }


    private void handleNodeMouseEnter(NodeDetails nodeDetails, Event event) {
        mostRecentTargetNode = Optional.of(nodeDetails);
        nodeMouseEnterHandler.accept(nodeDetails, event);
    }

    private void handleNodeMouseExit(NodeDetails nodeDetails, Event event) {
        mostRecentTargetNode = Optional.of(nodeDetails);
        nodeMouseExitHandler.accept(nodeDetails, event);
    }


    private void clearCanvas() {
        Element canvasElement = getCanvasElement();
        d3.selectElement(canvasElement).selectAll("*").remove();
    }

    private Element getCanvasElement() {
        return (Element) canvas.getElement();
    }
}

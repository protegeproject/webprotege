package edu.stanford.bmir.protege.web.client.graphlib;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.ui.ElementalUtil;
import edu.stanford.bmir.protege.web.client.viz.TextMeasurer;
import elemental.client.Browser;
import elemental.dom.Document;
import elemental.dom.Element;
import elemental.dom.Text;
import elemental.events.Event;
import elemental.svg.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Oct 2018
 */
public class Graph2Svg {

    private static final String CLOSED_ARROW_HEAD_ID = "closedArrowHead";

    private static final String OPEN_ARROW_HEAD_ID = "openArrowHead";

    private static final String SVG_NS = "http://www.w3.org/2000/svg";

    private static final String DATA_TAIL = "data-tail";

    private static final String DATA_HEAD = "data-head";

    private static final String WP_GRAPH = "wp-graph";

    private static final String DATA_NODE_ID = "data-node-id";

    private static final String DATA_TYPE = "data-type";

    private static final String NODE = "node";

    private static final String WP_GRAPH_NODE = "wp-graph__node";

    private static final String WP_GRAPH_NODE_SHAPE = "wp-graph__node__shape";

    private static final String WP_GRAPH_NODE_LABEL = "wp-graph__node__label";

    @Nonnull
    private final TextMeasurer measurer;

    @Nonnull
    private final Graph graph;

    private BiConsumer<NodeDetails, Event> nodeClickHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeDoubleClickHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeContextMenuClickHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeMouseOverHandler = (n, e) -> {
    };


    public Graph2Svg(@Nonnull TextMeasurer measurer, @Nonnull Graph graph) {
        this.measurer = checkNotNull(measurer);
        this.graph = checkNotNull(graph);
    }

    private static String inPixels(double i) {
        return Double.toString(i);
    }

    public Document getDocument() {
        return Browser.getDocument();
    }

    public void setNodeClickHandler(BiConsumer<NodeDetails, Event> nodeClickHandler) {
        this.nodeClickHandler = checkNotNull(nodeClickHandler);
    }

    public void setNodeDoubleClickHandler(BiConsumer<NodeDetails, Event> nodeDoubleClickHandler) {
        this.nodeDoubleClickHandler = checkNotNull(nodeDoubleClickHandler);
    }

    public void setNodeContextMenuClickHandler(BiConsumer<NodeDetails, Event> nodeContextMenuClickHandler) {
        this.nodeContextMenuClickHandler = checkNotNull(nodeContextMenuClickHandler);
    }

    public void setNodeMouseOverHandler(BiConsumer<NodeDetails, Event> nodeMouseOverHandler) {
        this.nodeMouseOverHandler = checkNotNull(nodeMouseOverHandler);
    }

    public void updateSvg(Element svgElement, Graph graph) {
        GWT.log("[Graph2SVG] updating svg");
        if (!checkNotNull(svgElement).getTagName().equals("svg")) {
            throw new RuntimeException("SVG Element Not specified");
        }
        checkNotNull(graph);
        Element rootGroup = ElementalUtil.firstChildGroupElement(svgElement);
        GWT.log("[Graph2SVG] Root Group: " + rootGroup);
        List<Element> groupElements = ElementalUtil.childSvgGroupElements(rootGroup).collect(toList());
        Element nodesGroup = groupElements.get(0);
        Element edgesGroup = groupElements.get(1);
        ElementalUtil.childSvgGroupElements(nodesGroup)
                .forEach(nodeGroupElement -> {
                    String nodeId = nodeGroupElement.getAttribute(DATA_NODE_ID);
                    NodeDetails nodeDetails = graph.node(nodeId);
                    if (nodeDetails == null) {
                        // The node group element needs to be deleted
                        // because it is not in the graph
                        nodesGroup.removeChild(nodeGroupElement);
                    }
                    else {
                        // Update
                        layoutNodeGroup(nodeDetails, nodeGroupElement, false);
                    }
                });
        graph.getNodes()
                .forEach(nd -> {
                    if (Browser.getDocument().getElementById(nd.getId()) == null) {
                        // Need to add to the DOM because it is in
                        // the graph but not in the DOM
                        Element nodeGroup = createNodeGroup(nd);
                        nodesGroup.appendChild(nodeGroup);
                    }
                });
        GWT.log("[Graph2SVG] There are " + ElementalUtil.childSvgGroupElements(edgesGroup).count());

        ElementalUtil.childSvgGroupElements(edgesGroup)
                .forEach(edgeGroupElement -> {
                    String tailId = edgeGroupElement.getAttribute(DATA_TAIL);
                    String headId = edgeGroupElement.getAttribute(DATA_HEAD);
                    EdgeDetails edgeDetails = graph.edge(tailId, headId);
                    if (edgeDetails == null) {
                        edgesGroup.removeChild(edgeGroupElement);
                    }
                    else {
                        //                         Update
                        updateEdgeDetails(edgeDetails, edgeGroupElement);
                    }
                });
        graph.getEdges()
                .forEach(ed -> {
                    if (Browser.getDocument().getElementById(ed.getTailId() + ed.getHeadId()) == null) {
                        // Need to add to the DOM because it is not in the graph
                        Element edgeElement = createEdgeGroup(ed);
                        edgesGroup.appendChild(edgeElement);
                    }
                });
    }

    @Nonnull
    public Element createSvg() {
        Document document = getDocument();
        SVGElement svg = document.createSVGElement();
        // Arrow head defs
        SVGMarkerElement closedArrowHead = createArrowHeadMarker(document, CLOSED_ARROW_HEAD_ID, "wp-graph__edge__arrow-head wp-graph__edge__arrow-head--is-a", true);
        SVGMarkerElement openArrowHead = createArrowHeadMarker(document, OPEN_ARROW_HEAD_ID, "wp-graph__edge__arrow-head wp-graph__edge__arrow-head--rel", false);
        Element defsElement = document.createElementNS(SVG_NS, "defs");
        svg.appendChild(defsElement);
        defsElement.appendChild(openArrowHead);
        defsElement.appendChild(closedArrowHead);

        Element groupElement = document.createElementNS(SVG_NS, "g");
        svg.setAttribute("class", WP_GRAPH);
        svg.appendChild(groupElement);

        Element nodeGroupElement = document.createElementNS(SVG_NS, "g");
        nodeGroupElement.setAttribute("data-nodes", "");
        groupElement.appendChild(nodeGroupElement);

        Element edgeGroupElement = document.createElementNS(SVG_NS, "g");
        edgeGroupElement.setAttribute("data-edges", "");
        groupElement.appendChild(edgeGroupElement);

        int w = graph.getWidth();
        int h = graph.getHeight();
        svg.setAttribute("viewbox", "0 0 " + w + " " + h);
        svg.setAttribute("preserveAspectRatio", "none");
        graph.getNodes()
                .map(this::createNodeGroup)
                .forEach(nodeGroupElement::appendChild);
        graph.getEdges()
                .map(this::createEdgeGroup)
                .forEach(edgeGroupElement::appendChild);
        return svg;
    }

    private SVGMarkerElement createArrowHeadMarker(@Nonnull Document document,
                                                   @Nonnull String id,
                                                   @Nonnull String styleNames,
                                                   boolean closed) {
        SVGMarkerElement marker = getDocument().createSVGMarkerElement();
        marker.setId(id);
        marker.setAttribute("viewBox", "0 0 10 10");
        marker.setAttribute("markerWidth", "6");
        marker.setAttribute("markerHeight", "6");
        marker.setAttribute("refX", "9");
        marker.setAttribute("refY", "5");
        marker.setOrientToAuto();
        marker.setAttribute("class", styleNames);
        SVGPathElement markerPath = document.createSVGPathElement();
        SVGPathSegList markerSegments = markerPath.getPathSegList();
        markerSegments.appendItem(markerPath.createSVGPathSegMovetoAbs(1, 1));
        markerSegments.appendItem(markerPath.createSVGPathSegLinetoAbs(9, 5));
        markerSegments.appendItem(markerPath.createSVGPathSegLinetoAbs(1, 9));
        if (closed) {
            markerSegments.appendItem(markerPath.createSVGPathSegClosePath());
        }
        marker.appendChild(markerPath);
        return marker;
    }

    @Nonnull
    private Element createNodeGroup(@Nonnull NodeDetails nodeDetails) {
        Document document = getDocument();

        Element group = document.createElementNS(SVG_NS, "g");
        group.setId(nodeDetails.getId());
        group.setAttribute(DATA_TYPE, NODE);
        group.setAttribute(DATA_NODE_ID, nodeDetails.getId());
        group.setAttribute("class",
                           (WP_GRAPH_NODE + " " + nodeDetails.getNodeStyleNames()).trim());

        SVGRectElement shape = createRect(nodeDetails);
        shape.setAttribute("class",
                           (WP_GRAPH_NODE_SHAPE + " " + nodeDetails.getNodeShapeStyleNames()).trim());
        shape.setAttribute("pointer-events", "visible");
        SVGTextElement text = createText(nodeDetails);
        text.setAttribute("class",
                          (WP_GRAPH_NODE_LABEL + " " + nodeDetails.getNodeTextStyleNames()).trim());
        group.appendChild(text);
        group.appendChild(shape);
        layoutNodeGroup(nodeDetails, group, true);
        return group;
    }

    private SVGRectElement createRect(@Nonnull NodeDetails nodeDetails) {
        return getDocument().createSVGRectElement();
    }

    private void layoutNodeGroup(@Nonnull NodeDetails nodeDetails,
                                 @Nonnull Element groupElement,
                                 boolean attachHandlers) {
        Element rectElement = ElementalUtil.firstChildElementByTagName(groupElement, "rect");
        measurer.setStyleNames(WP_GRAPH_NODE_SHAPE + " " + nodeDetails.getNodeStyleNames());
        double strokeWidth = measurer.getStrokeWidth();
        double halfStrokeWidth = strokeWidth / 2;

        rectElement.setAttribute("x", inPixels(nodeDetails.getTopLeftX() + halfStrokeWidth));
        rectElement.setAttribute("y", inPixels(nodeDetails.getTopLeftY() + halfStrokeWidth));
        rectElement.setAttribute("rx", "2");
        rectElement.setAttribute("ry", "2");
        if (nodeDetails.getWidth() > strokeWidth && nodeDetails.getHeight() > strokeWidth) {
            rectElement.setAttribute("width", inPixels(nodeDetails.getWidth() - strokeWidth));
            rectElement.setAttribute("height", inPixels(nodeDetails.getHeight() - strokeWidth));
        }

        Element textElement = (Element) groupElement.getElementsByTagName("text").item(0);
        updateTextElement(textElement, nodeDetails.getX(), nodeDetails.getY());

        if (attachHandlers) {
            rectElement.addEventListener(Event.CLICK, evt -> nodeClickHandler.accept(nodeDetails, evt));
            rectElement.addEventListener(Event.DBLCLICK, evt -> nodeDoubleClickHandler.accept(nodeDetails, evt));
            rectElement.addEventListener(Event.CONTEXTMENU, evt -> nodeContextMenuClickHandler.accept(nodeDetails, evt));
            rectElement.addEventListener(Event.MOUSEOVER, evt -> nodeMouseOverHandler.accept(nodeDetails, evt));
        }

    }

    @Nonnull
    private SVGTextElement createText(@Nonnull NodeDetails details) {
        return createTextElement(details.getLabel(), details.getX(), details.getY());
    }

    @Nonnull
    private SVGTextElement createText(@Nonnull EdgeDetails details) {
        return createTextElement(details.getLabel(), details.getX(), details.getY());
    }

    @Nonnull
    private SVGTextElement createTextElement(@Nonnull String text, int x, int y) {
        SVGTextElement textElement = getDocument().createSVGTextElement();
        Text textNode = getDocument().createTextNode(text);
        textElement.appendChild(textNode);
        textElement.setAttribute("text-anchor", "middle");
        textElement.setAttribute("alignment-baseline", "middle");
        textElement.setAttribute("fill", "var(--primary--color)");
        updateTextElement(textElement, x, y);
        return textElement;
    }

    private void updateTextElement(Element textElement, int x, int y) {
        textElement.setAttribute("x", inPixels(x));
        textElement.setAttribute("y", inPixels(y));
    }

    /**
     * Creates an edge group.  This is comprised of a group node that
     * contains a path element and another group element that represents
     * the edge label.  The label group element comprises a text element
     * that contains the label text and a rect element that represents
     * the label area.
     */
    @Nonnull
    Element createEdgeGroup(@Nonnull EdgeDetails edgeDetails) {
        // Edge
        Element groupElement = getDocument().createElementNS(SVG_NS, "g");
        groupElement.setId(edgeDetails.getTailId() + edgeDetails.getHeadId());
        groupElement.setAttribute("data-type", "edge");
        groupElement.setAttribute(DATA_TAIL, edgeDetails.getTailId());
        groupElement.setAttribute(DATA_HEAD, edgeDetails.getHeadId());

        // Path
        SVGPathElement pathElement = getDocument().createSVGPathElement();
        updatePathElement(edgeDetails, pathElement);
        groupElement.appendChild(pathElement);

        // Edge label
        if (!edgeDetails.getLabel().isEmpty()) {
            SVGTextElement text = createText(edgeDetails);
            SVGRectElement textRect = getDocument().createSVGRectElement();

            Element labelGroup = getDocument().createElementNS(SVG_NS, "g");
            labelGroup.appendChild(textRect);
            labelGroup.appendChild(text);


            updateEdgeLabelGroup(edgeDetails, labelGroup);

            groupElement.appendChild(labelGroup);
        }
        return groupElement;
    }

    private void updateEdgeLabelGroup(@Nonnull EdgeDetails edgeDetails,
                                      @Nonnull Element edgeLabelGroupElement) {
        // Update the text element
        Element textElement = ElementalUtil.firstChildElementByTagName(edgeLabelGroupElement, "text");
        textElement.setInnerText(edgeDetails.getLabel());
        textElement.setAttribute("x", inPixels(edgeDetails.getX()));
        textElement.setAttribute("y", inPixels(edgeDetails.getY()));

        // Update the rect element
        Element textRect = ElementalUtil.firstChildElementByTagName(edgeLabelGroupElement, "rect");
        int w = edgeDetails.getLabelWidth();
        int h = edgeDetails.getLabelHeight();
        textRect.setAttribute("width", inPixels(w));
        textRect.setAttribute("height", inPixels(h));
        textRect.setAttribute("x", inPixels(edgeDetails.getX() - (w / 2.0)));
        textRect.setAttribute("y", inPixels(edgeDetails.getY() - (h / 2.0)));
        textRect.setAttribute("class", "wp-graph__edge__label");

    }

    private void updateEdgeDetails(EdgeDetails edgeDetails, Element edgeGroupElement) {
        SVGPathElement pathElement = (SVGPathElement) ElementalUtil.firstChildElementByTagName(edgeGroupElement, "path");
        updatePathElement(edgeDetails, pathElement);
        ElementalUtil.childElementsByTagName(edgeGroupElement, "g").findFirst().ifPresent(edgeLabelGroup -> {
            updateEdgeLabelGroup(edgeDetails, edgeLabelGroup);
        });
    }


    private void updatePathElement(@Nonnull EdgeDetails edgeDetails, SVGPathElement pathElement) {
        pathElement.getPathSegList().clear();
        List<Point> points = edgeDetails.getPoints().collect(toList());
        pathElement.setAttribute("class", edgeDetails.getStyleNames());
        pathElement.setAttribute("fill", "none");
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (i == 0) {
                SVGPathSegMovetoAbs move = pathElement.createSVGPathSegMovetoAbs(point.getX(), point.getY());
                pathElement.getPathSegList().appendItem(move);
            }
            else {
                SVGPathSegLinetoAbs lineTo = pathElement.createSVGPathSegLinetoAbs(point.getX(), point.getY());
                pathElement.getPathSegList().appendItem(lineTo);
            }
        }

        String arrowHeadId = "open".equalsIgnoreCase(edgeDetails.getArrowHeadStyle()) ? OPEN_ARROW_HEAD_ID : CLOSED_ARROW_HEAD_ID;
        pathElement.setAttribute("marker-end", "url(#" + arrowHeadId + ")");
    }

}

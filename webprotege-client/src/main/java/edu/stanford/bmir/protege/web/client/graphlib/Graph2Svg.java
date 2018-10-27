package edu.stanford.bmir.protege.web.client.graphlib;

import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;
import edu.stanford.bmir.protege.web.client.ui.ElementalUtil;
import edu.stanford.bmir.protege.web.client.viz.TextMeasurer;
import edu.stanford.bmir.protege.web.shared.obo.OboId;
import elemental.client.Browser;
import elemental.dom.Document;
import elemental.dom.Element;
import elemental.dom.Text;
import elemental.events.Event;
import elemental.events.EventTarget;
import elemental.svg.SVGElement;
import elemental.svg.SVGMarkerElement;
import elemental.svg.SVGRectElement;
import elemental.svg.SVGTextElement;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.graphlib.GraphConstants.*;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Oct 2018
 */
public class Graph2Svg {


    private static final String SVG_NS = "http://www.w3.org/2000/svg";


    private static final String CLOSED_ARROW_HEAD_ID = "closedArrowHead";

    private static final String OPEN_ARROW_HEAD_ID = "openArrowHead";


    @Nonnull
    private final TextMeasurer measurer;

    @Nonnull
    private final Graph graph;

    private final List<Tooltip> generatedTooltips = new ArrayList<>();

    private BiConsumer<NodeDetails, Event> nodeClickHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeDoubleClickHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeContextMenuClickHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeMouseOverHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeMouseOutHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeMouseEnterHandler = (n, e) -> {
    };

    private BiConsumer<NodeDetails, Event> nodeMouseLeaveHandler = (n, e) -> {
    };


    public Graph2Svg(@Nonnull TextMeasurer measurer, @Nonnull Graph graph) {
        this.measurer = checkNotNull(measurer);
        this.graph = checkNotNull(graph);
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

    public void setNodeMouseOutHandler(BiConsumer<NodeDetails, Event> nodeMouseOutHandler) {
        this.nodeMouseOutHandler = checkNotNull(nodeMouseOutHandler);
    }

    public void setNodeMouseEnterHandler(BiConsumer<NodeDetails, Event> nodeMouseEnterHandler) {
        this.nodeMouseEnterHandler = checkNotNull(nodeMouseEnterHandler);
    }

    public void setNodeMouseLeaveHandler(BiConsumer<NodeDetails, Event> nodeMouseLeaveHandler) {
        this.nodeMouseLeaveHandler = checkNotNull(nodeMouseLeaveHandler);
    }

    public void updateSvg(Element svgElement, Graph graph) {
        if (!checkNotNull(svgElement).getTagName().equals("svg")) {
            throw new RuntimeException("SVG Element Not specified");
        }
        checkNotNull(graph);
        Element rootGroup = ElementalUtil.firstChildGroupElement(svgElement);
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
        graph.getEdgeList()
                .forEach(ed -> {
                    if (Browser.getDocument().getElementById(ed.getTailId() + ed.getHeadId()) == null) {
                        // Need to add to the DOM because it is not in the graph
                        Element edgeElement = createEdgeGroup(ed);
                        edgesGroup.appendChild(edgeElement);
                    }
                });
    }

    public List<Tooltip> getGeneratedTooltips() {
        return new ArrayList<>(generatedTooltips);
    }

    @Nonnull
    public Element createSvg() {
        Document document = getDocument();
        SVGElement svg = document.createSVGElement();
        svg.setAttribute("class", WP_GRAPH);

        svg.addEventListener(Event.CONTEXTMENU, evt -> processNodeEvent(evt, nodeContextMenuClickHandler));
        svg.addEventListener(Event.CLICK, evt -> processNodeEvent(evt, nodeClickHandler));
        svg.addEventListener(Event.DBLCLICK, evt -> processNodeEvent(evt, nodeDoubleClickHandler));
        svg.addEventListener(Event.MOUSEOVER, evt -> processNodeEvent(evt, nodeMouseOverHandler));
        svg.addEventListener(Event.MOUSEOUT, evt -> processNodeEvent(evt, nodeMouseOutHandler));


        // Arrow head defs
        SVGMarkerElement closedArrowHead = createArrowHeadMarker(document, CLOSED_ARROW_HEAD_ID, WP_GRAPH__EDGE__ARROW_HEAD + " " + WP_GRAPH__EDGE__ARROW_HEAD_IS_A, true);
        SVGMarkerElement openArrowHead = createArrowHeadMarker(document, OPEN_ARROW_HEAD_ID, WP_GRAPH__EDGE__ARROW_HEAD + " " + WP_GRAPH__EDGE__ARROW_HEAD_REL, false);
        Element defsElement = document.createElementNS(SVG_NS, "defs");
        svg.appendChild(defsElement);
        defsElement.appendChild(openArrowHead);
        defsElement.appendChild(closedArrowHead);

        Element groupElement = document.createElementNS(SVG_NS, "g");
        svg.appendChild(groupElement);

        Element nodeGroupElement = document.createElementNS(SVG_NS, "g");
        nodeGroupElement.setAttribute(DATA_NODES, "");
        groupElement.appendChild(nodeGroupElement);

        Element edgeGroupElement = document.createElementNS(SVG_NS, "g");
        edgeGroupElement.setAttribute(DATA_EDGES, "");
        groupElement.appendChild(edgeGroupElement);

        int w = graph.getWidth();
        int h = graph.getHeight();
        svg.setAttribute("viewbox", "0 0 " + w + " " + h);
        svg.setAttribute("preserveAspectRatio", "none");
        graph.getNodes()
                .map(this::createNodeGroup)
                .forEach(nodeGroupElement::appendChild);
        graph.getEdgeList()
                .forEach(edge -> {
                    Element element = createEdgeGroup(edge);
                    edgeGroupElement.appendChild(element);
                });
        return svg;
    }

    public Document getDocument() {
        return Browser.getDocument();
    }

    private void processNodeEvent(Event evt, BiConsumer<NodeDetails, Event> consumer) {
        EventTarget target = evt.getTarget();
        ElementalUtil.getAncestorAttribute(target, DATA_NODE_ID)
                .flatMap(graph::getNodeDetails)
                .ifPresent(nodeDetails -> consumer.accept(nodeDetails, evt));
    }

    private SVGMarkerElement createArrowHeadMarker(@Nonnull Document document,
                                                   @Nonnull String id,
                                                   @Nonnull String styleNames,
                                                   boolean closed) {
        SVGMarkerElement marker = document.createSVGMarkerElement();
        marker.setId(id);
        marker.setAttribute("viewBox", "0 0 10 10");
        marker.setAttribute("markerWidth", "6");
        marker.setAttribute("markerHeight", "6");
        marker.setAttribute("refX", "9");
        marker.setAttribute("refY", "5");
        marker.setOrientToAuto();
        marker.setAttribute("class", styleNames);
        Element markerPath = getDocument().createElementNS(SVG_NS, "path");
        String dAttr = "M 1,1 L 9,5 L 1,9";
        if (closed) {
            dAttr = dAttr + " Z";
        }
        markerPath.setAttribute("d", dAttr);
        marker.appendChild(markerPath);
        return marker;
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
        groupElement.setAttribute(DATA_EDGE, "");
        groupElement.setAttribute(DATA_TAIL, edgeDetails.getTailId());
        groupElement.setAttribute(DATA_HEAD, edgeDetails.getHeadId());

        // Path
        Element pathElement = getDocument().createElementNS(SVG_NS, "path");
        updatePathElement(edgeDetails, pathElement);
        groupElement.appendChild(pathElement);

        // Edge label
        if (!edgeDetails.getLabel().isEmpty()) {
            SVGTextElement text = createText(edgeDetails);
            SVGRectElement textRect = getDocument().createSVGRectElement();
            Element labelGroup = getDocument().createElementNS(SVG_NS, "g");
            labelGroup.appendChild(textRect);
            labelGroup.appendChild(text);
            String tooltip = edgeDetails.getLabel()
                    + edgeDetails.getRelation()
                    .flatMap(rel -> OboId.getOboId(rel.getEntity().getIRI())
                            .map(id -> " (" + id + ")"))
                    .orElse("");

            generatedTooltips.add(Tooltip.createOnRight(text, tooltip));
            updateEdgeLabelGroup(edgeDetails, labelGroup);
            groupElement.appendChild(labelGroup);
        }
        return groupElement;
    }

    private void updatePathElement(@Nonnull EdgeDetails edgeDetails, Element pathElement) {
        List<Point> points = edgeDetails.getPoints().collect(toList());
        pathElement.setAttribute("class", edgeDetails.getStyleNames());
        pathElement.setAttribute("fill", "none");
        StringBuilder dAttr = new StringBuilder();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (i == 0) {
                dAttr.append("M ");
            }
            else {
                dAttr.append("L ");
            }

            dAttr.append(point.getX());
            dAttr.append(",");
            dAttr.append(point.getY());
            dAttr.append(" ");
        }
        pathElement.setAttribute("d", dAttr.toString());
        String arrowHeadId = "open".equalsIgnoreCase(edgeDetails.getArrowHeadStyle()) ? OPEN_ARROW_HEAD_ID : CLOSED_ARROW_HEAD_ID;
        pathElement.setAttribute("marker-end", "url(#" + arrowHeadId + ")");
    }

    @Nonnull
    private SVGTextElement createText(@Nonnull EdgeDetails details) {
        return createTextElement(details.getLabel(), details.getX(), details.getY());
    }

    private void updateEdgeLabelGroup(@Nonnull EdgeDetails edgeDetails,
                                      @Nonnull Element edgeLabelGroupElement) {
        // Update the text element
        Element textElement = ElementalUtil.firstChildElementByTagName(edgeLabelGroupElement, "text");
        textElement.setInnerText(edgeDetails.getLabel());
        textElement.setAttribute("x", inPixels(edgeDetails.getX()));
        textElement.setAttribute("y", inPixels(edgeDetails.getY()));
        textElement.setAttribute("class", WP_GRAPH__EDGE__LABEL);

        // Update the rect element
        Element textRect = ElementalUtil.firstChildElementByTagName(edgeLabelGroupElement, "rect");
        int w = edgeDetails.getLabelWidth();
        int h = edgeDetails.getLabelHeight();
        textRect.setAttribute("width", inPixels(w));
        textRect.setAttribute("height", inPixels(h));
        textRect.setAttribute("x", inPixels(edgeDetails.getX() - (w / 2.0)));
        textRect.setAttribute("y", inPixels(edgeDetails.getY() - (h / 2.0)));
        textRect.setAttribute("class", WP_GRAPH__EDGE__LABEL);
    }

    @Nonnull
    private SVGTextElement createTextElement(@Nonnull String text, int x, int y) {
        SVGTextElement textElement = getDocument().createSVGTextElement();
        Text textNode = getDocument().createTextNode(text);
        textElement.appendChild(textNode);
        textElement.setAttribute("text-anchor", "middle");
        textElement.setAttribute("dominant-baseline", "middle");
        textElement.setAttribute("fill", "var(--primary--color)");
        updateTextElement(textElement, x, y);
        return textElement;
    }

    private static String inPixels(double i) {
        return Double.toString(i);
    }

    private void updateTextElement(Element textElement, int x, int y) {
        textElement.setAttribute("x", inPixels(x));
        textElement.setAttribute("y", inPixels(y));
    }

    @Nonnull
    private Element createNodeGroup(@Nonnull NodeDetails nodeDetails) {
        Document document = getDocument();

        Element group = document.createElementNS(SVG_NS, "g");
        group.setId(nodeDetails.getId());
        group.setAttribute(DATA_NODE, "");
        group.setAttribute(DATA_NODE_ID, nodeDetails.getId());
        ElementalUtil.addClassNames(group,
                                    WP_GRAPH__NODE,
                                    nodeDetails.getNodeStyleNames());

        SVGRectElement shape = createRect(nodeDetails);
        IRI iri = nodeDetails.getEntity().getIRI();
        String tooltip = nodeDetails.getLabel()
                + OboId.getOboId(iri)
                .map(id -> " (" + id + ")")
                .orElse("");
        generatedTooltips.add(Tooltip.create(shape, tooltip));
        ElementalUtil.addClassNames(shape,
                                    WP_GRAPH__NODE__SHAPE,
                                    nodeDetails.getNodeShapeStyleNames());
        shape.setAttribute("pointer-events", "visible");

        SVGTextElement text = createText(nodeDetails);
        ElementalUtil.addClassNames(text,
                                    WP_GRAPH__NODE__LABEL,
                                    nodeDetails.getNodeTextStyleNames());
        group.appendChild(shape);
        group.appendChild(text);

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
        measurer.setStyleNames(WP_GRAPH__NODE__SHAPE + " " + nodeDetails.getNodeStyleNames());
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
            rectElement.addEventListener("mouseenter", evt -> processNodeEvent(evt, nodeMouseEnterHandler));
            rectElement.addEventListener("mouseleave", evt -> processNodeEvent(evt, nodeMouseLeaveHandler));
        }

    }

    @Nonnull
    private SVGTextElement createText(@Nonnull NodeDetails details) {
        return createTextElement(details.getLabel(), details.getX(), details.getY());
    }

    private void updateEdgeDetails(EdgeDetails edgeDetails, Element edgeGroupElement) {
        Element pathElement = ElementalUtil.firstChildElementByTagName(edgeGroupElement, "path");
        updatePathElement(edgeDetails, pathElement);
        ElementalUtil.childElementsByTagName(edgeGroupElement, "g").findFirst().ifPresent(edgeLabelGroup -> {
            updateEdgeLabelGroup(edgeDetails, edgeLabelGroup);
        });
    }

}

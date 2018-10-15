package edu.stanford.bmir.protege.web.client.graphlib;

import com.google.gwt.core.client.GWT;
import elemental.client.Browser;
import elemental.dom.Document;
import elemental.dom.Element;
import elemental.svg.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Oct 2018
 */
public class Graph2Svg {

    public Graph2Svg() {
    }

    private static String inPixels(double i) {
        return Double.toString(i);
    }

    public Document getDocument() {
        return Browser.getDocument();
    }

    @Nonnull
    public Element convertToSvg(@Nonnull Graph graph) {
        Document document = getDocument();
        SVGElement svg = document.createSVGElement();
        int w = graph.getWidth() + 10;
        svg.setAttribute("width", inPixels(w));
        int h = graph.getHeight() + 10;
        svg.setAttribute("height", inPixels(h));
        svg.setAttribute("viewbox", "-5 -5 " + w + " " + h);
        graph.getEdges()
                .flatMap(e -> toSvgElements(e).stream())
                .forEach(svg::appendChild);
        graph.getNodes()
                .map(this::toSvgElement)
                .forEach(svg::appendChild);
        return svg;
    }

    @Nonnull
    private Element toSvgElement(@Nonnull NodeDetails nodeDetails) {
        Document document = getDocument();
        Element group = document.createElement("g");
        SVGRectElement rect = createRect(nodeDetails, document);
        SVGTextElement text = createText(nodeDetails);
        group.appendChild(rect);
        group.appendChild(text);
        return group;
    }

    private SVGRectElement createRect(@Nonnull NodeDetails nodeDetails, Document document) {
        SVGRectElement rectElement = document.createSVGRectElement();
        rectElement.setAttribute("x", inPixels(nodeDetails.getTopLeftX()));
        rectElement.setAttribute("y", inPixels(nodeDetails.getTopLeftY()));
        rectElement.setAttribute("rx", "2");
        rectElement.setAttribute("ry", "2");
        rectElement.setAttribute("width", inPixels(nodeDetails.getWidth()));
        rectElement.setAttribute("height", inPixels(nodeDetails.getHeight()));
        rectElement.setAttribute("class", nodeDetails.getStyleNames());
        return rectElement;
    }

    @Nonnull
    private SVGTextElement createText(@Nonnull NodeDetails nodeDetails) {
        SVGTextElement textElement = getDocument().createSVGTextElement();
        textElement.appendChild(getDocument().createTextNode(nodeDetails.getLabel()));
        textElement.setAttribute("text-anchor", "middle");
        textElement.setAttribute("alignment-baseline", "middle");
        textElement.setAttribute("fill", "var(--primary--color)");
        textElement.setAttribute("x", inPixels(nodeDetails.getX()));
        textElement.setAttribute("y", inPixels(nodeDetails.getY()));
        return textElement;
    }

    @Nonnull
    private SVGTextElement createText(@Nonnull EdgeDetails details) {
        SVGTextElement textElement = getDocument().createSVGTextElement();
        textElement.appendChild(getDocument().createTextNode(details.getLabel()));
        textElement.setAttribute("text-anchor", "middle");
        textElement.setAttribute("alignment-baseline", "middle");
        textElement.setAttribute("fill", "var(--primary--color)");
        textElement.setAttribute("x", inPixels(details.getX()));
        textElement.setAttribute("y", inPixels(details.getY()));
        return textElement;
    }

    @Nonnull
    List<Element> toSvgElements(@Nonnull EdgeDetails edgeDetails) {
        GWT.log("[Graph2SVG] Converting Edge: " + edgeDetails.stringify());
        List<Point> points = edgeDetails.getPoints().collect(toList());
        SVGPathElement pathElement = getDocument().createSVGPathElement();
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
        List<Element> edgeElements = new ArrayList<>();
        edgeElements.add(pathElement);

        if (!edgeDetails.getLabel().isEmpty()) {
            int w = edgeDetails.getLabelWidth();
            int h = edgeDetails.getLabelHeight();
            SVGRectElement textRect = getDocument().createSVGRectElement();
            textRect.setAttribute("width", inPixels(w));
            textRect.setAttribute("height", inPixels(h));
            textRect.setAttribute("x", inPixels(edgeDetails.getX() - (w / 2.0)));
            textRect.setAttribute("y", inPixels(edgeDetails.getY() - (h / 2.0)));
            textRect.setAttribute("class", "wp-graph__edge__label");

            SVGTextElement text = createText(edgeDetails);
            Element labelGroup = getDocument().createElement("g");
            labelGroup.appendChild(textRect);
            labelGroup.appendChild(text);
            edgeElements.add(labelGroup);
        }
        return edgeElements;
    }

}

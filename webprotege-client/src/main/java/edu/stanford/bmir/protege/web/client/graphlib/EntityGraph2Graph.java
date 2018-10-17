package edu.stanford.bmir.protege.web.client.graphlib;

import edu.stanford.bmir.protege.web.client.viz.TextDimensions;
import edu.stanford.bmir.protege.web.client.viz.TextMeasurer;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.viz.Edge;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraph;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Oct 2018
 */
public class EntityGraph2Graph {

    private final TextMeasurer textMeasurer;

    public EntityGraph2Graph(TextMeasurer textMeasurer) {
        this.textMeasurer = checkNotNull(textMeasurer);
    }

    @Nonnull
    public Graph convertGraph(@Nonnull EntityGraph graph) {
        Graph g = Graph.create();
        g.setMarginX(10);
        g.setMarginY(10);
        graph.getNodes()
                .stream()
                .map(this::toNodeDetails)
                .forEach(g::addNode);
        graph.getEdges()
                .forEach(edge -> {
                    EdgeDetails edgeDetails = toEdgeDetails(edge);
                    String tailId = toNodeId(edge.getTail());
                    String headId = toNodeId(edge.getHead());
                    g.setEdge(tailId, headId, edgeDetails);
                });
        return g;
    }

    private String getEdgeStyleNames(Edge edge) {
        if(edge.isIsA()) {
            if (edge.getTail().getEntity().isOWLClass()) {
                return "wp-graph__edge wp-graph__edge--is-a wp-graph__edge--cls-cls";
            }
            else {
                if(edge.getHead().getEntity().isOWLClass()) {
                    return "wp-graph__edge wp-graph__edge--is-a wp-graph__edge--ind-cls";
                }
                else {
                    return "wp-graph__edge wp-graph__edge--is-a wp-graph__edge--ind-ind";
                }
            }
        }
        else {
            return "wp-graph__edge wp-graph__edge--rel";
        }
    }

    private String toNodeId(OWLEntityData entityData) {
        return entityData.getEntity().getEntityType().getName() + "(" + entityData.getEntity().getIRI().toString() + ")";
    }

    private NodeDetails toNodeDetails(OWLEntityData node) {
        TextDimensions textDimensions = getTextDimensions(node);
        NodeDetails nodeDetails = new NodeDetails(node,
                                                  textDimensions.getWidth() + 4,
                                                  textDimensions.getHeight() + 2,
                                                  node.getBrowserText());
        String styleNames = getStyleNames(node);
        nodeDetails.setStyleNames(styleNames);
        return nodeDetails;
    }

    private String getStyleNames(OWLEntityData node) {
        if(node.getEntity().isOWLClass()) {
            return "wp-graph__node wp-graph__node--class";
        }
        else if(node.getEntity().isOWLNamedIndividual()) {
            return "wp-graph__node wp-graph__node--individual";
        }
        else {
            return "wp-graph__node";
        }
    }

    private TextDimensions getTextDimensions(OWLEntityData node) {
        textMeasurer.setStyleNames(getStyleNames(node));
        return textMeasurer.getTextDimensions(node.getBrowserText());
    }

    private EdgeDetails toEdgeDetails(Edge edge) {
        EdgeDetails edgeDetails = new EdgeDetails(edge.getLabel());
        edgeDetails.setStyleNames(getEdgeStyleNames(edge));
        if (!edge.isIsA()) {
            textMeasurer.setStyleNames("wp-graph__edge__label");
            TextDimensions dimensions = textMeasurer.getTextDimensions(edge.getLabel());
            edgeDetails.setLabelWidth(dimensions.getWidth());
            edgeDetails.setLabelHeight(dimensions.getHeight());
            edgeDetails.setLabelPosCenter();
            edgeDetails.setArrowHeadStyle("open");
        }
        else {
            edgeDetails.setLabelHeight(0);
            edgeDetails.setLabelWidth(0);
            edgeDetails.setArrowHeadStyle("closed");
        }
        return edgeDetails;
    }
}

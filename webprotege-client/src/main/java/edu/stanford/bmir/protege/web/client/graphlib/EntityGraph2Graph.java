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
        nodeDetails.setNodeStyleNames(getNodeStyleNames(node));
        nodeDetails.setNodeShapeStyleNames(getNodeShapeStyleNames(node));
        nodeDetails.setNodeTextStyleNames(getNodeTextStyleNames(node));
        return nodeDetails;
    }

    @Nonnull
    private String getNodeStyleNames(OWLEntityData node) {
        return "";
    }

    @Nonnull
    private String getNodeShapeStyleNames(OWLEntityData node) {
        if(node.getEntity().isOWLClass()) {
            return "wp-graph__node__shape--class";
        }
        else if(node.getEntity().isOWLNamedIndividual()) {
            return "wp-graph__node__shape--individual";
        }
        else {
            return "";
        }
    }

    @Nonnull
    private String getNodeTextStyleNames(OWLEntityData node) {
        return "";
    }

    private TextDimensions getTextDimensions(OWLEntityData node) {
        textMeasurer.setStyleNames("wp_graph__node__shape " + getNodeShapeStyleNames(node));
        return textMeasurer.getTextDimensions(node.getBrowserText());
    }

    private EdgeDetails toEdgeDetails(Edge edge) {
        EdgeDetails edgeDetails = new EdgeDetails(edge.getLabel());
        edgeDetails.setTailId(toNodeId(edge.getTail()));
        edgeDetails.setHeadId(toNodeId(edge.getHead()));
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
            edgeDetails.setArrowHeadStyle("closed");
        }
        return edgeDetails;
    }
}

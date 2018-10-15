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
                    if(edge.isIsA()) {
                        edgeDetails.setStyleNames("wp-graph__edge wp-graph__edge--is-a");
                    }
                    else {
                        edgeDetails.setStyleNames("wp-graph__edge wp-graph__edge--rel");
                    }
                });
        g.setRankDirBottomToTop();
        g.setNodeSep(10);
        g.setRankSep(30);
        g.setRankerToLongestPath();
        return g;
    }

    private String toNodeId(OWLEntityData entityData) {
        return entityData.getEntity().getEntityType().getName() + "(" + entityData.getEntity().getIRI().toString() + ")";
    }

    private NodeDetails toNodeDetails(OWLEntityData node) {
        TextDimensions textDimensions = getTextDimensions(node);
        NodeDetails nodeDetails = new NodeDetails(toNodeId(node),
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
        return new EdgeDetails(edge.getLabel());
    }
}

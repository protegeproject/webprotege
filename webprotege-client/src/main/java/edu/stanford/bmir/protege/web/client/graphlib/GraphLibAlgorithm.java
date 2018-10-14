package edu.stanford.bmir.protege.web.client.graphlib;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;

import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Oct 2018
 */
@JsType(isNative = true, namespace = "graphlib", name = "alg")
public class GraphLibAlgorithm {

    public static native String [] preorder(Graph g, String nodeId);

    public static native String [] postorder(Graph g, String nodeId);

    public static native boolean isAcyclic(Graph g);

    @JsOverlay
    public static Stream<NodeDetails> getNodesInPreorder(Graph g, NodeDetails fromNode) {
        String [] nodes = preorder(g, fromNode.getId());
        return Stream.of(nodes).map(nId -> g.node(nId));
    }
}

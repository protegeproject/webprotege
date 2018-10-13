package edu.stanford.bmir.protege.web.server.viz;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue
public abstract class Graph {

    public static Graph create(OWLEntityData root, ImmutableSet<Edge> edges) {
        ImmutableSetMultimap.Builder<OWLEntityData, String> byTail = ImmutableSetMultimap.builder();
        ImmutableSetMultimap.Builder<OWLEntityData, Edge> byTailEdge = ImmutableSetMultimap.builder();
        ImmutableMultimap.Builder<String, Edge> byDescriptor = ImmutableMultimap.builder();
        ImmutableSet.Builder<OWLEntityData> nodes = ImmutableSet.builder();
        for (Edge edge : edges) {
            nodes.add(edge.getTail());
            nodes.add(edge.getHead());
            byTailEdge.put(edge.getTail(), edge);
            byTail.put(edge.getTail(), edge.getRelationshipDescriptor());
            byDescriptor.put(edge.getRelationshipDescriptor(), edge);
        }
        return new AutoValue_Graph(root, nodes.build(), edges, byTailEdge.build(), byTail.build(), byDescriptor.build());
    }

    public abstract OWLEntityData getRoot();

    public OWLEntity getRootEntity() {
        return getRoot().getEntity();
    }

    public abstract ImmutableSet<OWLEntityData> getNodes();

    public abstract ImmutableSet<Edge> getEdges();

    public abstract ImmutableMultimap<OWLEntityData, Edge> getEdgesByTailNode();

    public abstract ImmutableMultimap<OWLEntityData, String> getDescriptorsByTailNode();

    public abstract ImmutableMultimap<String, Edge> getEdgesByDescriptor();

    public ImmutableSetMultimap<OWLEntityData, Edge> getEdgesByCluster(OWLEntity rootNode) {
        Set<OWLEntityData> processed = new HashSet<>();
        ImmutableSetMultimap.Builder<OWLEntityData, Edge> resultBuilder = ImmutableSetMultimap.builder();
        getEdges().forEach(e -> {
                               Set<OWLEntityData> tailClusters = new HashSet<>();
                               Set<OWLEntityData> headClusters = new HashSet<>();
                               if (!rootNode.equals(e.getTail().getEntity())) {
                                   getIsAClusters(tailClusters, e.getTail(), new HashSet<>());
                                   getIsAClusters(headClusters, e.getHead(), new HashSet<>());
                                   if(tailClusters.equals(headClusters)) {
                                       tailClusters.forEach(c -> resultBuilder.put(c, e));
                                   }
                               }
                           }
        );
        return resultBuilder.build();
    }

    private void getIsAClusters(Set<OWLEntityData> clutersBuilder,
                                OWLEntityData node,
                                Set<OWLEntityData> processed) {
        if (processed.contains(node)) {
            return;
        }
        processed.add(node);
        long isACount = getEdgesByTailNode().get(node)
                .stream()
                .filter(Edge::isIsA)
                .peek(e -> getIsAClusters(clutersBuilder, e.getHead(), processed))
                .count();
        if (isACount == 0) {
            clutersBuilder.add(node);
        }

    }

}

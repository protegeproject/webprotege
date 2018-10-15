package edu.stanford.bmir.protege.web.shared.viz;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.gwt.user.client.rpc.IsSerializable;
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
@GwtCompatible(serializable = true)
public abstract class EntityGraph {



    public static EntityGraph create(OWLEntityData root, ImmutableSet<Edge> edges) {
        return new AutoValue_EntityGraph(root, edges);
    }

    public abstract OWLEntityData getRoot();

    public OWLEntity getRootEntity() {
        return getRoot().getEntity();
    }

    public ImmutableSet<OWLEntityData> getNodes() {
        ImmutableSet.Builder<OWLEntityData> nodes = ImmutableSet.builder();
        for (Edge edge : getEdges()) {
            nodes.add(edge.getTail());
            nodes.add(edge.getHead());
        }
        return nodes.build();
    }

    public abstract ImmutableSet<Edge> getEdges();

    public ImmutableMultimap<OWLEntityData, Edge> getEdgesByTailNode() {
        ImmutableMultimap.Builder<OWLEntityData, Edge> result = ImmutableMultimap.builder();
        for (Edge edge : getEdges()) {
            result.put(edge.getTail(), edge);
        }
        return result.build();
    }

    public ImmutableMultimap<OWLEntityData, String> getDescriptorsByTailNode() {
        ImmutableMultimap.Builder<OWLEntityData, String> result = ImmutableMultimap.builder();
        for (Edge edge : getEdges()) {
            result.put(edge.getTail(), edge.getRelationshipDescriptor());
        }
        return result.build();
    }

    public ImmutableMultimap<String, Edge> getEdgesByDescriptor() {
        ImmutableMultimap.Builder<String, Edge> result = ImmutableMultimap.builder();
        for (Edge edge : getEdges()) {
            result.put(edge.getRelationshipDescriptor(), edge);
        }
        return result.build();
    }

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

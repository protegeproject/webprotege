package edu.stanford.bmir.protege.web.server.viz;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue
public abstract class Graph {

    public static Graph create(ImmutableSet<Edge> edges) {
        ImmutableSetMultimap.Builder<OWLEntityData, String> byTail = ImmutableSetMultimap.builder();
        ImmutableSetMultimap.Builder<OWLEntityData, Edge> byTailEdge = ImmutableSetMultimap.builder();
        ImmutableMultimap.Builder<String, Edge> byDescriptor = ImmutableMultimap.builder();
        ImmutableSet.Builder<OWLEntityData> nodes = ImmutableSet.builder();
        for(Edge edge : edges) {
            nodes.add(edge.getTail());
            nodes.add(edge.getHead());
            byTailEdge.put(edge.getTail(), edge);
            byTail.put(edge.getTail(), edge.getRelationshipDescriptor());
            byDescriptor.put(edge.getRelationshipDescriptor(), edge);
        }
         return new AutoValue_Graph(nodes.build(), edges, byTailEdge.build(), byTail.build(), byDescriptor.build());
    }

    public abstract ImmutableSet<OWLEntityData> getNodes();

    public abstract ImmutableSet<Edge> getEdges();

    public abstract ImmutableMultimap<OWLEntityData, Edge> getEdgesByTailNode();

    public abstract ImmutableMultimap<OWLEntityData, String> getDescriptorsByTailNode();

    public abstract ImmutableMultimap<String, Edge> getEdgesByDescriptor();

}

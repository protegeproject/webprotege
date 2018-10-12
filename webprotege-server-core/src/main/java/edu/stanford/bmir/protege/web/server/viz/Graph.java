package edu.stanford.bmir.protege.web.server.viz;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

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
        for(Edge edge : edges) {
            byTailEdge.put(edge.getTail(), edge);
            byTail.put(edge.getTail(), edge.getRelationshipDescriptor());
            byDescriptor.put(edge.getRelationshipDescriptor(), edge);
        }
         return new AutoValue_Graph(edges, byTailEdge.build(), byTail.build(), byDescriptor.build());
    }

    public abstract ImmutableSet<Edge> getEdges();

    public abstract ImmutableMultimap<OWLEntityData, Edge> getEdgesByTailNode();

    public abstract ImmutableMultimap<OWLEntityData, String> getDescriptorsByTailNode();

    public abstract ImmutableMultimap<String, Edge> getEdgesByDescriptor();
}

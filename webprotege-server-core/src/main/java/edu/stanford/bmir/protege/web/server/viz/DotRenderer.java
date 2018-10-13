package edu.stanford.bmir.protege.web.server.viz;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class DotRenderer {


    @Nonnull
    private final ProjectId projectId;


    @Nonnull
    private final PlaceUrl placeUrl;

    @Nonnull
    private final Graph graph;

    @AutoFactory
    @Inject
    public DotRenderer(@Provided @Nonnull ProjectId projectId,
                       @Provided @Nonnull PlaceUrl placeUrl,
                       @Nonnull Graph graph) {
        this.projectId = checkNotNull(projectId);
        this.placeUrl = checkNotNull(placeUrl);
        this.graph = checkNotNull(graph);
    }



    public void render(Writer writer) {
        Multimap<OWLEntityData, String> descriptorsByTailNode = graph.getDescriptorsByTailNode();
        Multimap<String, Edge> edgesByDescriptor = graph.getEdgesByDescriptor();
        PrintWriter pw = new PrintWriter(writer);
        pw.println("digraph \"${title}\" {");
        pw.println("graph [fontname =\"${fontname}\"];");
        pw.println("layout=${layout}; rankdir=${rankdir}; ranksep=${ranksep} nodesep=${nodesep}; concentrate=${concentrate}; splines=${splines};");
        pw.println("node [penwidth=0.5; style=${node.style} fontname=\"${fontname}\" shape=${node.shape}; fontsize=8; margin=${node.margin} width=0 height=0; color=\"${node.color}\" fontcolor=\"${node.fontcolor}\"];");
        pw.println("edge [penwidth=0.5; fontsize=8; fontname=\"${fontname}\" arrowsize=${edge.arrowsize};];");
        renderNodes(graph, pw);
        renderEdgesWithoutClusters(graph, descriptorsByTailNode, edgesByDescriptor, pw);
//        renderEdgesWithClustering(entity, graph, pw);
        pw.print("}");
        pw.flush();
    }

    private void renderNodes(Graph graph, PrintWriter pw) {
        graph.getNodes().forEach(node -> {
            String entityUrl = placeUrl.getEntityUrl(projectId, node.getEntity());
            pw.printf("\"%s\" [href=\"%s\"; color=\"%s\"]\n",
                      node.getBrowserText(),
                      entityUrl,
                      node.getEntity().isOWLClass() ? "${node.color}" : "${node.ind.color}");
        });
    }

    private void renderEdgesWithoutClusters(Graph graph, Multimap<OWLEntityData, String> descriptorsByTailNode, Multimap<String, Edge> edgesByDescriptor, PrintWriter pw) {
        descriptorsByTailNode.forEach((tailNode, descriptor) -> {
            edgesByDescriptor.get(descriptor)
                    .stream()
                    .filter(e -> e.getTail().equals(tailNode))
                    .map(e -> toEdgeRendering(graph, e, true))
                    .forEach(pw::println);
        });
    }

    private void renderEdgesWithClustering(OWLEntity entity, Graph graph, PrintWriter pw) {
        Set<Edge> toRender = new HashSet<>(graph.getEdges());
        ImmutableMultimap<OWLEntityData, Edge> clusters = graph.getEdgesByCluster(entity);
        for(OWLEntityData cluster : clusters.keySet()) {
            pw.printf("subgraph \"cluster_%s\"{style=filled; color=\"#f9f9f9\"", cluster.getBrowserText());
            ImmutableCollection<Edge> edges = clusters.get(cluster);
            graph.getDescriptorsByTailNode().forEach((tailNode, descriptor) -> {
                graph.getEdgesByDescriptor().get(descriptor)
                        .stream()
                        .filter(edges::contains)
                        .filter(e -> e.getTail().equals(tailNode))
                        .peek(toRender::remove)
                        .map(e -> toEdgeRendering(graph, e, true))
                        .forEach(pw::println);
            });
            pw.println("}");
        }
        toRender.stream()
                .map(e -> toEdgeRendering(graph, e, false))
                .forEach(pw::println);
    }

    private String toEdgeRendering(Graph graph, Edge edge, boolean constraint) {
        String l = edge.getLabel();
        int outDegree = graph.getEdgesByTailNode().size();
        if(edge.isIsA()) {
            return String.format("\"%s\" -> \"%s\" [fillcolor=none; color=\"${edge.isa.color}\"; style=%s];",
                                 edge.getTail().getBrowserText(),
                                 edge.getHead().getBrowserText(),
                                 edge.getTail().getEntity().isOWLNamedIndividual() ? "dashed" : "solid");
        }
        else {
            return String.format("\"%s\" -> \"%s\" [color=\"${edge.rel.color}\"; label=\"%s\" fontcolor=\"${edge.rel.color}\"; arrowhead=vee; constraint=%s];",
                                 edge.getTail().getBrowserText(),
                                 edge.getHead().getBrowserText(),
                                 l,
                                 constraint);
        }
    }
}

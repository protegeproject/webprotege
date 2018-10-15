package edu.stanford.bmir.protege.web.server.viz;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.viz.Edge;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraph;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

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
    private final EntityGraph graph;

    @AutoFactory
    @Inject
    public DotRenderer(@Provided @Nonnull ProjectId projectId,
                       @Provided @Nonnull PlaceUrl placeUrl,
                       @Nonnull EntityGraph graph) {
        this.projectId = checkNotNull(projectId);
        this.placeUrl = checkNotNull(placeUrl);
        this.graph = checkNotNull(graph);
    }


    public void render(Writer writer) {
        PrintWriter pw = new PrintWriter(writer);
        pw.println("digraph \"${title}\" {");
        renderOptionsAndDefaults(pw);
        renderNodes(pw);
        renderEdgesWithoutClusters(pw);
        //        renderEdgesWithClustering(entity, graph, pw);
        pw.print("}");
        pw.flush();
    }

    private void renderOptionsAndDefaults(PrintWriter pw) {
        pw.println("graph [fontname =\"${fontname}\"];");
        pw.println("layout=${layout};");
        pw.println("rankdir=${rankdir};");
        pw.println("ranksep=${ranksep};");
        pw.println("nodesep=${nodesep};");
        pw.println("concentrate=${concentrate};");
        pw.println("splines=${splines};");
        pw.println("node [penwidth=0.5; style=${node.style} fontname=\"${fontname}\" shape=${node.shape}; fontsize=8; margin=${node.margin} width=0 height=0; color=\"${node.color}\" fontcolor=\"${node.fontcolor}\"];");
        pw.println("edge [penwidth=0.5; fontsize=8; fontname=\"${fontname}\" arrowsize=${edge.arrowsize};];");
    }

    private void renderNodes(PrintWriter pw) {
        graph.getNodes().forEach(node -> {
            String entityUrl = placeUrl.getEntityUrl(projectId, node.getEntity());
            pw.printf("\"%s\" [href=\"%s\"; color=\"%s\"]\n",
                      node.getBrowserText(),
                      entityUrl,
                      node.getEntity().isOWLClass() ? "${node.color}" : "${node.ind.color}");
        });
    }

    private Stream<Edge> getOrderedEdges(@Nonnull Set<Edge> include) {
        // By Edge type
        return graph.getDescriptorsByTailNode()
                .entries()
                .stream()
                .flatMap(entry -> {
                    OWLEntityData tailNode = entry.getKey();
                    String descriptor = entry.getValue();
                    return graph.getEdgesByDescriptor()
                            .get(descriptor)
                            .stream()
                            .filter(include::contains)
                            .filter(edge -> edge.getTail().equals(tailNode));
                });
    }

    private void renderEdgesWithoutClusters(PrintWriter pw) {
        getOrderedEdges(graph.getEdges())
                .map(e -> renderEdge(e, true))
                .forEach(pw::println);
    }

    private void renderEdgesWithClustering(OWLEntity entity, PrintWriter pw) {
        Set<Edge> toRender = new HashSet<>(graph.getEdges());
        ImmutableSetMultimap<OWLEntityData, Edge> clusters = graph.getEdgesByCluster(entity);
        for (OWLEntityData cluster : clusters.keySet()) {
            pw.printf("subgraph \"cluster_%s\"{style=solid; color=\"#f9f9f9\"", cluster.getBrowserText());
            ImmutableSet<Edge> edges = clusters.get(cluster);
            getOrderedEdges(edges)
                    .peek(toRender::remove)
                    .map(e -> renderEdge(e, true))
                    .forEach(pw::println);
            pw.println("}");
        }
        toRender.stream()
                .map(e -> renderEdge(e, false))
                .forEach(pw::println);
    }

    private String renderEdge(Edge edge, boolean constraint) {
        String l = edge.getLabel();
        int outDegree = graph.getEdgesByTailNode().size();
        if (edge.isIsA()) {
            return renderIsAEdge(edge);
        }
        else {
            return renderRelEdge(edge, constraint, l);
        }
    }

    private String renderRelEdge(Edge edge, boolean constraint, String l) {
        return String.format("\"%s\" -> \"%s\" [color=\"${edge.rel.color}\"; label=\"%s\" fontcolor=\"${edge.rel.color}\"; arrowhead=vee; constraint=%s];",
                             edge.getTail().getBrowserText(),
                             edge.getHead().getBrowserText(),
                             l,
                             constraint);
    }

    private String renderIsAEdge(Edge edge) {
        return String.format("\"%s\" -> \"%s\" [fillcolor=none; color=\"${edge.isa.color}\"; style=%s];",
                             edge.getTail().getBrowserText(),
                             edge.getHead().getBrowserText(),
                             edge.getTail().getEntity().isOWLNamedIndividual() ? "dashed" : "solid");
    }
}

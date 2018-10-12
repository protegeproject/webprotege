package edu.stanford.bmir.protege.web.server.viz;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
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
    private final OWLOntology ontology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final OWLEntity root;

    @Nonnull
    private final PlaceUrl placeUrl;

    @AutoFactory
    @Inject
    public DotRenderer(@Provided @Nonnull ProjectId projectId, @Provided @Nonnull OWLOntology ontology,
                       @Provided @Nonnull RenderingManager renderingManager,
                       @Nonnull OWLEntity root, @Provided @Nonnull PlaceUrl placeUrl) {
        this.projectId = checkNotNull(projectId);
        this.ontology = checkNotNull(ontology);
        this.renderingManager = checkNotNull(renderingManager);
        this.root = checkNotNull(root);
        this.placeUrl = checkNotNull(placeUrl);
    }

    private Graph createGraph() {
        LinkedHashSet<Edge> edges = new LinkedHashSet<>();
        createGraph(root, edges, new HashSet<>());
        return Graph.create(ImmutableSet.copyOf(edges));
    }

    private void createGraph(@Nonnull OWLEntity entity, Set<Edge> g, Set<OWLEntity> processed) {
        if(processed.contains(entity)) {
            return;
        }
        processed.add(entity);
        if(!entity.isOWLClass()) {
            return;
        }
        OWLClass cls = entity.asOWLClass();
        Stream<OWLSubClassOfAxiom> subClsAx = ontology.getSubClassAxiomsForSubClass(cls).stream();
        Stream<OWLSubClassOfAxiom> defs =
                ontology.getEquivalentClassesAxioms(cls)
                        .stream()
                        .flatMap(ax -> ax.asOWLSubClassOfAxioms().stream());
        Streams.concat(subClsAx, defs)
                .filter(ax -> !ax.getSubClass().isAnonymous())
                .forEach(ax -> addEdge(cls, g, processed, ax));
    }

    private void addEdge(OWLClass subCls, Set<Edge> edges, Set<OWLEntity> processed, OWLSubClassOfAxiom ax) {
        OWLEntityData subClsData = renderingManager.getRendering(subCls);
        ax.getSuperClass().asConjunctSet()
                .stream()
                .filter(c -> !c.isOWLThing())
                .forEach(superClass -> {
                    if(!superClass.isAnonymous()) {
                        OWLClass superCls = superClass.asOWLClass();
                        OWLEntityData superClsData = renderingManager.getRendering(superCls);
                        Edge edge = IsAEdge.get(subClsData, superClsData);
                        edges.add(edge);
                        createGraph(superCls, edges, processed);
                    }
                    else {
                        if(superClass instanceof OWLObjectSomeValuesFrom) {
                            OWLObjectSomeValuesFrom svf = (OWLObjectSomeValuesFrom) superClass;
                            OWLClassExpression filler = svf.getFiller();
                            if(!filler.isAnonymous()) {
                                OWLClass fillerCls = filler.asOWLClass();
                                OWLClassData fillerClsData = renderingManager.getRendering(fillerCls);
                                OWLObjectProperty prop = svf.getProperty().asOWLObjectProperty();
                                OWLEntityData propData = renderingManager.getRendering(prop);
                                Edge edge = RelationshipEdge.get(subClsData, fillerClsData, propData);
                                edges.add(edge);
                                createGraph(fillerCls, edges, processed);
                            }
                        }
                    }
                });
    }

    private OWLEntityData toEntity(@Nonnull OWLClass cls) {
        String ren = getRendering(cls);
        return renderingManager.getRendering(cls);
    }

    private String getRendering(@Nonnull OWLEntity cls) {
        String shortForm = cls.getIRI().getShortForm();
        return EntitySearcher.getAnnotationAssertionAxioms(cls, ontology)
                .stream()
                .filter(ax -> ax.getProperty().isLabel())
                .map(ax -> ax.getValue())
                .map(v -> v.asLiteral())
                .filter(v -> v.isPresent())
                .map(v -> v.get())
                .map(v -> v.getLiteral())
                .map(l -> {
                    if(cls.isOWLClass()) {
                        return l.replace(" ", "\\n");
                    }
                    else {
                        return l;
                    }
                })
                .findFirst()
                .orElse(shortForm);
    }

    public void render(Writer writer) {
        Graph graph = createGraph();
        Multimap<OWLEntityData, String> descriptorsByTailNode = graph.getDescriptorsByTailNode();
        Multimap<String, Edge> edgesByDescriptor = graph.getEdgesByDescriptor();
        PrintWriter pw = new PrintWriter(writer);
        pw.println("digraph \"${title}\" {");
        pw.println("layout=${layout}; rankdir=${rankdir}; ranksep=${ranksep} nodesep=${nodesep}; concentrate=${concentrate}; splines=${splines};");
        pw.println("node [style=${node.style} shape=${node.shape}; fontsize=9; margin=${node.margin} width=0 height=0; color=\"${node.color}\" fontcolor=\"${node.fontcolor}\"];");
        pw.println("edge [fontsize=9; arrowsize=${edge.arrowsize};];");
        graph.getNodes().forEach(node -> {
            pw.printf("\"%s\" [href=\"%s\"]\n",
                      node.getBrowserText(),
                      placeUrl.getEntityUrl(projectId, node.getEntity()));
        });
        descriptorsByTailNode.forEach((tail, descriptor) -> {
            String block = edgesByDescriptor.get(descriptor)
                    .stream()
                    .filter(e -> e.getTail().equals(tail))
                    .map(e -> toEdgeRendering(graph, e))
                    .collect(joining(";\n"));
            pw.print(block);
            pw.println(";");
        });
        pw.print("}");
        pw.flush();
    }

    private String toEdgeRendering(Graph graph, Edge edge) {
        String l = edge.getLabel();
        int outDegree = graph.getEdgesByTailNode().size();
        if(edge.isIsA()) {
            return String.format("\"%s\" -> \"%s\" [fillcolor=none; color=\"${edge.isa.color}\";]",
                                 edge.getTail().getBrowserText(),
                                 edge.getHead().getBrowserText());
        }
        else {
            return String.format("\"%s\" -> \"%s\" [color=\"${edge.rel.color}\"; label=\"%s\" fontcolor=\"${edge.rel.color}\";]",
                                 edge.getTail().getBrowserText(),
                                 edge.getHead().getBrowserText(),
                                 l);
        }
    }
}

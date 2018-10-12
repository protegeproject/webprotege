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
        if(entity.isOWLClass()) {
            OWLClass cls = entity.asOWLClass();
            renderClass(g, processed, cls);
        }
        else if(entity.isOWLNamedIndividual()) {
            OWLNamedIndividual ind = entity.asOWLNamedIndividual();
            renderIndividual(g, processed, ind);
        }
    }

    private void renderIndividual(Set<Edge> g, Set<OWLEntity> processed, OWLNamedIndividual individual) {
        OWLNamedIndividualData indvidualData = renderingManager.getRendering(individual);
        ontology.getClassAssertionAxioms(individual)
                .stream()
                .filter(ax -> !ax.getClassExpression().isOWLThing())
                .filter(ax -> !ax.getClassExpression().isAnonymous())
                .forEach(ax -> {
                    OWLClass cls = ax.getClassExpression().asOWLClass();
                    OWLClassData clsData = renderingManager.getRendering(cls);
                    g.add(IsAEdge.get(indvidualData, clsData));
                    renderClass(g, processed, cls);
                });
        ontology.getObjectPropertyAssertionAxioms(individual)
                .stream()
                .filter(ax -> ax.getObject().isNamed())
                .filter(ax -> !ax.getProperty().isAnonymous())
                .forEach(ax -> {
                    OWLNamedIndividual object = ax.getObject().asOWLNamedIndividual();
                    OWLNamedIndividualData objectData = renderingManager.getRendering(object);
                    OWLObjectPropertyData propertyData = renderingManager.getRendering(ax.getProperty().asOWLObjectProperty());
                    g.add(RelationshipEdge.get(indvidualData, objectData, propertyData));
                    renderIndividual(g, processed, object);
                });

    }

    private void renderClass(Set<Edge> g, Set<OWLEntity> processed, OWLClass cls) {
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
        pw.println("graph [fontname =\"${fontname}\"];");
        pw.println("layout=${layout}; rankdir=${rankdir}; ranksep=${ranksep} nodesep=${nodesep}; concentrate=${concentrate}; splines=${splines};");
        pw.println("node [penwidth=0.5; style=${node.style} fontname=\"${fontname}\" shape=${node.shape}; fontsize=9; margin=${node.margin} width=0 height=0; color=\"${node.color}\" fontcolor=\"${node.fontcolor}\"];");
        pw.println("edge [penwidth=0.5; fontsize=9; fontname=\"${fontname}\" arrowsize=${edge.arrowsize};];");
        graph.getNodes().forEach(node -> {
            String entityUrl = placeUrl.getEntityUrl(projectId, node.getEntity());
            pw.printf("\"%s\" [href=\"%s\"; color=\"%s\"]\n",
                      node.getBrowserText(),
                      entityUrl,
                      node.getEntity().isOWLClass() ? "${node.color}" : "${node.ind.color}");
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
            return String.format("\"%s\" -> \"%s\" [fillcolor=none; color=\"${edge.isa.color}\"; style=%s]",
                                 edge.getTail().getBrowserText(),
                                 edge.getHead().getBrowserText(),
                                 edge.getTail().getEntity().isOWLNamedIndividual() ? "dashed" : "solid");
        }
        else {
            return String.format("\"%s\" -> \"%s\" [color=\"${edge.rel.color}\"; label=\"%s\" fontcolor=\"${edge.rel.color}\"; arrowhead=vee;]",
                                 edge.getTail().getBrowserText(),
                                 edge.getHead().getBrowserText(),
                                 l);
        }
    }
}

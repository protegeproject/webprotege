package edu.stanford.bmir.protege.web.server.viz;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Streams;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class DotRenderer {

    @Nonnull
    private final OWLOntology ontology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final OWLEntity root;

    @AutoFactory
    @Inject
    public DotRenderer(@Provided @Nonnull OWLOntology ontology,
                       @Provided @Nonnull RenderingManager renderingManager,
                       @Nonnull OWLEntity root) {
        this.ontology = checkNotNull(ontology);
        this.renderingManager = checkNotNull(renderingManager);
        this.root = checkNotNull(root);
    }

    private MutableValueGraph<OWLEntityData, String> createGraph() {
        MutableValueGraph<OWLEntityData, String> g = ValueGraphBuilder
                .directed()
                .allowsSelfLoops(true)
                .build();

        createGraph(root, g, new HashSet<>());
        return g;
    }

    private void createGraph(@Nonnull OWLEntity entity, MutableValueGraph<OWLEntityData, String> g, Set<OWLEntity> processed) {
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
                .forEach(ax -> addEdge(g, processed, cls, ax));
    }

    private void addEdge(MutableValueGraph<OWLEntityData, String> g, Set<OWLEntity> processed, OWLClass cls, OWLSubClassOfAxiom ax) {
        ax.getSuperClass().asConjunctSet()
                .stream()
                .filter(c -> !c.isOWLThing())
                .forEach(superClass -> {
                    if(!superClass.isAnonymous()) {
                        OWLClass superCls = superClass.asOWLClass();
                        g.putEdgeValue(toEntity(cls), toEntity(superCls), "SubClassOf");

                        createGraph(superCls, g, processed);
                    }
                    else {
                        if(superClass instanceof OWLObjectSomeValuesFrom) {
                            OWLObjectSomeValuesFrom svf = (OWLObjectSomeValuesFrom) superClass;
                            OWLClassExpression filler = svf.getFiller();
                            if(!filler.isAnonymous()) {
                                OWLClass fillerCls = filler.asOWLClass();
                                OWLObjectProperty prop = svf.getProperty().asOWLObjectProperty();
                                g.putEdgeValue(toEntity(cls), toEntity(fillerCls), getRendering(prop));
                                createGraph(fillerCls, g, processed);
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
        MutableValueGraph<OWLEntityData, String> g = createGraph();
        PrintWriter pw = new PrintWriter(writer);
        pw.println("digraph {");
        pw.println("rankdir=BT; concentrate=true;");
        pw.println("node [shape=rect; fontsize=11; shape=box margin=0 width=0 height=0]; edge [fontsize=9;]");
        String edges = g.edges().stream()
                .map(p -> toEdgeRendering(g, p))
                .collect(Collectors.joining(";\n")) + ";";
        pw.println(edges);
        pw.print("}");
        pw.flush();
    }

    private String toEdgeRendering(MutableValueGraph<OWLEntityData, String> g,
                                   EndpointPair<OWLEntityData> p) {

        Optional<String> label = g.edgeValue(p.source(), p.target());
        if(label.isPresent()) {
            String l = label.get();
            String edge = "\"" + p.source().getBrowserText() + "\" -> \"" + p.target().getBrowserText() + "\"";
            int count = g.incidentEdges(p.source()).size();
            double minLen = count > 3 ? 1 + count * 0.3 : 1;
            if(l.equals("SubClassOf")) {
                return String.format("%s [fillcolor=none; color=\"#a0a0a0\"; minlen=%.3f]", edge, minLen);
            }
            else {
                return String.format("%s [color=\"#4784d1\"; label=\"%s\" labeldistance=2 fontcolor=\"#4784d1\"; minlen=%.3f]", edge, l, minLen);
            }
        }
        else {
            return "";
        }
    }
}

package edu.stanford.bmir.protege.web.server.viz;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.viz.Edge;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraph;
import edu.stanford.bmir.protege.web.shared.viz.IsAEdge;
import edu.stanford.bmir.protege.web.shared.viz.RelationshipEdge;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Oct 2018
 *
 * Builds a simple graph for an entity.  The graph is rooted at the entity and contains edges to depict
 * ISA links e.g. SubClassOf(:A :B), and other relationship links, e.g. SubClassOf(:A ObjectSomeValuesFrom(:R :B))
 */
public class EntityGraphBuilder {

    @Nonnull
    private final RenderingManager renderer;

    @Nonnull
    private final OWLOntology ont;

    @Nullable
    private ImmutableSet<OWLOntology> ontologies = null;

    @Inject
    public EntityGraphBuilder(@Nonnull RenderingManager renderer,
                              @Nonnull OWLOntology ontology) {
        this.renderer = checkNotNull(renderer);
        this.ont = checkNotNull(ontology);
    }

    public Stream<OWLOntology> getOntologies() {
        if (ontologies == null) {
            ontologies = ImmutableSet.copyOf(ont.getImportsClosure());
        }
        return ontologies.stream();
    }

    @Nonnull
    public EntityGraph createGraph(@Nonnull OWLEntity root) {
        var edges = new LinkedHashSet<Edge>();
        createGraph(root, edges, new HashSet<>());
        return EntityGraph.create(renderer.getRendering(root),
                                  ImmutableSet.copyOf(edges));
    }

    private void createGraph(@Nonnull OWLEntity entity,
                             @Nonnull Set<Edge> edges,
                             @Nonnull Set<OWLEntity> processed) {
        if (processed.contains(entity)) {
            return;
        }
        processed.add(entity);
        if (entity.isOWLClass()) {
            var cls = entity.asOWLClass();
            createEdgesForClass(edges, processed, cls);
        }
        else if (entity.isOWLNamedIndividual()) {
            var ind = entity.asOWLNamedIndividual();
            createEdgesForIndividual(edges, processed, ind);
        }
    }

    private void createEdgesForIndividual(Set<Edge> edges, Set<OWLEntity> processed, OWLNamedIndividual individual) {
        var individualData = renderer.getIndividualData(individual);
        getOntologies().flatMap(o -> o.getClassAssertionAxioms(individual).stream())
                .filter(ax -> !ax.getClassExpression().isOWLThing())
                .filter(ax -> !ax.getClassExpression().isAnonymous())
                .forEach(ax -> {
                    var cls = ax.getClassExpression().asOWLClass();
                    var clsData = renderer.getClassData(cls);
                    edges.add(IsAEdge.get(individualData, clsData));
                    createEdgesForClass(edges, processed, cls);
                });
        getOntologies().flatMap(o -> o.getObjectPropertyAssertionAxioms(individual).stream())
                .filter(ax -> ax.getObject().isNamed())
                .filter(ax -> !ax.getProperty().isAnonymous())
                .forEach(ax -> {
                    var object = ax.getObject().asOWLNamedIndividual();
                    var objectData = renderer.getIndividualData(object);
                    var propertyData = renderer.getObjectPropertyData(ax.getProperty().asOWLObjectProperty());
                    edges.add(RelationshipEdge.get(individualData, objectData, propertyData));
                    createEdgesForIndividual(edges, processed, object);
                });

    }

    private void createEdgesForClass(Set<Edge> g, Set<OWLEntity> processed, OWLClass cls) {
        var subClassAxioms = getOntologies().flatMap(o -> o.getSubClassAxiomsForSubClass(cls).stream().sorted());
        var equivalentClassesAxioms = getEquivalentClassAxiomsAsSubClassOfAxioms(cls);
        var combinedAxioms = Streams.concat(subClassAxioms, equivalentClassesAxioms);
        combinedAxioms
                .filter(ax -> !ax.getSubClass().isAnonymous())
                .forEach(ax -> createEdgeForSubClassOfAxiom(cls, ax, g, processed));
    }

    private Stream<OWLSubClassOfAxiom> getEquivalentClassAxiomsAsSubClassOfAxioms(OWLClass cls) {
        return getOntologies().flatMap(o -> o.getEquivalentClassesAxioms(cls).stream())
                .flatMap(ax -> ax.asOWLSubClassOfAxioms().stream());
    }

    private void createEdgeForSubClassOfAxiom(OWLClass subCls, OWLSubClassOfAxiom ax, Set<Edge> edges, Set<OWLEntity> processed) {
        OWLEntityData subClsData = renderer.getClassData(subCls);
        ax.getSuperClass().asConjunctSet()
                .stream()
                .filter(c -> !c.isOWLThing())
                .forEach(superClass -> {
                    if (!superClass.isAnonymous()) {
                        var superCls = superClass.asOWLClass();
                        var superClsData = renderer.getClassData(superCls);
                        edges.add(IsAEdge.get(subClsData, superClsData));
                        createGraph(superCls, edges, processed);
                    }
                    else {
                        addEdgeForComplexSuperClass(edges, processed, subClsData, superClass);
                    }
                });
    }

    private void addEdgeForComplexSuperClass(Set<Edge> edges, Set<OWLEntity> processed, OWLEntityData subClsData, OWLClassExpression superClass) {
        if (superClass instanceof OWLObjectSomeValuesFrom) {
            var someValuesFrom = (OWLObjectSomeValuesFrom) superClass;
            var property = someValuesFrom.getProperty();
            var filler = someValuesFrom.getFiller();
            if (!filler.isAnonymous() && !property.isAnonymous()) {
                var fillerCls = filler.asOWLClass();
                var fillerClsData = renderer.getClassData(fillerCls);
                var propData = renderer.getObjectPropertyData(property.asOWLObjectProperty());
                edges.add(RelationshipEdge.get(subClsData, fillerClsData, propData));
                createGraph(fillerCls, edges, processed);
            }
        }
        else if (superClass instanceof OWLObjectHasValue) {
            var hasValue = (OWLObjectHasValue) superClass;
            var property = hasValue.getProperty();
            var filler = hasValue.getFiller();
            if (filler.isNamed() && !property.isAnonymous()) {
                var ind = filler.asOWLNamedIndividual();
                var indData = renderer.getIndividualData(ind);
                var propData = renderer.getObjectPropertyData(property.asOWLObjectProperty());
                edges.add(RelationshipEdge.get(subClsData, indData, propData));
                createGraph(ind, edges, processed);
            }
        }
    }

    private OWLEntityData toEntity(@Nonnull OWLClass cls) {
        return renderer.getClassData(cls);
    }
}

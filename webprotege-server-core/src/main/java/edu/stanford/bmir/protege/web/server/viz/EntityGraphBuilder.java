package edu.stanford.bmir.protege.web.server.viz;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.util.ClassExpression;
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
import static edu.stanford.bmir.protege.web.server.util.ClassExpression.isNotOwlThing;
import static edu.stanford.bmir.protege.web.server.util.Named.*;

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
        if(ontologies == null) {
            ontologies = ImmutableSet.copyOf(ont.getImportsClosure());
        }
        return ontologies.stream();
    }

    @Nonnull
    public EntityGraph createGraph(@Nonnull OWLEntity root) {
        var edges = new LinkedHashSet<Edge>();
        createGraph(root, edges, new HashSet<>());
        return EntityGraph.create(renderer.getRendering(root), ImmutableSet.copyOf(edges));
    }

    private void createGraph(@Nonnull OWLEntity entity,
                             @Nonnull Set<Edge> edges,
                             @Nonnull Set<OWLEntity> processed) {
        if(processed.contains(entity)) {
            return;
        }
        processed.add(entity);
        if(entity.isOWLClass()) {
            var cls = entity.asOWLClass();
            createEdgesForClass(edges, processed, cls);
        }
        else if(entity.isOWLNamedIndividual()) {
            var ind = entity.asOWLNamedIndividual();
            createEdgesForIndividual(edges, processed, ind);
        }
    }

    private void createEdgesForIndividual(Set<Edge> edges,
                                          Set<OWLEntity> processed,
                                          OWLNamedIndividual individual) {
        var individualData = renderer.getIndividualData(individual);
        createEdgesForClassAssertions(edges, processed, individual, individualData);
        createEdgesForObjectPropertyAssertions(edges, processed, individual, individualData);
    }

    private void createEdgesForObjectPropertyAssertions(Set<Edge> edges,
                                                        Set<OWLEntity> processed,
                                                        OWLNamedIndividual individual,
                                                        OWLNamedIndividualData individualData) {
        getOntologies()
                .flatMap(o -> o.getObjectPropertyAssertionAxioms(individual).stream())
                .filter(ax -> isNamedIndividual(ax.getObject()))
                .filter(ax -> isNamedProperty(ax.getProperty()))
                .forEach(ax -> {
                    var object = ax.getObject().asOWLNamedIndividual();
                    var objectData = renderer.getIndividualData(object);
                    var propertyData = renderer.getObjectPropertyData(ax.getProperty().asOWLObjectProperty());
                    edges.add(RelationshipEdge.get(individualData, objectData, propertyData));
                    createGraph(object, edges, processed);
                });
    }

    private void createEdgesForClassAssertions(Set<Edge> edges,
                                               Set<OWLEntity> processed,
                                               OWLNamedIndividual individual,
                                               OWLNamedIndividualData individualData) {
        getOntologies()
                .flatMap(o -> o.getClassAssertionAxioms(individual).stream())
                .filter(ax -> isNotOwlThing(ax.getClassExpression()))
                .filter(ax -> isNamedClass(ax.getClassExpression()))
                .forEach(ax -> {
                    var cls = ax.getClassExpression().asOWLClass();
                    var clsData = renderer.getClassData(cls);
                    edges.add(IsAEdge.get(individualData, clsData));
                    createEdgesForClass(edges, processed, cls);
                });
    }

    private void createEdgesForClass(Set<Edge> edges,
                                     Set<OWLEntity> processed,
                                     OWLClass cls) {
        var subClassAxioms = getOntologies().flatMap(o -> o.getSubClassAxiomsForSubClass(cls).stream().sorted());
        var equivalentClassesAxioms = getEquivalentClassAxiomsAsSubClassOfAxioms(cls);
        var combinedAxioms = Streams.concat(subClassAxioms, equivalentClassesAxioms);
        combinedAxioms
                .filter(ax -> isNamedClass(ax.getSubClass()))
                .forEach(ax -> createEdgeForSubClassOfAxiom(cls, ax, edges, processed));
    }

    private Stream<OWLSubClassOfAxiom> getEquivalentClassAxiomsAsSubClassOfAxioms(OWLClass cls) {
        return getOntologies()
                .flatMap(o -> o.getEquivalentClassesAxioms(cls).stream())
                .flatMap(ax -> ax.asOWLSubClassOfAxioms().stream());
    }

    private void createEdgeForSubClassOfAxiom(OWLClass subCls,
                                              OWLSubClassOfAxiom subClassOfAxiom,
                                              Set<Edge> edges,
                                              Set<OWLEntity> processed) {
        OWLEntityData subClsData = renderer.getClassData(subCls);
        subClassOfAxiom
                .getSuperClass()
                .asConjunctSet()
                .stream()
                .filter(ClassExpression::isNotOwlThing)
                .forEach(superClass -> addEdgeForSuperClass(edges, processed, subClsData, superClass));
    }

    private void addEdgeForSuperClass(Set<Edge> edges,
                                      Set<OWLEntity> processed,
                                      OWLEntityData subClsData,
                                      OWLClassExpression superClass) {
        if(isNamedClass(superClass)) {
            addEdgeForNamedSuperClass(edges, processed, subClsData, superClass);
        }
        else {
            addEdgeForComplexSuperClass(edges, processed, subClsData, superClass);
        }
    }

    private void addEdgeForNamedSuperClass(Set<Edge> edges,
                                           Set<OWLEntity> processed,
                                           OWLEntityData subClsData,
                                           OWLClassExpression superClass) {
        var superCls = superClass.asOWLClass();
        var superClsData = renderer.getClassData(superCls);
        edges.add(IsAEdge.get(subClsData, superClsData));
        createGraph(superCls, edges, processed);
    }

    private void addEdgeForComplexSuperClass(Set<Edge> edges,
                                             Set<OWLEntity> processed,
                                             OWLEntityData subClsData,
                                             OWLClassExpression superClass) {
        if(superClass instanceof OWLObjectSomeValuesFrom) {
            var someValuesFrom = (OWLObjectSomeValuesFrom) superClass;
            addEdgeForSomeValuesFrom(edges, processed, subClsData, someValuesFrom);
        }
        else if(superClass instanceof OWLObjectHasValue) {
            var hasValue = (OWLObjectHasValue) superClass;
            addEdgeForHasValue(edges, processed, subClsData, hasValue);
        }
    }

    private void addEdgeForHasValue(Set<Edge> edges,
                                    Set<OWLEntity> processed,
                                    OWLEntityData subClsData,
                                    OWLObjectHasValue hasValue) {
        var property = hasValue.getProperty();
        if(isInverseProperty(property)) {
            return;
        }
        var filler = hasValue.getFiller();
        if(isAnonymousIndividual(filler)) {
            return;
        }
        var individual = filler.asOWLNamedIndividual();
        var individualData = renderer.getIndividualData(individual);
        var propertyData = renderer.getObjectPropertyData(property.asOWLObjectProperty());
        edges.add(RelationshipEdge.get(subClsData, individualData, propertyData));
        createGraph(individual, edges, processed);
    }

    private void addEdgeForSomeValuesFrom(Set<Edge> edges,
                                          Set<OWLEntity> processed,
                                          OWLEntityData subClsData,
                                          OWLObjectSomeValuesFrom someValuesFrom) {
        var property = someValuesFrom.getProperty();
        if(!isNamedProperty(property)) {
            return;
        }
        var filler = someValuesFrom.getFiller();
        if(!isNamedClass(filler)) {
            return;
        }
        var fillerCls = filler.asOWLClass();
        var fillerClsData = renderer.getClassData(fillerCls);
        var propertyData = renderer.getObjectPropertyData(property.asOWLObjectProperty());
        edges.add(RelationshipEdge.get(subClsData, fillerClsData, propertyData));
        createGraph(fillerCls, edges, processed);
    }
}

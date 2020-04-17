package edu.stanford.bmir.protege.web.server.viz;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.owlapi.ConjunctSet;
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
import javax.inject.Inject;
import java.util.*;
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
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertions;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxioms;

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex classAssertionAxioms;

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxioms;

    @Nonnull
    private final Set<OWLEntity> processed = new HashSet<>();

    private final int edgeLimit;

    @Nonnull
    private final Queue<OWLEntity> queue = new ArrayDeque<>();

    private final EdgeMatcher edgeMatcher;

    private boolean edgeLimitReached = false;

    @AutoFactory
    @Inject
    public EntityGraphBuilder(@Nonnull @Provided RenderingManager renderer,
                              @Nonnull @Provided ProjectOntologiesIndex projectOntologiesIndex,
                              @Nonnull @Provided ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertions,
                              @Nonnull @Provided SubClassOfAxiomsBySubClassIndex subClassOfAxioms,
                              @Nonnull @Provided ClassAssertionAxiomsByIndividualIndex classAssertionAxioms,
                              @Nonnull @Provided EquivalentClassesAxiomsIndex equivalentClassesAxioms,
                              @Provided @EntityGraphEdgeLimit int edgeLimit,
                              @Nonnull EdgeMatcher edgeMatcher) {
        this.renderer = checkNotNull(renderer);
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.objectPropertyAssertions = objectPropertyAssertions;
        this.subClassOfAxioms = subClassOfAxioms;
        this.classAssertionAxioms = classAssertionAxioms;
        this.equivalentClassesAxioms = equivalentClassesAxioms;
        this.edgeLimit = edgeLimit;
        this.edgeMatcher = edgeMatcher;
    }

    private void addEdgeForComplexSuperClass(Set<Edge> edges,
                                             OWLEntityData subClsData,
                                             OWLClassExpression superClass) {
        if(superClass instanceof OWLObjectSomeValuesFrom) {
            var someValuesFrom = (OWLObjectSomeValuesFrom) superClass;
            addEdgeForSomeValuesFrom(edges, subClsData, someValuesFrom);
        }
        else if(superClass instanceof OWLObjectHasValue) {
            var hasValue = (OWLObjectHasValue) superClass;
            addEdgeForHasValue(edges, subClsData, hasValue);
        }
    }

    private void addEdgeForHasValue(Set<Edge> edges,
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
        RelationshipEdge edge = RelationshipEdge.get(subClsData, individualData, propertyData);
        if(edgeMatcher.matches(edge)) {
            edges.add(edge);
            push(individual);
        }
    }

    private void addEdgeForNamedSuperClass(Set<Edge> edges,
                                           OWLEntityData subClsData,
                                           OWLClassExpression superClass) {
        var superCls = superClass.asOWLClass();
        var superClsData = renderer.getClassData(superCls);
        IsAEdge edge = IsAEdge.get(subClsData, superClsData);
        if(edgeMatcher.matches(edge)) {
            edges.add(edge);
            push(superCls);
        }
    }

    private void addEdgeForSomeValuesFrom(Set<Edge> edges,
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
        RelationshipEdge edge = RelationshipEdge.get(subClsData, fillerClsData, propertyData);
        if(edgeMatcher.matches(edge)) {
            edges.add(edge);
            push(fillerCls);
        }
    }

    private void addEdgeForSuperClass(Set<Edge> edges,
                                      OWLEntityData subClsData,
                                      OWLClassExpression superClass) {
        if(isNamedClass(superClass)) {
            addEdgeForNamedSuperClass(edges, subClsData, superClass);
        }
        else {
            addEdgeForComplexSuperClass(edges, subClsData, superClass);
        }
    }

    private void createEdgeForSubClassOfAxiom(OWLClass subCls,
                                              OWLSubClassOfAxiom subClassOfAxiom,
                                              Set<Edge> edges) {
        OWLEntityData subClsData = renderer.getClassData(subCls);
        OWLClassExpression superCls = subClassOfAxiom.getSuperClass();
        ConjunctSet.asConjuncts(superCls)
                .filter(ClassExpression::isNotOwlThing)
                .forEach(superClass -> addEdgeForSuperClass(edges, subClsData, superClass));
    }

    private void createEdgesForClass(Set<Edge> edges,
                                     OWLClass cls) {
        var subClassAxioms = getSubClassAxioms(cls);
        var equivalentClassesAxioms = getEquivalentClassAxiomsAsSubClassOfAxioms(cls);
        var combinedAxioms = Streams.concat(subClassAxioms, equivalentClassesAxioms);
        combinedAxioms
                .filter(ax -> isNamedClass(ax.getSubClass()))
                .forEach(ax -> createEdgeForSubClassOfAxiom(cls, ax, edges));
    }

    private void createEdgesForClassAssertions(Set<Edge> edges,
                                               OWLNamedIndividual individual,
                                               OWLNamedIndividualData individualData) {
        projectOntologiesIndex.getOntologyIds()
                              .flatMap(ontId -> classAssertionAxioms.getClassAssertionAxioms(individual, ontId))
                              .filter(ax -> isNotOwlThing(ax.getClassExpression()))
                              .forEach(ax -> {
                                  var ce = ax.getClassExpression();
                                  addEdgeToInstanceOf(edges, individualData, ce);
                              });
    }

    private void addEdgeToInstanceOf(Set<Edge> edges,
                                     OWLNamedIndividualData individualData,
                                     OWLClassExpression ce) {
        if(ce.isNamed()) {
            addInstanceOfToClassName(edges, individualData, ce.asOWLClass());
        }
        else {
            addInstanceOfToComplexClassExpression(edges, individualData, ce);
        }
    }

    private void addInstanceOfToClassName(Set<Edge> edges,
                                          OWLNamedIndividualData individualData,
                                          OWLClass cls) {
        var clsData = renderer.getClassData(cls);
        IsAEdge edge = IsAEdge.get(individualData, clsData);
        if(edgeMatcher.matches(edge)) {
            edges.add(edge);
            push(cls);
        }
    }

    private void addInstanceOfToComplexClassExpression(Set<Edge> edges,
                                                       OWLNamedIndividualData individualData, OWLClassExpression ce) {
        if(ce instanceof OWLObjectSomeValuesFrom) {
            addInstanceOfObjectSomeValuesFrom(edges, individualData, (OWLObjectSomeValuesFrom) ce);
        }
        else if(ce instanceof OWLObjectHasValue) {
            addInstanceOfObjectHasValue(edges, individualData, (OWLObjectHasValue) ce);
        }
    }

    private void addInstanceOfObjectHasValue(Set<Edge> edges,
                                             OWLNamedIndividualData individualData,
                                             OWLObjectHasValue ce) {
        var property = ce.getProperty();
        if(property.isAnonymous()) {
            return;
        }
        var individual = ce.getFiller();
        if(individual.isAnonymous()) {
            return;
        }
        var propertyData = renderer.getObjectPropertyData(property.asOWLObjectProperty());
        var fillerData = renderer.getIndividualData(individual.asOWLNamedIndividual());
        RelationshipEdge edge = RelationshipEdge.get(individualData, fillerData, propertyData);
        if(edgeMatcher.matches(edge)) {
            edges.add(edge);
            push(individual.asOWLNamedIndividual());
        }
    }

    private void addInstanceOfObjectSomeValuesFrom(Set<Edge> edges,
                                                   OWLNamedIndividualData individualData,
                                                   OWLObjectSomeValuesFrom ce) {
        var property = ce.getProperty();
        if(property.isAnonymous()) {
            return;
        }
        var fillerCls = ce.getFiller();
        if(fillerCls.isAnonymous()) {
            return;
        }
        var propertyData = renderer.getObjectPropertyData(property.asOWLObjectProperty());
        var fillerData = renderer.getClassData(fillerCls.asOWLClass());
        RelationshipEdge edge = RelationshipEdge.get(individualData, fillerData, propertyData);
        if(edgeMatcher.matches(edge)) {
            edges.add(edge);
            push(fillerCls.asOWLClass());
        }
    }

    private void createEdgesForIndividual(Set<Edge> edges,
                                          OWLNamedIndividual individual) {
        var individualData = renderer.getIndividualData(individual);
        createEdgesForClassAssertions(edges, individual, individualData);
        createEdgesForObjectPropertyAssertions(edges, individual, individualData);
    }

    private void createEdgesForObjectPropertyAssertions(Set<Edge> edges,
                                                        OWLNamedIndividual individual,
                                                        OWLNamedIndividualData individualData) {
        projectOntologiesIndex.getOntologyIds()
                              .flatMap(ontId -> objectPropertyAssertions.getObjectPropertyAssertions(individual, ontId))
                              .filter(ax -> isNamedIndividual(ax.getObject()))
                              .filter(ax -> isNamedProperty(ax.getProperty()))
                              .forEach(ax -> {
                                  var object = ax.getObject()
                                                 .asOWLNamedIndividual();
                                  var objectData = renderer.getIndividualData(object);
                                  var propertyData = renderer.getObjectPropertyData(ax.getProperty()
                                                                                      .asOWLObjectProperty());
                                  RelationshipEdge edge = RelationshipEdge.get(individualData, objectData, propertyData);
                                  if(edgeMatcher.matches(edge)) {
                                      edges.add(edge);
                                      push(object);
                                  }
                              });
    }

    @Nonnull
    public EntityGraph createGraph(@Nonnull OWLEntity root) {
        processed.clear();
        queue.add(root);
        edgeLimitReached = false;
        var edges = createGraph();
        return EntityGraph.create(renderer.getRendering(root), edges, edgeLimitReached);
    }

    private ImmutableSet<Edge> createGraph() {
        @Nonnull var edges = new LinkedHashSet<Edge>();
        while(!queue.isEmpty()) {
            if(edges.size() >= edgeLimit) {
                edgeLimitReached = true;
                return ImmutableSet.copyOf(edges);
            }
            var entity = queue.poll();
            if(entity.isOWLClass()) {
                var cls = entity.asOWLClass();
                createEdgesForClass(edges, cls);
            }
            else if(entity.isOWLNamedIndividual()) {
                var ind = entity.asOWLNamedIndividual();
                createEdgesForIndividual(edges, ind);
            }
        }
        return ImmutableSet.copyOf(edges);
    }

    private void push(@Nonnull OWLEntity entity) {
        if(processed.contains(entity)) {
            return;
        }
        processed.add(entity);
        queue.add(entity);
    }

    private Stream<OWLSubClassOfAxiom> getEquivalentClassAxiomsAsSubClassOfAxioms(OWLClass cls) {
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> equivalentClassesAxioms.getEquivalentClassesAxioms(cls, ontId))
                                     .flatMap(ax -> ax.asOWLSubClassOfAxioms()
                                                      .stream());
    }

    private Stream<OWLSubClassOfAxiom> getSubClassAxioms(OWLClass cls) {
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> subClassOfAxioms.getSubClassOfAxiomsForSubClass(cls, ontId));
    }
}

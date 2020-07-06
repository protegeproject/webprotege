package edu.stanford.bmir.protege.web.server.hierarchy;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.protege.owlapi.inference.cls.ChildClassExtractor;
import org.protege.owlapi.inference.orphan.TerminalElementFinder;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 17-Jan-2007<br><br>
 */
@ProjectSingleton
public class ClassHierarchyProviderImpl extends AbstractHierarchyProvider<OWLClass> implements ClassHierarchyProvider {

    private static final Logger logger = LoggerFactory.getLogger(ClassHierarchyProviderImpl.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OWLClass root;

    @Nonnull
    private final TerminalElementFinder<OWLClass> rootFinder;

    @Nonnull
    private final Set<OWLClass> nodesToUpdate = new HashSet<>();

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex;

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;

    @Nonnull
    private final ProjectSignatureByTypeIndex projectSignatureByTypeIndex;

    @Nonnull
    private final AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    private boolean stale = true;

    @Inject
    public ClassHierarchyProviderImpl(ProjectId projectId,
                                      @Nonnull @ClassHierarchyRoot OWLClass rootCls,
                                      @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                      @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex,
                                      @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex,
                                      @Nonnull ProjectSignatureByTypeIndex projectSignatureByTypeIndex,
                                      @Nonnull AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex,
                                      @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex) {
        this.projectId = checkNotNull(projectId);
        this.root = checkNotNull(rootCls);
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.subClassOfAxiomsIndex = subClassOfAxiomsIndex;
        this.equivalentClassesAxiomsIndex = equivalentClassesAxiomsIndex;
        this.projectSignatureByTypeIndex = projectSignatureByTypeIndex;
        this.axiomsByEntityReferenceIndex = axiomsByEntityReferenceIndex;
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
        rootFinder = new TerminalElementFinder<>(this::getParents);
        nodesToUpdate.clear();
    }

    public synchronized Collection<OWLClass> getParents(OWLClass object) {
        rebuildIfNecessary();
        // If the object is thing then there are no
        // parents
        if(object.equals(root)) {
            return Collections.emptySet();
        }
        Stream<OWLClass> parentsCombined = getParentsStream(object);
        var parents = parentsCombined.collect(toSet());
        // Thing if the object is a root class
        if(rootFinder.getTerminalElements()
                     .contains(object)) {
            parents.add(root);
        }
        return parents;
    }

    @Override
    public boolean isParent(OWLClass child, OWLClass parent) {
        return getParentsStream(child).anyMatch(c -> c.equals(parent));
    }

    private Stream<OWLClass> getParentsStream(OWLClass object) {
        var subClassOfAxiomsParents =
                projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> subClassOfAxiomsIndex.getSubClassOfAxiomsForSubClass(object,
                                                                                                             ontId))
                                      .map(OWLSubClassOfAxiom::getSuperClass)
                                      .flatMap(this::asConjunctSet)
                                      .filter(OWLClassExpression::isNamed)
                                      .map(OWLClassExpression::asOWLClass);


        var equivalentClassesAxiomsParents =
                projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(
                                              object,
                                              ontId))
                                      .flatMap(ax -> ax.getClassExpressions()
                                                       .stream())
                                      .filter(ce -> !ce.equals(object))
                                      .flatMap(this::asConjunctSet)
                                      .filter(OWLClassExpression::isNamed)
                                      .map(OWLClassExpression::asOWLClass);

        return Stream.concat(subClassOfAxiomsParents, equivalentClassesAxiomsParents);
    }

    private void rebuildIfNecessary() {
        if(stale) {
            rebuildImplicitRoots();
        }
    }

    private Stream<OWLClassExpression> asConjunctSet(@Nonnull OWLClassExpression cls) {
        if(cls instanceof OWLObjectIntersectionOf) {
            return ((OWLObjectIntersectionOf) cls).getOperandsAsList()
                                           .stream()
                                           .flatMap(this::asConjunctSet);
        }
        else {
            return Stream.of(cls);
        }
    }

    private void rebuildImplicitRoots() {
        stale = false;
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.info("{} Rebuilding class hierarchy", projectId);
        rootFinder.clear();
        var signature = projectSignatureByTypeIndex.getSignature(EntityType.CLASS)
                                                   .collect(toImmutableSet());
        rootFinder.appendTerminalElements(signature);
        rootFinder.finish();
        logger.info("{} Rebuilt class hierarchy in {} ms", projectId, stopwatch.elapsed(MILLISECONDS));
    }

    public void dispose() {
    }

    public synchronized void handleChanges(@Nonnull List<OntologyChange> changes) {
        Set<OWLClass> oldTerminalElements = new HashSet<>(rootFinder.getTerminalElements());
        Set<OWLClass> changedClasses = new HashSet<>();
        changedClasses.add(root);
        var filteredChanges = filterIrrelevantChanges(changes);
        updateImplicitRoots(filteredChanges);
        for(OntologyChange change : filteredChanges) {
            changedClasses.addAll(change.getSignature()
                                        .stream()
                                        .filter(OWLEntity::isOWLClass)
                                        .filter(entity -> !entity.equals(root))
                                        .map(entity -> (OWLClass) entity)
                                        .collect(toList()));
        }
        changedClasses.forEach(this::registerNodeChanged);
        rootFinder.getTerminalElements()
                  .stream()
                  .filter(cls -> !oldTerminalElements.contains(cls))
                  .forEach(this::registerNodeChanged);
        oldTerminalElements.stream()
                           .filter(cls -> !rootFinder.getTerminalElements()
                                                     .contains(cls))
                           .forEach(this::registerNodeChanged);
        notifyNodeChanges();
    }

    private List<OntologyChange> filterIrrelevantChanges(List<OntologyChange> changes) {
        return changes.stream()
                      .filter(OntologyChange::isAxiomChange)
                      .collect(toList());
    }

    private void updateImplicitRoots(List<OntologyChange> changes) {
        Set<OWLClass> possibleTerminalElements = new HashSet<>();
        Set<OWLClass> notInOntologies = new HashSet<>();

        // only listen for changes on the appropriate ontologies
        changes.stream()
               .filter(OntologyChange::isAxiomChange)
               .forEach(change -> {
                   boolean remove = change.isRemoveAxiom();
                   var axiom = change.getAxiomOrThrow();
                   axiom.getSignature()
                        .stream()
                        .filter(OWLEntity::isOWLClass)
                        .filter(entity -> !entity.equals(root))
                        .forEach(entity -> {
                            OWLClass cls = (OWLClass) entity;
                            if(!remove || containsReference(cls)) {
                                possibleTerminalElements.add(cls);
                            }
                            else {
                                notInOntologies.add(cls);
                            }
                        });
               });

        possibleTerminalElements.addAll(rootFinder.getTerminalElements());
        possibleTerminalElements.removeAll(notInOntologies);
        rootFinder.findTerminalElements(possibleTerminalElements);
    }

    private void registerNodeChanged(OWLClass node) {
        nodesToUpdate.add(node);
    }

    private void notifyNodeChanges() {
        nodesToUpdate.clear();
    }

    public synchronized boolean containsReference(OWLClass object) {
        return entitiesInProjectSignatureByIriIndex
                .getEntitiesInSignature(object.getIRI())
                .anyMatch(entity -> entity.equals(object));
    }

    public synchronized Collection<OWLClass> getRoots() {
        rebuildIfNecessary();
        return Collections.singleton(root);
    }

    public synchronized Collection<OWLClass> getChildren(OWLClass object) {
        rebuildIfNecessary();
        Set<OWLClass> result;
        if(object.equals(root)) {
            result = new HashSet<>();
            result.addAll(rootFinder.getTerminalElements());
            result.addAll(extractChildren(object));
            result.remove(object);
        }
        else {
            result = extractChildren(object);
            //            result.removeIf(curChild -> getAncestors(object).contains(curChild));
        }

        return result;
    }

    private Set<OWLClass> extractChildren(OWLClass parent) {
        ChildClassExtractor childClassExtractor = new ChildClassExtractor();
        childClassExtractor.setCurrentParentClass(parent);
        projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> axiomsByEntityReferenceIndex.getReferencingAxioms(parent, ontId))
                .filter(OWLAxiom::isLogicalAxiom)
                .forEach(ax -> ax.accept(childClassExtractor));
        return childClassExtractor.getResult();
    }

}

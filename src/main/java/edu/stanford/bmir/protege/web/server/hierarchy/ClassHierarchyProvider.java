package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.protege.owlapi.inference.cls.ChildClassExtractor;
import org.protege.owlapi.inference.cls.ParentClassExtractor;
import org.protege.owlapi.inference.orphan.TerminalElementFinder;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 17-Jan-2007<br><br>
 */
@ProjectSingleton
public class ClassHierarchyProvider extends AbstractHierarchyProvider<OWLClass> {

    /*
     * It is not safe to set the collection of ontologies to a HashSet or TreeSet.
     * When an ontology changes name it gets a new Hash Code and it is sorted
     * differently, so these Collections do not work.
     */
    private final OWLOntology rootOntology;

    private final OWLClass root;

    private final TerminalElementFinder<OWLClass> rootFinder;

    private final Set<OWLClass> nodesToUpdate = new HashSet<>();

    @Inject
    public ClassHierarchyProvider(@Nonnull @RootOntology OWLOntology rootOntology,
                                  @Nonnull @ClassHierarchyRoot OWLClass rootCls) {
        this.root = checkNotNull(rootCls);
        this.rootOntology = checkNotNull(rootOntology);
        rootFinder = new TerminalElementFinder<>(cls -> {
            Collection<OWLClass> parents = getParents(cls);
            parents.remove(root);
            return parents;
        });
        nodesToUpdate.clear();
        rebuildImplicitRoots();
        fireHierarchyChanged();
    }

    @Nonnull
    private Set<OWLOntology> getOntologies() {
        return rootOntology.getImportsClosure();
    }

    private void rebuildImplicitRoots() {
        rootFinder.clear();
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            Set<OWLClass> ref = ont.getClassesInSignature();
            rootFinder.appendTerminalElements(ref);
        }
        rootFinder.finish();
    }

    public void dispose() {
    }


    public void handleChanges(@Nonnull List<? extends OWLOntologyChange> changes) {
        Set<OWLClass> oldTerminalElements = new HashSet<>(rootFinder.getTerminalElements());
        Set<OWLClass> changedClasses = new HashSet<>();
        changedClasses.add(root);
        List<OWLAxiomChange> filteredChanges = filterIrrelevantChanges(changes);
        updateImplicitRoots(filteredChanges);
        for (OWLOntologyChange change : filteredChanges) {
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
                           .filter(cls -> !rootFinder.getTerminalElements().contains(cls))
                           .forEach(this::registerNodeChanged);
        notifyNodeChanges();
    }

    private List<OWLAxiomChange> filterIrrelevantChanges(List<? extends OWLOntologyChange> changes) {
        return changes.stream()
                      .filter((Predicate<OWLOntologyChange>) OWLOntologyChange::isAxiomChange)
                      .filter(change -> getOntologies().contains(change.getOntology()))
                      .map(change -> (OWLAxiomChange) change)
                      .collect(toList());
    }


    private void registerNodeChanged(OWLClass node) {
        nodesToUpdate.add(node);
    }


    private void notifyNodeChanges() {
        nodesToUpdate.forEach(this::fireNodeChanged);
        nodesToUpdate.clear();
    }


    private void updateImplicitRoots(List<OWLAxiomChange> changes) {
        Set<OWLClass> possibleTerminalElements = new HashSet<>();
        Set<OWLClass> notInOntologies = new HashSet<>();

        // only listen for changes on the appropriate ontologies
        changes.stream()
               .filter(OWLOntologyChange::isAxiomChange)
               .filter(change -> getOntologies().contains(change.getOntology()))
               .forEach(change -> {
                   boolean remove = change instanceof RemoveAxiom;
                   OWLAxiom axiom = change.getAxiom();

                   axiom.getSignature()
                        .stream()
                        .filter(OWLEntity::isOWLClass)
                        .filter(entity -> !entity.equals(root))
                        .forEach(entity -> {
                            OWLClass cls = (OWLClass) entity;
                            if (!remove || containsReference(cls)) {
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

    public Set<OWLClass> getRoots() {
        return Collections.singleton(root);
    }


    public Set<OWLClass> getChildren(OWLClass object) {
        Set<OWLClass> result;
        if (object.equals(root)) {
            result = new HashSet<>();
            result.addAll(rootFinder.getTerminalElements());
            result.addAll(extractChildren(object));
            result.remove(object);
        }
        else {
            result = extractChildren(object);
            for (Iterator<OWLClass> it = result.iterator(); it.hasNext();) {
                OWLClass curChild = it.next();
                if (getAncestors(object).contains(curChild)) {
                    it.remove();
                }
            }
        }

        return result;
    }


    private Set<OWLClass> extractChildren(OWLClass parent) {
        ChildClassExtractor childClassExtractor = new ChildClassExtractor();
        childClassExtractor.setCurrentParentClass(parent);
        for (OWLOntology ont : getOntologies()) {
            ont.getReferencingAxioms(parent).stream()
               .filter(OWLAxiom::isLogicalAxiom)
               .forEach(ax -> ax.accept(childClassExtractor));
        }
        return childClassExtractor.getResult();
    }


    public boolean containsReference(OWLClass object) {
        for (OWLOntology ont : getOntologies()) {
            if (ont.containsClassInSignature(object.getIRI())) {
                return true;
            }
        }
        return false;
    }


    public Set<OWLClass> getParents(OWLClass object) {
        ParentClassExtractor parentClassExtractor = new ParentClassExtractor();
        // If the object is thing then there are no
        // parents
        if (object.equals(root)) {
            return Collections.emptySet();
        }
        Set<OWLClass> result = new HashSet<>();
        // Thing if the object is a root class
        if (rootFinder.getTerminalElements().contains(object)) {
            result.add(root);
        }
        // Not a root, so must have another parent
        parentClassExtractor.reset();
        parentClassExtractor.setCurrentClass(object);
        for (OWLOntology ont : getOntologies()) {
            for (OWLAxiom ax : ont.getAxioms(object, Imports.EXCLUDED)) {
                ax.accept(parentClassExtractor);
            }
        }
        result.addAll(parentClassExtractor.getResult());
        return result;
    }


    public Set<OWLClass> getEquivalents(OWLClass object) {
        Set<OWLClass> result = new HashSet<>();
        for (OWLOntology ont : getOntologies()) {
            result.addAll(EntitySearcher.getEquivalentClasses(object, ont)
                                        .stream()
                                        .filter(equiv -> !equiv.isAnonymous())
                                        .map(equiv -> (OWLClass) equiv)
                                        .collect(toList()));
        }
        Set<OWLClass> ancestors = getAncestors(object);
        if (ancestors.contains(object)) {
            result.addAll(ancestors.stream()
                                   .filter(cls -> getAncestors(cls).contains(object))
                                   .collect(toList()));
            result.remove(object);
            result.remove(root);
        }
        return result;
    }

}

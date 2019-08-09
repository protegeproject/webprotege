package edu.stanford.bmir.protege.web.server.hierarchy;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.protege.owlapi.inference.cls.ChildClassExtractor;
import org.protege.owlapi.inference.cls.ParentClassExtractor;
import org.protege.owlapi.inference.orphan.TerminalElementFinder;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.change.RemoveAxiomData;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 17-Jan-2007<br><br>
 */
@ProjectSingleton
public class ClassHierarchyProvider extends AbstractHierarchyProvider<OWLClass> {

    private static final Logger logger = LoggerFactory.getLogger(ClassHierarchyProvider.class);

    private final ProjectId projectId;

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
    public ClassHierarchyProvider(ProjectId projectId,
                                  @Nonnull @RootOntology OWLOntology rootOntology,
                                  @Nonnull @ClassHierarchyRoot OWLClass rootCls) {
        this.projectId = checkNotNull(projectId);
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

    public Set<OWLClass> getParents(OWLClass object) {
        // If the object is thing then there are no
        // parents
        if(object.equals(root)) {
            return Collections.emptySet();
        }
        Set<OWLClass> result = new HashSet<>();
        // Thing if the object is a root class
        if(rootFinder.getTerminalElements().contains(object)) {
            result.add(root);
        }
        // Not a root, so must have another parent
        ParentClassExtractor parentClassExtractor = new ParentClassExtractor();
        parentClassExtractor.setCurrentClass(object);
        for(OWLOntology ont : getOntologies()) {
            for(OWLAxiom ax : ont.getAxioms(object, Imports.EXCLUDED)) {
                ax.accept(parentClassExtractor);
            }
        }
        result.addAll(parentClassExtractor.getResult());
        return result;
    }

    private void rebuildImplicitRoots() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.info("{} Rebuilding class hierarchy", projectId);
        rootFinder.clear();
        for(OWLOntology ont : rootOntology.getImportsClosure()) {
            Set<OWLClass> ref = ont.getClassesInSignature();
            rootFinder.appendTerminalElements(ref);
        }
        rootFinder.finish();
        logger.info("{} Rebuilt class hierarchy in {} ms", projectId, stopwatch.elapsed(MILLISECONDS));
    }

    @Nonnull
    private Set<OWLOntology> getOntologies() {
        return rootOntology.getImportsClosure();
    }

    public void dispose() {
    }

    public void handleChanges(@Nonnull List<OWLOntologyChangeRecord> changes) {
        Set<OWLClass> oldTerminalElements = new HashSet<>(rootFinder.getTerminalElements());
        Set<OWLClass> changedClasses = new HashSet<>();
        changedClasses.add(root);
        List<OWLOntologyChangeRecord> filteredChanges = filterIrrelevantChanges(changes);
        updateImplicitRoots(filteredChanges);
        for(OWLOntologyChangeRecord change : filteredChanges) {
            changedClasses.addAll(change
                                          .getData()
                                          .getSignature()
                                          .stream()
                                          .filter(OWLEntity::isOWLClass)
                                          .filter(entity -> !entity.equals(root))
                                          .map(entity -> (OWLClass) entity)
                                          .collect(toList()));
        }
        changedClasses.forEach(this::registerNodeChanged);
        rootFinder
                .getTerminalElements()
                .stream()
                .filter(cls -> !oldTerminalElements.contains(cls))
                .forEach(this::registerNodeChanged);
        oldTerminalElements
                .stream()
                .filter(cls -> !rootFinder.getTerminalElements().contains(cls))
                .forEach(this::registerNodeChanged);
        notifyNodeChanges();
    }

    private List<OWLOntologyChangeRecord> filterIrrelevantChanges(List<OWLOntologyChangeRecord> changes) {
        return changes.stream().filter(ClassHierarchyProvider::isAxiomChange)
                .collect(toList());
    }

    private void updateImplicitRoots(List<OWLOntologyChangeRecord> changes) {
        Set<OWLClass> possibleTerminalElements = new HashSet<>();
        Set<OWLClass> notInOntologies = new HashSet<>();

        // only listen for changes on the appropriate ontologies
        changes.stream().filter(ClassHierarchyProvider::isAxiomChange).forEach(change -> {
            boolean remove = change.getData() instanceof RemoveAxiomData;
            var axiom = ((AxiomChangeData) change.getData()).getItem();
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
        nodesToUpdate.forEach(this::fireNodeChanged);
        nodesToUpdate.clear();
    }

    private static boolean isAxiomChange(OWLOntologyChangeRecord rec) {
        return rec.getData() instanceof AxiomChangeData;
    }

    public boolean containsReference(OWLClass object) {
        for(OWLOntology ont : getOntologies()) {
            if(ont.containsClassInSignature(object.getIRI())) {
                return true;
            }
        }
        return false;
    }

    public Set<OWLClass> getRoots() {
        return Collections.singleton(root);
    }

    public Set<OWLClass> getChildren(OWLClass object) {
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
        for(OWLOntology ont : getOntologies()) {
            ont
                    .getReferencingAxioms(parent)
                    .stream()
                    .filter(OWLAxiom::isLogicalAxiom)
                    .forEach(ax -> ax.accept(childClassExtractor));
        }
        return childClassExtractor.getResult();
    }

    public Set<OWLClass> getEquivalents(OWLClass object) {
        Set<OWLClass> result = new HashSet<>();
        for(OWLOntology ont : getOntologies()) {
            result.addAll(EntitySearcher
                                  .getEquivalentClasses(object, ont)
                                  .stream()
                                  .filter(equiv -> !equiv.isAnonymous())
                                  .map(equiv -> (OWLClass) equiv)
                                  .collect(toList()));
        }
        Set<OWLClass> ancestors = getAncestors(object);
        if(ancestors.contains(object)) {
            result.addAll(ancestors.stream().filter(cls -> getAncestors(cls).contains(object)).collect(toList()));
            result.remove(object);
            result.remove(root);
        }
        return result;
    }

}

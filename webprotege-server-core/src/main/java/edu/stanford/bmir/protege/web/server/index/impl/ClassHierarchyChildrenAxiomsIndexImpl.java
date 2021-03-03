package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.ClassHierarchyChildrenAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.Index;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-09
 */
@ProjectSingleton
public class ClassHierarchyChildrenAxiomsIndexImpl implements ClassHierarchyChildrenAxiomsIndex, UpdatableIndex, DependentIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AxiomMultimapIndex<OWLClass, OWLClassAxiom> index;

    private OWLAxiomVisitorExAdapter<List<OWLClass>> parentExtractorVisitor = new OWLAxiomVisitorExAdapter<>(Collections.<OWLClass>emptyList()) {
        @Nonnull
        @Override
        public List<OWLClass> visit(OWLSubClassOfAxiom axiom) {
            return extractParentsFromSubClassOfAxiom(axiom);
        }

        @Nonnull
        @Override
        public List<OWLClass> visit(OWLEquivalentClassesAxiom axiom) {
            return getParentsFromEquivalentClassesAxiom(axiom);
        }
    };

    @Inject
    public ClassHierarchyChildrenAxiomsIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(OWLClassAxiom.class, this::extractParents);
    }

    private Iterable<OWLClass> extractParents(OWLClassAxiom ax) {
        return ax.accept(parentExtractorVisitor);
    }

    private List<OWLClass> getParentsFromEquivalentClassesAxiom(OWLEquivalentClassesAxiom axiom) {
        return axiom.getClassExpressionsAsList()
             .stream()
             .filter(ce -> ce instanceof OWLObjectIntersectionOf)
             .map(ce -> (OWLObjectIntersectionOf) ce)
             .flatMap(ce -> getParentsFromObjectIntersectionOf(ce).stream())
             .collect(toImmutableList());
    }

    private List<OWLClass> extractParentsFromSubClassOfAxiom(OWLSubClassOfAxiom axiom) {
        if (!axiom.getSubClass().isNamed()) {
            return Collections.emptyList();
        }
        var superClass = axiom.getSuperClass();
        if(superClass.isNamed()) {
            return ImmutableList.of(superClass.asOWLClass());
        }
        else if(superClass instanceof OWLObjectIntersectionOf) {
            return getParentsFromObjectIntersectionOf((OWLObjectIntersectionOf) superClass);
        }
        return Collections.emptyList();
    }

    private ImmutableList<OWLClass> getParentsFromObjectIntersectionOf(OWLObjectIntersectionOf superClass) {
        return superClass.getOperandsAsList()
                         .stream()
                         .filter(IsAnonymous::isNamed)
                         .map(OWLClassExpression::asOWLClass)
                         .collect(toImmutableList());
    }


    @Nonnull
    @Override
    public Stream<OWLClassAxiom> getChildrenAxioms(@Nonnull OWLClass cls) {
        return projectOntologiesIndex.getOntologyDocumentIds()
                .flatMap(ont -> index.getAxioms(cls, ont));
    }

    @Override
    public boolean isLeaf(@Nonnull OWLClass cls) {
        return projectOntologiesIndex.getOntologyDocumentIds()
                .noneMatch(ont -> index.hasValues(cls, ont));
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return Collections.singleton(projectOntologiesIndex);
    }
}
